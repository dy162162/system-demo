package com.system.demo.core.security.config;

import com.system.demo.core.security.interceptor.TenantContextInterceptor;
import com.system.demo.core.security.resolver.TenantResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "system.governance.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityGovernanceAutoConfiguration implements WebMvcConfigurer {

    private final TenantContextInterceptor tenantContextInterceptor;

    public SecurityGovernanceAutoConfiguration(TenantContextInterceptor tenantContextInterceptor) {
        this.tenantContextInterceptor = tenantContextInterceptor;
    }

    @Bean
    public TenantResolver tenantResolver() {
        return new TenantResolver();
    }

    @Bean
    public TenantContextInterceptor tenantContextInterceptor(TenantResolver tenantResolver) {
        return new TenantContextInterceptor(tenantResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantContextInterceptor).addPathPatterns("/**");
    }
}
