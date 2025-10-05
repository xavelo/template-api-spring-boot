package com.xavelo.template.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chucknorris")
public class ChuckNorrisProperties {

    /**
     * Base URL for the Chuck Norris jokes API.
     */
    private String baseUrl = "https://api.chucknorris.io";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
