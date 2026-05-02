package com.system.demo.core.security.context;

/**
 * Holds {@link TenantContext} for the current request thread.
 */
public final class TenantContextHolder {

    private static final ThreadLocal<TenantContext> HOLDER = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void set(TenantContext context) {
        HOLDER.set(context);
    }

    public static TenantContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
