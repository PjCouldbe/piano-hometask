package io.piano.nlp.test.steps;

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
import io.piano.nlp.domain.tool.Tool;
import io.piano.nlp.domain.tool.ToolQualifier;
import io.piano.nlp.processor.ParseQueryPipeline;
import io.piano.nlp.processor.utils.TimeRangeComparator;
import io.piano.nlp.test.BaseNLPTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Tests examples of various possible user queries parsing to object representation.
 *
 * Created by Dima on 03.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestParseQuery extends BaseNLPTest {
    private static final ParseQueryPipeline pipeline = ParseQueryPipeline.initialize();
    private static final TimeRangeComparator TIME_RANGE_COMPARATOR = new TimeRangeComparator(2);
    private static final Date now = new Date();


    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @DataProvider
    public static Object[][] dataProviderInit() {
        Calendar c = Calendar.getInstance();
        ResultOperator oper;
        Date begin, end;

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
        ParsedQuery expected4 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion_rate")) )
                .build();
        oper = new ResultOperator(OperatorType.GROUP);
            oper.addValueFor("tool", "groupQualifier");
            oper.addValueFor("browser", "groupValue");
        expected4.addOperator(oper);

        String text5 = "What location has the highest conversion rate?";
        ParsedQuery expected5 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion_rate")) )
                .build();
        oper = new ResultOperator(OperatorType.GROUP);
            oper.addValueFor("location", "groupQualifier");
            oper.addValueFor("city", "groupValue");
            expected5.addOperator(oper);
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("max", "operator");
            expected5.addOperator(oper);

        String text6 = "Is my churn rate improving?";
        ParsedQuery expected6 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CHURN, "churn_rate")) )
                .build();
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("grows", "operator");
        expected6.addOperator(oper);

        String text7 = "Show me conversions for the past week";
        ParsedQuery expected7 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion")) )
                .build();
            c.setTime(now);
            c.add(Calendar.WEEK_OF_MONTH, -2);
            begin = c.getTime();
            c.setTime(now);
            c.add(Calendar.WEEK_OF_MONTH, -1);
            end = c.getTime();
        expected7.setTimeRange( of(begin, end) );

        String text8 = " Show me how many conversions I have had";
        ParsedQuery expected8 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion")) )
                .build();
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("count", "operator");
        expected8.addOperator(oper);

        String text9 = "How many cancelled subscriptions do I have?";
        ParsedQuery expected9 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CHURN, "churned_subscription")) )
                .build();
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("count", "operator");
        expected9.addOperator(oper);

        String text10 = "How many subscriptions cancelled last week?";
        ParsedQuery expected10 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CHURN, "churned_subscription")) )
                .build();
            c.setTime(now);
            c.add(Calendar.WEEK_OF_MONTH, -2);
            begin = c.getTime();
        expected10.setTimeRange( of(begin ,now) );
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("count", "operator");
        expected10.addOperator(oper);

        String text11 = "What are the 8 locations with the highest conversion rate?";
        ParsedQuery expected11 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CONVERSION, "conversion_rate")) )
                .build();
        oper = new ResultOperator(OperatorType.GROUP);
            oper.addValueFor("location", "groupQualifier");
            oper.addValueFor("city", "groupValue");
        expected11.addOperator(oper);
        oper = new ResultOperator(OperatorType.SELECT);
            oper.addValueFor("8", "take");
        expected11.addOperator(oper);

        String text12 = "How many cancelled subscriptions does my site had between June, 1 and July, 15?";
        ParsedQuery expected12 = ParsedQuery.builder()
                .stateDomains( asList(new StateDomain(MetricGroup.CHURN, "churned_subscription")) )
                .tools( singletonList(
                        new Tool(ToolQualifier.APPLICATION, "site")
                ))
                .build();
        oper = new ResultOperator(OperatorType.AGGREGATE);
            oper.addValueFor("operator", "count");
        expected12.addOperator(oper);
            c.setTime(now);
            c.set(Calendar.MONTH, Calendar.JUNE);
            c.set(Calendar.DAY_OF_MONTH, 1);
            begin = c.getTime();
            c.set(Calendar.MONTH, Calendar.JULY);
            c.set(Calendar.DAY_OF_MONTH, 15);
            end = c.getTime();
        expected12.setTimeRange( of(begin, end) );



        return new Object[][] {
                {text1, expected1},
                {text2, expected2},
                {text3, expected3},
                {text4, expected4},
                {text5, expected5},
                {text6, expected6},
                {text7, expected7},
                {text8, expected8},
                {text9, expected9},
                {text10, expected10},
                {text11, expected11},
                {text12, expected12},
        };
    }

    @UseDataProvider("dataProviderInit")
    @Test
    public void testParse(String text, ParsedQuery expected) {
        ParsedQuery actual = pipeline.run( new UserDefinedQuery(text) );

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertTrue( TIME_RANGE_COMPARATOR.equals(expected.getTimeRange(), actual.getTimeRange()) );
    }
}
