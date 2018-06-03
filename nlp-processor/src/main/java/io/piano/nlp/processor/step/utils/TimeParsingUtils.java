package io.piano.nlp.processor.step.utils;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.shared.Token;

import java.util.*;

/**
 * Parsing utility for time range description recognition.
 *
 * Created by Dima on 03.06.2018.
 */
public class TimeParsingUtils {
    @SuppressWarnings("PointlessBooleanExpression")
    private void tryParseAsTime(ParsedQuery query, List<Token> tokens, int index, BitSet markedTokens) {
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
    }
}
