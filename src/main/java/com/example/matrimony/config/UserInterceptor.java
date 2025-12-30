package com.example.matrimony.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // Access STOMP headers
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // When a WebSocket client CONNECTS
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Read custom header "user-id"
            String userId = accessor.getFirstNativeHeader("user-id");

            // Validate header
            if (StringUtils.hasText(userId)) {
                // Attach authenticated principal to WebSocket session
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null);
                accessor.setUser(auth);
            }
        }

        return message;
    }
}
