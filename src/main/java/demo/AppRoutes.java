package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import sample.handler.MessageHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AppRoutes {

    @Autowired
    private MessageHandler messageHandler;

    @Bean
    RouterFunction<?> apis() {
        return nest(path("/hotels"), nest(accept(MediaType.APPLICATION_JSON),
                route(
                        GET("/"), messageHandler::getMessages)
                        .andRoute(POST("/"), messageHandler::addMessage)
                        .andRoute(GET("/{id}"), messageHandler::getMessage)
                        .andRoute(PUT("/{id}"), messageHandler::updateMessage)
                        .andRoute(DELETE("/{id}"), messageHandler::deleteMessage)
        ));
    }

}