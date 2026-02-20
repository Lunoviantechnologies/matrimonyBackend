package com.example.matrimony.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.dto.PrivacySettingsDto;
import com.example.matrimony.dto.ProfileDto;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.referral.ReferralService;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.Notificationadminservice;
import com.example.matrimony.service.ProfilePhotoService;
import com.example.matrimony.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;



@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfilePhotoService profilePhotoService;

    
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Notificationadminservice notificationservice;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ReferralService referralService;


    // ✅ Constructor Injection (prevents NullPointerException)
    public ProfileController(ProfileService profileService,
                             ProfilePhotoService profilePhotoService,
                             ReferralService referralService) {

        this.profileService = profileService;
        this.profilePhotoService = profilePhotoService;
        this.referralService = referralService;
    }

    
     
    @PostMapping("/register")
    public ResponseEntity<?> createProfile(
            @RequestParam("profile") String profileJson,
            @RequestParam("document") MultipartFile document) {

        try {
            // Convert JSON to Profile
            Profile profile = objectMapper.readValue(profileJson, Profile.class);

            if (profile.getEmailId() == null || profile.getEmailId().isBlank()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            // Encrypt password
            profile.setCreatePassword(
                    passwordEncoder.encode(profile.getCreatePassword())
            );

            // ================= FILE VALIDATION =================
            if (document != null && !document.isEmpty()) {

                //  1MB validation
                long maxSize = 1 * 1024 * 1024; // 1MB

                if (document.getSize() > maxSize) {
                    return ResponseEntity
                            .status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File too large! Max allowed is 1MB");
                }

                //  File type validation
                String contentType = document.getContentType();
                if (contentType == null ||
                        (!contentType.equals("application/pdf") &&
                         !contentType.startsWith("image/"))) {
                    return ResponseEntity
                            .badRequest()
                            .body("Only PDF or Image files are allowed");
                }

                // Create directory if not exists
                Files.createDirectories(Paths.get(uploadDir));

                //  Get extension only
                String originalName = document.getOriginalFilename();
                String extension = "";

                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }

                //  Custom filename (ignore user filename)
                String fileName = "document_" + System.currentTimeMillis() + extension;

                Path filePath = Paths.get(uploadDir)
                        .resolve(fileName)
                        .normalize();

                // Save file
                Files.copy(document.getInputStream(), filePath);

                // Save filename in DB
                profile.setDocumentFile(fileName);
            }

            // ================= SAVE PROFILE =================
            Profile saved = profileRepository.save(profile);

            // ================= REFERRAL =================
            try {
                String usedCode = profile.getSignupReferralCode();
                if (usedCode != null && !usedCode.isBlank()) {
                    referralService.applyReferralCode(saved, usedCode.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ================= NOTIFICATION =================
            String adminMessage =
                    "New user registered: " +
                    profile.getFirstName() + " " + profile.getLastName() +
                    " (" + profile.getEmailId() + ")";

            notificationservice.notifyAdmin(
                    "USER_REGISTERED",
                    adminMessage,
                    Map.of(
                            "userId", saved.getId(),
                            "name", profile.getFirstName() + " " + profile.getLastName(),
                            "email", profile.getEmailId(),
                            "mobile", profile.getMobileNumber()
                    )
            );

            return ResponseEntity.ok(saved);

        } catch (MaxUploadSizeExceededException ex) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File exceeds maximum allowed size (50MB)");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body("Registration failed: " + ex.getMessage());
        }
    }

 // api for the registration
//    @PostMapping("/register")
//    public ResponseEntity<?> createProfile(
//            @RequestParam("profile") String profileJson,
//            @RequestParam("document") MultipartFile document) {
//
//        try {
//            //  Convert JSON string to Profile object
//            Profile profile = objectMapper.readValue(profileJson, Profile.class);
//
//            if (profile.getEmailId() == null || profile.getEmailId().isBlank()) {
//                return ResponseEntity.badRequest().body("Email is required");
//            }
//
//            //  Encrypt password
//            profile.setCreatePassword(
//                    passwordEncoder.encode(profile.getCreatePassword())
//            );
//
//            //  Save document file (USING SAME PATH AS VIEW API)
//            if (document != null && !document.isEmpty()) {
//
//                // Create directory if not exists
//                Files.createDirectories(Paths.get(uploadDir));
//
//                // Unique filename
//                String fileName = System.currentTimeMillis()
//                        + "_" + document.getOriginalFilename();
//
//                // Resolve absolute path
//                Path filePath = Paths.get(uploadDir)
//                        .resolve(fileName)
//                        .normalize();
//
//                // Save file
//                Files.copy(document.getInputStream(), filePath);
//
//                // Save filename in DB
//                profile.setDocumentFile(fileName);
//            }
//
//            //  Save profile
//            Profile saved = profileRepository.save(profile);
//
//            // If user provided a referral code at registration, apply it
//            try {
//                String usedCode = profile.getSignupReferralCode();
//                if (usedCode != null && !usedCode.isBlank()) {
//                    referralService.applyReferralCode(saved, usedCode.trim());
//                }
//            } catch (Exception e) {
//                // Do not block registration if referral fails; just log.
//                e.printStackTrace();
//            }
//
//            // ================== 🔔 ADMIN NOTIFICATION ==================
//            String adminMessage =
//                    "New user registered: " +
//                    profile.getFirstName() + " " + profile.getLastName() +
//                    " (" + profile.getEmailId() + ")";
//
//            notificationservice.notifyAdmin(
//                    "USER_REGISTERED",
//                    adminMessage,
//                    Map.of(
//                            "userId", profile.getId(),
//                            "name", profile.getFirstName() + " " + profile.getLastName(),
//                            "email", profile.getEmailId(),
//                            "mobile", profile.getMobileNumber()
//                    )
//            );
//
//
//            return ResponseEntity.ok(saved);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.badRequest()
//                    .body("Registration failed: " + ex.getMessage());
//        }
//    }


    
    
    @GetMapping("/myprofiles/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
        Optional<Profile> p = profileRepository.findById(id);
        if (p.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

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

        dto.setProfileVisibility(profile.getProfileVisibility());
        dto.setHideProfilePhoto(profile.getHideProfilePhoto());
       
       
//       dto.setCountry(profile.getCountry());
      
       dto.setState(profile.getState());
        dto.setCountry(profile.getCountry());
       dto.setDistrict(profile.getDistrict());
       dto.setResidenceStatus(profile.getResidenceStatus());

        
        dto.setCity(profile.getCity());
        dto.setBodyType(profile.getBodyType());
        dto.setComplexion(profile.getComplexion());
        dto.setExperience(profile.getExperience());
        dto.setFatherName(profile.getFatherName());
        dto.setMotherName(profile.getMotherName());
        dto.setSiblings(profile.getSiblings());
        dto.setRashi(profile.getRashi());
        dto.setNakshatra(profile.getNakshatra());
        dto.setGothram(profile.getGothram());
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
        dto.setHabbits(profile.getHabbits());
        dto.setVegiterian(profile.getVegiterian());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setIsChildrenLivingWithYou(profile.getIsChildrenLivingWithYou());
        dto.setUpdatePhoto1(profilePhotoService.getPhotoBase64(id, 1));
        dto.setUpdatePhoto2(profilePhotoService.getPhotoBase64(id, 2));
        dto.setUpdatePhoto3(profilePhotoService.getPhotoBase64(id, 3));
        dto.setUpdatePhoto4(profilePhotoService.getPhotoBase64(id, 4));


        if (profile.getPayments() != null && !profile.getPayments().isEmpty()) {

            List<PaymentDto> paymentDtos = profile.getPayments()
                    .stream()
                    .map(payment -> {
                        PaymentDto paymentDto = new PaymentDto();

                        paymentDto.setId(payment.getId());
                        paymentDto.setUserId(payment.getProfile().getId()); // profile id
                        paymentDto.setName(payment.getName());

                        paymentDto.setPlanCode(payment.getPlanCode());
                        paymentDto.setAmount(payment.getAmount());

                        paymentDto.setCurrency(payment.getCurrency());
                        paymentDto.setStatus(payment.getStatus());

                        paymentDto.setRazorpayOrderId(payment.getRazorpayOrderId());
                        paymentDto.setRazorpayPaymentId(payment.getRazorpayPaymentId());

                        paymentDto.setPaymentMode(payment.getPaymentMode());
                        paymentDto.setTransactionId(payment.getTransactionId());

                        
                        paymentDto.setPlanName(payment.getPlanName());
                        paymentDto.setPremiumStart(payment.getPremiumStart());
                        paymentDto.setPremiumEnd(payment.getPremiumEnd());
                        paymentDto.setExpiryMessage(payment.getExpiryMessage());

                        paymentDto.setCreatedAt(payment.getCreatedAt());

                        return paymentDto;
                    })
                    .toList();

            dto.setPayments(paymentDtos);
        }

        // ------------------------------
        //  New: build image URL instead of returning Base64
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
    public ResponseEntity<Page<Profile>> listProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Profile> profilePage = profileService.listAll(pageable);

        profilePage.getContent().forEach(profile -> {

            // ---------- MAIN updatePhoto ----------
            if (profile.getUpdatePhoto() != null && !profile.getUpdatePhoto().isBlank()) {
                try {
                    String fileName = Paths.get(profile.getUpdatePhoto()).getFileName().toString();
                    Path imagePath = Paths.get("uploads/profile-photos").resolve(fileName);

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

            // ---------- Extra Photos ----------
            profile.setUpdatePhoto1(null);
            profile.setUpdatePhoto2(null);
            profile.setUpdatePhoto3(null);
            profile.setUpdatePhoto4(null);

            if (profile.getProfilePictures() != null) {
                profile.getProfilePictures().forEach(pic -> {
                    try {
                        if (pic.getFileName() != null && !pic.getFileName().isBlank()) {

                            String fileName = Paths.get(pic.getFileName()).getFileName().toString();
                            Path imagePath = Paths.get("uploads/profile-photos").resolve(fileName);

                            if (Files.exists(imagePath)) {
                                byte[] imageBytes = Files.readAllBytes(imagePath);
                                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                                String base64 = "data:image/jpeg;base64," + base64Image;

                                if (pic.getPhotoNumber() == 1) profile.setUpdatePhoto1(base64);
                                if (pic.getPhotoNumber() == 2) profile.setUpdatePhoto2(base64);
                                if (pic.getPhotoNumber() == 3) profile.setUpdatePhoto3(base64);
                                if (pic.getPhotoNumber() == 4) profile.setUpdatePhoto4(base64);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        return ResponseEntity.ok(profilePage);
    }
    
//    @GetMapping("/Allprofiles")
//    public ResponseEntity<List<Profile>> listProfiles() {
//
//        List<Profile> profiles = profileService.listAll();
//
//        profiles.forEach(profile -> {
//
//            // ---------- MAIN updatePhoto ----------update 
//            if (profile.getUpdatePhoto() != null && !profile.getUpdatePhoto().isBlank()) {
//                try {
//                    String fileName = Paths.get(profile.getUpdatePhoto()).getFileName().toString();
//                    Path imagePath = Paths.get("uploads/profile-photos").resolve(fileName);
//
//                    if (Files.exists(imagePath)) {
//                        byte[] imageBytes = Files.readAllBytes(imagePath);
//                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//                        profile.setUpdatePhoto("data:image/jpeg;base64," + base64Image);
//                    } else {
//                        profile.setUpdatePhoto(null);
//                    }
//                } catch (Exception e) {
//                    profile.setUpdatePhoto(null);
//                }
//            } else {
//                profile.setUpdatePhoto(null);
//            }
//
//            // ---------- updatePhoto1 2 3 4 ----------
//            profile.setUpdatePhoto1(null);
//            profile.setUpdatePhoto2(null);
//            profile.setUpdatePhoto3(null);
//            profile.setUpdatePhoto4(null);
//
//            if (profile.getProfilePictures() != null) {
//                profile.getProfilePictures().forEach(pic -> {
//
//                    if (pic.getFileName() != null && !pic.getFileName().isBlank()) {
//                        try {
//                            String fileName = Paths.get(pic.getFileName()).getFileName().toString();
//                            Path imagePath = Paths.get("uploads/profile-photos").resolve(fileName);
//
//                            if (Files.exists(imagePath)) {
//                                byte[] imageBytes = Files.readAllBytes(imagePath);
//                                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//                                String base64 = "data:image/jpeg;base64," + base64Image;
//
//                                if (pic.getPhotoNumber() == 1) profile.setUpdatePhoto1(base64);
//                                if (pic.getPhotoNumber() == 2) profile.setUpdatePhoto2(base64);
//                                if (pic.getPhotoNumber() == 3) profile.setUpdatePhoto3(base64);
//                                if (pic.getPhotoNumber() == 4) profile.setUpdatePhoto4(base64);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//        });
//
//        return ResponseEntity.ok(profiles);
//    }


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
    
    @GetMapping("/view-document/{fileName}")
    public ResponseEntity<Resource> viewDocument(@PathVariable String fileName) {
        try {
            if (fileName.contains("..")) {
                return ResponseEntity.badRequest().build();
            }

            Path filePath = Paths.get(uploadDir)
                    .resolve(fileName)
                    .normalize();

            // 🔍 DEBUG (KEEP UNTIL VERIFIED)
            System.out.println("Looking for file at: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\""
                    )
                    .body(resource);
 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    



}