package io.piano.nlp.domain.operator;

import java.util.Collections;
import java.util.List;

/**
 * This implementation defines that specified attribute may have any string value or concrete value set is specified in external place.
 *
 * Created by Dima on 02.06.2018.
 */
public class AnyAttrPossibleValues implements AttrPossibleValues {
    @Override
    public String expectValuesQualifier() {
        return "any";
    }

    @Override
    public List<String> getValuesList() {
        return Collections.emptyList();
    }

    @Override
    public boolean supprotsAttrValue(String attrValue) {
        return true;
    }
}
