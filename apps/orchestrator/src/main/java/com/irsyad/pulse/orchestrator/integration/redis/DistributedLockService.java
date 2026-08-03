package com.irsyad.pulse.orchestrator.integration.redis;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ExpireArgs;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for distributed locking using Redis
 * Used to prevent duplicate checkout for the same customer
 *
 * Uses Redis SET key value NX EX ttl for atomic lock acquisition
 */
@ApplicationScoped
public class DistributedLockService {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLockService.class);

    @Inject
    RedisDataSource redisDataSource;

    /**
     * Acquire a distributed lock with TTL using Redis SET NX EX atomically
     *
     * @param lockKey   the lock key (e.g., "checkout:CUS001")
     * @param ttlSeconds time to live in seconds (e.g., 300 = 5 minutes)
     * @return true if lock acquired, false if already locked
     */
    public boolean acquireLock(String lockKey, int ttlSeconds) {
        LOG.info("Attempting to acquire lock: {} with TTL: {} seconds", lockKey, ttlSeconds);

        try {
            ValueCommands<String, String> valueCommands = redisDataSource.value(String.class, String.class);
            KeyCommands<String> keyCommands = redisDataSource.key(String.class);

            // SETNX lockKey "LOCKED" - returns true if set, false if key exists
            boolean acquired = valueCommands.setnx(lockKey, "LOCKED");

            if (acquired) {
                // Set TTL on the lock key so it auto-expires
                keyCommands.expire(lockKey, ttlSeconds);
                LOG.info("Lock acquired: {}", lockKey);
                return true;
            } else {
                LOG.warn("Lock already held: {}", lockKey);
                return false;
            }

        } catch (Exception e) {
            LOG.error("Failed to acquire lock: {}", lockKey, e);
            return false;
        }
    }

    /**
     * Release a distributed lock using Redis DEL
     *
     * @param lockKey the lock key to release
     */
    public void releaseLock(String lockKey) {
        LOG.info("Releasing lock: {}", lockKey);

        try {
            KeyCommands<String> keyCommands = redisDataSource.key(String.class);
            int deleted = keyCommands.del(lockKey);
            LOG.info("Lock released: {} (deleted: {})", lockKey, deleted);

        } catch (Exception e) {
            LOG.error("Failed to release lock: {}", lockKey, e);
        }
    }

    /**
     * Check if a lock is held using Redis EXISTS
     *
     * @param lockKey the lock key to check
     * @return true if lock is held, false otherwise
     */
    public boolean isLocked(String lockKey) {
        try {
            KeyCommands<String> keyCommands = redisDataSource.key(String.class);
            return keyCommands.exists(lockKey);

        } catch (Exception e) {
            LOG.error("Failed to check lock status: {}", lockKey, e);
            return false;
        }
    }
}