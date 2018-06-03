package io.piano.nlp.domain;

import io.piano.nlp.domain.location.Location;
import io.piano.nlp.domain.operator.ResultOperator;
import io.piano.nlp.domain.operator.ResultOperatorsDescriptor;
import io.piano.nlp.domain.participant.Participant;
import io.piano.nlp.domain.state.StateDomain;
import io.piano.nlp.domain.time.TimeRange;
import io.piano.nlp.domain.tool.Tool;
import lombok.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the output of parsing the user defined query in OOP way.
 *
 * Created by Dima on 02.06.2018.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ParsedQuery {
    private String fromState;
    private String toState;
    private List<Location> locations;
    private List<Tool> tools;
    private TimeRange timeRange;
    private List<Participant> participants;
    private List<StateDomain> stateDomains;
    private ResultOperatorsDescriptor operatorsDescriptor;

    public void addLocation(@Nonnull Location loc) {
        if (locations == null) {
            locations = new ArrayList<>();
        }
        this.locations.add(loc);
    }

    public void addTool(@Nonnull Tool tool) {
        if (tools == null) {
            tools = new ArrayList<>();
        }
        this.tools.add(tool);
    }

    public void addStateDomain(@Nonnull StateDomain stateDomain) {
        if (stateDomains == null) {
            stateDomains = new ArrayList<>();
        }
        this.stateDomains.add(stateDomain);
    }

    public void addParticipant(@Nonnull Participant participant) {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        this.participants.add(participant);
    }

    public void addOperator(@Nonnull ResultOperator operator) {
        if (operatorsDescriptor == null) {
            this.operatorsDescriptor = new ResultOperatorsDescriptor();
        }
        operatorsDescriptor.add(operator);
    }

    public void addOperator(@Nonnull List<ResultOperator> operators) {
        if (operatorsDescriptor == null) {
            this.operatorsDescriptor = new ResultOperatorsDescriptor();
        }
        operatorsDescriptor.addAll(operators);
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Parsed query: [\n");
        appendProperty(fromState, "fromState", sb);
        appendProperty(toState, "toState", sb);
        appendProperty(locations, "locations", sb);
        appendProperty(tools, "tools", sb);
        appendProperty(timeRange, "timeRange", sb);
        appendProperty(stateDomains, "stateDomains", sb);
        appendProperty(operatorsDescriptor, "operatorsDescriptor", sb);
        sb.append(']');

        return sb.toString();
    }

    private void appendProperty(Object property, String propName, StringBuilder sb) {
        if (property != null) {
            sb.append('\t').append(propName).append(':').append('\t');

            if (property instanceof List)
            {
                List lst = (List) property;
                String lstStr = lst.toString()
                        .replace("[", "[\n\t\t")
                        .replace(", ", ",\n\t\t")
                        .replace("]", "\t\n]");
                sb.append(lstStr);
            } else if (property.getClass() == ResultOperatorsDescriptor.class) {
                ResultOperatorsDescriptor lst = (ResultOperatorsDescriptor) property;
                String lstStr = lst.toString()
                        .replace("[", "[\n\t\t")
                        .replace(", ", ",\n\t\t")
                        .replace("]", "\t\n]");
                sb.append(lstStr);
            } else {
                sb.append(property);
            }

            sb.append(";").append("\n");
        }
    }
}
