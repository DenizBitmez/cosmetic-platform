package com.cosmeticPlatform.CosmeticPlatform.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisRateLimitConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .build());
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
        try {
            return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        } catch (Exception e) {
            System.err.println("REDIS CONNECTION FAILED: " + e.getMessage());
            return null; // Handle null in ProxyManager
        }
    }

    @Bean
    public ProxyManager<String> proxyManager(ObjectProvider<StatefulRedisConnection<String, byte[]>> redisConnectionProvider) {
        StatefulRedisConnection<String, byte[]> redisConnection = redisConnectionProvider.getIfAvailable();
        if (redisConnection == null) {
            return null; 
        }
        return LettuceBasedProxyManager.builderFor(redisConnection)
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(1000)
                        .refillGreedy(1000, Duration.ofMinutes(1))
                        .build())
                .build();
    }
}
