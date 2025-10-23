package com.mock.interview.filter;

import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.lib.util.PrivateAccessRouteUtil;
import com.mock.interview.service.security.SecurityService;
import com.mock.interview.util.ExceptionConvertUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static com.mock.interview.util.Constants.USER_HEADER;

@Profile("prod")
@Order(1)
@Component
@RequiredArgsConstructor
public class UserFilter implements Filter {

    private final SecurityService securityService;

    @Override
    public void doFilter(
            @NonNull ServletRequest servletRequest,
            @NonNull ServletResponse servletResponse,
            @NonNull FilterChain filterChain) throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;
        var path = request.getPathTranslated();
        if (!PrivateAccessRouteUtil.isPrivateRoute(HttpMethod.valueOf(request.getMethod()), path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var headers = Optional.of(request.getHeader(USER_HEADER));
        headers.ifPresentOrElse(header -> {
            var id = Long.parseLong(header);
            if (header.isBlank()) {
                return;
            }
            try {
                securityService.setUser(id);
            } catch (MockInterviewException e){
                sendUnauthorized(response, e);
            }
        }, () -> sendUnauthorized(response, null));
        doFilter(servletRequest, servletResponse, filterChain);
    }

    private void sendUnauthorized(HttpServletResponse response, MockInterviewException e) {
        var body = JsonHelper.toJson(Map.of("message", "Unauthorized"));
        if (e != null) {
            body = ExceptionConvertUtil.convertExceptionIntoBody(e);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            response.getWriter().write(body);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
