package io.piano.nlp.processor.domain.parsing;

import io.piano.nlp.domain.operator.AttrPossibleValues;
import io.piano.nlp.domain.operator.AttributeToValuesResolver;
import io.piano.nlp.processor.utils.TokenUtils;
import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents of attribute of ParsedQuery object to which the corresponding Interpretation applies for.
 *
 * Created by Dima on 09.06.2018.
 */
@AllArgsConstructor
public enum InterpretationCategory {
    LOCATION(
            t -> false,
            t -> {
                TokenUtils utils = new TokenUtils();
                String word = utils.normalizeText(t);

                return utils.isCapitalized(t) || word.equals("location")
                        || Optional.ofNullable(
                                new AttributeToValuesResolver().getPossibleValueForAttrBasedOnOtherAttr(
                                        "groupValue", "groupQualifier", "location")
                           )
                           .map(AttrPossibleValues::getValuesList)
                           .filter(lst -> lst.contains(word)).isPresent();
            }),
    TERM(
            t -> false,
            t -> {
                TokenUtils utils = new TokenUtils();
                return utils.normalizeText(t).equals("term") || utils.isCapitalized(t) || utils.isQouted(t);
            }
    ),
    TOOL(
            t -> false,
            t -> {
                String word = new TokenUtils().normalizeText(t);
                return ! Arrays.asList("term", "location").contains(word);
            }
    ),
    FUNCTION(
            t -> t.getType() == TokenType.NUMBER || new TokenUtils().isAuxiliary(t),
            t -> t.getText().toLowerCase().equals( t.getText() )
    );

    private Predicate<Token> byTokenExactConditions;
    private Predicate<Token> byTokenPassConditions;


    public static InterpretationCategory[] of(Token t) {
        InterpretationCategory[] values = InterpretationCategory.values();

        for (InterpretationCategory ic : values) {
            if ( ic.byTokenExactConditions.test(t) ) {
                return new InterpretationCategory[] {ic};
            }
        }

        List<InterpretationCategory> lst = new ArrayList<>(values.length);
        for (InterpretationCategory ic : values) {
            if ( ic.byTokenPassConditions.test(t) ) {
                lst.add(ic);
            }
        }

        return lst.toArray( new InterpretationCategory[lst.size()] );
    }
}
