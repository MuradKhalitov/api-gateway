package ru.murad.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-api",
                        r -> r.path("/api/v1/user-api/**")
                                .uri("lb://USER-API"))
                .build();
    }

    @Bean
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Request: {} {} {}", request.getMethod(), request.getPath(), request.getURI());
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
                    log.info("Response: {} {} {}", exchange.getResponse().getStatusCode(), request.getPath(), request.getURI())));
        };
    }

}
