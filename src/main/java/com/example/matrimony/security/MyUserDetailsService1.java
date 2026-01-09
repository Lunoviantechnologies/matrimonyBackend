package com.example.matrimony.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.repository.AdminRepository;



@Service
public class MyUserDetailsService1 implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByEmailId(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Admin not found with email: " + email));

        // ✅ NULL SAFE ROLE HANDLING
        String role = admin.getAdminRole();
        if (role == null || role.isBlank()) {
            role = "ADMIN"; // default role
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmailId())
                .password(admin.getCreatePassword())
                .authorities(
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                        )
                )
                .build();
    }
    
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Admin admin = adminRepo.findByEmailId(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(admin.getEmailId())
//                .password(admin.getCreatePassword())
//                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getAdminRole().toUpperCase())))
//                .build();
//    }


}
