package com.example.matrimony.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ===========================
    // CORS CONFIGURATION
    // ===========================

    // Default "*" so missing/wrong property on server does not cause 403
    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Allow multiple origins (comma-separated); trim spaces and trailing slashes
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .map(s -> s.endsWith("/") ? s.substring(0, s.length() - 1) : s)
                .filter(s -> !s.isEmpty())
                .toList();

        // Use setAllowedOriginPatterns so https + http and www + non-www all work (avoids 403 from CORS)
        config.setAllowedOriginPatterns(origins);

        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(List.of("*"));

        // When credentials are true, browser CORS does NOT allow origin "*".
        boolean useWildcard = origins.size() == 1 && "*".equals(origins.get(0));
        config.setAllowCredentials(!useWildcard);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ===========================
    // STATIC FILE (IMAGE) SERVING
    // ===========================

    @Value("${app.upload.profile-photos-path}")
    private String profilePhotosPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/profile-photos/**")
                .addResourceLocations("file:" + profilePhotosPath);
        
    }
    
    
}