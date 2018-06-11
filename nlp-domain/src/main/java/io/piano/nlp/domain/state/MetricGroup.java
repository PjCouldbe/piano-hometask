package io.piano.nlp.domain.state;

/**
 * Group of conversion metrics used by Piano
 *
 * Created by Dima on 02.06.2018.
 */
public enum MetricGroup {
    CONVERSION {
        @Override
        public String[] getConversionMetrics() {
            return new String[] {"conversion", "conversion_rate"};
        }
    },
    CHURN {
        @Override
        public String[] getConversionMetrics() {
            return new String[] {"churned_subscription", "churn_rate"};
        }
    },
    ADBLOCK {
        @Override
        public String[] getConversionMetrics() {
            return new String[] {"adblock_whitelisting", "adblock_whitelist_rate"};
        }
    };

    public abstract String[] getConversionMetrics();
}
