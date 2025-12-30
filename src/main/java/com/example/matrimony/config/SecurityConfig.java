package com.example.matrimony.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.example.matrimony.filter.JwtFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//    
//    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http  .cors().and().csrf().disable()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    .and()
//    .authorizeHttpRequests()
//
//    // ✅ PUBLIC ENDPOINTS
//    .requestMatchers(
//                //"/api/auth/**",
//                "/api/profiles/register",
//                //"/api/admin/photo/**", 
//                //"/profile-photos/**",
//        		//"/api/admin/profiles/**",
//
//        		"/api/admin/profiles",
//
//                "/api/notifications/**"
//            ).permitAll()
//            
//            // Admin endpoints
//            .requestMatchers("/api/admin/delete/**").hasRole("ADMIN")
//            
//            // User endpoints (profiles, friends, chat)
//            .requestMatchers(
////                "/api/friends/accepted/received/**",
////                "/api/friends/accepted/sent/**",
////                "/api/friends/received/**",
////                "/api/friends/rejected/sent/**",
////        		"/api/friends/rejected/received/**",
////        		"/api/friends/respond/**",
////        		"/api/friends/delete/**",
////        		"/api/friends/sent/**",
////        		"/api/friends/send/**",
////        		"/api/admin/update/**",
//        		
//                "/api/chat/**"
//            ).hasRole("USER")
//            
//            // Any other request must be authenticated
//            .anyRequest().authenticated()
//            
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // Add JWT filter before UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.cors().and().csrf().disable()
////            .authorizeHttpRequests()
////            .requestMatchers("/api/auth/**",
////            		"/api/profiles/register",
////            		//"/api/admin/profiles",
////            		"/api/admin/profiles/**",
////            		"/api/friends/send/**",
////            		"/api/friends/received/**",
////            		"/api/friends/respond/**",
////            		"/api/friends/sent/**",
////            		"/api/friends/accepted/received/**",
////            		"/api/friends/accepted/sent/**",
////            		"/api/friends/rejected/sent/**",
////            		"/api/friends/rejected/received/**",
////            		"/api/friends/rejected",            		
////            		"/api/chat/chat.send/**",
////            		"/api/chat/conversation/**",
////            		"/api/chat/online",
////            	//	"/api/admin/delete/**",
////            		//"/api/admin/update/**",
////            		"/api/friends/all",
////            		//"/api/admin/photo/**",
////            		"/api/notifications/read/**",
////            		"/api/notifications/mark-all-read",
////            		"/api/notifications/unread-count",
////            		"/api/notifications/GetAll",
////            		"/api/friends/sent/delete/**",
////            		"/api/admin/create-admin"
////            		).permitAll()
////            .requestMatchers("/api/admin/**",
////            		"/api/admin/delete/**"
////            		//"/api/admin/create-admin"
////            		
////            		).hasRole("ADMIN")
////            .anyRequest().permitAll()
////            .and()
////            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////
////        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
////        return http.build();
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//	
//
//}