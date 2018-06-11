package io.piano.nlp.test.parsers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.state.MetricGroup;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.processor.step.parsers.MetricsParser;
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
 * Tests MetricsParser.
 *
 * Created by Dima on 10.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestMetricsParser extends BaseNLPTest {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @DataProvider
    public static Object[][] dataProviderInit() {
        List<Token> tokens1 = asList(
                of("churned", WORD),
                of("subscriptions", WORD)
        );
        StateDomain expected1 = new StateDomain(MetricGroup.CHURN, "churned_subscription");

        List<Token> tokens2 = asList(
                of("subscriptions", WORD),
                of("churned", WORD)
        );
        //noinspection UnnecessaryLocalVariable
        StateDomain expected2 = expected1;

        List<Token> tokens3 = asList(
                of("conversions", WORD)
        );
        StateDomain expected3 = new StateDomain(MetricGroup.CONVERSION, "conversion");

        List<Token> tokens4 = asList(
                of("conversion", WORD),
                of("rate", WORD)
        );
        StateDomain expected4 = new StateDomain(MetricGroup.CONVERSION, "conversion_rate");

        List<Token> tokens5 = asList(
                of("AdBlock", WORD),
                of("Whitelist", WORD)
        );
        StateDomain expected5 = new StateDomain(MetricGroup.ADBLOCK, "adblock_whiltelisting");

        return new Object[][] {
                {tokens1, expected1},
                {tokens2, expected2},
                {tokens3, expected3},
                {tokens4, expected4},
                {tokens5, expected5},
        };
    }

    @UseDataProvider("dataProviderInit")
    @Test
    public void testParse(List<Token> tokens, StateDomain expected) {
        ParsedQuery parsedQuery = new ParsedQuery();
        MetricsParser parser = new MetricsParser();
        parser.parseMetrics(parsedQuery, tokens, new BitSet(tokens.size()));

        Assert.assertEquals(1, parsedQuery.getStateDomains().size() );
        Assert.assertNotNull( parsedQuery.getStateDomains().get(0) );
        Assert.assertEquals(expected, parsedQuery.getStateDomains().get(0) );
    }
}
