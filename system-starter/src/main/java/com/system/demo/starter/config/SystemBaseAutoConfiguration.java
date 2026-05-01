package com.system.demo.starter.config;

import com.system.demo.core.datasource.config.DataSourceCoreAutoConfiguration;
import com.system.demo.core.doc.config.OpenApiAutoConfiguration;
import com.system.demo.core.observability.config.ObservabilityAutoConfiguration;
import com.system.demo.core.redis.config.RedisCoreAutoConfiguration;
import com.system.demo.core.web.config.WebMvcCoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        WebMvcCoreConfig.class,
        DataSourceCoreAutoConfiguration.class,
        RedisCoreAutoConfiguration.class,
        OpenApiAutoConfiguration.class,
        ObservabilityAutoConfiguration.class
})
public class SystemBaseAutoConfiguration {
}
