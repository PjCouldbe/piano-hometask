package io.piano.nlp.domain.operator;

import java.util.Collections;
import java.util.List;

/**
 * This implementation defines that specified attribute may have any only numeric values.
 * So, the number parsing from string representation should be successfull.
 *
 * Created by Dima on 02.06.2018.
 */
public class NumericAttrPossibleValues implements AttrPossibleValues {
    @Override
    public String expectValuesQualifier() {
        return "number";
    }

    @Override
    public List<String> getValuesList() {
        return Collections.emptyList();
    }

    @Override
    public boolean supprotsAttrValue(String attrValue) {
        try {
            int i = Integer.parseInt(attrValue);
            return i >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
