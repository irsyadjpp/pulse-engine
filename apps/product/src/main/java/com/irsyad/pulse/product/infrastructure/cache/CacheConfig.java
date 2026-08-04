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
    public static final String COMPANY_CACHE = "company";
    public static final String COMPANY_SEARCH_CACHE = "companySearch";
    public static final String PRODUCT_VERSION_CACHE = "product-version";
    public static final String VERSION_HISTORY_CACHE = "versionHistory";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues();

        RedisCacheConfiguration productDetailConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(30));
        
        RedisCacheConfiguration productListingConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(10));
        
        RedisCacheConfiguration companyConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(30));
        
        RedisCacheConfiguration companySearchConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(10));
        
        RedisCacheConfiguration productVersionConfig = defaultConfig
                .entryTtl(Duration.ofHours(24));
        
        RedisCacheConfiguration versionHistoryConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(60));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig.entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration(PRODUCT_DETAIL_CACHE, productDetailConfig)
                .withCacheConfiguration(PRODUCT_LISTING_CACHE, productListingConfig)
                .withCacheConfiguration(COMPANY_CACHE, companyConfig)
                .withCacheConfiguration(COMPANY_SEARCH_CACHE, companySearchConfig)
                .withCacheConfiguration(PRODUCT_VERSION_CACHE, productVersionConfig)
                .withCacheConfiguration(VERSION_HISTORY_CACHE, versionHistoryConfig)
                .build();
    }
}
