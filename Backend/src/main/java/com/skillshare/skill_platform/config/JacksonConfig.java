package com.skillshare.skill_platform.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Add a default mixin for all proxied classes to handle lazy loading
        mapper.addMixIn(Object.class, IgnoreHibernateMixin.class);
        
        // Disable failing on empty beans
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return new MappingJackson2HttpMessageConverter(mapper);
    }
    
    // Mixin to ignore hibernate-specific properties
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract class IgnoreHibernateMixin {
        // This is just a mixin class, no implementation needed
    }
} 