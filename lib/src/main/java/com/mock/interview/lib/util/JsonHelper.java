package com.mock.interview.lib.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toBeautifulJson(Object o) {
        if (o == null) {
            return "null";
        } else {
            try {
                String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
                return result;
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static String toJson(Object o) {
        if (o == null) {
            return "null";
        } else {
            try {
                String result = mapper.writeValueAsString(o);
                return result;
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static Object fromJson(String json, Class valueClass) {
        if (json == null) {
            return null;
        } else {
            try {
                Object result = mapper.readValue(json, valueClass);
                return result;
            } catch (Exception var4) {
                return null;
            }
        }
    }

    public static <T> T parseString(String json, Class<T> valueClass) throws JsonHelperException {
        try {
            Objects.requireNonNull(json, "json is null");
            return (T)mapper.readValue(json, valueClass);
        } catch (Exception e) {
            throw new JsonHelperException("Can't parse string '" + json + "'", e);
        }
    }


}
