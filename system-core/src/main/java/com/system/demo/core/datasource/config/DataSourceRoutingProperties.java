package com.system.demo.core.datasource.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system.datasource")
public class DataSourceRoutingProperties {
    private boolean enabled = true;
    private String primary = "master";
    private List<String> availableKeys = new ArrayList<String>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<String> getAvailableKeys() {
        return availableKeys;
    }

    public void setAvailableKeys(List<String> availableKeys) {
        this.availableKeys = availableKeys;
    }
}
