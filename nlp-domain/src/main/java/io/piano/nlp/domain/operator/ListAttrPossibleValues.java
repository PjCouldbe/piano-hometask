package io.piano.nlp.domain.operator;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents concrete list of possible attribute values.
 *
 * Created by Dima on 02.06.2018.
 */
@AllArgsConstructor
public class ListAttrPossibleValues implements AttrPossibleValues {
    private List<String> values;

    @Override
    public String expectValuesQualifier() {
        return "list";
    }

    @Override
    public List<String> getValuesList() {
        return values;
    }

    @Override
    public boolean supprotsAttrValue(String attrValue) {
        return values.contains(attrValue);
    }
}
