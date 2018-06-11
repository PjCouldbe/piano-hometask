package io.piano.nlp.test.steps;

import io.piano.nlp.processor.step.ExtrasRemovingStep;
import io.piano.nlp.processor.step.POSTagger;
import io.piano.nlp.shared.Token;
import io.piano.nlp.test.BaseNLPTest;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.piano.nlp.shared.TokenType.*;

/**
 * Tests removing extra tokens pipeline step.
 *
 * Created by Dima on 03.06.2018.
 */
public class TestExtrasRemoving extends BaseNLPTest {
    @Test
    public void testExtrasRemoving() {
        List<Token> tokens = Arrays.asList(
                of("how", WORD),  of("many", WORD),
                of("cancelled", WORD),  of("subscriptions", WORD),
                of("does", WORD),  of("my", WORD),
                of("site", WORD),  of("had", WORD),
                of("between", WORD),  of("June", WORD),
                of(",", PUNCT),  of("1", NUMBER),
                of("and", WORD),  of("July", WORD),
                of(",", PUNCT),  of("15", NUMBER),
                of("?", PUNCT)
        );

        List<Token> expected = Arrays.asList(
                of("how", WORD),    of("many", WORD),
                of("cancelled", WORD),    of("subscriptions", WORD),
                of("site", WORD),
                of("June", WORD),   of("1", NUMBER),
                of("and", WORD),
                of("July", WORD),   of("15", NUMBER)
        );
        List<Token> actual = new ExtrasRemovingStep().filterExtraTokens(
                new POSTagger().assignPOSTags(tokens)
        );

        Assert.assertTrue( CollectionUtils.isEqualCollection(expected, actual) );
    }
}
