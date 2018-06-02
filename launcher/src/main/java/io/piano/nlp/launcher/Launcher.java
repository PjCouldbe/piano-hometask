package io.piano.nlp.launcher;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.UserDefinedQuery;
import io.piano.nlp.processor.ParseQueryPipeline;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Launcher for demonstrating parser working on some inputs.
 *
 * Created by Dima on 02.06.2018.
 */
public class Launcher {
    public static void main(String[] args) {

    }

    private void parseAndShowOutputs(List<String> userQueriesStrings) {
        ParseQueryPipeline pipeline = ParseQueryPipeline.initialize();

        for (String query : userQueriesStrings) {
            ParsedQuery parsedQuery = pipeline.run( new UserDefinedQuery(query) );
            System.out.println(parsedQuery);
        }
    }

    private List<String> defineInputs() {
        return asList(
                "Show me number of conversion for Annual term for last month",
                "Show me conversion rate by browser",
                "What location has the highest conversion rate?",
                "Is my churn rate improving?",
                "Show me conversions for the past week",
                "Show me how many conversions I have had",
                "How many cancelled subscriptions do I have?",
                "How many subscriptions cancelled last week?",
                "What are the 8 locations with the highest conversion rate?",
                "How many cancelled subscriptions does my site had between June, 1 and July, 15?"
        );
    }
}
