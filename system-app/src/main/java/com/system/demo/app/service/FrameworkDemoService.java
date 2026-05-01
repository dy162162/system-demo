package com.system.demo.app.service;

import com.system.demo.core.cache.CacheService;
import com.system.demo.core.datasource.config.DataSourceRoutingProperties;
import com.system.demo.core.datasource.context.DataSourceContextHolder;
import com.system.demo.core.redis.route.RedisSourceType;
import com.system.demo.core.redis.route.RedisTemplateRouter;
import com.system.demo.core.web.response.PageQuery;
import com.system.demo.core.web.response.PageResult;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrameworkDemoService {
    private final DataSourceRoutingProperties dataSourceRoutingProperties;
    private final RedisTemplateRouter redisTemplateRouter;
    private final CacheService cacheService;

    public FrameworkDemoService(
            DataSourceRoutingProperties dataSourceRoutingProperties,
            RedisTemplateRouter redisTemplateRouter,
            CacheService cacheService) {
        this.dataSourceRoutingProperties = dataSourceRoutingProperties;
        this.redisTemplateRouter = redisTemplateRouter;
        this.cacheService = cacheService;
    }

    public PageResult<String> pageDemo(PageQuery pageQuery) {
        return new PageResult<String>(
                Arrays.asList("base-framework-item-1", "base-framework-item-2"),
                2,
                pageQuery.safePageNum(),
                pageQuery.safePageSize());
    }

    public Map<String, Object> dataSourceDemo(String key) {
        String target = key == null || key.trim().isEmpty() ? dataSourceRoutingProperties.getPrimary() : key;
        DataSourceContextHolder.use(target);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("selected", DataSourceContextHolder.current());
        result.put("primary", dataSourceRoutingProperties.getPrimary());
        result.put("availableKeys", dataSourceRoutingProperties.getAvailableKeys());
        DataSourceContextHolder.clear();
        return result;
    }

    public Map<String, Object> redisDemo() {
        Map<String, Object> result = new HashMap<String, Object>();
        RedisTemplate<String, Object> redisTemplate = redisTemplateRouter.select(RedisSourceType.CACHE);
        result.put("redisTemplateAvailable", redisTemplate != null);
        cacheService.put("framework:ping", "pong", Duration.ofMinutes(1));
        String value = cacheService.get("framework:ping", String.class);
        result.put("cacheRoundTrip", value);
        return result;
    }
}
