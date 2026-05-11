package com.botagendamento.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Value("${app.rate-limit.requests-per-minute:60}")
    private int limitPerMinute;

    private final StringRedisTemplate redisTemplate;

    public RateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/api/webhooks") && !uri.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getRemoteAddr() + ":" + uri;
        long currentMinute = Instant.now().getEpochSecond() / 60;
        String redisKey = "ratelimit:" + key + ":" + currentMinute;
        Long count = redisTemplate.opsForValue().increment(redisKey);
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, 70, TimeUnit.SECONDS);
        }
        if (count != null && count > limitPerMinute) {
            response.setStatus(429);
            response.getWriter().write("Rate limit excedido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
