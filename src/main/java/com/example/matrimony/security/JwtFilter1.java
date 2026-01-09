package com.example.matrimony.security;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter1 extends OncePerRequestFilter {	
	
	
	
	 @Autowired
	    private JwtUtil1 jwtUtil;

	    @Autowired
	    private MyUserDetailsService1 adminDetailsService;

	    @Autowired
	    private EmployeeUserDetailsService employeeDetailsService;

//	    @Override
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	            throws ServletException, IOException {
//
//	        final String authHeader = request.getHeader("Authorization");
//
//	        String token = null;
//	        String email = null;
//	        String role = null;
//
//	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//	            token = authHeader.substring(7);
//	            try {
//	                email = jwtUtil.extractUsername(token);
//	                role = jwtUtil.extractAllClaims(token).get("roles", java.util.List.class).get(0).toString();
//	            } catch (Exception e) {
//	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	                response.setContentType("application/json");
//	                response.getWriter().write("{\"error\": \"Invalid JWT token.\"}");
//	                return;
//	            }
//	        }
//
//	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//	            UserDetails userDetails = role.equals("ADMIN") ?
//	                    adminDetailsService.loadUserByUsername(email) :
//	                    employeeDetailsService.loadUserByUsername(email);
//
//	            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
//	                UsernamePasswordAuthenticationToken authToken =
//	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//	                SecurityContextHolder.getContext().setAuthentication(authToken);
//	            }
//	        }
//
//	        filterChain.doFilter(request, response);
//	    }
//	
	    
	    
	    @Override
	    protected void doFilterInternal(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain filterChain
	    ) throws ServletException, IOException {

	        final String authHeader = request.getHeader("Authorization");

	        String token = null;
	        String email = null;
	        List<String> roles = null;

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            try {
	                email = jwtUtil.extractUsername(token);
	                roles = jwtUtil.extractAllClaims(token).get("roles", List.class);
	            } catch (Exception e) {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Invalid JWT");
	                return;
	            }
	        }

	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	            // Decide which service to use (OK)
	            UserDetails userDetails =
	                    roles.contains("ADMIN")
	                            ? adminDetailsService.loadUserByUsername(email)
	                            : employeeDetailsService.loadUserByUsername(email);

	            if (jwtUtil.validateToken(token, userDetails.getUsername())) {

	                // ✅ AUTHORITIES MUST COME FROM JWT
	                var authorities = roles.stream()
	                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
	                        .toList();

	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(
	                                userDetails,
	                                null,
	                                authorities
	                        );

	                authToken.setDetails(
	                        new WebAuthenticationDetailsSource().buildDetails(request)
	                );

	                SecurityContextHolder.getContext().setAuthentication(authToken);

	                // DEBUG
	                System.out.println("Authenticated roles: " + authorities);
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
	
//	   @Autowired
//	    private JwtUtil1 jwtUtil;
//	   
//	   
//	   @Autowired
//	    private MyUserDetailsService1 adminDetailsService;
//
//	    @Autowired
//	    private EmployeeUserDetailsService employeeDetailsService;
//
//	    @Override
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	            throws ServletException, IOException {
//
//	        final String authHeader = request.getHeader("Authorization");
//
//	        // Log the Authorization header
//	        System.out.println("🔐 Authorization Header: " + authHeader);
//
//	        String token = null;
//	        String email = null;
//	        String role = null;
//
//	        // Extract token
//	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//	            token = authHeader.substring(7);
//
//	            try {
//	                // Extract email and role from token
//	                email = jwtUtil.extractUsername(token);
//	                role = jwtUtil.extractUsername(token);
//
//	                System.out.println("✅ Token Extracted: " + token);
//	                System.out.println("📧 Email from token: " + email);
//	                System.out.println("🛡 Role from token: " + role);
//
//	            } catch (Exception e) {
//	                System.err.println("❌ Failed to extract details from token: " + e.getMessage());
//	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	                response.setContentType("application/json");
//	                response.getWriter().write("{\"error\": \"Invalid or malformed JWT token.\"}");
//	                return;
//	            }
//	        } else if (authHeader != null) {
//	            // Malformed Authorization header (e.g., no "Bearer ")
//	            System.err.println("❌ Invalid Authorization header format");
//	        }
//
//	        // Proceed with authentication only if we have valid email and no existing auth
//	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//	            UserDetails userDetails;
//
//	            try {
//	                if ("ROLE_EMPLOYEE".equals(role)) {
//	                    userDetails = employeeDetailsService.loadUserByUsername(email);
//	                } else {
//	                    userDetails = adminDetailsService.loadUserByUsername(email);
//	                }
//
//	                // Validate token
//	                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
//	                    UsernamePasswordAuthenticationToken authToken =
//	                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//	                    SecurityContextHolder.getContext().setAuthentication(authToken);
//	                    System.out.println("✅ User authenticated: " + email);
//	                } else {
//	                    System.err.println("❌ Token validation failed for user: " + email);
//	                }
//
//	            } catch (Exception e) {
//	                System.err.println("❌ Error loading user or validating token: " + e.getMessage());
//	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	                response.setContentType("application/json");
//	                response.getWriter().write("{\"error\": \"Unauthorized access. Invalid credentials or token.\"}");
//	                return;
//	            }
//	        }
//
//	        filterChain.doFilter(request, response);
//	    }
//	   



}
