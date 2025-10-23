package com.mock.interview.lib.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

import java.util.Map;

@UtilityClass
public class PrivateAccessRouteUtil {

    private static final Map<HttpMethod, String> PRIVATE_ROUTES = Map.of(
            HttpMethod.POST, "api/v1/interviews"
    );

    public static Boolean isPrivateRoute(HttpMethod method, String route) {
        var entrySet = PRIVATE_ROUTES.entrySet();
        for (Map.Entry<HttpMethod, String> entry : entrySet) {
            if (entry.getKey().equals(method) &&  route.contains(entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}
