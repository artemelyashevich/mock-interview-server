package com.mock.interview.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> ENDPOINTS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/users",
            "api/v1/eureka/**",
            "/v3/api-docs/users",
            "/v3/api-docs/products",
            "/v3/api-docs/categories",
            "/v3/api-docs/carts",
            "/v3/api-docs/orders"
    );

    private static final Map<String, List<String>> ADMIN_ENDPOINTS = Map.of(
            "/api/v1/products", List.of("POST", "PUT", "DELETE"),
            "/api/v1/categories", List.of("POST", "PUT", "DELETE"),
            "/api/v1/orders", List.of("GET", "POST", "PUT", "DELETE")
    );

    private RouteValidator() {
    }

    public static final Predicate<ServerHttpRequest> isSecured = request ->
            ENDPOINTS.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

    public static final Predicate<ServerHttpRequest> isAdminRequest = request ->
            ADMIN_ENDPOINTS.keySet().stream().noneMatch(uri -> {
                var method = request.getMethod();
                return request.getURI().getPath().contains(uri) && ADMIN_ENDPOINTS.get(uri).contains(method.name());
            });
}