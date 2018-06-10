package io.piano.nlp.domain.operator;

import io.piano.nlp.domain.location.LocationQualifier;
import io.piano.nlp.domain.participant.ParticipantQualifier;
import io.piano.nlp.domain.tool.ToolQualifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * Utility class that provides possible values sets for specified attributes in result operator descriptions.
 *
 * Created by Dima on 02.06.2018.
 */
@SuppressWarnings("WeakerAccess")
public class AttributeToValuesResolver {
    @Nonnull
    public AttrPossibleValues getPossibleAttrs(OperatorType type) {
        List<String> values;

        switch (type) {
            case GROUP:
                values = asList(
                        "groupQualifier", "groupValue"
                );
                break;
            case AGGREGATE:
                values = singletonList(
                        "operator"
                );
                break;
            case SELECT:
                values = asList(
                        "take", "skip", "order"
                );
                break;
            case PAGE:
                values = emptyList();
                break;
            default:
                throw new IllegalArgumentException();
        }

        return new ListAttrPossibleValues(values);
    }

    @Nullable
    public AttrPossibleValues getPossibleValuesForAttr(@Nonnull String attr, @Nullable String parentAttrValue) {
        List<String> values;

        switch (attr) {
            case "groupQualifier":
                values = asList(
                        "location", "tool", "time", "term", "participant", "other"
                );
                break;
            case "groupValue":
                return new OtherAttrBasedAttrPossibleValues("groupQualifier", parentAttrValue, "groupValue");
            case "operator":
                values = asList(
                        "max", "min", "count", "sum", "avg", "norm", "grows", "falls"
                );
                break;
            case "skip":
            case "take":
                return new NumericAttrPossibleValues();
            case "order":
                values = asList(
                        "first", "last"
                );
                break;
            default:
                return null;
        }

        return new ListAttrPossibleValues(values);
    }

    @Nullable
    public AttrPossibleValues getPossibleValueForAttrBasedOnOtherAttr(
            @Nonnull String attr, @Nonnull  String baseAttr, @Nonnull String baseAttrValue)
    {
        if (attr.equals("groupValue") && baseAttr.equals("groupQualifier")) {
            return getForGroupColonParent(baseAttrValue);
        }

        return null;
    }
    private AttrPossibleValues getForGroupColonParent(@Nonnull String attrValue) {
        List<String> values;

        switch (attrValue) {
            case "location":
                values = Arrays.stream( LocationQualifier.values() )
                        .map(Enum::toString)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                break;
            case "tool":
                values = Arrays.stream( ToolQualifier.values() )
                        .map(Enum::toString)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                break;
            case "time":
                values = Arrays.stream( Calendar.class.getDeclaredFields() )
                        .peek(f -> f.setAccessible(true))
                        .map(Field::getName)
                        .filter(name -> name.contains("_OF_"))
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                values.addAll( asList(
                        "second", "minute", "week", "month", "year",
                        "start_date", "end_date", "time_for_conversion"
                ) );
                break;
            case "term":
                values = asList(
                        "term", "term_length"
                );

                break;
            case "participant":
                values = Arrays.stream( ParticipantQualifier.values() )
                        .map(Enum::toString)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                break;
            case "other":
                values = asList(
                        "exposures_seen", "session_30_days_prior", "pageviews_30_days_prior", "conversion_type"
                );
                break;
            default:
                return null;
        }

        return new ListAttrPossibleValues(values);
    }

    public List<String> getAllPossibleGroupAttrValuesList() {
        return Stream.of("location", "tool", "time", "term", "participant", "other")
                .map(this::getForGroupColonParent)
                .filter(Objects::nonNull)
                .map(AttrPossibleValues::getValuesList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
