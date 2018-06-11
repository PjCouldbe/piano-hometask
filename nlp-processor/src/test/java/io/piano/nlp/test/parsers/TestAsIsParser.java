package io.piano.nlp.test.parsers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.location.Location;
import io.piano.nlp.domain.location.LocationQualifier;
import io.piano.nlp.domain.state.MetricGroup;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.domain.tool.Tool;
import io.piano.nlp.domain.tool.ToolQualifier;
import io.piano.nlp.processor.step.parsers.GeneralParser;
import io.piano.nlp.shared.Token;
import io.piano.nlp.test.BaseNLPTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.BitSet;
import java.util.List;

import static io.piano.nlp.shared.TokenType.WORD;
import static java.util.Arrays.asList;

/**
 * Tests AsIs interpreter substep of GeneralParser algorithm.
 *
 * Created by Dima on 10.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestAsIsParser extends BaseNLPTest {
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
                of("site", WORD, "NN")
        );
        ParsedQuery expected1 = getPrimaryQuery();
        Tool tool = new Tool();
        tool.add(ToolQualifier.APPLICATION, "site");
        expected1.addTool(tool);


        List<Token> tokens2 = asList(
                of("Kazan", WORD, "NN")
        );
        ParsedQuery expected2 = getPrimaryQuery();
        expected2.addLocation( new Location(LocationQualifier.CITY, "Kazan") );


        return new Object[][] {
                {tokens1, expected1},
                {tokens2, expected2},
        };
    }

    @UseDataProvider("dataProviderInit")
    @Test
    public void testParseAsIs(List<Token> tokens, ParsedQuery expected) {
        ParsedQuery actual = getPrimaryQuery();
        GeneralParser parser = new GeneralParser();
        parser.parse(tokens, new BitSet(tokens.size()), actual);

        Assert.assertEquals(expected, actual);
    }
}
