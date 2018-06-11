package io.piano.nlp.launcher;

import io.piano.nlp.domain.ParsedQuery;
import io.piano.nlp.domain.UserDefinedQuery;
import io.piano.nlp.processor.ParseQueryPipeline;

import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

/**
 * Launcher for demonstrating parser working on some inputs.
 *
 * Created by Dima on 02.06.2018.
 */
public class Launcher {
    private static final ParseQueryPipeline pipeline = ParseQueryPipeline.initialize();

    public static void main(String[] args) {
        new Launcher().run();
    }


    private void run() {
        try (Scanner sc = new Scanner(System.in)) {
            String input = sc.nextLine();

            while (input != null && ! (input.equals("exit")) ) {
                if (input.equals("show demo")) {
                    demoData();
                } else if (input.equals("help")) {
                    System.out.println("How to use this small utility:\n" +
                            "\t- type \"help\" to see readme instructions.\n" +
                            "\t- type \"show demo\" for demo examples parsing from test-task." +
                            "\t- type any other non-empty string for parsing desired input and viewing result." +
                            "\t- type \"exit\" for closing utility.");
                } else if ( ! input.isEmpty()) {
                    parseInput(input);
                    System.out.println("\n\n");
                }

                input = sc.nextLine();
            }
        }
    }

    private void parseInput(String query) {
        ParsedQuery parsedQuery = pipeline.run( new UserDefinedQuery(query) );
        System.out.println(parsedQuery);
    }

    private void demoData() {
        List<String> userQueriesStrings = defineInputs();
        for (String query : userQueriesStrings) {
            parseInput(query);
            System.out.println("\n------------------------------\n");
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
