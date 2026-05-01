package com.system.demo.core.datasource.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceRoutingProperties.class)
public class DataSourceCoreAutoConfiguration {
    private final DataSourceRoutingProperties properties;

    public DataSourceCoreAutoConfiguration(DataSourceRoutingProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            return;
        }
        List<String> normalized = new ArrayList<String>();
        for (String key : properties.getAvailableKeys()) {
            if (StringUtils.hasText(key)) {
                normalized.add(key.trim());
            }
        }
        if (normalized.isEmpty()) {
            normalized.add(properties.getPrimary());
        }
        if (!normalized.contains(properties.getPrimary())) {
            normalized.add(0, properties.getPrimary());
        }
        properties.setAvailableKeys(normalized);
    }
}
