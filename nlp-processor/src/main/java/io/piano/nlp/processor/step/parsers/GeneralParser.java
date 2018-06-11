package io.piano.nlp.processor.step.parsers;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
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
import io.piano.nlp.processor.utils.EnumUtils;
import io.piano.nlp.processor.utils.TokenUtils;
import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;

import java.util.*;

import static java.util.Collections.emptyList;

/**
 * Parser of general logic for not-particular cases.
 * Uses various interpretations assigning and resolving strategy.
 *
 * Created by Dima on 09.06.2018.
 */
public class GeneralParser {
    @SuppressWarnings("PointlessBooleanExpression")
    public void parse(List<Token> tokens, BitSet markedTokens, ParsedQuery parsedQuery) {
        //Select non-marked tokens and collect into separate list
        List<Token> nonMarkedTokens = new ArrayList<>(tokens.size());
        TIntList indexesFromSource = new TIntArrayList(tokens.size());

        for (int i = 0; i < tokens.size(); i++) {
            if (markedTokens.get(i) == false) {
                nonMarkedTokens.add( tokens.get(i) );
                indexesFromSource.add(i);
            }
        }

        List<Interpretations> interpretationsList = assignInterpretationsToTokens(nonMarkedTokens);
        processTokensWithInterpretations(nonMarkedTokens, interpretationsList,
                indexesFromSource, markedTokens, parsedQuery);

        tryParseUnmarkedTokensAsIs(nonMarkedTokens, interpretationsList,
                indexesFromSource, markedTokens, parsedQuery);
    }


    private List<Interpretations> assignInterpretationsToTokens(List<Token> tokens) {
        List<Interpretations> res = new ArrayList<>();

        for (Token t : tokens) {
            Interpretations interpretations = new Interpretations();
            InterpretationCategory[] categories = InterpretationCategory.of(t);

            for (InterpretationCategory ic : categories) {
                Interpretation inter = getInterpretation(ic, t);
                if (inter != null) {
                    interpretations.set(ic, inter);
                }
            }

            res.add(interpretations);
        }

        return res;
    }

    private Interpretation getInterpretation(InterpretationCategory category, Token t) {
        final TokenUtils utils = new TokenUtils();

        boolean capitalized = utils.isCapitalized(t);
        String ttext = capitalized ? t.getText() : utils.normalizeText(t);
        boolean isLower = ttext.toLowerCase().equals(ttext);
        AttributeToValuesResolver resolver = new AttributeToValuesResolver();

        Interpretation res = new Interpretation(category);
        switch (category) {
            case LOCATION:
                if (capitalized) {
                    res.setValue(ttext);
                    return res;
                } else {
                    LocationQualifier q = ttext.equals("location")
                            ? LocationQualifier.REGION    //for simple solution
                            : new EnumUtils().valueOf(ttext, LocationQualifier.values());
                    if (q != null) {
                        TIntList subcats = new TIntArrayList();
                        subcats.add( q.ordinal() );
                        res.setSubCategories(subcats);
                        return res;
                    }
                }
            case TERM:
                res.setValue(ttext);
                return res;
            case TOOL:
                if (isLower) {
                    ToolQualifier q = new EnumUtils().valueOf(ttext, ToolQualifier.values());
                    if (q != null) {
                        TIntList subcats = new TIntArrayList();
                        subcats.add( q.ordinal() );
                        res.setSubCategories(subcats);
                        return res;
                    }
                }

                res.setValue(ttext);
                return res;
            case FUNCTION:
                TIntList subcats = new TIntArrayList();
                if (t.getType() == TokenType.NUMBER) {
                    int index = Optional.of(new AttributeToValuesResolver().getPossibleAttrs(OperatorType.SELECT))
                            .map(AttrPossibleValues::getValuesList)
                            .map(lst -> lst.indexOf("take"))
                            .orElse(-1);

                    if (index == -1) return null;

                    subcats.add( OperatorType.SELECT.ordinal() );
                    subcats.add(index);

                    res.setSubCategories(subcats);
                    res.setValue(ttext);
                } else if (t.getPOSTag() != null && t.getPOSTag().startsWith("W") || utils.isAuxiliary(t)) {
                     subcats.add( OperatorType.GROUP.ordinal() );
                     res.setSubCategories(subcats);
                } else {
                    subcats.add( OperatorType.GROUP.ordinal() );

                    List<String> groupQualifiers = Optional.ofNullable(
                                    resolver.getPossibleValuesForAttr("groupQualifier", null))
                            .map(AttrPossibleValues::getValuesList)
                            .orElse( emptyList() );
                    int index = groupQualifiers.indexOf(ttext);

                    if (index == -1) {
                        for (int i = 0; i < groupQualifiers.size(); i++) {
                            String q = groupQualifiers.get(i);

                            index = Optional.ofNullable(
                                            resolver.getPossibleValuesForAttr("groupValue", q))
                                    .map(AttrPossibleValues::getValuesList)
                                    .map(lst -> lst.indexOf(ttext))
                                    .orElse(-1);
                            if (index != -1) {
                                subcats.add(i);
                                res.setValue(ttext);
                            }
                        }
                    } else {
                        subcats.add(index);
                        res.setValue( getDefaultValuesForGroupQualifiers(ttext) );

                    }

                    res.setSubCategories(subcats);
                }

                return res;
            default:
                return null;
        }
    }
    private String getDefaultValuesForGroupQualifiers(String groupQualifier) {
        switch (groupQualifier) {
            case "location": return LocationQualifier.CITY.name().toLowerCase();
            case "tool" :    return ToolQualifier.BROWSER.name().toLowerCase();
            default:         return null;
        }
    }


