package io.piano.nlp.processor.domain.parsing;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import lombok.*;

import static com.google.common.base.Strings.isNullOrEmpty;
import static io.piano.nlp.processor.domain.parsing.InterpretationCategory.TERM;

/**
 * Represents possible interpretation of the token in ParsedQuery object as its attribute with some value
 * or of corresponding category.
 *
 * Created by Dima on 09.06.2018.
 */
@SuppressWarnings("WeakerAccess")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Interpretation {
    private InterpretationCategory category;
    private TIntList subCategories;
    private String value;

    public Interpretation() {
        this.subCategories = new TIntArrayList();
    }

    public Interpretation(InterpretationCategory category) {
        this.category = category;
        this.subCategories = new TIntArrayList();
    }


    public boolean isFull() {
        boolean hasValue = ! isNullOrEmpty(value);
        return (category != TERM)
                ? subCategories.size() != 0 && hasValue
                : hasValue;
    }

    public boolean conflictsWith(Interpretation other) {
        return  (subCategories.size() != 0 && subCategories.size() != 0 && other.subCategories != subCategories)
                ||  ! (isNullOrEmpty(value) || isNullOrEmpty(value) || other.value.equals(value))
                || ! this.equals(other);
    }

    public Interpretation append(Interpretation other) {
        if (this.subCategories.size() != 0) {
            this.subCategories = other.subCategories;
        }

        if ( isNullOrEmpty(this.value) ) {
            this.value = other.value;
        }

        return this;
    }
}
