package io.piano.nlp.processor.domain.parsing;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by Dima on 06.06.2018.
 */
public class TimeQueryPossibleModBuffer {
    private TIntObjectMap<TimeQueryPossibleMod> map = new TIntObjectHashMap<>();

    public void putNew(int calendarField) {
        map.put(calendarField, new TimeQueryPossibleMod(calendarField));
    }

    public TimeQueryPossibleMod get(int calendarField) {
        return map.get(calendarField);
    }
}
