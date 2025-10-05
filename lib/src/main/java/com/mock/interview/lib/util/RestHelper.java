package com.mock.interview.lib.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class RestHelper {

    public static HttpEntity<String> createHttpEntity(Object interviewTemplate) {
        return new HttpEntity<String>(JsonHelper.toJson(interviewTemplate), HttpHeaders.EMPTY);
    }
}
