package com.backend.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;

import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouterFunction<?> cartServiceRoute() {
        return GatewayRouterFunctions.route("cart-service")
                .route(path("/api/cart/**"),
                        HandlerFunctions.http("lb://CART-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<?> orderServiceRoute() {
        return GatewayRouterFunctions.route("order-service")
                .route(path("/api/order/**"),
                        HandlerFunctions.http("lb://ORDER-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<?> restaurantMainRoute() {
        return GatewayRouterFunctions.route("restaurant-service-main")
                .route(path("/api/restaurant/**"),
                        HandlerFunctions.http("lb://RESTAURANT-SERVICE"))
                .build();
    }

    // For menu item operations under restaurant
    @Bean
    public RouterFunction<?> restaurantMenuItemRoute() {
        return GatewayRouterFunctions.route("restaurant-service-menuitem")
                .route(path("/api/MenuItem/**"),
                        HandlerFunctions.http("lb://RESTAURANT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<?> userServiceRoute() {
        return GatewayRouterFunctions.route("user-service")
                .route(path("/api/user/**"),
                        HandlerFunctions.http("lb://USER-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<?> paymentServiceRoute() {
        return GatewayRouterFunctions.route("payment-service")
                .route(path("/api/payment/**"),
                        HandlerFunctions.http("lb://PAYMENT-SERVICE"))
                .build();
    }
}