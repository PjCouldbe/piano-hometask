package io.piano.nlp.processor.step.parsers;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.time.TimeRange;
import io.piano.nlp.processor.domain.parsing.TimeQuerySetting;
import io.piano.nlp.processor.domain.parsing.TimeUnitCategory;
import io.piano.nlp.processor.utils.*;
import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import static io.piano.nlp.processor.domain.parsing.TimeUnitCategory.*;
import static java.lang.Integer.parseInt;

/**
 * Parsing utility for time range description recognition.
 *
 * Created by Dima on 03.06.2018.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class TimeParser {
    private static final TimeUnitDictionary TIME_UNIT_DICTIONARY = new TimeUnitDictionary();
    private static final TimeServiceTermsDictionary TIME_SERVICE_TERMS_DICTIONARY = new TimeServiceTermsDictionary();

    @NoArgsConstructor
    @Getter
    public static class TimeUnitsRange {
        private int fromIndex;
        private int toIndex;
        private TimeUnitCategory[] categories;

        TimeUnitsRange(int fromIndex, List<TimeUnitCategory> categories) {
            this.fromIndex = fromIndex;
            this.toIndex = fromIndex + categories.size();
            this.categories = categories.toArray( new TimeUnitCategory[categories.size()] );
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    public boolean parseTimeRange(ParsedQuery query, List<Token> tokens, BitSet markedTokens) {
        int index = findFirstTimeUnit(tokens, markedTokens);
        if (index < 0) return false;

        TimeQuerySetting begin = new TimeQuerySetting();
        TimeQuerySetting end = new TimeQuerySetting();
        TimeUnitsRange timeUnitsRange = markAllTimeUnits(tokens, index, markedTokens);

        processConcretizingWords(tokens, timeUnitsRange, markedTokens, begin, end);
        processRemainedTimeUnits(tokens, timeUnitsRange, markedTokens, begin, end);

        TimeRange tr = new TimeRange(
                begin.getResultTime(),
                end.getResultTime()
        );
        query.setTimeRange(tr);

        return true;
    }


    private int findFirstTimeUnit(List<Token> tokens, BitSet markedTokens) {
        TokenUtils tokenUtils = new TokenUtils();

        for (int i = 0; i < tokens.size(); i++) {
            if (markedTokens.get(i) == false) {
                final String word = tokenUtils.normalizeText(tokens.get(i));
                boolean isTimeUnit = TIME_UNIT_DICTIONARY.isTimeUnit(word);
                if (isTimeUnit) {
                    return i;
                }
            }
        }

        return -1;
    }



    private TimeUnitsRange markAllTimeUnits(List<Token> tokens, int index, BitSet markedTokens) {
        List<TimeUnitCategory> timeCategories = new LinkedList<>();
        boolean[] toEnd = { true };

        final TokenConsumer consumer = (t, i) -> {
            if (markedTokens.get(i) == true) return false;

            TimeUnitCategory cat = getPossibleTimeUnitCategory(t);
            if (cat == null) {
                return false;
            } else {
                if (toEnd[0]) {
                    timeCategories.add(cat);
                } else {
                    timeCategories.add(0, cat);
                }

                return true;
            }
        };

        consumer.apply(tokens.get(index), index);

        TokenExpansionUtils tokenExpansionUtils = new TokenExpansionUtils();
        tokenExpansionUtils.tryExpanseRight(tokens, index, consumer);

        toEnd[0] = false;
        int oldSize = timeCategories.size();
        tokenExpansionUtils.tryExpanseLeft(tokens, index, consumer);

        return new TimeUnitsRange(index - (timeCategories.size() - oldSize), timeCategories);
    }

    private TimeUnitCategory getPossibleTimeUnitCategory(Token t) {
        String word = new TokenUtils().normalizeText(t).toLowerCase();

        if (t.getType() == TokenType.NUMBER) {
            return NUMERIC;
        } else if ( TIME_SERVICE_TERMS_DICTIONARY.isConcretizingWord(word) ) {
            return CONCRETIZING_WORD;
        } else if ( TIME_SERVICE_TERMS_DICTIONARY.isAnd(word) ) {
            return AND;
        } else if ( TIME_UNIT_DICTIONARY.isGeneralTimeUnit(word) ) {
            return GENERAL;
        } else if ( TIME_UNIT_DICTIONARY.isTimeUnit(word) ) {
            return CONCRETE;
        }

        return null;
    }


    private void processConcretizingWords(List<Token> tokens, TimeUnitsRange timeUnitsRange, BitSet markedTokens,
                                         TimeQuerySetting begin, TimeQuerySetting end)
    {
        int fromIndex = timeUnitsRange.fromIndex;
        for (int i = fromIndex; i < timeUnitsRange.toIndex; i++) {
            if (timeUnitsRange.categories[i - fromIndex] == CONCRETIZING_WORD) {
                TIME_SERVICE_TERMS_DICTIONARY.processConcretizingWord(tokens, i,
                        timeUnitsRange, markedTokens, begin, end);
            }
        }
    }

    private void processRemainedTimeUnits(List<Token> tokens, TimeUnitsRange timeUnitsRange, BitSet markedTokens,
                                          TimeQuerySetting begin, TimeQuerySetting end) {
        int fromIndex = timeUnitsRange.fromIndex;
        for (int i = fromIndex; i < timeUnitsRange.toIndex; i++) {
            if (markedTokens.get(i) == true) continue;

            TimeConcreteModification concreteMod = getTimeEntryModifier( tokens.get(i) );
            applyTimeConcreteModification(concreteMod, begin, end);
        }
    }



    @Getter
    @Setter
    @NoArgsConstructor
    private static class TimeConcreteModification {
        private Consumer<TimeQuerySetting> setter = null;
        private ToIntFunction<TimeQuerySetting> getter = null;
    }

    @Nullable
    private TimeConcreteModification getTimeEntryModifier(Token t)
    {
        String word = new TokenUtils().normalizeText(t).toLowerCase();
        TimeConcreteModification res = new TimeConcreteModification();

        if (t.getType() == TokenType.NUMBER) {
            try {
                int value = getAsDayOfAsAYear( parseInt(word) );
                if (value >= 100) {
                    res.setGetter(TimeQuerySetting::getYear);
                    res.setSetter( tset -> tset.setYear(value) );
                } else {
                    res.setGetter(TimeQuerySetting::getDay);
                    res.setSetter( tset -> tset.setDay(value) );
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                return null;
            }
        } else if ( TIME_UNIT_DICTIONARY.isDayOfWeek(word) ) {
            int dayOfWeek = TIME_UNIT_DICTIONARY.getNumberAsDayOfWeek(word);
            res.setGetter(TimeQuerySetting::getDayOfWeek);
            res.setSetter( tset -> tset.setDayOfWeek(dayOfWeek) );
        } else if ( TIME_UNIT_DICTIONARY.isMonth(word) ) {
            int month = TIME_UNIT_DICTIONARY.getNumberAsMonth(word);
            res.setGetter(TimeQuerySetting::getMonth);
            res.setSetter( tset -> tset.setMonth(month) );
        }

        return (res.getter == null || res.setter == null) ? null : res;
    }
    private int getAsDayOfAsAYear(int num) {
        return num > 31
                ? (num >= 100) ? num : num + 1900
                : num;
    }


    private void applyTimeConcreteModification(@Nullable TimeConcreteModification concreteMod,
                                               TimeQuerySetting begin, TimeQuerySetting end)
    {
        if (concreteMod == null) return;

        if ( concreteMod.getter.applyAsInt(begin) == TimeQuerySetting.getNoValue() ) {
            concreteMod.setter.accept(begin);
        } else {
            concreteMod.setter.accept(end);
        }
    }

    /*@SuppressWarnings("PointlessBooleanExpression")
    private void tryParseAsTime_Private(ParsedQuery query, List<Token> tokens, int index, BitSet markedTokens) {
        Calendar c = Calendar.getInstance();
        c.setTime( new Date() );

        String lowTokenText = tokens.get(index).getText().toLowerCase();
        int calendarUnit = tryInterpretAsCalendarUnit(lowTokenText);
        if (calendarUnit != -1) {
            if ( isDayOfWeek(lowTokenText) ) {
                c.set(Calendar.DAY_OF_WEEK, calendarUnit);
            } else if ( isMonth(lowTokenText) ) {
                c.set(Calendar.MONTH, calendarUnit);
            } else {
                Token nextToken = getNearToken(tokens, index, 1, markedTokens);
                Token prevToken = getNearToken(tokens, index, -1, markedTokens);

            }
        }
    }

    private int tryInterpretAsCalendarUnit(final String lowText) {
        return Arrays.stream( Calendar.class.getDeclaredFields() )
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getName().toLowerCase().equals(lowText) )
                .findFirst()
                .map(f -> {
                    try {
                        return f.get(Calendar.getInstance());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .map(o -> (Integer)o)
                .orElse(-1);
    }

    private boolean isDayOfWeek(String lowText) {
        return ("sunday|monday|tuesday|wednesday|thursday|friday|saturday").contains(lowText);
    }

    private boolean isMonth(String lowText) {
        return ("january|february|march|april|may|june|july|august|september|november|december").contains(lowText);
    }

    @SuppressWarnings("PointlessBooleanExpression")
    private Token getNearToken(List<Token> tokens, int index, int near, BitSet markedTokens) {
        int i = index + near;
        if (i < tokens.size() && i >= 0 && markedTokens.get(i) == false) {
            return tokens.get(i);
        }

        return null;
    }

    private boolean checkToken(Token t) {
        //if (t.i)
        return false;
    }*/
}
