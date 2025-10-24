package com.mock.interview.gateway.filter;

import com.mock.interview.gateway.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
public class CustomAuthenticationFilter extends AbstractGatewayFilterFactory<CustomAuthenticationFilter.Config> {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String MISSING_JWT_TOKEN = "Invalid or missing JWT token";

    private final WebClient webClient;

    private final JwtTokenService jwtTokenService;

    public static class Config {

    }

    @Autowired
    public CustomAuthenticationFilter(final WebClient.Builder webClient, JwtTokenService jwtTokenService) {
        super(Config.class);
        this.webClient = webClient.build();
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            if (!RouteValidator.isSecured.test(exchange.getRequest())) {
                log.info(exchange.getRequest().getURI().toString());
                return chain.filter(exchange);
            }
            var authorizationHeaders = exchange
                    .getRequest()
                    .getHeaders()
                    .getOrDefault(AUTHORIZATION_HEADER, Collections.emptyList());
            var jwt = authorizationHeaders.stream()
                    .filter(Objects::nonNull)
                    .filter(header -> header.trim().startsWith("Bearer "))
                    .map(header -> header.substring(7))
                    .findFirst()
                    .orElse(null);
            return jwt != null
                    ? validateToken(exchange, chain, jwt)
                    : handleInvalidAccess(HttpStatus.UNAUTHORIZED, exchange, MISSING_JWT_TOKEN);
        };
    }

    private Mono<Void> validateToken(
            final ServerWebExchange exchange,
            final GatewayFilterChain chain,
            final String token
    ) {
        if (jwtTokenService.validateToken(token)) {
            var req = exchange.getRequest().mutate()
                    .header(AUTHORIZATION_HEADER, token)
                    .build();
            return chain.filter(exchange.mutate().request(req).build());
        }
        return handleInvalidAccess(HttpStatus.UNAUTHORIZED, exchange, "Token validation failed");
    }

    private Mono<Void> handleInvalidAccess(final HttpStatus status, final ServerWebExchange exchange, final String errorMessage) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var res = Mono.just(response.bufferFactory().wrap(errorMessage.getBytes()));
        return response.writeWith(res);
    }
}