package com.example.matrimony.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.JwtResponse;
import com.example.matrimony.dto.LoginRequest;
import com.example.matrimony.entity.Admin;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.AdminRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.security.JwtUtil1;
@Service
@Transactional
public class AuthService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil1 jwtUtil;
    private final AdminRepository adminRepository;


    public AuthService(ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil1 jwtUtil,
            AdminRepository adminRepository) {
    	this.profileRepository = profileRepository;
    	this.passwordEncoder = passwordEncoder;
    	this.jwtUtil = jwtUtil;
    	this.adminRepository = adminRepository;
    }

    
    public void createAdmin(Admin req ) {
        Admin admin = new Admin();
        admin.setUsername(req.getUsername());
        admin.setEmailId(req.getEmailId());
        //admin.setAdminRole("ADMIN"); // 🔥 MUST

        admin.setAdminRole("ADMIN");
        admin.setCreatePassword(req.getCreatePassword());
        admin.setCreatePassword(
                passwordEncoder.encode(admin.getCreatePassword())
        );

        adminRepository.save(admin);
    }



    // CHECK ANY PROFILE EXISTS
    public boolean anyAdminExists() {
        return !profileRepository.findAll().isEmpty();
    }
    
    public JwtResponse login(LoginRequest req) {

        // ADMIN Login
        Optional<Admin> adminOpt = adminRepository.findByEmailId(req.getEmailId());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            // Compare password (plain text for now)
            if (!req.getCreatePassword().equals(admin.getCreatePassword())) {
                throw new IllegalArgumentException("Invalid Admin Password");
            }

            String token = jwtUtil.generateToken(admin.getAdminId(), admin.getEmailId(), List.of("ADMIN"));
            return new JwtResponse(token, "ADMIN", admin.getEmailId(), admin.getAdminId());
        }

        // PROFILE Login
        Optional<Profile> profileOpt = profileRepository.findByEmailId(req.getEmailId());
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();

            if (!req.getCreatePassword().equals(profile.getCreatePassword())) {
                throw new IllegalArgumentException("Invalid Profile Password");
            }

            String token = jwtUtil.generateToken(profile.getId(), profile.getEmailId(), List.of("USER"));
            return new JwtResponse(token, "USER", profile.getEmailId(), profile.getId());
        }

        throw new IllegalArgumentException("User not found");
    }
    
    
    

//    public JwtResponse login(LoginRequest req) {
//
//        // ADMIN Login
//        Optional<Admin> adminOpt = adminRepository.findByEmailId(req.getEmailId());
//        if (adminOpt.isPresent()) {
//            Admin admin = adminOpt.get();
//            if (!req.getCreatePassword().equals(admin.getCreatePassword())) {
//                throw new IllegalArgumentException("Invalid Admin Password");
//            }
//
//            String token = jwtUtil.generateToken(admin.getEmailId(), List.of("ADMIN"));
//            return new JwtResponse(token, "ADMIN", admin.getEmailId());
//        }
//
//        // PROFILE Login
//        Optional<Profile> profileOpt = profileRepository.findByEmailId(req.getEmailId());
//        if (profileOpt.isPresent()) {
//            Profile profile = profileOpt.get();
//
//            if (!req.getCreatePassword().equals(profile.getCreatePassword())) {
//                throw new IllegalArgumentException("Invalid Profile Password");
//            }
//
//            String token = jwtUtil.generateToken(profile.getEmailId(), List.of("PROFILE"));
//            return new JwtResponse(token, "PROFILE", profile.getEmailId());
//        }
//
//        throw new IllegalArgumentException("User not found");
//    }


}
