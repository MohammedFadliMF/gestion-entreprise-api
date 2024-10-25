package com.example.springbootoauthjwt.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig  implements WebSocketMessageBrokerConfigurer{
   
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // pour permettre à un simple courtier de messages basé sur la mémoire de
        //transmettre les messages d'accueil au client sur des destinations préfixées
        // par ... 
        registry.enableSimpleBroker("/topic");
        // registry.setUserDestinationPrefix("/user");
    }
}
