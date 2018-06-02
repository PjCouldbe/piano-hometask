package io.piano.nlp.domain.state;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.ArrayUtils;

/**
 * Represents info about in what userchanged his state. For now it represents information about conversion metrics.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@ToString
public class StateDomain {
    private MetricGroup metricGroup;
    private String metric;
    @Setter
    private String term;

    public StateDomain(MetricGroup metricGroup, String metric) {
        this.metricGroup = metricGroup;
        this.metric = metric;
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
