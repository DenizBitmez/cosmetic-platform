package com.cosmeticPlatform.CosmeticPlatform.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfiguration;
    private final Map<String, Bucket> localBuckets = new ConcurrentHashMap<>();

    @Autowired
    public RateLimitFilter(ObjectProvider<ProxyManager<String>> proxyManagerProvider, 
                           ObjectProvider<BucketConfiguration> bucketConfigurationProvider) {
        this.proxyManager = proxyManagerProvider.getIfAvailable();
        this.bucketConfiguration = bucketConfigurationProvider.getIfAvailable();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ipAddress = httpRequest.getRemoteAddr();
        boolean allowed;

        try {
            if (proxyManager != null) {
                // Attempt distributed rate limiting via Redis
                var bucket = proxyManager.builder().build(ipAddress, () -> bucketConfiguration);
                allowed = bucket.tryConsume(1);
            } else {
                throw new RuntimeException("ProxyManager is null (Redis is likely down)");
            }
        } catch (Exception e) {
            // Fallback to local in-memory rate limiting if Redis is down
            Bucket localBucket = localBuckets.computeIfAbsent(ipAddress, k -> 
                Bucket.builder()
                    .addLimit(io.github.bucket4j.Bandwidth.builder()
                        .capacity(1000)
                        .refillGreedy(1000, java.time.Duration.ofMinutes(1))
                        .build())
                    .build());
            allowed = localBucket.tryConsume(1);
        }

        if (allowed) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests. Please try again later.");
        }
    }
}
