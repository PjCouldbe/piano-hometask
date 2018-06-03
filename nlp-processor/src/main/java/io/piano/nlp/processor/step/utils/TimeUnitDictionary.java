package io.piano.nlp.processor.step.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dima on 03.06.2018.
 */
@SuppressWarnings("UnusedAssignment")
public class TimeUnitDictionary {
    private static final Map<String, Integer> timeUnitToIds = new HashMap<>();

    static {
        int id = 0;

        timeUnitToIds.put("year", id++);
        timeUnitToIds.put("month", id++);
        timeUnitToIds.put("week", id++);
        timeUnitToIds.put("hour", id++);
        timeUnitToIds.put("minute", id++);

        timeUnitToIds.put("january", id++);
        timeUnitToIds.put("february", id++);
        timeUnitToIds.put("march", id++);
        timeUnitToIds.put("april", id++);
        timeUnitToIds.put("may", id++);
        timeUnitToIds.put("june", id++);
        timeUnitToIds.put("july", id++);
        timeUnitToIds.put("august", id++);
        timeUnitToIds.put("september", id++);
        timeUnitToIds.put("october", id++);
        timeUnitToIds.put("november", id++);
        timeUnitToIds.put("december", id++);

        timeUnitToIds.put("monday", id++);
        timeUnitToIds.put("tuesday", id++);
        timeUnitToIds.put("wednesday", id++);
        timeUnitToIds.put("thursday", id++);
        timeUnitToIds.put("friday", id++);
        timeUnitToIds.put("saturday", id++);
        timeUnitToIds.put("sunday", id++);
    }

    public boolean isTimeUnit(String word) {
        return timeUnitToIds.containsKey( word.toLowerCase() );
    }

    public boolean isGeneralTimeUnit(String word) {
        int unitId = timeUnitToIds.get(word);
        return unitId >= timeUnitToIds.get("year") && unitId <= timeUnitToIds.get("minute");
    }

    public boolean isMonth(String word) {
        int unitId = timeUnitToIds.get(word);
        return unitId >= timeUnitToIds.get("january") && unitId <= timeUnitToIds.get("december");
    }

    public boolean isDayOfWeek(String word) {
        int unitId = timeUnitToIds.get(word);
        return unitId >= timeUnitToIds.get("monday") && unitId <= timeUnitToIds.get("sunday");
    }
}
