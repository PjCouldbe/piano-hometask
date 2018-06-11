package io.piano.nlp.processor.utils;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.operator.ResultOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.piano.nlp.domain.operator.OperatorType.AGGREGATE;
import static io.piano.nlp.domain.operator.OperatorType.PAGE;

/**
 * Resolves default word-tags replaced before by function of filling operator or parse query object
 *
 * Created by Dima on 03.06.2018.
 */
public class DefaultTagsResolver {
    private static final Map<String, Consumer<ParsedQuery>> resolveDictionary = new HashMap<>();

    static {
        final BiConsumer<String, ParsedQuery> addAggregateOperator = (operValue, pq) -> {
            ResultOperator oper = new ResultOperator(AGGREGATE);
            boolean added = oper.addValueFor(operValue, "operator");
            if (added) {
                pq.addOperator(oper);
            }
        };

        resolveDictionary.put("<MAX>", q -> addAggregateOperator.accept("max", q) );
        resolveDictionary.put("<MIN>", q -> addAggregateOperator.accept("min", q) );
        resolveDictionary.put("<COUNT>", q -> addAggregateOperator.accept("count", q) );
        resolveDictionary.put("<GROWS>", q -> addAggregateOperator.accept("grows", q) );
        resolveDictionary.put("<FALLS>", q -> addAggregateOperator.accept("falls", q) );
        resolveDictionary.put("<PAGE>", q -> q.addOperator( new ResultOperator(PAGE) ));
    }

    public boolean resolveDefaultTag(String tag, ParsedQuery parsedQuery) {
        if (resolveDictionary.containsKey(tag)) {
            resolveDictionary.get(tag).accept(parsedQuery);
            return true;
        }

        return false;
    }
}
