package io.piano.nlp.domain.operator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Represent sequence of result operators that should be applied to result objects set to satisfy user requirements.
 *
 * Created by Dima on 02.06.2018.
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResultOperatorsDescriptor implements Iterable<ResultOperator> {
    private List<ResultOperator> operators;

    public ResultOperatorsDescriptor() {
        this.operators = new ArrayList<>();
    }

    @SuppressWarnings("WeakerAccess")
    public List<ResultOperator> getOperatorInExecutionOrder() {
        operators.sort( Comparator.comparing(ResultOperator::getOperatorType) );
        return operators;
    }


    @Override
    @Nonnull
    public Iterator<ResultOperator> iterator() {
        return getOperatorInExecutionOrder().iterator();
    }

    public void add(ResultOperator oper) {
        operators.add(oper);
    }

    public void addAll(List<ResultOperator> opers) {
        operators.addAll(opers);
    }
}
