package io.piano.nlp.test;

import io.piano.nlp.processor.step.Tokenizer;
import io.piano.nlp.shared.Token;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.piano.nlp.shared.TokenType.*;

/**
 * Tests the tokenizer step of pipeline.
 *
 * Created by Dima on 03.06.2018.
 */
public class TestTokenization extends BaseNLPTest {
    @Test
    public void testTokenize() {
        final String text = "How many cancelled subscriptions does my site had between June, 1 and July, 15?";
        List<Token> expected = Arrays.asList(
                of("How", WORD),  of("many", WORD),
                of("cancelled", WORD),  of("subscriptions", WORD),
                of("does", WORD),  of("my", WORD),
                of("site", WORD),  of("had", WORD),
                of("between", WORD),  of("June", WORD),
                of(",", PUNCT),  of("1", NUMBER),
                of("and", WORD),  of("July", WORD),
                of(",", PUNCT),  of("15", NUMBER),
                of("?", PUNCT)
        );

        List<Token> actual = new Tokenizer().getTokens(text);
        Assert.assertTrue( CollectionUtils.isEqualCollection(expected, actual) );
    }

    @Test
    public void testTokenize_WithQoutes() {
        final String text = "How many \"Qouted entity\" does?";
        List<Token> expected = Arrays.asList(
                of("How", WORD),
                of("many", WORD),
                of("\"Qouted entity\"", WORD),
                of("does", WORD),
                of("?", PUNCT)
        );

        List<Token> actual = new Tokenizer().getTokens(text);
        Assert.assertTrue( CollectionUtils.isEqualCollection(expected, actual) );
    }
}
