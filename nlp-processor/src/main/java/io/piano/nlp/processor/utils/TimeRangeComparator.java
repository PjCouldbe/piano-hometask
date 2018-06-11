package io.piano.nlp.processor.utils;

import io.piano.nlp.domain.time.TimeRange;

/**
 * Utility for TimeRange instances equality defining.
 *
 * Created by Dima on 10.06.2018.
 */
public class TimeRangeComparator {
    private int diff;

    public TimeRangeComparator() {
        this(1);
    }

    public TimeRangeComparator(int seconds) {
        this.diff = seconds * 1000;
    }


    public boolean equals(TimeRange tr1, TimeRange tr2) {
        if (tr1 == null) return tr2 == null;
        if (tr2 == null) return false;    //(tr1 == null) == false because of upper condition

        long b1 = tr1.getBegin().getTime();
        long e1 = tr1.getEnd().getTime();
        long b2 = tr2.getBegin().getTime();
        long e2 = tr2.getEnd().getTime();

        return Math.abs(b1 - b2) < 2 * diff
                && Math.abs((e1 - b1) - (e2 - b2)) < diff;
    }
}
