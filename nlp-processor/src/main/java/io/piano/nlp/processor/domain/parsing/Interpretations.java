package io.piano.nlp.processor.domain.parsing;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Possible interpretaions set of single token.
 *
 * Created by Dima on 09.06.2018.
 */
public class Interpretations {
    private Interpretation[] interpretations;

    public Interpretations() {
        this.interpretations = new Interpretation[ InterpretationCategory.values().length ];
    }

    private Interpretations(Interpretation[] interpretations) {
        this();
        for (Interpretation i : interpretations) {
            this.interpretations[ i.getCategory().ordinal() ] = i;
        }
    }

    public Interpretations(List<Interpretation> interpretations) {
        this(
                interpretations.toArray( new Interpretation[interpretations.size()] )
        );
    }


    public Interpretation get(InterpretationCategory category) {
        return interpretations[category.ordinal()];
    }

    public void set(InterpretationCategory category, Interpretation interpretation) {
        interpretations[category.ordinal()] = interpretation;
    }

    public List<Interpretation> merge(Interpretations other) {
        List<Interpretation> mergedInterpretations = IntStream.range(0, InterpretationCategory.values().length)
                .filter(i -> interpretations[i] != null && other.interpretations[i] != null)
                .filter(i -> ! interpretations[i].conflictsWith(other.interpretations[i]))
                .mapToObj(i -> interpretations[i].append(other.interpretations[i]))
                .collect( Collectors.toList() );

        return mergedInterpretations.stream()
                .filter(Interpretation::isFull)
                .findFirst().map(Collections::singletonList)
                .orElse(mergedInterpretations);
    }

    @Nullable
    public Interpretation getFull() {
        for (Interpretation inter : interpretations) {
            if (inter != null && inter.getCategory() != InterpretationCategory.TERM && inter.isFull()) {
                return inter;
            }
        }

        return null;
    }
}
