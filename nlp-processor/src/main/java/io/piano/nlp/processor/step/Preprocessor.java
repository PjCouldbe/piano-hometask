package io.piano.nlp.processor.step;

import io.piano.nlp.domain.UserDefinedQuery;

/**
 * Step of pipeline that preprocesses the input to prepare it for NLP processing
 *
 * Created by Dima on 02.06.2018.
 */
public class Preprocessor {
    public String preprocess(UserDefinedQuery query) {
        return query.getQuery();
    }
}
