package com.system.demo.core.redis.route;

import java.util.Map;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTemplateRouter {
    private final Map<String, RedisTemplate<String, Object>> redisTemplates;

    public RedisTemplateRouter(Map<String, RedisTemplate<String, Object>> redisTemplates) {
        this.redisTemplates = redisTemplates;
    }

    public RedisTemplate<String, Object> select(RedisSourceType sourceType) {
        String beanName = "redisTemplate" + sourceType.name().charAt(0) + sourceType.name().substring(1).toLowerCase();
        RedisTemplate<String, Object> redisTemplate = redisTemplates.get(beanName);
        if (redisTemplate == null) {
            redisTemplate = redisTemplates.get("redisTemplateCache");
        }
        if (redisTemplate == null) {
            redisTemplate = redisTemplates.get("redisTemplateSession");
        }
        if (redisTemplate == null) {
            redisTemplate = redisTemplates.get("redisTemplateLock");
        }
        if (redisTemplate == null) {
            redisTemplate = redisTemplates.get("redisTemplate");
        }
        return redisTemplate;
    }
}
