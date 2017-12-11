package com.google.maps;

import com.google.maps.internal.StringJoin.UrlValue;

import java.util.Map;
import java.util.StringJoiner;

/**
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StyledMap implements UrlValue {

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

    public enum Element implements UrlValue {
        ALL,
        LABELS,
        GEOMETRY;

        public String toUrlValue() {
            return name().toLowerCase();
        }
    }

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

    public StyledMap(Feature feature, Map<Rule, String> rules) {
        this(feature, null, rules);
    }

    public StyledMap(Feature feature, Element element, Map<Rule, String> rules) {
        this.feature = feature;
        this.element = element;
        this.rules = rules;
    }

    public String toUrlValue() {
        final StringJoiner joiner = new StringJoiner("|");
        joiner.add("feature:" + feature.toUrlValue());

        if (null != element) {
            joiner.add("element:" + element.toUrlValue());
        }
        rules.forEach((key, value) -> joiner.add(key.toUrlValue() + ":"  + value));

        return joiner.toString();
    }
}
