package com.example.matrimony.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;
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

import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.dto.PrivacySettingsDto;
import com.example.matrimony.dto.ProfileDto;
import com.example.matrimony.entity.Profile;
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
    @GetMapping("/myprofiles/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
        Optional<Profile> p = profileRepository.findById(id);
        if (p.isEmpty()) return ResponseEntity.notFound().build();

        Profile profile = p.get();
        ProfileDto dto = new ProfileDto();

        dto.setId(profile.getId());
        dto.setProfileFor(profile.getProfileFor());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setMobileNumber(profile.getMobileNumber());
        dto.setCreatePassword(profile.getCreatePassword());
        dto.setRole(profile.getRole());
        dto.setAge(profile.getAge());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setEmailId(profile.getEmailId());
        dto.setGender(profile.getGender());
        dto.setAboutYourself(profile.getAboutYourself());
        dto.setReligion(profile.getReligion());
        dto.setHabbits(profile.getHabbits());
        dto.setSubCaste(profile.getSubCaste());
        dto.setDosham(profile.getDosham());
        dto.setMotherTongue(profile.getMotherTongue());
        dto.setMaritalStatus(profile.getMaritalStatus());
        dto.setNoOfChildren(profile.getNoOfChildren());
        dto.setHeight(profile.getHeight());
        dto.setweight(profile.getWeight());
        dto.setHobbies(profile.getHobbies());
        dto.setAscendant(profile.getAscendant());
        dto.setBasicPlanetaryPosition(profile.getBasicPlanetaryPosition());       
        dto.setFamilyStatus(profile.getFamilyStatus());
        dto.setFamilyType(profile.getFamilyType());
        dto.setHighestEducation(profile.getHighestEducation());
        dto.setCollegeName(profile.getCollegeName());
        dto.setEmployedIn(profile.getEmployedIn());
        dto.setSector(profile.getSector());
        dto.setSpiritualPath(profile.getSpiritualPath());
        dto.setOccupation(profile.getOccupation());
        dto.setCompanyName(profile.getCompanyName());
        dto.setAnnualIncome(profile.getAnnualIncome());
        dto.setWorkLocation(profile.getWorkLocation());
        dto.setState(profile.getState());
        dto.setCountry(profile.getCountry());
        dto.setCity(profile.getCity());
        dto.setBodyType(profile.getBodyType());
        dto.setComplexion(profile.getComplexion());
        dto.setExperience(profile.getExperience());
        dto.setFatherName(profile.getFatherName());
        dto.setMotherName(profile.getMotherName());
        dto.setSiblings(profile.getSiblings());
        dto.setRashi(profile.getRashi());
        dto.setNakshatra(profile.getNakshatra());
        dto.setPartnerAgeRange(profile.getPartnerAgeRange());
        dto.setPartnerEducation(profile.getPartnerEducation());
        dto.setPartnerLocationPref(profile.getPartnerLocationPref());
        dto.setPartnerWorkStatus(profile.getPartnerWorkStatus());
        dto.setPremium(profile.isPremium());
        dto.setLastActive(profile.getLastActive());
        dto.setAncestralOrigin(profile.getAncestralOrigin());
        dto.setLivingWith(profile.getLivingWith());
        dto.setChildrenDetails(profile.getChildrenDetails());
        dto.setFatherStatus(profile.getFatherStatus());
        dto.setMotherStatus(profile.getMotherStatus());
        dto.setNumberOfBrothers(profile.getNumberOfBrothers());
        dto.setNumberOfSisters(profile.getNumberOfSisters());
        dto.setPartnerReligion(profile.getPartnerReligion());
        dto.setPartnerWork(profile.getPartnerWork());
        dto.setPartnerHobbies(profile.getPartnerHobbies());
        dto.setSports(profile.getSports());
        dto.setVegiterian(profile.getVegiterian());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setIsChildrenLivingWithYou(profile.getIsChildrenLivingWithYou());
       
        if (profile.getPayments() != null && !profile.getPayments().isEmpty()) {

            List<PaymentDto> paymentDtos = profile.getPayments()
                    .stream()
                    .map(payment -> {
                        PaymentDto paymentDto = new PaymentDto();

                        paymentDto.setId(payment.getId());
                        paymentDto.setUserId(payment.getProfile().getId()); // profile id
                        paymentDto.setName(payment.getName());

                        paymentDto.setPlanCode(payment.getPlanCode());
                        paymentDto.setAmount((int) (payment.getAmount() / 100));
                        paymentDto.setCurrency(payment.getCurrency());
                        paymentDto.setStatus(payment.getStatus());

                        paymentDto.setRazorpayOrderId(payment.getRazorpayOrderId());
                        paymentDto.setRazorpayPaymentId(payment.getRazorpayPaymentId());

                        paymentDto.setPaymentMode(payment.getPaymentMode());
                        paymentDto.setTransactionId(payment.getTransactionId());

                        paymentDto.setCreatedAt(payment.getCreatedAt());

                        return paymentDto;
                    })
                    .toList();

            dto.setPayments(paymentDtos);
        }





        // ------------------------------
        // ✅ New: build image URL instead of returning Base64
        // ------------------------------
        if (profile.getUpdatePhoto() != null && !profile.getUpdatePhoto().isBlank()) {
            try {
                Path imagePath = Paths.get("uploads/profile-photos")
                        .resolve(profile.getUpdatePhoto());

                if (Files.exists(imagePath)) {
                    byte[] imageBytes = Files.readAllBytes(imagePath);
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    dto.setUpdatePhoto("data:image/jpeg;base64," + base64Image);
                } else {
                    dto.setUpdatePhoto(null);
                }
            } catch (Exception e) {
                dto.setUpdatePhoto(null);
            }
        } else {
            dto.setUpdatePhoto(null);
        }


       
        return ResponseEntity.ok(dto);
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