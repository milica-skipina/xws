package com.example.emailservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Value("${LOG_STORAGE:emailRequests.log}")
    private String logStorage;

    public String getLogStorage() {
        return logStorage;
    }

    public void setLogStorage(String logStorage) {
        this.logStorage = logStorage;
    }
}
