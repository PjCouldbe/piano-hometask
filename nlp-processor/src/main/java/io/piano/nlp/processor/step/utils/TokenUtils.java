package io.piano.nlp.processor.step.utils;

import io.piano.nlp.shared.Token;

/**
 * Utilities ofr various manipulation orchecks for tokens.
 *
 * Created by Dima on 03.06.2018.
 */
public class TokenUtils {
    public boolean isAuxiliary(Token t) {
        final String posTag = t.getPOSTag();
        return ("CC|DT|IN|RP|TO|UH").contains(posTag);
    }
}
