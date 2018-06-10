package io.piano.nlp.processor.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Dcitionary for predefined time units during parsing and service operations for parsing step.
 *
 * Created by Dima on 03.06.2018.
 */
@SuppressWarnings({"UnusedAssignment", "WeakerAccess"})
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

    public int getNumberAsDayOfWeek(String word) {
        if (! isDayOfWeek(word)) return -1000;

        int unitId = timeUnitToIds.get(word);
        return unitId - timeUnitToIds.get("monday");
    }

    public int getNumberAsMonth(String word) {
        if (! isMonth(word)) return -1000;

        int unitId = timeUnitToIds.get(word);
        return unitId - timeUnitToIds.get("january");
    }

    public int generalTimeUnitAsCalendarField(String word) {
        return Arrays.stream( Calendar.class.getDeclaredFields() )
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getName().toLowerCase().equals(word))
                .findFirst()
                .map(f -> {
                    try {
                        return (Integer) f.get(Calendar.getInstance());
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    }
                })
                .orElseThrow(IllegalArgumentException::new);
    }
}
