package io.piano.nlp.processor.utils;

import io.piano.nlp.shared.Token;

import static java.lang.Character.isUpperCase;

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

    public String normalizeText(Token t) {
        String tokenText = t.getText();
        return tokenText.replaceAll("e?s($|\")", "s")
                .replaceAll("ies($|\")", "y");
    }

    public boolean isCapitalized(Token t) {
        String tokenText = t.getText();
        return isUpperCase( tokenText.charAt(0) ) && ! isUppercase( tokenText.substring(1) );
    }
    private boolean isUppercase(String text) {
        return text.toUpperCase().equals(text);
    }

    public boolean isQouted(Token t) {
        String text = t.getText();
        return text.charAt(0) == '\"' && text.charAt(text.length() - 1) == '\"';
    }
}
