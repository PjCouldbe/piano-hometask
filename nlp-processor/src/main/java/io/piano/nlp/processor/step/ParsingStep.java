package io.piano.nlp.processor.step;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.processor.step.utils.DefaultTagsResolver;
import io.piano.nlp.shared.Token;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

/**
 * Main step of pipeline that parses result tokens of user's query to structured query object.
 *
 * Created by Dima on 02.06.2018.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class ParsingStep {
    private BitSet markedTokens;

    public ParsedQuery parse(List<Token> tokens) {
        ParsedQuery result = new ParsedQuery();
        markedTokens = new BitSet( tokens.size() );

        return null; //TODO
    }


    private void detectUnambiguous(ParsedQuery query, List<Token> tokens) {
        detectUnambiguousWithDefaultTags(query, tokens);
        detectRestOfUnambiguous(query, tokens);
    }

    private void detectUnambiguousWithDefaultTags(ParsedQuery query, List<Token> tokens) {
        final DefaultTagsResolver defResolver = new DefaultTagsResolver();
        List<ResultOperator> operators = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            if (markedTokens.get(i) == false) {
                boolean accepted = defResolver.resolveDefaultTag(tokens.get(i).getText(), query);
                if (accepted) {
                    markedTokens.set(i, true);
                }
            }
        }

        operators.sort( Comparator.comparing(ResultOperator::getOperatorType) );

    }


    private void detectRestOfUnambiguous(ParsedQuery query, List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (markedTokens.get(i) == false) {

            }
        }
    }

    /*private int tryInterpretAsCalendarUnit(final String text) {
        final String lowText = text.toLowerCase();
        int val = Arrays.stream( Calendar.class.getDeclaredFields() )
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
    }*/

    private String tryExtendWord(String word, int index, List<Token> tokens) {
        if (index < tokens.size() - 1 && markedTokens.get(index + 1) == false) {
            return word + ' ' + tokens.get(index + 1).getText();
        }

        if (index > 0 && markedTokens.get(index - 1) == false) {
            return tokens.get(index - 1).getText() + ' ' + word;
        }

        return null;
    }


}
