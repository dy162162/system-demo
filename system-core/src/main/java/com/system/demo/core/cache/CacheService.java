package com.system.demo.core.cache;

import java.time.Duration;

public interface CacheService {
    void put(String key, Object value, Duration ttl);

    <T> T get(String key, Class<T> clazz);

    void delete(String key);
}
