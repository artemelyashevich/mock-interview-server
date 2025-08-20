package com.mock.interview.auth.infrastructure.interceptor;

import com.mock.interview.auth.application.port.in.UserService;
import com.mock.interview.lib.annotation.AuthorizationRequired;
import com.mock.interview.lib.exception.MockInterviewException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Map<Method, MethodAnnotations> methodAnnotationMap = new ConcurrentHashMap<>();
    private final UserService userService;

    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            var methodAnnotations = getMethodOrClassAnnotation(handlerMethod);
            if (methodAnnotations.getSecurityRequired() != null) {
                authenticate(methodAnnotations.getSecurityRequired(), request);
            }
        }
        return true;
    }

    // FIXME: extract from token
    private void authenticate(AuthorizationRequired securityRequired, HttpServletRequest request) {
        var role = securityRequired.roles();
        var user = userService.findByLogin(request.getParameter("login"));

        if (Arrays.stream(role).noneMatch(r -> user.getRoles().contains(r))) {
            throw new MockInterviewException(403);
        }
    }

    private MethodAnnotations getMethodOrClassAnnotation(HandlerMethod handlerMethod) {
        return methodAnnotationMap.computeIfAbsent(handlerMethod.getMethod(), m -> {
            var methodAnnotations = new MethodAnnotations();
            var securityRequired = handlerMethod.getMethod().getAnnotation(AuthorizationRequired.class);
            if (securityRequired == null) {
                securityRequired = handlerMethod.getBeanType().getAnnotation(AuthorizationRequired.class);
            }
            methodAnnotations.setSecurityRequired(securityRequired);
            return methodAnnotations;
        });
    }

    @Getter
    @Setter
    private static class MethodAnnotations {
        AuthorizationRequired securityRequired;
    }
}
