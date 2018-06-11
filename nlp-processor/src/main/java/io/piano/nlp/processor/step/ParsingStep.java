package io.piano.nlp.processor.step;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.processor.step.parsers.GeneralParser;
import io.piano.nlp.processor.step.parsers.MetricsParser;
import io.piano.nlp.processor.step.parsers.TimeParser;
import io.piano.nlp.processor.utils.DefaultTagsResolver;
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
        ParsedQuery result = ParsedQuery.builder().build();
        markedTokens = new BitSet( tokens.size() );

        detectUnambiguous(result, tokens);
        new GeneralParser().parse(tokens, markedTokens, result);

        return result;
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
        new TimeParser().parseTimeRange(query, tokens, markedTokens);
        new MetricsParser().parseMetrics(query, tokens, markedTokens);
    }


}
