package io.piano.nlp.test;

import io.piano.nlp.processor.step.ExtrasRemovingStep;
import io.piano.nlp.processor.step.POSTagger;
import io.piano.nlp.processor.step.SynonymsResolvingStep;
import io.piano.nlp.shared.Token;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.piano.nlp.shared.TokenType.*;

/**
 * Tests resolving synonyms pipeline step.
 *
 * Created by Dima on 03.06.2018.
 */
public class TestResolveSynonyms extends BaseNLPTest {
    @Test
    public void testExtrasRemoving() {
        List<Token> tokens = Arrays.asList(
                of("how", WORD),  of("many", WORD),
                of("cancelled", WORD),  of("subscriptions", WORD),
                of("does", WORD),  of("my", WORD),
                of("a", WORD),
                of("site", WORD),  of("had", WORD),
                of("between", WORD),  of("June", WORD),
                of(",", PUNCT),  of("1", NUMBER),
                of("and", WORD),  of("July", WORD),
                of(",", PUNCT),  of("15", NUMBER),
                of("?", PUNCT)
        );

        List<Token> expected = Arrays.asList(
                of("<COUNT>", WORD),
                of("churned", WORD),    of("subscriptions", WORD),
                of("1", NUMBER), of("site", WORD),
                of("between", WORD),
                of("June", WORD),   of("1", NUMBER),
                of("and", WORD),
                of("July", WORD),   of("15", NUMBER)
        );

        List<Token> actual = new ExtrasRemovingStep().filterExtraTokens(
                new POSTagger().assignPOSTags(tokens)
        );
        actual = new SynonymsResolvingStep().resolveSynonyms(actual);

        Assert.assertTrue( CollectionUtils.isEqualCollection(expected, actual) );
    }
}
