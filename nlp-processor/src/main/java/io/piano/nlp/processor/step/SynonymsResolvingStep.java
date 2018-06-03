package io.piano.nlp.processor.step;

import io.piano.nlp.processor.utils.SynonymsResolver;
import io.piano.nlp.shared.Token;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.piano.nlp.shared.TokenType.NUMBER;
import static io.piano.nlp.shared.TokenType.PUNCT;
import static io.piano.nlp.shared.TokenType.WORD;
import static java.util.Arrays.asList;

/**
 * Step of pipeline that replaces tokens that has synonyms by default predefined one.
 *
 * Created by Dima on 02.06.2018.
 */
public class SynonymsResolvingStep {
    private static final SynonymsResolver SYN_RESOLVER = new SynonymsResolver();

    public List<Token> resolveSynonyms(List<Token> tokens) {
        //Try to replace tokens one-by-one
        List<Token> res = new ArrayList<>( tokens.size() );
        for (Token t : tokens) {
            res.addAll(
                    asList( resolveSynonymsForToken(t) )
            );
        }
        tokens = res;

        //Try to replace tokens by two
        res = new ArrayList<>( tokens.size() );
        Token[] resTokens = new Token[0];
        for (int i = 0; i < tokens.size() - 1; ) {
            resTokens = resolveSynonymsForToken(tokens.get(i), tokens.get(i+1));
            res.add(resTokens[0]);
            if (resTokens.length == 1) {
                i += 2;
            } else {
                i++;
            }
        }
        if (resTokens.length > 1) {
            res.addAll( Arrays.asList(resTokens).subList(1, resTokens.length) );
        }

        return res;
    }


    private Token[] resolveSynonymsForToken(Token... ts) {
        for (Token t : ts) {
            if (t.getType() == PUNCT || t.getType() == NUMBER) return ts;
        }

        String defWord = SYN_RESOLVER.getDefaultWordBySynonym(
                Arrays.stream(ts).map(Token::getText).toArray(String[]::new)
        );
        if (defWord.contains(" ")) return ts;

        if ( StringUtils.isNumeric(defWord) ) {
            return new Token[] {
                    new Token(defWord, NUMBER, null)
            };
        } else {
            return new Token[] {
                    new Token(defWord, WORD, ts[0].getPOSTag() )
            };
        }
    }
}
