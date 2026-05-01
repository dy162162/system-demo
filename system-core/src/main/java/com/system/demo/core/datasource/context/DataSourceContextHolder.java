package com.system.demo.core.datasource.context;

public final class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<String>();

    private DataSourceContextHolder() {
    }

    public static void use(String dataSourceKey) {
        CONTEXT.set(dataSourceKey);
    }

    public static String current() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
