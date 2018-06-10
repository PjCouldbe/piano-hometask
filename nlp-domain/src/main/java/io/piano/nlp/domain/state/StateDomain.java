package io.piano.nlp.domain.state;

import lombok.*;
import org.apache.commons.lang.ArrayUtils;

/**
 * Represents info about in what userchanged his state. For now it represents information about conversion metrics.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StateDomain {
    private MetricGroup metricGroup;
    @Setter
    private String metric;
    @Setter
    private String term;

    public StateDomain(MetricGroup metricGroup, String metric) {
        this.metricGroup = metricGroup;
        this.metric = metric;
    }

    public StateDomain(MetricGroup metricGroup) {
        this.metricGroup = metricGroup;
    }

    private boolean setMetricAndGroup(String metric) {
        for (MetricGroup mg : MetricGroup.values()) {
            if ( ArrayUtils.contains(mg.getConversionMetrics(), metric) ) {
                this.metricGroup = mg;
                this.metric = metric;
                return true;
            }
        }

        return false;
    }
}
