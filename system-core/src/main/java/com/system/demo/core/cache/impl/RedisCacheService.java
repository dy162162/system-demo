package com.system.demo.core.cache.impl;

import com.system.demo.core.cache.CacheService;
import com.system.demo.core.redis.route.RedisSourceType;
import com.system.demo.core.redis.route.RedisTemplateRouter;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService implements CacheService {
    private final RedisTemplateRouter redisTemplateRouter;

    public RedisCacheService(RedisTemplateRouter redisTemplateRouter) {
        this.redisTemplateRouter = redisTemplateRouter;
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        RedisTemplate<String, Object> redisTemplate = redisTemplateRouter.select(RedisSourceType.CACHE);
        if (redisTemplate == null) {
            return;
        }
        if (ttl == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, ttl);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        RedisTemplate<String, Object> redisTemplate = redisTemplateRouter.select(RedisSourceType.CACHE);
        if (redisTemplate == null) {
            return null;
        }
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    @Override
    public void delete(String key) {
        RedisTemplate<String, Object> redisTemplate = redisTemplateRouter.select(RedisSourceType.CACHE);
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        }
    }
}
