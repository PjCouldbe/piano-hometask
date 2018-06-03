package io.piano.nlp.processor.step;

import io.piano.nlp.shared.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.piano.nlp.shared.TokenType.NUMBER;
import static io.piano.nlp.shared.TokenType.PUNCT;

/**
 * Step of pipeline that removes tokens from user's query that are not valuable ofr current processing goal.
 * Works based on text, type and POSTag info
 *
 * Created by Dima on 02.06.2018.
 */
public class ExtrasRemovingStep {
    public List<Token> filterExtraTokens(List<Token> tokens) {
        List<Token> res = new ArrayList<>( tokens.size() );
        for (Token t : tokens) {
            if ( isTokenValuable(t) ) {
                res.add(t);
            }
        }

        return res;
    }

    private boolean isTokenValuable(Token t) {
        if (t.getType() == PUNCT) return false;

        if (t.getType() == NUMBER) return true;

        String posTag = t.getPOSTag();
        String text = t.getText();
        if (posTag == null) return true;

        //noinspection SimplifiableIfStatement
        if (posTag.startsWith("V") && ! posTag.equals("VBG")
                || posTag.startsWith("W") && ! text.equals("how")
                || posTag.startsWith("PRP")) {
            return false;
        }

        return ! textEqualsToAny(text, "the", "for", "with");
    }

    private boolean textEqualsToAny(String text, String... args) {
        return Arrays.asList(args).contains(text);
    }
}
