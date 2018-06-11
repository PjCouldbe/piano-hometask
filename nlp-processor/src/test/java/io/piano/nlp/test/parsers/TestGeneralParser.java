package io.piano.nlp.test.parsers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.operator.OperatorType;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.domain.state.MetricGroup;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.processor.step.parsers.GeneralParser;
import io.piano.nlp.shared.Token;
import io.piano.nlp.test.BaseNLPTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.BitSet;
import java.util.List;

import static io.piano.nlp.shared.TokenType.NUMBER;
import static io.piano.nlp.shared.TokenType.WORD;
import static java.util.Arrays.asList;

/**
 * Tests GeneralParser without as-is parsing substep.
 *
 * Created by Dima on 10.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestGeneralParser extends BaseNLPTest {
    private static ParsedQuery getPrimaryQuery() {
        ParsedQuery parsedQuery = ParsedQuery.builder().build();
        parsedQuery.addStateDomain(
                new StateDomain(MetricGroup.CONVERSION, MetricGroup.CONVERSION.getConversionMetrics()[0])
        );
        return parsedQuery;
    }


    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @DataProvider
    public static Object[][] dataProviderInit() {
        List<Token> tokens1 = asList(
                of("Annual", WORD),
                of("term", WORD)
        );
        ParsedQuery expected1 = getPrimaryQuery();
        expected1.getStateDomains().get(0).setTerm("Annual");


        List<Token> tokens2 = asList(
                of("what", WORD, "WDT"),
                of("8", NUMBER),
                of("locations", WORD)
        );
        ParsedQuery expected2 = getPrimaryQuery();
        ResultOperator oper = new ResultOperator(OperatorType.GROUP);
            oper.addValueFor("location", "groupQualifier");
            oper.addValueFor("city", "groupValue");
            expected2.getOperatorsDescriptor().add(oper);
        oper = new ResultOperator(OperatorType.SELECT);
            oper.addValueFor("8", "take");
            expected2.getOperatorsDescriptor().add(oper);


        List<Token> tokens3 = asList(
                of("by", WORD, "IN"),
                of("browser", WORD)
        );
        ParsedQuery expected3 = getPrimaryQuery();
        oper = new ResultOperator(OperatorType.GROUP);
            oper.addValueFor("tool", "groupQualifier");
            oper.addValueFor("browser", "groupValue");
        expected3.getOperatorsDescriptor().add(oper);



        return new Object[][] {
                {tokens1, expected1},
                {tokens2, expected2},
                {tokens3, expected3},
        };
    }

    @UseDataProvider("dataProviderInit")
    @Test
    public void testParse(List<Token> tokens, ParsedQuery expected) {
        ParsedQuery actual = getPrimaryQuery();
        GeneralParser parser = new GeneralParser();
        parser.parse(tokens, new BitSet(tokens.size()), actual);

        Assert.assertEquals(expected, actual);
    }
}
