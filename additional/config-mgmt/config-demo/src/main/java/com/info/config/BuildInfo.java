package com.info.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "build")
public class BuildInfo {
    private String id;
    private String version;
    private String name;
    private String type;
}
