package com.botagendamento.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int LIMIT_PER_MINUTE = 60;
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

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
        WindowCounter counter = counters.computeIfAbsent(key, k -> new WindowCounter(currentMinute));

        synchronized (counter) {
            if (counter.minute != currentMinute) {
                counter.minute = currentMinute;
                counter.count.set(0);
            }

            if (counter.count.incrementAndGet() > LIMIT_PER_MINUTE) {
                response.setStatus(429);
                response.getWriter().write("Rate limit excedido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class WindowCounter {
        private long minute;
        private final AtomicInteger count;

        private WindowCounter(long minute) {
            this.minute = minute;
            this.count = new AtomicInteger(0);
        }
    }
}
