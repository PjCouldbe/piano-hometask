package io.piano.nlp.processor.utils;

import io.piano.nlp.processor.domain.parsing.TimeQuerySetting;
import io.piano.nlp.processor.domain.parsing.TimeUnitCategory;
import io.piano.nlp.processor.step.parsers.TimeParser.TimeUnitsRange;
import io.piano.nlp.shared.Token;

import java.util.BitSet;
import java.util.List;

import static io.piano.nlp.processor.domain.parsing.TimeUnitCategory.*;
import static java.util.Arrays.asList;

/**
 * Dictionary for service words during time parsing like: concretizing words, time interval flags or others.
 *
 * Created by Dima on 10.06.2018.
 */
public class TimeServiceTermsDictionary {
    private static final List<String> concretizingWords = asList(
            "last", "past", "ago"
    );

    public boolean isConcretizingWord(String word) {
        return concretizingWords.contains(word);
    }

    public boolean isAnd(String word) {
        return word.equals("and");
    }


    public void processConcretizingWord(List<Token> tokens, int concreteWordIndex, TimeUnitsRange timeUnitsRange,
                                        BitSet markedTokens, TimeQuerySetting begin, TimeQuerySetting end) {
        switch ( tokens.get(concreteWordIndex).getText() ) {
            case "last":
                processLastAndPastWords(tokens, concreteWordIndex, timeUnitsRange, markedTokens,
                        begin, end, 0);
                break;
            case "past":
                processLastAndPastWords(tokens, concreteWordIndex, timeUnitsRange, markedTokens,
                        begin, end, -1);
                break;
            case "ago":
                markedTokens.set(concreteWordIndex, true);
                break;
        }
    }

    private void processLastAndPastWords(List<Token> tokens, int concreteWordIndex, TimeUnitsRange timeUnitsRange,
                                         BitSet markedTokens, TimeQuerySetting begin, TimeQuerySetting end, int addMod)
    {
        int fromIndex = timeUnitsRange.getFromIndex();
        TimeUnitDictionary dict = new TimeUnitDictionary();
        Integer value = null;

        for (int k = concreteWordIndex + 1; k < timeUnitsRange.getToIndex(); k++) {
            TimeUnitCategory cat = timeUnitsRange.getCategories()[k - fromIndex];
            if (cat == CONCRETIZING_WORD || cat == AND) break;

            String ttext = new TokenUtils().normalizeText( tokens.get(k) ).toLowerCase();
            if (cat == NUMERIC) {
                value = Integer.parseInt(ttext);
            } else if (cat == GENERAL) {
                processPastGeneral(ttext, k, markedTokens, begin, end, value, addMod);
            } else if (cat == CONCRETE) {
                processPastConcrete(ttext, k, dict, markedTokens, begin, end);
            } else {
                continue;   //do not mark this token then!
            }

            markedTokens.set(k, true);
        }
    }

    private void processPastGeneral(String ttext, int index, BitSet markedTokens,
                                    TimeQuerySetting begin, TimeQuerySetting end,
                                    Integer value, int addMod)
    {
        final int finalValue = ((value == null) ? -1 : -value) + addMod;

        switch (ttext) {
            case "year":
                begin.setYearMod(finalValue);
                end.setYearMod(addMod);
                break;
            case "month":
                begin.setMonthMod(finalValue);
                end.setMonthMod(addMod);
                break;
            case "week":
                begin.setWeekMod(finalValue);
                end.setWeekMod(addMod);
                break;
            case "hour":
                begin.setHourMod(finalValue);
                end.setHourMod(addMod);
                break;
            case "minute":
                begin.setMinuteMod(finalValue);
                end.setMinuteMod(addMod);
                break;
        }
    }

    private void processPastConcrete(String ttext, int index, TimeUnitDictionary dict, BitSet markedTokens,
                                     TimeQuerySetting begin, TimeQuerySetting end) {
        if (dict.isDayOfWeek(ttext)) {
            int dayOfWeek = dict.getNumberAsDayOfWeek(ttext);
            markedTokens.set(index, true);

            begin.setWeekMod(-1);
            begin.setDayOfWeek(dayOfWeek);
            end.setWeekMod(-1);
            end.setDayOfWeek( getNextCircular(dayOfWeek, 7) );
        } else if (dict.isMonth(ttext)) {
            int month = dict.getNumberAsMonth(ttext);
            markedTokens.set(index, true);

            begin.setYearMod(-1);
            begin.setMonth(month);
            end.setYearMod(-1);
            end.setMonth( getNextCircular(month, 12) );
        }
    }

    private int getNextCircular(int current, int maxInPeriod) {
        return current < maxInPeriod ? current + 1 : 0;
    }
}
