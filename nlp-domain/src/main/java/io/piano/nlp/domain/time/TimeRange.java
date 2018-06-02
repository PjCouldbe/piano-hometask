package io.piano.nlp.domain.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents range of time.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimeRange {
    private Date begin;
    private Date end;
}