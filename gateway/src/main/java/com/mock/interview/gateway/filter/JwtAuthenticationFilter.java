package com.mock.interview.gateway.filter;

import com.mock.interview.gateway.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {
    
    private final JwtTokenService jwtTokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var path = exchange.getRequest().getURI().getPath();
        
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        var token = extractToken(exchange.getRequest());
        
        if (token == null || !jwtTokenService.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        var request = exchange.getRequest().mutate()
                .header("X-User-Id", jwtTokenService.getUserId(token))
                .build();
                
        return chain.filter(exchange.mutate().request(request).build());
    }
    
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/") || 
               path.startsWith("/public/") ||
               path.equals("/health");
    }
    
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}