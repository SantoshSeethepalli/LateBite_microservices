package com.backend.api_gateway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class LoadBalancerConfig {

    // ✅ This makes the Gateway’s RestClient understand lb:// URIs
    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
