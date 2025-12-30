//package com.example.matrimony.filter;
//
//import com.example.matrimony.service.JwtUtil;
//import com.example.matrimony.config.CustomUserDetailsService;
//import com.example.matrimony.hrms.MyUserDetailsService1;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//	
//	
//	
//	private final JwtUtil jwtUtil;
//    private final MyUserDetailsService1 userDetailsService;
//
//    @Autowired
//    public JwtFilter(JwtUtil jwtUtil, MyUserDetailsService1 userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        // ✅ Only skip endpoints that NEVER need authentication
//        if (path.startsWith("/api/auth/") ||
//            path.startsWith("/api/profiles/register") ||
//           // path.startsWith("/api/admin/profiles") ||
//           // path.startsWith("/api/admin/delete/**") ||
//            path.startsWith("/ws") ||
//            path.startsWith("/app") ||
//            path.startsWith("/ws-chat") ||
//            path.startsWith("/h2-console")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        // ✅ If no token, allow request to continue (SecurityConfig will decide)
////        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////            filterChain.doFilter(request, response);
////            return;
////        }
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
//            return;
//        }
//
//
//        String token = authHeader.substring(7);
//
//        try {
//            Claims claims = jwtUtil.validate(token).getBody();
//            String email = claims.getSubject();
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//            if (userDetails != null && jwtUtil.isTokenValid(token, userDetails)) {
//
//                List<String> roles = claims.get("roles", List.class);
//                var authorities = roles.stream()
//                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase())
//                        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                Long userId = jwtUtil.extractUserId(token);
//                request.setAttribute("userId", userId);
//            }
//
//            filterChain.doFilter(request, response);
//
//        } catch (Exception ex) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
//        }
//    }
//	
//	
//	
//	
////    private final JwtUtil jwtUtil;
////    private final CustomUserDetailsService userDetailsService;
////
////    @Autowired
////    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
////        this.jwtUtil = jwtUtil;
////        this.userDetailsService = userDetailsService;
////    }
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
////            throws ServletException, IOException {
////
////        String path = request.getServletPath();
////
////        // Skip public endpoints
////        if (path.startsWith("/api/auth/") ||
////        	    path.startsWith("/api/profiles/register") ||
////        	    path.startsWith("/api/friends/") ||  // ✅ This skips ALL friend APIs
////        	    path.startsWith("/ws") ||
////        	    path.startsWith("/app") ||
////        	    path.startsWith("/ws-chat") ||
////        	    path.startsWith("/h2-console") ||
////        	    path.startsWith("/api/admin/create-admin")||
////        	    path.startsWith("/api/admin/profiles")) {
////
////        	    filterChain.doFilter(request, response);
////        	    return;
////        	}
////
////
////
////        String authHeader = request.getHeader("Authorization");
////
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////            String token = authHeader.substring(7);
////
////            try {
////                Claims claims = jwtUtil.validate(token).getBody();
////                String email = claims.getSubject();
////
////                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
////
////                if (userDetails != null && jwtUtil.isTokenValid(token, userDetails)) {
////
////                    List<String> roles = claims.get("roles", List.class);
////                    var authorities = roles.stream()
////                            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase())
////                            .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
////                            .collect(Collectors.toList());
////
////                    UsernamePasswordAuthenticationToken authToken =
////                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
////
////                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                    SecurityContextHolder.getContext().setAuthentication(authToken);
////
////                    // Attach userId to request
////                    Long userId = jwtUtil.extractUserId(token);
////                    request.setAttribute("userId", userId);
////                }
////            } catch (Exception ex) {
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
////                return;
////            }
////        } else {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
////            return;
////        }
////
////        filterChain.doFilter(request, response);
////    }
//}
