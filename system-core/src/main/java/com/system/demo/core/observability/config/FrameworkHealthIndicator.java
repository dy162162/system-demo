package com.system.demo.core.observability.config;

import com.system.demo.core.datasource.config.DataSourceRoutingProperties;
import com.system.demo.core.redis.route.RedisSourceType;
import com.system.demo.core.redis.route.RedisTemplateRouter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FrameworkHealthIndicator implements HealthIndicator {
    private final DataSourceRoutingProperties dataSourceRoutingProperties;
    private final RedisTemplateRouter redisTemplateRouter;

    public FrameworkHealthIndicator(
            DataSourceRoutingProperties dataSourceRoutingProperties,
            RedisTemplateRouter redisTemplateRouter) {
        this.dataSourceRoutingProperties = dataSourceRoutingProperties;
        this.redisTemplateRouter = redisTemplateRouter;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new LinkedHashMap<String, Object>();
        details.put("primaryDataSource", dataSourceRoutingProperties.getPrimary());
        details.put("dataSourceKeys", dataSourceRoutingProperties.getAvailableKeys());
        RedisTemplate<String, Object> cacheTemplate = redisTemplateRouter.select(RedisSourceType.CACHE);
        details.put("cacheRedisTemplateReady", cacheTemplate != null);
        return Health.up().withDetails(details).build();
    }
}
