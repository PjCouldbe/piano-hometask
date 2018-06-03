package io.piano.nlp.processor.step.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dima on 03.06.2018.
 */
@Getter
@Setter
@NoArgsConstructor
public class TimeQuerySetting {
    private int year = -1;
    private int yearMod = -1;
    private int month = -1;
    private int monthMod = -1;
    private int weekMod = -1;
    private int day = -1;
    private int dayMod = -1;
    private int hour = -1;
    private int hourMod = -1;
    private int minute = -1;
    private int minuteMod = -1;

    public Date getResultTime() {
        Calendar c = Calendar.getInstance();

        if (year != -1) {
            c.set(Calendar.YEAR, year);
        }
        if (yearMod != -1) {
            c.add(Calendar.YEAR, yearMod);
        }
        if (month != -1) {
            c.set(Calendar.MONTH, month);
        }
        if (monthMod != -1) {
            c.add(Calendar.MONTH, monthMod);
        }
        if (weekMod != -1) {
            c.add(Calendar.WEEK_OF_MONTH, weekMod);
        }
        if (day != -1) {
            c.set(Calendar.DAY_OF_MONTH, day);
        }
        if (dayMod != -1) {
            c.add(Calendar.DAY_OF_MONTH, dayMod);
        }
        if (hour != -1) {
            c.set(Calendar.HOUR, hour);
        }
        if (hourMod != -1) {
            c.add(Calendar.HOUR, hourMod);
        }
        if (minute != -1) {
            c.set(Calendar.MINUTE, minute);
        }
        if (minuteMod != -1) {
            c.add(Calendar.MINUTE, minuteMod);
        }


        return c.getTime();
    }
}
