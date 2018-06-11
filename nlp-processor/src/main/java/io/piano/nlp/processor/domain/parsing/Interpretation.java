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
@EqualsAndHashCode
@ToString
@AllArgsConstructor
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
        return  category != other.category
                || subCategoriesConflicts(other)
                || valuesConflicts(other);
    }
    private boolean subCategoriesConflicts(Interpretation other) {
        if (subCategories.size() == 0 || other.subCategories.size() == 0
                || other.subCategories.equals(subCategories) ) {
            return false;
        }

        for (int i = 0; i < Math.min(subCategories.size(), other.subCategories.size()); i++) {
            if (subCategories.get(i) != other.subCategories.get(i)) {
                return true;
            }
        }

        return false;
    }
    private boolean valuesConflicts(Interpretation other) {
        return ! (isNullOrEmpty(value) || isNullOrEmpty(other.value) || other.value.equals(value))
                && ! (category == TERM && (value.contains("term") || other.value.contains("term")) );
    }


    public Interpretation append(Interpretation other) {
        this.subCategories = this.subCategories.size() >= other.subCategories.size()
                ? this.subCategories
                : other.subCategories;

        if ( isNullOrEmpty(this.value) || (category == TERM && this.value.contains("term")) ) {
            this.value = other.value;
        }

        return this;
    }
}
