package com.system.demo.core.security.context;

/**
 * Tenant-scoped request identity resolved from HTTP headers.
 */
public final class TenantContext {

    private final String tenantId;
    private final String env;
    private final String userId;

    public TenantContext(String tenantId, String env, String userId) {
        this.tenantId = tenantId;
        this.env = env;
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getEnv() {
        return env;
    }

    public String getUserId() {
        return userId;
    }
}
