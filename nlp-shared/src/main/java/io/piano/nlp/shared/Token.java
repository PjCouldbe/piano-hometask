package io.piano.nlp.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the single token in user defined query for conversion.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@AllArgsConstructor
public class Token {
    private String text;
    private TokenType type;
    @Setter
    private String POSTag;

    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }
}
