package io.piano.nlp.domain.location;

import lombok.*;

/**
 * Represents a single location by whick the user filters its query
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Location {
    private LocationQualifier qualifier;
    private String value;
}
