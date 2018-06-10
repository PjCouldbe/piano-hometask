package io.piano.nlp.processor.utils;

import io.piano.nlp.shared.Token;

/**
 * Function for token and its index in source collection and returning boolean flag of execution success.
 *
 * Created by Dima on 06.06.2018.
 */
@FunctionalInterface
public interface TokenConsumer {
    boolean apply(Token t, int tokenIndex);
}
