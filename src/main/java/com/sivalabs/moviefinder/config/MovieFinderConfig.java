package com.sivalabs.moviefinder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MovieFinder configuration holder
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "moviefinder")
public class MovieFinderConfig {
    private List<String> dataFiles;
}
