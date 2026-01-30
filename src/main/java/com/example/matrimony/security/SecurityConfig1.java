package com.example.matrimony.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig1 {

    @Autowired
    private JwtFilter1 jwtFilter;

    @Autowired
    private MyUserDetailsService1 myUserDetailsService;

    @Autowired
    private EmployeeUserDetailsService employeeDetailsService;

    @Autowired
    private AppConfig appconfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Public Endpoints
                .requestMatchers(
                    "/api/auth/login",
                    "/api/payment/create-order",
                   "/api/payment/verify",
                   "/api/payment/successful",
                   "/api/auth/register/send-otp",
                   "/api/auth/register/verify-otp",
                   "/api/profiles/register",
                   "/api/payment/successful/**",
                   "/api/auth/forgot-password",
                   "/api/auth/verify-otp",
                    "/api/auth/reset-password",
                    "/api/contact/send",
                    "/api/profiles/count",
                   "/ws-chat/**" ,
                   "/api/tickets",
                   "/api/admin/create-admin"
                  
                ).permitAll()

                // ✅ Restricted employee endpoints
                .requestMatchers(
                		
                		// "/api/admin/profiles/*/status",
                		 //"/api/admin/profiles/**",
                		 "/api/friends/send/**",
                		 "/api/admin/update/**",
                         "/api/friends/sent/**",
                         "/api/friends/respond/**",
                         "/api/friends/accepted/received/**",
                         "/api/friends/accepted/sent/**",
                         "/api/friends/sent/**",
                         "/api/friends/sent/delete/**",
                         "/api/friends/rejected/received/**",
                         "/api/friends/rejected/sent/**",
                         "/api/profiles/*/privacy",
                         "/api/profiles/update/**",
                         "/api/profiles/*/privacy",
                         "/api/auth/change-password/**",
                         "/api/profiles/delete/**",                         
                          "/api/notifications/read/**",
                          "/api/notifications/unread-count",
                          "/api/notifications/GetAll",
                          "/api/chat/conversation/**",
                          "/api/chat/chat.send/**",
                          "/api/chat/online",
                          "/api/profiles/Allprofiles",//user
                          "/api/payment/create-order",
                          "/api/payment/verify",
                          "/api/payment/successful/latest/**",
                          "/api/notifications/mark-all-read",
                          "/api/friends/received/**"   ,
                          "/api/chat/send/**",
                          "/api/profiles/record/**",
                          "/api/profiles/views/**",
                          "/api/profiles/total-views/**",
                          "/api/astro-number/All",
                          "/api/plans",
                          "/api/profiles/myprofiles/**",
                          "/api/admin/photo/**",
                          "/api/chat/clear/**",
                          "/api/chat/seen/**",                       
                          "/api/block/user/**",
                          "/api/block/status/**",
                          "/api/block/unblock/**",
                          "/api/reports/user/**"
                   
                ).hasRole("USER")

                // ✅ Restricted admin endpoints (still protected)
                .requestMatchers(
                    //"/api/admin/**",
                    "/api/admin/mark-all-read",
                    "/api/admin/GetAll",
                    "/api/admin/unread-count",
                    "/api/admin/read/",
                    "/api/admin/profiles",//admin
                    "/api/friends/all",
                    "/api/admin/profiles/approve/**",
                    "/api/admin/profiles/reject/**",
                    "/api/payment/successful",
                     "/api/tickets/all",
                     "/api/tickets/*/resolve",
                     "/api/admin/delete/**",
                     "/api/admin/notifications/All",
                     "/api/admin/notifications/unread-count",
                     "/api/admin/notifications/read/**",
                     "/api/admin/notifications/mark-all-read",
                     "/api/astro-number/add",
                     "/api/astro-number/update/**",
                     "/api/admin/**",
                     "/api/admin/plan/**",
                     "/api/admin/plans/all",
                     "/api/astro-number/admin/All",
                     "/api/admin/bugs/report",
                     "/api/admin/delete-profile/**",
                     "/api/reports/GetAll",
                     "/api/archived-chats/between",
                     "/api/archived-chats/Get/**",
                     "/api/admin/reports/*/reject" ,
                     "/api/admin/backup-delete/**",
                     "api/admin/banuser/**"
                                        
                ).hasRole("ADMIN")

                // ✅ All others must be authenticated
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(myUserDetailsService);
        adminProvider.setPasswordEncoder(appconfig.passwordEncoder());

        DaoAuthenticationProvider employeeProvider = new DaoAuthenticationProvider();
        employeeProvider.setUserDetailsService(employeeDetailsService);
        employeeProvider.setPasswordEncoder(appconfig.passwordEncoder());

        return new ProviderManager(adminProvider, employeeProvider);
    }
}