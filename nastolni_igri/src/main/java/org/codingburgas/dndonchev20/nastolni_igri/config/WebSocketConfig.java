package org.codingburgas.dndonchev20.nastolni_igri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Register the WebSocket endpoint for the handshake (with SockJS fallback)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // Configure a simple in-memory message broker to broadcast messages to clients
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Messages sent to destinations starting with /topic will be routed to the broker
        config.enableSimpleBroker("/topic");
        // Application-level messages sent to /app will be routed to @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }
}
