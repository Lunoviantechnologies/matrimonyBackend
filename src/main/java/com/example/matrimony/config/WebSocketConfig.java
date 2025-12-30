package com.example.matrimony.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // For broadcast and private messages
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(
                            org.springframework.http.server.ServerHttpRequest request,
                            org.springframework.web.socket.WebSocketHandler wsHandler,
                            Map<String, Object> attributes) {

                        Long userId = -1L; // fallback if missing
                        String query = request.getURI().getQuery();
                        if (query != null) {
                            for (String q : query.split("&")) {
                                if (q.startsWith("userId=")) {
                                    try {
                                        userId = Long.parseLong(q.substring(7));
                                    } catch (NumberFormatException ignored) {}
                                }
                            }
                        }

                        final Long finalUserId = userId;
                        return () -> String.valueOf(finalUserId);
                    }
                })
                .withSockJS(); // enable SockJS fallback
    }
}