    private void processTokensWithInterpretations(List<Token> nonMarkedTokens, List<Interpretations> interpretationsList,
                                                  TIntList indexesFromSource, BitSet markedTokens, ParsedQuery parsedQuery)
    {
        outer:
        for (int i = 0; i < nonMarkedTokens.size(); i++) {
            Interpretations prev = interpretationsList.get(i);
            TIntList indicesForMarking = new TIntArrayList();

            for (int j = i + 1; j < nonMarkedTokens.size(); j++) {
                List<Interpretation> mergedInterpretations = interpretationsList.get(j).merge(prev);

                if (mergedInterpretations.size() == 1) {
                    fillParseQueryObject(mergedInterpretations.get(0), parsedQuery);
                    indicesForMarking.add(i);
                    indicesForMarking.add(j);

                    for (int k : indicesForMarking.toArray()) {
                        markedTokens.set(indexesFromSource.get(k), true);
                    }

                    continue outer;
                } else if (mergedInterpretations.size() > 1) {
                    prev = new Interpretations(mergedInterpretations);
                    indicesForMarking.add(j);
                }
            }
        }
    }

    private void fillParseQueryObject(Interpretation interpretation, ParsedQuery parsedQuery) {
        switch (interpretation.getCategory()) {
            case LOCATION:
                Location location = new Location(
                        LocationQualifier.values()[ interpretation.getSubCategories().get(0) ],
                        interpretation.getValue()
                );

                parsedQuery.addLocation(location);
                break;
            case TERM:
                List<StateDomain> stateDomains = parsedQuery.getStateDomains();
                for (StateDomain st : stateDomains) {
                    if (st.getTerm() == null || st.getTerm().isEmpty()) {
                        st.setTerm( interpretation.getValue() );
                    }
                }
                break;
            case TOOL:
                Tool tool = new Tool();
                tool.add(
                        ToolQualifier.values()[ interpretation.getSubCategories().get(0) ],
                        interpretation.getValue()
                );

                parsedQuery.addTool(tool);
                break;
            case FUNCTION:
                int[] subCategories = interpretation.getSubCategories().toArray();
                AttributeToValuesResolver attributeResolver = new AttributeToValuesResolver();

                ResultOperator operator = new ResultOperator(
                        OperatorType.values()[ subCategories[0] ]
                );

                switch (operator.getOperatorType()) {
                    case GROUP:
                        String groupQualifier =
                                Optional.ofNullable(attributeResolver
                                                .getPossibleValuesForAttr("groupQualifier", null))
                                        .map(AttrPossibleValues::getValuesList)
                                        .map(lst -> lst.get(subCategories[1]))
                                        .orElse(null);

                        if (groupQualifier != null) {
                            operator.addValueFor(groupQualifier, "groupQualifier");
                            operator.addValueFor(interpretation.getValue(), "groupValue");
                        }
                        break;
                    case AGGREGATE:
                        operator.addValueFor(interpretation.getValue(), "operator");
                        break;
                    case SELECT:
                        String selectAttr =
                                Optional.of(attributeResolver
                                                .getPossibleAttrs( operator.getOperatorType() ))
                                        .map(AttrPossibleValues::getValuesList)
                                        .map(lst -> lst.get(subCategories[1]))
                                        .orElse(null);

                        if (selectAttr != null) {
                            operator.addValueFor(interpretation.getValue(), selectAttr);
                        }
                        break;
                    default:
                        break;
                }

                parsedQuery.getOperatorsDescriptor().add(operator);
                break;
            default:
                throw new RuntimeException();
        }
    }


    @SuppressWarnings("PointlessBooleanExpression")
    private void tryParseUnmarkedTokensAsIs(List<Token> nonMarkedTokens, List<Interpretations> interpretationsList,
                                            TIntList indexesFromSource, BitSet markedTokens, ParsedQuery parsedQuery)
    {
        AsIsTokenInterpreter asIsTokenInterpreter = new AsIsTokenInterpreter();

        for (int i = 0; i < nonMarkedTokens.size(); i++) {
            int markedTokensIndex = indexesFromSource.get(i);
            if (markedTokens.get(markedTokensIndex) == true) continue;

            Interpretations inters = interpretationsList.get(i);
            Interpretation fullInter = inters.getFull();
            if (fullInter == null) {
                asIsTokenInterpreter.tryParseAsIs( interpretationsList.get(i), parsedQuery );
            } else {
                fillParseQueryObject(fullInter, parsedQuery);
            }
            markedTokens.set(markedTokensIndex, true);
        }
    }
}
