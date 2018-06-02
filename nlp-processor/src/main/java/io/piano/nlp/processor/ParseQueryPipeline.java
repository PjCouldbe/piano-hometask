package io.piano.nlp.processor;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.UserDefinedQuery;
import io.piano.nlp.processor.step.*;
import io.piano.nlp.shared.Token;

import java.util.List;

/**
 * Pipeline that handles UserDefinedQuery to obtain ParsedQuery for it and give it to backend
 *
 * Created by Dima on 02.06.2018.
 */
@SuppressWarnings("WeakerAccess")
public class ParseQueryPipeline {
    private ParseQueryPipeline() {

    }

    public static ParseQueryPipeline initialize() {
        return new ParseQueryPipeline();
    }

    public ParsedQuery run(UserDefinedQuery userQuery) {
        String text = new Preprocessor().preprocess(userQuery);

        List<Token> tokens = new Tokenizer().getTokens(text);
        tokens = new POSTagger().assignPOSTags(tokens);
        tokens = new ExtrasRemovingStep().filterExtraTokens(tokens);
        tokens = new SynonymsResolvingStep().resolveSynonyms(tokens);

        return new ParsingStep().parse(tokens);
    }
}
