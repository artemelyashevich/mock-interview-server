package com.mock.interview.interceptor;

import com.mock.interview.lib.annotation.Authenticated;
import com.mock.interview.service.security.SecurityService;
import com.mock.interview.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {
    
    private final SecurityService securityContextService;
    
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Authenticated auth = handlerMethod.getMethodAnnotation(Authenticated.class);
            if (auth != null) {
                Long userId = Long.valueOf(request.getHeader(Constants.USER_HEADER));
                securityContextService.setUser(userId);
            }
        }
        return true;
    }
}