package com.example.matrimony.admin;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.repository.AdminRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmailId(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Admin not found"));

        return User.builder()
                .username(admin.getEmailId())
                .password(admin.getCreatePassword()) // IMPORTANT
                .roles(admin.getAdminRole())          // SUPER_ADMIN / ADMIN
                .build();
    }
}