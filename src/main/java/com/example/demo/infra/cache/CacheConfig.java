package com.example.demo.infra.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "redis", name = {"host-name"})
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {
    private final CacheErrorHandler cacheErrorHandler;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(60L));
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return cacheErrorHandler;
    }
}
