package com.hradecek.maps.utils;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class JsonUtils {

    /**
     *
     * @param json
     * @param key
     * @return
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
