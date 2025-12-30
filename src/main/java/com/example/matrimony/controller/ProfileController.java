package com.example.matrimony.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.PrivacySettingsDto;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.hrms.MyUserDetailsService1;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.ProfileService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ✅ Constructor Injection (prevents NullPointerException)
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

   
 // api for the registration  
    @PostMapping("/register")
    public ResponseEntity<?> createProfile(@RequestBody Profile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().body("Profile cannot be null");
        }
        if (profile.getEmailId() == null || profile.getEmailId().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        try {
            // ✅ bcrypt before save
            profile.setCreatePassword(
                    passwordEncoder.encode(profile.getCreatePassword())
            );

            Profile saved = profileRepository.save(profile);
            return ResponseEntity.ok(saved);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
   


    @GetMapping("/Allprofiles")
    public ResponseEntity<List<Profile>> listProfiles() {

        List<Profile> profiles = profileService.listAll();

        profiles.forEach(profile -> {
            if (profile.getUpdatePhoto() != null && !profile.getUpdatePhoto().isBlank()) {
                try {
                    Path imagePath = Paths.get("uploads/profile-photos")
                            .resolve(profile.getUpdatePhoto());

                    if (Files.exists(imagePath)) {
                        byte[] imageBytes = Files.readAllBytes(imagePath);
                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        profile.setUpdatePhoto("data:image/jpeg;base64," + base64Image);
                    } else {
                        profile.setUpdatePhoto(null);
                    }
                } catch (Exception e) {
                    profile.setUpdatePhoto(null);
                }
            } else {
                profile.setUpdatePhoto(null);
            }
        });

        return ResponseEntity.ok(profiles);
    }
    
    // ✅ UPDATE PROFILE API
    @PutMapping("/update/{id}")
    public ResponseEntity<Profile> updateProfileByUser(
            @PathVariable Long id,
            @RequestBody Profile profile) {

        Profile updatedProfile = profileService.userUpdateProfile(id, profile);
        return ResponseEntity.ok(updatedProfile);
    }


 // GET current privacy settings
    @GetMapping("/{profileId}/privacy")
    public ResponseEntity<PrivacySettingsDto> getPrivacy(@PathVariable Long profileId) {

        Profile p = profileService.getProfile(profileId);

        PrivacySettingsDto dto = new PrivacySettingsDto();
        dto.setProfileVisibility(p.getProfileVisibility());
        dto.setHideProfilePhoto(p.getHideProfilePhoto());

        return ResponseEntity.ok(dto);
    }

    // UPDATE privacy settings
    @PutMapping("/{profileId}/privacy")
    public ResponseEntity<?> updatePrivacy(
            @PathVariable Long profileId,
            @RequestBody PrivacySettingsDto dto) {

        profileService.updatePrivacySettings(profileId, dto);
        return ResponseEntity.ok(Map.of("message", "Privacy settings updated"));
    }
// DELETE: Delete Profile by ID
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        profileService.requestAccountDeletion(id);
        return ResponseEntity.ok("Profile deletion scheduled");
    }

    
    
    @PostMapping("/recover-account")
    public ResponseEntity<?> recoverAccount(Authentication authentication) {

        String email = authentication.getName(); // ✅ from JWT

        Profile profile = profileService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileService.recoverAccount(profile.getId());

        return ResponseEntity.ok("Account recovered successfully");
    }
    
    
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getProfilesCount() {
        long count = profileService.getRegisteredProfilesCount();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

}
