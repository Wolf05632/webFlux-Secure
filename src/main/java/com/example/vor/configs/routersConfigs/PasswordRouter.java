package com.example.vor.configs.routersConfigs;

import com.example.vor.handlers.PasswordHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class PasswordRouter {

    @Bean
    public RouterFunction<ServerResponse> passwordRoute(PasswordHandler passwordHandler) {
        RequestPredicate routeChP = RequestPredicates
                .GET("/chp/{path}")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN));

        return RouterFunctions
                .route(routeChP, passwordHandler::changePassword);
    }
}
