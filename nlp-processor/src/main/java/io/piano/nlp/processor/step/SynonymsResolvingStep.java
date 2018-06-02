package io.piano.nlp.processor.step;

import io.piano.nlp.processor.utils.SynonymsResolver;
import io.piano.nlp.shared.Token;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.piano.nlp.shared.TokenType.NUMBER;
import static io.piano.nlp.shared.TokenType.PUNCT;
import static io.piano.nlp.shared.TokenType.WORD;

/**
 * Step of pipeline that replaces tokens that has synonyms by default predefined one.
 *
 * Created by Dima on 02.06.2018.
 */
public class SynonymsResolvingStep {
    private static final SynonymsResolver SYN_RESOLVER = new SynonymsResolver();

    public List<Token> resolveSynonyms(List<Token> tokens) {
        List<Token> res = new ArrayList<>( tokens.size() );
        for (Token t : tokens) {
            res.add( resolveSynonymsForToken(t) );
        }

        return res;
    }


    private Token resolveSynonymsForToken(Token t) {
        if (t.getType() == PUNCT || t.getType() == NUMBER) return t;

        String defWord = SYN_RESOLVER.getDefaultWordBySynonym( t.getText() );
        if ( StringUtils.isNumeric(defWord) ) {
            return new Token(defWord, NUMBER, null);
        } else {
            return new Token(defWord, WORD, t.getPOSTag() );
        }
    }
}
