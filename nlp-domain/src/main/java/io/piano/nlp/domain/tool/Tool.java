package io.piano.nlp.domain.tool;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.EnumMap;

/**
 * Represent information about instruments that user used for conversion performing.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Tool {
    private EnumMap<ToolQualifier, String> attrs;

    public Tool() {
        attrs = new EnumMap<>(ToolQualifier.class);
    }

    public String get(ToolQualifier qualifier) {
        return attrs.get(qualifier);
    }

    public ToolQualifier[] getNonNullQualifiers() {
        return attrs.keySet().toArray( new ToolQualifier[0] );
    }

    public Tool add(ToolQualifier qualifier, String toolValue) {
        this.attrs.put(qualifier, toolValue);
        return this;
    }
}
