package com.system.demo.core.redis.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system.redis")
public class MultiRedisProperties {
    private boolean enabled = true;
    private Map<String, Source> sources = new LinkedHashMap<String, Source>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, Source> getSources() {
        return sources;
    }

    public void setSources(Map<String, Source> sources) {
        this.sources = sources;
    }

    public static class Source {
        private String host;
        private int port = 6379;
        private int database = 0;
        private String password;
        private long timeoutMs = 3000L;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getDatabase() {
            return database;
        }

        public void setDatabase(int database) {
            this.database = database;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public long getTimeoutMs() {
            return timeoutMs;
        }

        public void setTimeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
        }
    }
}
