package io.piano.nlp.domain.operator;

/**
 * Represents the kind of operator that should be applied to result set of conversion values to satisfy user requirements.
 *
 * Created by Dima on 02.06.2018.
 */
public enum OperatorType {
    GROUP {
        @Override
        public String[] getPossibleAttributes() {
            return new String[] {"colonQualifier", "colonValue"};
        }
    },
    AGGREGATE {
        @Override
        public String[] getPossibleAttributes() {
            return new String[] {"function"};
        }
    },
    PAGE {
        @Override
        public String[] getPossibleAttributes() {
            return new String[0];
        }
    },
    SELECT {
        @Override
        public String[] getPossibleAttributes() {
            return new String[] {"skip", "take", "order"};
        }
    };


    public abstract String[] getPossibleAttributes();
}
