package io.piano.nlp.test;

import io.piano.nlp.domain.time.TimeRange;
import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;

import java.util.Date;

/**
 * Parent for all NLP tests containing base service methods
 *
 * Created by Dima on 03.06.2018.
 */
abstract class BaseNLPTest {
    Token of(String text, TokenType type) {
        return new Token(text, type);
    }

    TimeRange of(Date from, Date to) {
        return new TimeRange(from, to);
    }
}
