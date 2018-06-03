package io.piano.nlp.test;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.UserDefinedQuery;
import io.piano.nlp.processor.ParseQueryPipeline;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * Tests examples of various possible user queries parsing to object representation.
 *
 * Created by Dima on 03.06.2018.
 */
@RunWith(DataProviderRunner.class)
public class TestParseQuery {
    private static final ParseQueryPipeline pipeline = ParseQueryPipeline.initialize();

    @DataProvider
    public Object[][] dataProviderInit() {
        String text1 = "";
        ParsedQuery expected1 = new ParsedQuery();

        return new Object[][] {
                {text1, expected1},

        };
    }

    @UseDataProvider("dataProviderInit")
    public void testParse(String text, ParsedQuery expected) {
        ParsedQuery actual = pipeline.run( new UserDefinedQuery(text) );
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }
}
