package io.piano.nlp.processor.step;

import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.piano.nlp.shared.TokenType.*;

/**
 * Step of pipeline that tokenizes the user query's text.
 *
 * Created by Dima on 02.06.2018.
 */
public class Tokenizer {
    public List<Token> getTokens(String text) {
        final String puncts = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        final String splitPattern = "(?=\\p{Punct})|\\s+";
        final String[] parts = text.split(splitPattern);

        List<Token> res = new ArrayList<>(parts.length);
        StringBuilder largeToken = new StringBuilder();
        int quotesStack = 0;
        for (String s : parts) {
            if ( isQoute(s) ) {
                if (quotesStack > 0) {
                    quotesStack--;
                    res.add( new Token(largeToken.toString(), WORD) );
                    largeToken = new StringBuilder();
                } else {
                    quotesStack++;
                    largeToken = new StringBuilder(s);
                }
            }

            if (quotesStack > 1) {
                largeToken.append(' ').append(s);
            } else {
                TokenType type;
                if (puncts.contains(s)) {
                    type = PUNCT;
                } else if ( StringUtils.isNumeric(s) ) {
                    type = NUMBER;
                } else {
                    type = WORD;
                }

                Token t = new Token(s, type);
                res.add(t);
            }
        }

        return res;
    }

    private boolean isQoute(String s) {
        if (s.length() != 1) return false;

        char c = s.charAt(0);
        return c == '\"';
    }
}
