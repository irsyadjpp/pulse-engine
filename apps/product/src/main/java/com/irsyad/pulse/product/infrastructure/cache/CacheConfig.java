package com.irsyad.pulse.product.infrastructure.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Redis cache configuration (FSD_04 Section 13, NFR Caching).
 * Product Detail and Product Listing are cached to meet the <300 ms target.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String PRODUCT_DETAIL_CACHE = "productDetail";
    public static final String PRODUCT_LISTING_CACHE = "productListing";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .withCacheConfiguration(PRODUCT_DETAIL_CACHE, configuration)
                .withCacheConfiguration(PRODUCT_LISTING_CACHE, configuration)
                .build();
    }
}
