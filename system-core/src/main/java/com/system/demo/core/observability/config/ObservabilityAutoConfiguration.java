package com.system.demo.core.observability.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityAutoConfiguration {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(
            @Value("${spring.application.name:system-app}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
