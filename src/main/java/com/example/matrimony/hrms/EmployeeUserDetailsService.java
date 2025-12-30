package com.example.matrimony.hrms;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;

@Service
public class EmployeeUserDetailsService  implements org.springframework.security.core.userdetails.UserDetailsService{

    @Autowired
    private ProfileRepository employeeRepository;
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile emp = employeeRepository.findByEmailId(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

        String role = emp.getRole() != null ? emp.getRole().toUpperCase() : "EMPLOYEE";

        return org.springframework.security.core.userdetails.User
                .withUsername(emp.getEmailId())
                .password(emp.getCreatePassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)))
                .build();
    }
 }	

