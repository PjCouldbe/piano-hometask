package io.piano.nlp.processor.step.parsers;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.state.MetricGroup;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.processor.utils.TokenUtils;
import io.piano.nlp.shared.Token;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Particular-case parser for conversion metrics attribute of query.
 *
 * Created by Dima on 06.06.2018.
 */
@SuppressWarnings("UnusedReturnValue")
public class MetricsParser {
    private final TokenUtils tokenUtils = new TokenUtils();

    public boolean parseMetrics(ParsedQuery query, List<Token> tokens, BitSet markedTokens) {
        Pair<Integer, MetricGroup> result = findFirstMetricMention(tokens, markedTokens);
        if (result == null) return false;

        int index = result.getKey();              //index of metric-token
        MetricGroup mg = result.getValue();       //metric group of that token

        String[] metrics = mg.getConversionMetrics();
        final String metric = selectFinalMetric(tokens, markedTokens, index, metrics);

        StateDomain st = new StateDomain(mg, metric);
        query.addStateDomain(st);
        return true;
    }


    @SuppressWarnings("PointlessBooleanExpression")
    private Pair<Integer, MetricGroup> findFirstMetricMention(List<Token> tokens, BitSet markedTokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (markedTokens.get(i) == false) {
                final String word = tokens.get(i).getText().toLowerCase();
                MetricGroup metricGroup = Arrays.stream( MetricGroup.values() )
                        .filter(mg -> word.contains( mg.name().toLowerCase() ))
                        .findFirst()
                        .orElse(null);
                if (metricGroup != null) {
                    return new Pair<>(i, metricGroup);
                }
            }
        }

        return null;
    }

    private String selectFinalMetric(List<Token> tokens, BitSet markedTokens, int index, String[] metrics) {
        List<String> filteredMetrics = findMetrics(tokens.get(index), asList(metrics));
        if (filteredMetrics.size() == 1) return filteredMetrics.get(0);

        String metricFromRight = tryExpanse(tokens, markedTokens, index, 1, metrics);
        if ( ! isNullOrEmpty(metricFromRight) ) {
            return metricFromRight;
        }

        String metricFromLeft = tryExpanse(tokens, markedTokens, index, -1, metrics);
        return isNullOrEmpty(metricFromLeft) ? metrics[0] : metricFromLeft;
    }



    @SuppressWarnings("PointlessBooleanExpression")
    private String tryExpanse(List<Token> tokens, BitSet markedTokens, int currIndex, int step, String[] metrics) {
        if (step == 0) return null;

        List<String> metricsList = asList(metrics);
        for (int i = currIndex + step; (step > 0 ? i < tokens.size() : i >= 0); i += step) {
            if (markedTokens.get(i) == true) continue;

            metricsList = findMetrics(tokens.get(i), metricsList);

            if (metricsList.size() == 1) {
                return metricsList.get(0);
            }
        }

        //If no changes in metrics set, then return null, else return first of remained metrics
        return metricsList.size() == metrics.length ? null : metricsList.get(0);
    }

    private List<String> findMetrics(Token t, List<String> metricsList) {
        final String word = tokenUtils.normalizeText(t);

        if (word.equals("subscription")) {
            return singletonList( metricsList.get(0) );
        }

        final String firstMetricBackup = metricsList.get(0);
        List<String> toRemove =  metricsList.stream().filter(metric -> ! metric.contains(word)).collect(toList());
        metricsList.removeAll(toRemove);

        if (metricsList.size() == 0) {
            return singletonList(firstMetricBackup);
        } else {
            return metricsList;
        }
    }
}
