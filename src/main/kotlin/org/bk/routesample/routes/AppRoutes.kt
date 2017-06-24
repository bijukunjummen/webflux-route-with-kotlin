package org.bk.routesample.routes

import org.bk.routesample.handler.MessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.router

@Configuration
class AppRoutes(private val messageHandler: MessageHandler) {
    
    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/messages").nest { 
            GET("/", messageHandler::getMessages)
            GET("/{id}", messageHandler::getMessage)
            POST("/", messageHandler::addMessage)
            PUT("/{id}", messageHandler::updateMessage)
            DELETE("/{id}", messageHandler::deleteMessage)
        }
    }
    
}