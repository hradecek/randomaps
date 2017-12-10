package com.hradecek.maps.utils;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Various utility functions for {@link io.vertx.core.json.JsonObject JSON} manipulation.
 *
 * @see JsonObject
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class JsonUtils {

    /**
     * Get value of nested key defined in <i>dotted</i> form. If no key is found null is returned.
     * E.g. {@code some.nested.key} represents following structure with value {@code value}:
     * <pre>
     *   {
     *     "some": {
     *       "nested": {
     *         "key": "value"
     *       }
     *     }
     *   }
     * </pre>
     *
     * @param json JSON object to be searched
     * @param key nested key
     * @return value or null if no such key exists
     */
    public static String getString(JsonObject json, String key) {
        if (StringUtils.countMatches(key, ".") >= 1) {
            String[] path = key.split("\\.", 2);
            String partialKey = path[0];
            String remainingKey = path[1];

            return getString(json.getJsonObject(partialKey), remainingKey);
        }

        return null != json ? json.getString(key) : null;
    }
}
