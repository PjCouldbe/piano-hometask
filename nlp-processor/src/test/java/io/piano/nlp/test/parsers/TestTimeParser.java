package io.piano.nlp.test.parsers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.time.TimeRange;
import io.piano.nlp.processor.step.parsers.TimeParser;
import io.piano.nlp.processor.utils.TimeRangeComparator;
import io.piano.nlp.shared.Token;
import io.piano.nlp.test.BaseNLPTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static io.piano.nlp.shared.TokenType.NUMBER;
import static io.piano.nlp.shared.TokenType.WORD;
import static java.util.Arrays.asList;

/**
 * Tests TimeParser.
 *
 * Created by Dima on 10.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestTimeParser extends BaseNLPTest {
    private static final TimeRangeComparator TIME_RANGE_COMPARATOR = new TimeRangeComparator();

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @DataProvider
    public static Object[][] dataProviderInit() {
        Calendar c = Calendar.getInstance();
        final long NOW = new Date().getTime();

        List<Token> tokens1 = asList(
                of("past", WORD),
                of("week", WORD)
        );
        c.setTimeInMillis(NOW);
        c.add(Calendar.WEEK_OF_MONTH, -2);
        Date begin = c.getTime();
        c.setTimeInMillis(NOW);
        c.add(Calendar.WEEK_OF_MONTH, -1);
        Date end = c.getTime();
        TimeRange expected1 = of(begin, end);

        List<Token> tokens2 = asList(
                of("last", WORD),
                of("month", WORD)
        );
        c.setTimeInMillis(NOW);
        c.add(Calendar.MONTH, -1);
        begin = c.getTime();
        c.setTimeInMillis(NOW);
        end = c.getTime();
        TimeRange expected2 = of(begin, end);

        List<Token> tokens3 = asList(
                of("June", WORD),
                of("17", NUMBER),
                of("and", WORD),
                of("July", WORD),
                of("23", NUMBER)
        );
        c.setTimeInMillis(NOW);
        c.set(Calendar.MONTH, Calendar.JUNE);
        c.set(Calendar.DAY_OF_MONTH, 17);
        begin = c.getTime();
        c.setTimeInMillis(NOW);
        c.set(Calendar.MONTH, Calendar.JULY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        end = c.getTime();
        TimeRange expected3 = of(begin, end);

        List<Token> tokens4 = asList(
                of("last", WORD),
                of("3", NUMBER),
                of("years", WORD)
        );
        c.setTimeInMillis(NOW);
        c.add(Calendar.YEAR, -3);
        begin = c.getTime();
        c.setTimeInMillis(NOW);
        end = c.getTime();
        TimeRange expected4 = of(begin, end);


        return new Object[][] {
                {tokens1, expected1},
                {tokens2, expected2},
                {tokens3, expected3},
                {tokens4, expected4},
        };
    }

    @UseDataProvider("dataProviderInit")
    @Test
    public void testParse(List<Token> tokens, TimeRange expected) {
        ParsedQuery parsedQuery = new ParsedQuery();
        TimeParser parser = new TimeParser();
        parser.parseTimeRange(parsedQuery, tokens, new BitSet(tokens.size()));

        Assert.assertNotNull( parsedQuery.getTimeRange() );
        Assert.assertTrue( TIME_RANGE_COMPARATOR.equals(expected, parsedQuery.getTimeRange()) );
    }
}
