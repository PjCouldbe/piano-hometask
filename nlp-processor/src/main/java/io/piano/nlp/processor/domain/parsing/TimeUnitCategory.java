package io.piano.nlp.processor.domain.parsing;

/**
 * Category of time unit met in the source text.
 *
 * Created by Dima on 10.06.2018.
 */
public enum TimeUnitCategory {
    GENERAL,
    CONCRETE,
    NUMERIC,
    CONCRETIZING_WORD,
    AND
}
