package com.shoppingmall.apigateway.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorsConfig implements WebFluxConfigurer {

    public static CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:9001");
        allowedOriginPatterns.add("http://localhost:8081");
        allowedOriginPatterns.add("http://localhost:8082");
        allowedOriginPatterns.add("http://localhost:8084");
//        allowedOriginPatterns.add("http://localhost:3000");

        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");
        allowedHttpMethods.add("PUT");
        allowedHttpMethods.add("DELETE");

        configuration.setAllowedOrigins(allowedOriginPatterns);
        configuration.setAllowedMethods(allowedHttpMethods);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
