package com.example.vor.configs.routersConfigs;

import com.example.vor.handlers.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> greetingRoute(GreetingHandler greetingHandler) {
        RequestPredicate routeHello = RequestPredicates
                .GET("/hello")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN));

        return RouterFunctions
                .route(routeHello, greetingHandler::hello)
                .andRoute(RequestPredicates.GET("/"), greetingHandler::index);
    }
}
