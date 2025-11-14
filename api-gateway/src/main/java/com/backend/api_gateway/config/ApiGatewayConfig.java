package com.backend.api_gateway.config;

import com.backend.api_gateway.security.JwtAuthFilter;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;

import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class ApiGatewayConfig {

    private final JwtAuthFilter jwtFilter;

    public ApiGatewayConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Auth service routes (public, no JWT)
    @Bean
    public RouterFunction<?> authServiceRoute() {
        return GatewayRouterFunctions.route("auth-service")
                .route(path("/auth/**"),
                        HandlerFunctions.http("lb://AUTH-SERVICE"))
                .build();
    }

    // User service (protected)
    @Bean
    public RouterFunction<?> userServiceRoute() {
        return GatewayRouterFunctions.route("user-service")
                .filter(jwtFilter)
                .route(path("/api/user/**"),
                        HandlerFunctions.http("lb://USER-SERVICE"))
                .build();
    }

    // Restaurant main APIs (protected)
    @Bean
    public RouterFunction<?> restaurantMainRoute() {
        return GatewayRouterFunctions.route("restaurant-service-main")
                .filter(jwtFilter)
                .route(path("/api/restaurant/**"),
                        HandlerFunctions.http("lb://RESTAURANT-SERVICE"))
                .build();
    }

    // Restaurant MenuItem APIs (protected)
    @Bean
    public RouterFunction<?> restaurantMenuItemRoute() {
        return GatewayRouterFunctions.route("restaurant-service-menuitem")
                .filter(jwtFilter)
                .route(path("/api/MenuItem/**"),
                        HandlerFunctions.http("lb://RESTAURANT-SERVICE"))
                .build();
    }

    // Cart service (protected)
    @Bean
    public RouterFunction<?> cartServiceRoute() {
        return GatewayRouterFunctions.route("cart-service")
                .filter(jwtFilter)
                .route(path("/api/cart/**"),
                        HandlerFunctions.http("lb://CART-SERVICE"))
                .build();
    }

    // Order service (protected)
    @Bean
    public RouterFunction<?> orderServiceRoute() {
        return GatewayRouterFunctions.route("order-service")
                .route(path("/api/order/**"),
                        HandlerFunctions.http("lb://ORDER-SERVICE"))
                .filter(jwtFilter)
                .build();
    }

    // Payment service (protected)
    @Bean
    public RouterFunction<?> paymentServiceRoute() {
        return GatewayRouterFunctions.route("payment-service")
                .filter(jwtFilter)
                .route(path("/api/payment/**"),
                        HandlerFunctions.http("lb://PAYMENT-SERVICE"))
                .build();
    }
}
