package com.system.demo.core.redis.config;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(MultiRedisProperties.class)
public class RedisCoreAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "multiRedisTemplateMap")
    public Map<String, RedisTemplate<String, Object>> multiRedisTemplateMap(
            MultiRedisProperties properties,
            RedisConnectionFactory defaultConnectionFactory) {
        Map<String, RedisTemplate<String, Object>> templates = new LinkedHashMap<String, RedisTemplate<String, Object>>();
        if (!properties.isEnabled() || properties.getSources().isEmpty()) {
            RedisTemplate<String, Object> defaultTemplate = buildTemplate(defaultConnectionFactory);
            templates.put("redisTemplateCache", defaultTemplate);
            templates.put("redisTemplateSession", defaultTemplate);
            templates.put("redisTemplateLock", defaultTemplate);
            return templates;
        }
        for (Map.Entry<String, MultiRedisProperties.Source> entry : properties.getSources().entrySet()) {
            String key = entry.getKey();
            MultiRedisProperties.Source source = entry.getValue();
            RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration(source.getHost(), source.getPort());
            standalone.setDatabase(source.getDatabase());
            if (StringUtils.hasText(source.getPassword())) {
                standalone.setPassword(RedisPassword.of(source.getPassword()));
            }
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .commandTimeout(Duration.ofMillis(source.getTimeoutMs()))
                    .shutdownTimeout(Duration.ZERO)
                    .build();
            LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(standalone, clientConfig);
            connectionFactory.afterPropertiesSet();
            templates.put("redisTemplate" + capitalize(key), buildTemplate(connectionFactory));
        }
        if (!templates.containsKey("redisTemplateCache") && !templates.isEmpty()) {
            templates.put("redisTemplateCache", templates.values().iterator().next());
        }
        if (!templates.containsKey("redisTemplateSession")) {
            templates.put("redisTemplateSession", templates.get("redisTemplateCache"));
        }
        if (!templates.containsKey("redisTemplateLock")) {
            templates.put("redisTemplateLock", templates.get("redisTemplateCache"));
        }
        return templates;
    }

    @Bean("redisTemplateCache")
    @ConditionalOnBean(name = "multiRedisTemplateMap")
    @ConditionalOnMissingBean(name = "redisTemplateCache")
    public RedisTemplate<String, Object> redisTemplateCache(
            @Qualifier("multiRedisTemplateMap") Map<String, RedisTemplate<String, Object>> templates) {
        return templates.get("redisTemplateCache");
    }

    @Bean("redisTemplateSession")
    @ConditionalOnBean(name = "multiRedisTemplateMap")
    @ConditionalOnMissingBean(name = "redisTemplateSession")
    public RedisTemplate<String, Object> redisTemplateSession(
            @Qualifier("multiRedisTemplateMap") Map<String, RedisTemplate<String, Object>> templates) {
        RedisTemplate<String, Object> template = templates.get("redisTemplateSession");
        return template == null ? templates.get("redisTemplateCache") : template;
    }

    @Bean("redisTemplateLock")
    @ConditionalOnBean(name = "multiRedisTemplateMap")
    @ConditionalOnMissingBean(name = "redisTemplateLock")
    public RedisTemplate<String, Object> redisTemplateLock(
            @Qualifier("multiRedisTemplateMap") Map<String, RedisTemplate<String, Object>> templates) {
        RedisTemplate<String, Object> template = templates.get("redisTemplateLock");
        return template == null ? templates.get("redisTemplateCache") : template;
    }

    private RedisTemplate<String, Object> buildTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }

    private String capitalize(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
