package com.yhh.resource.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhh.common.result.Result;
import com.yhh.common.result.ResultCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import javax.crypto.SecretKey;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secret = "your-secret-key-must-be-at-least-32-characters-long"; // 确保与用户服务使用相同的密钥
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);
            log.debug("Received token: {}", token);

            if (StringUtils.hasText(token)) {
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

                Long userId = claims.get("userId", Long.class);
                String username = claims.getSubject();
                boolean isAdmin = claims.get("isAdmin", Boolean.class);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(isAdmin ? "ROLE_ADMIN" : "ROLE_USER"))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Set authentication for user: {}", username);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 