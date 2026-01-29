package com.example.matrimony.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.LoginRequest;
import com.example.matrimony.entity.Admin;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.AdminRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.security.JwtResponse;
import com.example.matrimony.security.JwtUtil1;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
	
	
    private final AdminRepository adminRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil1 jwtUtil;

    public AuthController(AdminRepository adminRepository,
                          ProfileRepository profileRepository,
                         PasswordEncoder passwordEncoder,
                          JwtUtil1 jwtUtil) {
        this.adminRepository = adminRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getEmailId() == null || loginRequest.getCreatePassword() == null) {
            return ResponseEntity.badRequest().body("Email and Password must not be empty");
        }

        String email = loginRequest.getEmailId().trim();
        String password = loginRequest.getCreatePassword().trim();

        // ----- ADMIN LOGIN -----
        Optional<Admin> adminOpt = adminRepository.findByEmailId(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches(password, admin.getCreatePassword())) {
                return ResponseEntity.status(401).body("Invalid admin password");
            }
            String token = jwtUtil.generateToken(admin.getAdminId(), admin.getEmailId(), List.of("ADMIN"));
            return ResponseEntity.ok(new JwtResponse(token, "ADMIN", admin.getEmailId(), admin.getAdminId()));
        }

        //user
     // ===== USER LOGIN =====
        Optional<Profile> profileOpt =
                profileRepository.findByEmailId(email.toLowerCase());

        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();

            // 🔒 Block login until admin approval
            if (Boolean.FALSE.equals(profile.getApproved())) {
                return ResponseEntity
                        .status(403)
                        .body("Your account approval is in pending please wait ");
            }
            
         // Block login if banned
            if (profile.isBanned()) {
                return ResponseEntity.status(403)
                        .body("Your account is banned by admin.");
            }

            // 🔐 Correct BCrypt comparison
            if (!passwordEncoder.matches(password, profile.getCreatePassword())) {
                return ResponseEntity.status(401).body("Invalid user password");
            }

            String token = jwtUtil.generateToken(
                    profile.getId(),
                    profile.getEmailId(),
                    List.of("USER")
            );

            return ResponseEntity.ok(
                    new JwtResponse(token, "USER", profile.getEmailId(), profile.getId())
            );
        }

        return ResponseEntity.status(404).body("User not found");
        
     // User login
//        Optional<Profile> profileOpt = profileRepository.findByEmailId(email.toLowerCase());
//        if (profileOpt.isPresent()) {
//            Profile profile = profileOpt.get();
//
//            System.out.println("Request password: [" + password + "]");
//            System.out.println("DB password: [" + profile.getCreatePassword() + "]");
//            System.out.println("Request length: " + password.length());
//            System.out.println("DB length: " + profile.getCreatePassword().length());
//
//           if (!password.equals(profile.getCreatePassword().trim())) {
//               return ResponseEntity.status(401).body("Invalid user password");
//           }
//
////            if (!passwordEncoder.matches(password, profile.getCreatePassword())) {
////              return ResponseEntity.status(401).body("Invalid profile password");
////          }
////            	
//            String token = jwtUtil.generateToken(profile.getId(), profile.getEmailId(), List.of("USER"));
//            return ResponseEntity.ok(new JwtResponse(token, "USER", profile.getEmailId(), profile.getId()));
//        }
//
//
//        return ResponseEntity.status(404).body("User not found");
//    }

	
	
//
//    private final AdminRepository adminRepository;
//    private final ProfileRepository profileRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil1 jwtUtil1;
//
//    public AuthController(AdminRepository adminRepository,
//                          ProfileRepository profileRepository,
//                          PasswordEncoder passwordEncoder,
//                          JwtUtil1 jwtUtil1) {
//        this.adminRepository = adminRepository;
//        this.profileRepository = profileRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtil1 = jwtUtil1;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//
//        if (loginRequest == null ||
//                loginRequest.getEmailId() == null || loginRequest.getEmailId().isBlank() ||
//                loginRequest.getCreatePassword() == null || loginRequest.getCreatePassword().isBlank()) {
//            return ResponseEntity.badRequest().body("Email and Password must not be empty");
//        }
//
//        String email = loginRequest.getEmailId().trim();
//        String password = loginRequest.getCreatePassword().trim();
//
//        // ===== ADMIN LOGIN =====
//        Optional<Admin> adminOpt = adminRepository.findByEmailId(email);
//        if (adminOpt.isPresent()) {
//            Admin admin = adminOpt.get();
//
//            if (!passwordEncoder.matches(password, admin.getCreatePassword())) {
//                return ResponseEntity.status(401).body("Invalid admin password");
//            }
//
//            String token = jwtUtil1.generateToken(admin.getAdminId(), admin.getEmailId(), List.of("ADMIN"));
//           // String refreshToken = jwtUtil1.generateRefreshToken(admin.getAdminId(), admin.getEmailId(), "ADMIN");
//
//            return ResponseEntity.ok(new JwtResponse(token, "admin", admin.getEmailId(), admin.getAdminId(), refreshToken));
//        }
//
//        // ===== USER LOGIN =====
//        Optional<Profile> profileOpt = profileRepository.findByEmailId(email);
//        if (profileOpt.isPresent()) {
//            Profile profile = profileOpt.get();
//
//            if (!passwordEncoder.matches(password, profile.getCreatePassword())) {
//                return ResponseEntity.status(401).body("Invalid profile password");
//            }
//
//            String token = jwtUtil1.generateToken(profile.getId(), profile.getEmailId(), List.of("USER"));
//            //String refreshToken = jwtUtil1.generateRefreshToken(profile.getId(), profile.getEmailId(), "USER");
//
//            //return ResponseEntity.ok(new JwtResponse(token, "user", profile.getEmailId(), profile.getId(), refreshToken));
//            return new JwtResponse(token, "USER", profile.getEmailId(), profile.getId(), refreshToken);
//
//        }
//
//        return ResponseEntity.status(404).body("User not found");
//    }
    }
   }
