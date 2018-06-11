package io.piano.nlp.domain.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Represents range of time.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class TimeRange {
    private Date begin;
    private Date end;
}
