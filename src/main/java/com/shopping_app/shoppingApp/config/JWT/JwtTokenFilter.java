package com.shopping_app.shoppingApp.config.JWT;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getAuthToken(request);
        try {
            log.info("Token from header {}", token);
            if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(token));
            }
        } catch (ExpiredJwtException e) {
            log.error("Expired Auth Token Passed");
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Invalid Token Signature exception");
        } catch (UnsupportedJwtException e) {
            log.error("UnSupported Auth Token");
        } catch (Exception e) {
            log.error("UnKnown Exception");
        }
        filterChain.doFilter(request, response);
    }

    public String getAuthToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token) || !token.startsWith("Bearer")) {
            log.error("Auth Token Missing in header");
        }
        return token.substring(7);
    }

}