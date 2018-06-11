package io.piano.nlp.domain.operator;

import java.util.List;

/**
 * Represents the possible values set for specified attribute in result operator description.
 * The actual value set depends on qualifier so as on concrete values enumeration.
 *
 * Created by Dima on 02.06.2018.
 */
public interface AttrPossibleValues {
    String expectValuesQualifier();

    List<String> getValuesList();

    boolean supprotsAttrValue(String attrValue);
}
