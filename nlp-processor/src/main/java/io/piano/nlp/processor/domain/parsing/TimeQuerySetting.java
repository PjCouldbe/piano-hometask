package io.piano.nlp.processor.domain.parsing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

/**
 * Settings of single timestamp to extract time range from query if any.
 *
 * Created by Dima on 03.06.2018.
 */
@Getter
@Setter
@NoArgsConstructor
public class TimeQuerySetting {
    private static final int NO_VALUE = -100000;

    private int year = NO_VALUE;
    private int yearMod = NO_VALUE;
    private int month = NO_VALUE;
    private int monthMod = NO_VALUE;
    private int weekMod = NO_VALUE;
    private int day = NO_VALUE;
    private int dayMod = NO_VALUE;
    private int dayOfWeek = NO_VALUE;
    private int hour = NO_VALUE;
    private int hourMod = NO_VALUE;
    private int minute = NO_VALUE;
    private int minuteMod = NO_VALUE;

    public Date getResultTime() {
        Calendar c = Calendar.getInstance();
        c.setTime( new Date() );

        if (year != NO_VALUE) {
            c.set(Calendar.YEAR, year);
        }
        if (yearMod != NO_VALUE) {
            c.add(Calendar.YEAR, yearMod);
        }
        if (month != NO_VALUE) {
            c.set(Calendar.MONTH, month);
        }
        if (monthMod != NO_VALUE) {
            c.add(Calendar.MONTH, monthMod);
        }
        if (weekMod != NO_VALUE) {
            c.add(Calendar.WEEK_OF_MONTH, weekMod);
        }
        if (day != NO_VALUE) {
            c.set(Calendar.DAY_OF_MONTH, day);
        }
        if (dayMod != NO_VALUE) {
            c.add(Calendar.DAY_OF_MONTH, dayMod);
        }
        if (dayOfWeek != NO_VALUE) {
            c.add(Calendar.DAY_OF_WEEK, dayOfWeek);
        }
        if (hour != NO_VALUE) {
            c.set(Calendar.HOUR, hour);
        }
        if (hourMod != NO_VALUE) {
            c.add(Calendar.HOUR, hourMod);
        }
        if (minute != NO_VALUE) {
            c.set(Calendar.MINUTE, minute);
        }
        if (minuteMod != NO_VALUE) {
            c.add(Calendar.MINUTE, minuteMod);
        }

        return c.getTime();
    }
}
