package com.example.matrimony.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.example.matrimony.hrms.JwtUtil1;

import io.jsonwebtoken.Claims;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
	    String query = request.getURI().getQuery();
	    String token = null;

	    if (query != null) {
	        for (String q : query.split("&")) {
	            if (q.startsWith("token=")) token = q.substring(6);
	        }
	    }

	    if (token == null) {
	        String header = request.getHeaders().getFirst("Authorization");
	        if (header != null && header.startsWith("Bearer ")) {
	            token = header.substring(7);
	        }
	    }

	    if (token != null) {
	        try {
	            // ✅ Use extractAllClaims instead of validate
	            Claims claims = jwtUtil.extractAllClaims(token);

	            // Optional: validate expiration
	            if (jwtUtil.validateToken(token, claims.getSubject())) {
	                String subject = claims.getSubject();
	                Principal p = () -> subject;
	                attributes.put("principal", p);
	                return true;
	            } else {
	                return false; // token expired
	            }
	        } catch (Exception e) {
	            // log exception
	            System.err.println("Invalid token in WebSocket handshake: " + e.getMessage());
	            return false;
	        }
	    }

	    // allow anonymous
	    return true;
	}

	
	
	
    private final JwtUtil1 jwtUtil;

    public WebSocketAuthInterceptor(JwtUtil1 jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        String query = request.getURI().getQuery();
//        String token = null;
//        if (query != null) {
//            for (String q : query.split("&")) {
//                if (q.startsWith("token=")) token = q.substring(6);
//            }
//        }
//        if (token == null) {
//            var headers = request.getHeaders().getFirst("Authorization");
//            if (headers != null && headers.startsWith("Bearer ")) token = headers.substring(7);
//        }
//        if (token != null) {
//            try {
//                Claims claims = jwtUtil.validate(token);
//                String subject = claims.getSubject();
//                Principal p = () -> subject;
//                attributes.put("principal", p);
//                return true;
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        // allow anonymous
//        return true;
//    }
    
    
//
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) { }
}
