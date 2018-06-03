package io.piano.nlp.processor.step;

import io.piano.nlp.shared.Token;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Step of pipeline that Assigns POS tags to provided tokens.
 *
 * Created by Dima on 02.06.2018.
 */
public class POSTagger {
    private POSModel model = null;

    public POSTagger() {
        try {
            InputStream modelStream =  getClass().getClassLoader().getResourceAsStream("en-pos-maxent.bin");
            model = new POSModel(modelStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Token> assignPOSTags(List<Token> tokens) {
        POSTaggerME tagger = new POSTaggerME(model);
        String[] posTags = tagger.tag(
                tokens.stream().map(Token::getText).toArray(String[]::new)
        );

        if (posTags.length != tokens.size()) throw new RuntimeException("POS tagging generated " +
                (posTags.length > tokens.size() ? "more" : "less") + " tags than tokens. The processing stops with failure.");

        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).setPOSTag( posTags[i] );
        }

        return tokens;
    }
}
