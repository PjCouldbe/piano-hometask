package io.piano.nlp.test;

import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;

/**
 * Parent for all NLP tests containing base service methods
 *
 * Created by Dima on 03.06.2018.
 */
abstract class BaseNLPTest {
    Token of(String text, TokenType type) {
        return new Token(text, type);
    }
}
