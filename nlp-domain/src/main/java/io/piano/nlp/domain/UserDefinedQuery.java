package io.piano.nlp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;

/**
 * Represents the container of user defined query and its attributes if any.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDefinedQuery {
    @Nonnull
    private String query;
}
