package io.piano.nlp.domain.operator;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Represent sequence of result operators that should be applied to result objects set to satisfy user requirements.
 *
 * Created by Dima on 02.06.2018.
 */
@AllArgsConstructor
@ToString
public class ResultOperatorsDescriptor implements Iterable<ResultOperator> {
    private ResultOperator[] operators;

    @SuppressWarnings("WeakerAccess")
    public ResultOperator[] getOperatorInExecutionOrder() {
        return operators;
    }


    @Override
    @Nonnull
    public Iterator<ResultOperator> iterator() {
        return Arrays.asList( getOperatorInExecutionOrder() ).iterator();
    }
}
