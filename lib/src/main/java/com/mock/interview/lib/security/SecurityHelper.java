package com.mock.interview.lib.security;

import com.mock.interview.lib.model.RoleModel;
import com.mock.interview.lib.model.UserModel;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityHelper {

    @Value("${app.security.jwt.secret:secret}")
    private String secret;

    public String generateToken(UserModel user, Long jwtExpiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("login", user.getLogin());
        claims.put("roles", user.getRoles().stream().map(RoleModel::getName).toList());
        claims.put("providers", user.getOAuthProviderModelList().stream()
                .map(UserOAuthProviderModel::getProvider).toList());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(user.getId().toString())
                .setSubject(user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secret))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getIssuer();
    }

    private SecretKey getSigningKey(String secret) {
        this.secret = secret;
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
