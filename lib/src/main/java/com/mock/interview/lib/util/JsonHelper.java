package com.mock.interview.lib.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.interview.lib.exception.MockInterviewException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@UtilityClass
public class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String FAILED = "Failed to convert object to json string: {}";

    public static String toBeautifulJson(Object o) {
        if (o == null) {
            return "null";
        } else {
            try {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            } catch (Exception ex) {
                log.warn(FAILED, ex.getMessage());
                return null;
            }
        }
    }

    public static String toJson(Object o) {
        if (o == null) {
            return "null";
        } else {
            try {
                return mapper.writeValueAsString(o);
            } catch (Exception ex) {
                log.warn(FAILED, ex.getMessage());
                return null;
            }
        }
    }

    public static Object fromJson(String json, Class valueClass) {
        if (json == null) {
            return null;
        } else {
            try {
                return mapper.readValue(json, valueClass);
            } catch (Exception ex) {
                log.warn(FAILED, ex.getMessage());
                return null;
            }
        }
    }

    public static <T> T parseString(String json, Class<T> valueClass) {
        try {
            Objects.requireNonNull(json, "json is null");
            return (T)mapper.readValue(json, valueClass);
        } catch (Exception e) {
            throw new MockInterviewException("Can't parse string '" + json + "'", 500);
        }
    }
}
