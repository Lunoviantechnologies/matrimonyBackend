package com.example.matrimony.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.example.matrimony.entity.Profile;
//import com.example.matrimony.repository.AdminRepository;
//import com.example.matrimony.repository.ProfileRepository;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//
//    private final ProfileRepository profileRepository;
//    private final AdminRepository adminRepository;
//
//    public CustomUserDetailsService(ProfileRepository profileRepository, AdminRepository adminRepository) {
//        this.profileRepository = profileRepository;
//        this.adminRepository = adminRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        if (email == null || email.isBlank()) {
//            throw new UsernameNotFoundException("Email cannot be empty");
//        }
//
//        // 1️⃣ Check Admin first
//        var adminOpt = adminRepository.findByEmailId(email);
//        if (adminOpt.isPresent()) {
//            var admin = adminOpt.get();
//
//            return User.builder()
//                    .username(admin.getEmailId())
//                    .password(admin.getCreatePassword())
//                    .authorities("ROLE_ADMIN")   // ✅ Always ROLE_ADMIN for admins
//                    .build();
//        }
//
//        // 2️⃣ Check Profile (USER)
//        Profile profile = profileRepository.findByEmailId(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        if (!profile.isActiveFlag()) {
//            throw new UsernameNotFoundException("User account deactivated");
//        }
//
//        // Map profile roles to ROLE_USER
//        String role = profile.getRole();
//        if (role == null || role.isBlank()) {
//            role = "USER";  // Default role
//        }
//
//        return User.builder()
//                .username(profile.getEmailId())
//                .password(profile.getCreatePassword())
//                .authorities("ROLE_USER") // Always ROLE_USER for profile
//                .build();
//    }
//}
//
//
//
//
//
//
//
////package com.example.matrimony.config;
////
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
////
////import com.example.matrimony.entity.Profile;
////import com.example.matrimony.repository.AdminRepository;
////import com.example.matrimony.repository.ProfileRepository;
////
/////**
//// * Null-safe UserDetailsService for authentication.
//// * - Uses profile.getActive() (nullable Boolean) safely.
//// * - Uses profile.isActiveFlag() convenience method (returns primitive boolean).
//// * - Normalizes role(s) and maps to granted authorities.
//// */
////@Service
////public class CustomUserDetailsService implements UserDetailsService {
////
////    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
////
////    private final ProfileRepository profileRepository;
////    
////    @Autowired
////    private AdminRepository adminRepository;
////
////
////    public CustomUserDetailsService(ProfileRepository profileRepository) {
////        this.profileRepository = profileRepository;
////    }
////
////    
////    @Override
////    public UserDetails loadUserByUsername(String email) {
////
////        // 1️⃣ CHECK ADMIN FIRST
////        var adminOpt = adminRepository.findByEmailId(email);
////        if (adminOpt.isPresent()) {
////            var admin = adminOpt.get();
////
////            return User.builder()
////                    .username(admin.getEmailId())
////                    .password(admin.getCreatePassword())
////                    .authorities("ROLE_ADMIN")
////                    .build();
////        }
////
////        // 2️⃣ THEN CHECK PROFILE (USER)
////        Profile profile = profileRepository.findByEmailId(email)
////                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////
////        if (!profile.isActiveFlag()) {
////            throw new UsernameNotFoundException("User deactivated");
////        }
////
////        return User.builder()
////                .username(profile.getEmailId())
////                .password(profile.getCreatePassword())
////                .authorities("ROLE_" + profile.getRole().toUpperCase())
////                .build();
////    }
////
////    
////    
////
////}
