package uz.sardor.simplebankingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // barcha endpointlarga
                        .allowedOrigins("*") // istalgan frontend (React, Angular, Postman va h.k.)
                        .allowedMethods("*") // GET, POST, PUT, DELETE, OPTIONS
                        .allowedHeaders("*") // barcha headerlarga ruxsat
                        .allowCredentials(false); // agar token bilan ishlasangiz true qiling
            }
        };
    }
}
