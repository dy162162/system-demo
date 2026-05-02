package com.system.demo.core.security.interceptor;

import com.system.demo.core.security.context.TenantContext;
import com.system.demo.core.security.context.TenantContextHolder;
import com.system.demo.core.security.resolver.TenantResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TenantContextInterceptor implements HandlerInterceptor {

    private final TenantResolver tenantResolver;

    public TenantContextInterceptor(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TenantContext context = tenantResolver.resolve(request);
        TenantContextHolder.set(context);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clear();
    }
}
