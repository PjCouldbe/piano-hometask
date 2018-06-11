package io.piano.nlp.test;

import io.piano.nlp.test.parsers.TestAsIsParser;
import io.piano.nlp.test.parsers.TestGeneralParser;
import io.piano.nlp.test.parsers.TestMetricsParser;
import io.piano.nlp.test.parsers.TestTimeParser;
import io.piano.nlp.test.steps.TestExtrasRemoving;
import io.piano.nlp.test.steps.TestResolveSynonyms;
import io.piano.nlp.test.steps.TestTokenization;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for all tests single launch entry-point
 *
 * Created by Dima on 11.06.2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestTokenization.class,
        TestExtrasRemoving.class,
        TestResolveSynonyms.class,
//        TestParseQuery.class,

        TestTimeParser.class,
        TestMetricsParser.class,
        TestGeneralParser.class,
        TestAsIsParser.class,
})
public class NLPTestSuite {
}
