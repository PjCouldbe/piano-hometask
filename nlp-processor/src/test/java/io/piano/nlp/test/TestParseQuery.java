package io.piano.nlp.test;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.UserDefinedQuery;
import io.piano.nlp.domain.operator.OperatorType;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.domain.operator.ResultOperatorsDescriptor;
import io.piano.nlp.domain.state.MetricGroup;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.processor.ParseQueryPipeline;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static java.util.Arrays.asList;

/**
 * Tests examples of various possible user queries parsing to object representation.
 *
 * Created by Dima on 03.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestParseQuery extends BaseNLPTest {
    private static final ParseQueryPipeline pipeline = ParseQueryPipeline.initialize();
    private final Date now = new Date();


    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @DataProvider
    public Object[][] dataProviderInit() {
        Calendar c = Calendar.getInstance();

        String text1 = "Show me conversion";
        ParsedQuery expected1 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion")) )
                .build();

        String text2 = "Show me conversion rate";
        ParsedQuery expected2 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion_rate")) )
                .build();

        String text3 = "Show me number of conversion for Annual term for last month";
        c.setTime(now);
        c.add(Calendar.MONTH, -1);
        ParsedQuery expected3 = ParsedQuery.builder()
                .timeRange( of(c.getTime(), now) )
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion", "Annual")) )
                .operatorsDescriptor( new ResultOperatorsDescriptor(asList(
                        new ResultOperator(OperatorType.PAGE)
                )) )
                .build();

        String text4 = "Show me conversion rate by browser";
        ResultOperator oper = new ResultOperator(OperatorType.GROUP);
        oper.addValueFor("groupQualifier", "tool");
        oper.addValueFor("groupValue", "browser");
        ParsedQuery expected4 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion", "Annual")) )
                .operatorsDescriptor( new ResultOperatorsDescriptor(asList(
                        new ResultOperator(OperatorType.PAGE)
                )) )
                .build();

        return new Object[][] {
                {text1, expected1},
                {text2, expected2},
                {text3, expected3},
                {text4, expected4},
        };
    }

    @UseDataProvider("dataProviderInit")
    public void testParse(String text, ParsedQuery expected) {
        ParsedQuery actual = pipeline.run( new UserDefinedQuery(text) );
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }
}
