package io.piano.nlp.processor.utils;

import io.piano.nlp.shared.Token;

import java.util.List;

/**
 * Utility generalizingiterating by tokebns collection to right and to left executing some processings.
 *
 * Created by Dima on 06.06.2018.
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TokenExpansionUtils {
    public boolean tryExpanseRight(List<Token> tokens, int currIndex, TokenConsumer tokenConsumer) {
        return tryExpanse(tokens, currIndex, 1, tokenConsumer);
    }

    public boolean tryExpanseLeft(List<Token> tokens, int currIndex, TokenConsumer tokenConsumer) {
        return tryExpanse(tokens, currIndex, -1, tokenConsumer);
    }

    private boolean tryExpanse(List<Token> tokens, int currIndex, int step, TokenConsumer tokenConsumer) {
        if (step == 0) return false;

        for (int i = currIndex + step; (step > 0 ? i < tokens.size() : i >= 0); i += step) {
            boolean operationSucceeded = tokenConsumer.apply(tokens.get(i), i);
            if ( ! operationSucceeded) {
                return false;
            }
        }

        return true;
    }
}
