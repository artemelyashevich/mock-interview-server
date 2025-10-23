package com.mock.interview.util;

import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.util.JsonHelper;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ExceptionConvertUtil {

    public static String convertExceptionIntoBody(MockInterviewException e) {
        return JsonHelper.toJson(
                Map.of(
                        "message", e.getMessage()
                )
        );
    }
}
