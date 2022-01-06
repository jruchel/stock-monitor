package com.jruchel.stockmonitor.security.jwt;

import com.jruchel.stockmonitor.config.security.JWTConfig;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final JWTConfig config;

    @SneakyThrows
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String token = request.getHeader(config.getJwtHeaderName());
        if (token != null && !token.isEmpty()) {
            String username = "";
            try {
                username = jwtUtils.extractUsername(token);
            } catch (Exception ex) {
                return;
            }
            if (username != null && !username.isEmpty()) {
                User user = userService.getByUsername(username);
                if (user != null && user.isEnabled() && jwtUtils.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }


}