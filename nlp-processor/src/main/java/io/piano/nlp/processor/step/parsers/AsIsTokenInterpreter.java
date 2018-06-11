package io.piano.nlp.processor.step.parsers;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.location.Location;
import io.piano.nlp.domain.location.LocationQualifier;
import io.piano.nlp.domain.operator.AttrPossibleValues;
import io.piano.nlp.domain.operator.AttributeToValuesResolver;
import io.piano.nlp.domain.operator.OperatorType;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.domain.tool.Tool;
import io.piano.nlp.domain.tool.ToolQualifier;
import io.piano.nlp.processor.domain.parsing.Interpretation;
import io.piano.nlp.processor.domain.parsing.InterpretationCategory;
import io.piano.nlp.processor.domain.parsing.Interpretations;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Parser-interpreter that takes interpretations of selected tokens and try parse them as is
 * by completing insufficient information in-place.
 *
 * Created by Dima on 09.06.2018.
 */
@SuppressWarnings("WeakerAccess")
public class AsIsTokenInterpreter {
    @Getter
    @AllArgsConstructor
    private static class WeightedResult {
        private Object result;
        private double weight;
    }


    public void tryParseAsIs(Interpretations interpretations, ParsedQuery parsedQuery) {
        InterpretationCategory[] allCategories = InterpretationCategory.values();

        double[] weights = new double[allCategories.length];
        Object[] results = new Object[allCategories.length];

        Arrays.stream(allCategories).forEach(category -> {
            WeightedResult wresult = callAppropriateHandler(category, interpretations.get(category), parsedQuery);
            if (wresult != null) {
                results[ category.ordinal() ] = wresult.getResult();
                weights[ category.ordinal() ] = wresult.getWeight();
            }
        });

        IntStream.range(0, allCategories.length)
                .filter(i -> results[i] != null)
                .boxed()
                .max( Comparator.comparingDouble(i -> weights[i]) )
                .ifPresent(i -> applyResultToParsedQuery(results[i], allCategories[i], parsedQuery) );
    }



    private WeightedResult callAppropriateHandler(InterpretationCategory category, Interpretation interpretation,
                                                  ParsedQuery parsedQuery) {
        if (interpretation == null) return null;
        Function<Interpretation, WeightedResult> handler;

        switch (category) {
            case LOCATION:
                handler = handleLocationInterpretation(parsedQuery);
                break;
            case TERM:
                handler = handleTermInterpretation(parsedQuery);
                break;
            case TOOL:
                handler = handleToolInterpretation(parsedQuery);
                break;
            case FUNCTION:
                handler = handleOperatorInterpretation(parsedQuery);
                break;
            default:
                return null;
        }

        return handler.apply(interpretation);
    }


    private Function<Interpretation, WeightedResult> handleLocationInterpretation(final ParsedQuery parsedQuery) {
        return inter -> {
            if ( isNullOrEmpty(inter.getValue()) ) return null;

            int locSize = parsedQuery.getLocations().size();
            return new WeightedResult(
                    new Location(LocationQualifier.CITY, inter.getValue()),
                    1.0 / (1 + locSize)
            );
        };
    }

    private Function<Interpretation, WeightedResult> handleTermInterpretation(final ParsedQuery parsedQuery) {
        return inter -> {
            if ( isNullOrEmpty(inter.getValue()) ) return null;

            int termsSize = 0;
            for (StateDomain st : parsedQuery.getStateDomains()) {
                if ( ! isNullOrEmpty(st.getTerm()) ) {
                    termsSize++;
                    continue;       //it's full one, skip it!
                }

                if ( isNullOrEmpty(st.getMetric()) ) return null;    //metric is undefined, so the token is not for term
            }

            return new WeightedResult(
                    inter.getValue(),
                    termsSize == 0 ? 1.0 : 1.0 / (5 + termsSize)
            );
        };
    }

    private Function<Interpretation, WeightedResult> handleToolInterpretation(final ParsedQuery parsedQuery) {
        return inter -> {
            if ( isNullOrEmpty(inter.getValue()) ) return null;

            int toolsSize = parsedQuery.getTools().size();
            Tool tool = new Tool();
            tool.add(ToolQualifier.APPLICATION, inter.getValue());

            return new WeightedResult(
                    tool,
                    1.0 / (1 + toolsSize)
            );
        };
    }

    private Function<Interpretation, WeightedResult> handleOperatorInterpretation(final ParsedQuery parsedQuery) {
        return inter -> {
            if ( isNullOrEmpty(inter.getValue()) ) return null;
            final String ttext = inter.getValue();

            ResultOperator oper = new ResultOperator(OperatorType.GROUP);

            AttributeToValuesResolver resolver = new AttributeToValuesResolver();
            AttrPossibleValues groupQualifierValues = resolver
                    .getPossibleValuesForAttr("groupQualifier", null);
            if (groupQualifierValues == null) {
                return null;
            }

            for (String groupQualifier : groupQualifierValues.getValuesList()) {
                AttrPossibleValues groupValues = resolver.getPossibleValuesForAttr("groupValue", groupQualifier);
                if (groupValues == null) continue;

                if (groupValues.getValuesList().contains(ttext)) {
                    oper.addValueFor(groupQualifier, "groupQualifier");
                    oper.addValueFor(ttext, "groupValue");
                }
            }

            if (oper.getValueFor("groupQualifier") == null) return null;

            int opersSize = parsedQuery.getOperatorsDescriptor().opertorsSize();
            return new WeightedResult(
                    oper,
                    Math.max(0, (4 - opersSize) / 4.0)
            );
        };
    }


    private void applyResultToParsedQuery(Object result, InterpretationCategory category, ParsedQuery parsedQuery) {
        switch (category) {
            case LOCATION:
                parsedQuery.addLocation( (Location)result );
                break;
            case TERM:
                for (StateDomain st : parsedQuery.getStateDomains()) {
                    if ( ! isNullOrEmpty(st.getTerm()) ) {
                        st.setTerm( result.toString() );
                    }
                }

                break;
            case TOOL:
                parsedQuery.addTool( (Tool) result );
                break;
            case FUNCTION:
                parsedQuery.getOperatorsDescriptor().add( (ResultOperator) result );
                break;
        }
    }
}
