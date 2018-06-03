package io.piano.nlp.processor.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class represents synonyms dictionary for synonyms resolving step.
 *
 * Created by Dima on 02.06.2018.
 */
public class SynonymsResolver {
    private Map<String, String> synonymsDictionary = new HashMap<>(200);

    {
        synonymsDictionary.put("location", "<LOCATION>");
        synonymsDictionary.put("place",    "<LOCATION>");
        synonymsDictionary.put("area",     "<LOCATION>");

        synonymsDictionary.put("town", "city");

        synonymsDictionary.put("keyword", "term");

        synonymsDictionary.put("cancelled", "churned");

        synonymsDictionary.put("a",  "1");
        synonymsDictionary.put("an", "1");

        synonymsDictionary.put("how many", "<COUNT>");
        synonymsDictionary.put("how much", "<COUNT>");

        synonymsDictionary.put("highest", "<MAX>");
        synonymsDictionary.put("biggest", "<MAX>");
        synonymsDictionary.put("largest", "<MAX>");
        synonymsDictionary.put("best",    "<MAX>");

        synonymsDictionary.put("smallest",    "<MIN>");
        synonymsDictionary.put("worst",       "<MIN>");
        synonymsDictionary.put("most little", "<MIN>");

        synonymsDictionary.put("improv",  "<GROWS>");
        synonymsDictionary.put("enhanc",  "<GROWS>");
        synonymsDictionary.put("increas", "<GROWS>");
        synonymsDictionary.put("magnify", "<GROWS>");

        synonymsDictionary.put("reduc",    "<FALLS>");
        synonymsDictionary.put("fall",     "<FALLS>");
        synonymsDictionary.put("diminish", "<FALLS>");
        synonymsDictionary.put("decreas",  "<FALLS>");

        synonymsDictionary.put("number of", "<PAGE>");
    }

    public String getDefaultWordBySynonym(String... words) {
        String unioned = Arrays.stream(words).collect(Collectors.joining(" "));
        return synonymsDictionary.getOrDefault(unioned, unioned);
    }
}
