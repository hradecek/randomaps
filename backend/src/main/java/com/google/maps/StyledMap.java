package com.google.maps;

import com.google.maps.internal.StringJoin.UrlValue;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Customization for presentation of static map.
 * <p>
 * For more information see:
 * <a href="https://developers.google.com/maps/documentation/static-maps/styling">developers.google.com</a>
 */
public class StyledMap implements UrlValue {

    /**
     * Feature for modification
     */
    public enum Feature implements UrlValue {
        ALL,
        POI,
        ROAD,
        WATER,
        TRANSIT,
        LANDSCAPE,
        ADMINISTRATIVE;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    /**
     * Element for modification
     */
    public enum Element implements UrlValue {
        ALL,
        LABELS,
        GEOMETRY;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    /**
     * Rule to apply to specified features and elements
     */
    public enum Rule implements UrlValue {
        HUE,
        COLOR,
        GAMMA,
        WEIGHT,
        LIGHTNESS,
        VISIBILITY,
        SATURATION,
        INVERT_LIGTHNESS;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

    private final Feature feature;
    private final Element element;
    private final Map<Rule, String> rules;

    /**
     * Constructor
     *
     * @param feature feature to be modified
     * @param rules set of rules with corresponding values
     */
    public StyledMap(Feature feature, Map<Rule, String> rules) {
        this(feature, null, rules);
    }

    /**
     * Constructor
     *
     * @param feature feature to be modified
     * @param element element to be modified
     * @param rules set of rules with corresponding values
     */
    public StyledMap(Feature feature, Element element, Map<Rule, String> rules) {
        this.feature = feature;
        this.element = element;
        this.rules = rules;
    }

    /**
     * Create URL string representing styled map
     *
     * @return URL 'style' string
     */
    public String toUrlValue() {
        final var joiner = new StringJoiner("|");
        joiner.add("feature:" + feature.toUrlValue());

        if (null != element) {
            joiner.add("element:" + element.toUrlValue());
        }
        rules.forEach((key, value) -> joiner.add(key.toUrlValue() + ":"  + value));

        return joiner.toString();
    }
}
