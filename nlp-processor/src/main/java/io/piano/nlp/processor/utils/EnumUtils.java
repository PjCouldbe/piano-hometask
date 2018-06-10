package io.piano.nlp.processor.utils;

import javax.annotation.Nullable;

/**
 * Utils that supports methods for generic enum data manipulations.
 *
 * Created by Dima on 09.06.2018.
 */
public class EnumUtils {
    @Nullable
    public <E extends Enum<E>> E valueOf(String s, E[] values) {
        for (E v : values) {
            if ( v.name().toLowerCase().equals(s) ) {
                return v;
            }
        }

        return null;
    }
}
