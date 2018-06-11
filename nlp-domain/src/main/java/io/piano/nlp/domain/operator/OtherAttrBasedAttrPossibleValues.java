package io.piano.nlp.domain.operator;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Set of possible values is specified by another attribute and its value.
 *
 * Created by Dima on 02.06.2018.
 */
@AllArgsConstructor
public class OtherAttrBasedAttrPossibleValues implements AttrPossibleValues {
    private static final AttributeToValuesResolver RESOLVER = new AttributeToValuesResolver();

    private String baseAttr;
    private String baseAttrValue;
    private String attr;

    @Override
    public String expectValuesQualifier() {
        return "other-attr";
    }

    @Override
    public List<String> getValuesList() {
        AttrPossibleValues values = RESOLVER.getPossibleValueForAttrBasedOnOtherAttr(attr, baseAttr, baseAttrValue);
        return (values == null || values.getClass() == this.getClass() )
                ? Collections.emptyList()
                : values.getValuesList();
    }

    @Override
    public boolean supprotsAttrValue(String attrValue) {
        if (attrValue == null) return false;

        AttrPossibleValues values = RESOLVER.getPossibleValueForAttrBasedOnOtherAttr(attr, baseAttr, baseAttrValue);
        return ! (values == null || values.getClass() == this.getClass())
                && values.supprotsAttrValue(attrValue);
    }
}
