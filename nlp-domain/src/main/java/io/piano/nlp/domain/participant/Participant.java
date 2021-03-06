package io.piano.nlp.domain.participant;

import lombok.*;

/**
 * Subject which influence onto conversion event somehow. His role reflected in the qualifier and its id (name) - in string value.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Participant {
    private ParticipantQualifier qualifier;
    private String value;
}
