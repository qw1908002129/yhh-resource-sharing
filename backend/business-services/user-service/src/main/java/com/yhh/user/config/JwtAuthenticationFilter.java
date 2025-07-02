package com.yhh.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhh.common.result.Result;
import com.yhh.common.result.ResultCode;
import com.yhh.user.utils.JwtUtils;
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

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String bearerToken = getJwtFromRequest(request);
            log.debug("Received token: {}", bearerToken);

            if (StringUtils.hasText(bearerToken)) {
                String token = bearerToken.substring(7); // 去掉 "Bearer " 前缀
                if (JwtUtils.validateToken(token)) {
                    Long userId = JwtUtils.getUserIdFromToken(token);
                    String username = JwtUtils.getUsernameFromToken(token);
                    boolean isAdmin = JwtUtils.isAdmin(token);
                    
                    log.debug("Token validated. UserID: {}, Username: {}, IsAdmin: {}", userId, username, isAdmin);
                    
                    // 创建认证信息
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(isAdmin ? "ROLE_ADMIN" : "ROLE_USER"))
                    );
                    
                    // 设置认证信息到上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication set in SecurityContext");
                } else {
                    log.warn("Invalid JWT token");
                    handleAuthenticationError(response, "无效的token");
                    return;
                }
            } else {
                log.debug("No token found in request");
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            handleAuthenticationError(response, "认证失败：" + ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken;
        }
        return null;
    }

    private void handleAuthenticationError(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
} 