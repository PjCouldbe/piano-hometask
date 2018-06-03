package io.piano.nlp.domain.operator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Operator that should be applied to result set of conversion values to satisfy user requirements.
 * Represents the description of operation in real query that is to be univocal and quite straightforward for interpretation.
 *
 * Created by Dima on 02.06.2018.
 */
@ToString
@EqualsAndHashCode
public class ResultOperator {
    private static final AttributeToValuesResolver RESOLVER = new AttributeToValuesResolver();

    @Getter
    private OperatorType operatorType;
    private Map<String, String> attrs;

    public ResultOperator(OperatorType operatorType) {
        this.operatorType = operatorType;
        this.attrs = new HashMap<>();
    }

    public String getValueFor(String attr) {
        return attrs.get(attr);
    }

    public boolean addValueFor(String value, String attr) {
        String baseAttrValue = attr.equals("groupValue") ? attrs.get("groupQualifier") : null;
        boolean supprots = Optional.ofNullable( RESOLVER.getPossibleValuesForAttr(attr, baseAttrValue) )
                .filter(o -> o.supprotsAttrValue(value))
                .isPresent();

        if (supprots) {
            attrs.put(attr, value);
            return true;
        } else {
            return false;
        }
    }
}
