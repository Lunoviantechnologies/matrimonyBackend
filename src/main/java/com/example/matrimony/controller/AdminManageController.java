package com.example.matrimony.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.dto.ProfileDto;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.ChatService;
import com.example.matrimony.service.ProfileService;

@RestController
@RequestMapping("/api/admin")
public class AdminManageController {

	private static final Logger logger = LoggerFactory.getLogger(AdminManageController.class);

    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ChatService chatService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminManageController(ProfileRepository profileRepository, ProfileService profileService, ChatService chatService) {
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.chatService = chatService;
    }


    @GetMapping("/profiles")
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



    @GetMapping("/profiles/{id}")
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
        dto.setOccupation(profile.getOccupation());
        dto.setCompanyName(profile.getCompanyName());
        dto.setAnnualIncome(profile.getAnnualIncome());
        dto.setWorkLocation(profile.getWorkLocation());
      
        dto.setCityName(profile.getCity());
        dto.setBodyType(profile.getBodyType());
        dto.setComplexion(profile.getComplexion());
        dto.setExperience(profile.getExperience());
        dto.setFatherName(profile.getFatherName());
        dto.setMotherName(profile.getMotherName());
        dto.setSiblings(profile.getSiblings());
        dto.setRashi(profile.getRashi());
        dto.setSpiritualPath(profile.getSpiritualPath());
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
        dto.setGothram(profile.getGothram());
        dto.setSports(profile.getSports());
        dto.setHabbits(profile.getHabbits());
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
                        paymentDto.setAmount(payment.getAmount());

                        paymentDto.setCurrency(payment.getCurrency());
                        paymentDto.setStatus(payment.getStatus());

                        paymentDto.setRazorpayOrderId(payment.getRazorpayOrderId());
                        paymentDto.setRazorpayPaymentId(payment.getRazorpayPaymentId());

                        paymentDto.setPaymentMode(payment.getPaymentMode());
                        paymentDto.setTransactionId(payment.getTransactionId());
                        paymentDto.setPremiumStart(payment.getPremiumStart());
                        paymentDto.setPremiumEnd(payment.getPremiumEnd());
                        paymentDto.setPlanName(payment.getPlanName());
                        paymentDto.setExpiryMessage(payment.getExpiryMessage());
                        paymentDto.setCreatedAt(payment.getCreatedAt());

                        return paymentDto;
                    })
                    .toList();

            dto.setPayments(paymentDtos);
        }

        // ================= DOCUMENT FILE (same dir as registration/view-document) =================
        if (profile.getDocumentFile() != null && !profile.getDocumentFile().isBlank()) {
            try {
                Path docPath = Paths.get(uploadDir)
                        .resolve(profile.getDocumentFile());

                if (Files.exists(docPath)) {
                    byte[] docBytes = Files.readAllBytes(docPath);
                    String base64Doc = Base64.getEncoder().encodeToString(docBytes);

                    String mimeType = Files.probeContentType(docPath);
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }

                    dto.setDocumentFile("data:" + mimeType + ";base64," + base64Doc);
                } else {
                    dto.setDocumentFile(null);
                }
            } catch (Exception e) {
                dto.setDocumentFile(null);
            }
        } else {
            dto.setDocumentFile(null);
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

   // @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
  //  @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        if (!profileRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Profile not found", "id", id));
        }

        try {
            // This calls the service method above which deletes dependents first
            profileService.deleteById(id);

           // return ResponseEntity.noContent().build();
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Profile deleted successfully",
                            "id", id
                    )
            );

        } catch (DataIntegrityViolationException ex) {
            logger.warn("Failed to delete profile id={} due to integrity violation: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "cannot delete profile — referenced by other records",
                            "details", ex.getRootCause() == null ? ex.getMessage() : ex.getRootCause().getMessage()
                    ));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting profile id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete profile", "message", ex.getMessage()));
        }
    }

    // existing profile update endpoint (kept, assuming path /profiles/{id}/update)
 // ============================
    // USER UPDATE PROFILE (partial)
    // ============================
  //======
//    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfilePartially(
            @PathVariable Long id,
            @RequestBody ProfileDto updatedDto
    ) {
        // Fetch existing profile
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profile not found"));


        // --- BASIC DETAILS ---
        if (updatedDto.getProfileFor() != null) {
			existing.setProfileFor(updatedDto.getProfileFor());
		}
        if (updatedDto.getFirstName() != null) {
			existing.setFirstName(updatedDto.getFirstName());
		}
        if (updatedDto.getLastName() != null) {
			existing.setLastName(updatedDto.getLastName());
		}
        if (updatedDto.getMobileNumber() != null) {
			existing.setMobileNumber(updatedDto.getMobileNumber());
		}
        if (updatedDto.getDateOfBirth() != null) {
			existing.setDateOfBirth(updatedDto.getDateOfBirth());
		}
        if (updatedDto.getGender() != null) {
			existing.setGender(updatedDto.getGender());
		}
        if (updatedDto.getAboutYourself() != null) {
			existing.setAboutYourself(updatedDto.getAboutYourself());
		}
        if (updatedDto.getAge() != null) {
			existing.setAge(updatedDto.getAge());
		}
        if (updatedDto.getRole() != null) {
			existing.setRole(updatedDto.getRole());
		}
        if (updatedDto.getVegiterian() != null) {
			existing.setVegiterian(updatedDto.getVegiterian());
		}
        if (updatedDto.getCreatePassword() != null) {
			existing.setCreatePassword(updatedDto.getCreatePassword());
		}
        if (updatedDto.getSports() != null) {
			existing.setSports(updatedDto.getSports());
		}

        // --- EMAIL ---
        if (updatedDto.getEmailId() != null && !updatedDto.getEmailId().equals(existing.getEmailId())) {
            if (profileRepository.existsByEmailId(updatedDto.getEmailId())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            existing.setEmailId(updatedDto.getEmailId());
        }
     //   if (updatedDto.getEmailVerified() != null) existing.setEmailVerified(updatedDto.getEmailVerified());

        // --- IDENTITY / PHYSICAL ---
        if (updatedDto.getHeight() != null) {
			existing.setHeight(updatedDto.getHeight());
		}
        if (updatedDto.getweight() != null) {
			existing.setWeight(updatedDto.getweight());
		}
        if (updatedDto.getBodyType() != null) {
			existing.setBodyType(updatedDto.getBodyType());
		}
        if (updatedDto.getComplexion() != null) {
			existing.setComplexion(updatedDto.getComplexion());
		}
        if (updatedDto.getExperience() != null) {
			existing.setExperience(updatedDto.getExperience());
		}

        // --- RELIGION & PERSONAL ---
        if (updatedDto.getReligion() != null) {
			existing.setReligion(updatedDto.getReligion());
		}

        if (updatedDto.getSubCaste() != null) {
			existing.setSubCaste(updatedDto.getSubCaste());
		}
        if (updatedDto.getDosham() != null) {
			existing.setDosham(updatedDto.getDosham());
		}
        if (updatedDto.getMotherTongue() != null) {
			existing.setMotherTongue(updatedDto.getMotherTongue());
		}
        if (updatedDto.getManglik() != null) {
			existing.setManglik(updatedDto.getManglik());
		}

        // --- FAMILY ---
        if (updatedDto.getFatherName() != null) {
			existing.setFatherName(updatedDto.getFatherName());
		}
        if (updatedDto.getMotherName() != null) {
			existing.setMotherName(updatedDto.getMotherName());
		}
        if (updatedDto.getSiblings() != null) {
			existing.setSiblings(updatedDto.getSiblings());
		}
        if (updatedDto.getFamilyStatus() != null) {
			existing.setFamilyStatus(updatedDto.getFamilyStatus());
		}
        if (updatedDto.getFamilyType() != null) {
			existing.setFamilyType(updatedDto.getFamilyType());
		}
        if (updatedDto.getFatherStatus() != null) {
			existing.setFatherStatus(updatedDto.getFatherStatus());
		}
        if (updatedDto.getMotherStatus() != null) {
			existing.setMotherStatus(updatedDto.getMotherStatus());
		}
        if (updatedDto.getNumberOfBrothers() != null) {
			existing.setNumberOfBrothers(updatedDto.getNumberOfBrothers());
		}
        if (updatedDto.getNumberOfSisters() != null) {
			existing.setNumberOfSisters(updatedDto.getNumberOfSisters());
		}
        if (updatedDto.getAncestralOrigin() != null) {
			existing.setAncestralOrigin(updatedDto.getAncestralOrigin());
		}
        if (updatedDto.getLivingWith() != null) {
			existing.setLivingWith(updatedDto.getLivingWith());
		}
        if (updatedDto.getChildrenDetails() != null) {
			existing.setChildrenDetails(updatedDto.getChildrenDetails());
		}
        if (updatedDto.getNoOfChildren() != null) {
			existing.setNoOfChildren(updatedDto.getNoOfChildren());
		}
        if (updatedDto.getIsChildrenLivingWithYou() != null) {
			existing.setIsChildrenLivingWithYou(updatedDto.getIsChildrenLivingWithYou());
		}
        if (updatedDto.getSpiritualPath() !=null) {
			existing.setSpiritualPath(updatedDto.getSpiritualPath());
		}
        if (updatedDto.getHabbits() !=null) {
			existing.setHabbits(updatedDto.getHabbits());
		}

        // --- EDUCATION & WORK ---
        if (updatedDto.getHighestEducation() != null) {
			existing.setHighestEducation(updatedDto.getHighestEducation());
		}
        if (updatedDto.getCollegeName() != null) {
			existing.setCollegeName(updatedDto.getCollegeName());
		}
        if (updatedDto.getEmployedIn() != null) {
			existing.setEmployedIn(updatedDto.getEmployedIn());
		}
        if (updatedDto.getSector() != null) {
			existing.setSector(updatedDto.getSector());
		}
        if (updatedDto.getOccupation() != null) {
			existing.setOccupation(updatedDto.getOccupation());
		}
        if (updatedDto.getCompanyName() != null) {
			existing.setCompanyName(updatedDto.getCompanyName());
		}
        if (updatedDto.getAnnualIncome() != null) {
			existing.setAnnualIncome(updatedDto.getAnnualIncome());
		}
        if (updatedDto.getWorkLocation() != null) {
			existing.setWorkLocation(updatedDto.getWorkLocation());
		}

        // --- LOCATION ---
        if (updatedDto.getCityName() != null) {
			existing.setCity(updatedDto.getCityName());
		}
//        if (updatedDto.getState() != null) {
//			existing.setState(updatedDto.getState());
//		}
//        if (updatedDto.getCountry() != null) {
//			existing.setCountry(updatedDto.getCountry());
//		}

        // --- HOBBIES ---
        if (updatedDto.getHobbies() != null) {
			existing.setHobbies(updatedDto.getHobbies());
		}

        // --- HOROSCOPE ---
        if (updatedDto.getRashi() != null) {
			existing.setRashi(updatedDto.getRashi());
		}
        if (updatedDto.getNakshatra() != null) {
			existing.setNakshatra(updatedDto.getNakshatra());
		}
        if (updatedDto.getAscendant() != null) {
			existing.setAscendant(updatedDto.getAscendant());
		}
        if (updatedDto.getBasicPlanetaryPosition() != null) {
			existing.setBasicPlanetaryPosition(updatedDto.getBasicPlanetaryPosition());
		}

        // --- PARTNER PREFERENCES ---
        if (updatedDto.getPartnerAgeRange() != null) {
			existing.setPartnerAgeRange(updatedDto.getPartnerAgeRange());
		}
        if (updatedDto.getPartnerEducation() != null) {
			existing.setPartnerEducation(updatedDto.getPartnerEducation());
		}
        if (updatedDto.getPartnerLocationPref() != null) {
			existing.setPartnerLocationPref(updatedDto.getPartnerLocationPref());
		}
        if (updatedDto.getPartnerWorkStatus() != null) {
			existing.setPartnerWorkStatus(updatedDto.getPartnerWorkStatus());
		}
        if (updatedDto.getPartnerReligion() != null) {
			existing.setPartnerReligion(updatedDto.getPartnerReligion());
		}
        if (updatedDto.getPartnerWork() != null) {
			existing.setPartnerWork(updatedDto.getPartnerWork());
		}
        if (updatedDto.getPartnerHobbies() != null) {
			existing.setPartnerHobbies(updatedDto.getPartnerHobbies());
		}
        if (updatedDto.getGothram() != null) {
			existing.setGothram(updatedDto.getGothram());
		}
        // --- MEMBERSHIP & SYSTEM ---
        if (updatedDto.getMembershipType() != null) {
			existing.setMembershipType(updatedDto.getMembershipType());
		}
        if (updatedDto.getAccountStatus() != null) {
			existing.setAccountStatus(updatedDto.getAccountStatus());
		}
        if (updatedDto.getActive() != null) {
			existing.setActive(updatedDto.getActive());
		}
        if (updatedDto.getPremium() != null) {
			existing.setPremium(updatedDto.getPremium());
		}


        // --- FILES / PHOTOS ---
       // if (updatedDto.getUpdatePhoto() != null) existing.setUpdatePhoto(updatedDto.getUpdatePhoto());
        if (updatedDto.getDocumentFile() != null) {
			existing.setDocumentFile(updatedDto.getDocumentFile());
      // if (updatedDto.getDocumentFilePresent() != null) existing.setDocumentFilePresent(updatedDto.getDocumentFilePresent());
		}


        // --- SYSTEM TIMESTAMP ---
        existing.setLastActive(LocalDateTime.now());

        // Save
        Profile saved = profileRepository.save(existing);
        return ResponseEntity.ok(saved);
    }


    //Update Account Status (Verify / Suspend / Activate)


    //Update Membership Type (Free / Premium / Gold)
  //======

    //Update Account Status (Verify / Suspend / Activate)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/profiles/{id}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam boolean status) {

        Profile profile = profileRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profile not found with id: " + id
                ));

        profile.setActive(status);
        profileRepository.save(profile);

        return ResponseEntity.ok(Map.of("message", "Status updated", "status", status));
    }

    //Update Membership Type (Free / Premium / Gold)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/profiles/{id}/membership")
    public ResponseEntity<?> updateMembershipType(
            @PathVariable Long id,
            @RequestParam boolean type) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPremium(type);
        profileRepository.save(profile);

        return ResponseEntity.ok(Map.of("message", "Membership updated", "type", type));
    }

    //. Update Last Active Manually (if needed)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/profiles/{id}/last-active")
    public ResponseEntity<?> updateLastActive(@PathVariable Long id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setLastActive(LocalDateTime.now());
        profileRepository.save(profile);

        return ResponseEntity.ok(Map.of("message", "Last active updated"));
    }



//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/chat-messages")
    public ResponseEntity<List<com.example.matrimony.entity.ChatMessage>> getChatMessages() {
        return ResponseEntity.ok(chatService.recentMessages());
    }

    @PutMapping("/photo/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        return profileRepository.findById(id).map(profile -> {

            try {
                // Create folder if not exists
                Path uploadPath = Paths.get("uploads/profile-photos");
                Files.createDirectories(uploadPath);

                // Create unique filename
                String fileName = "profile_" + id + "_" + System.currentTimeMillis() + ".jpg";
                Path filePath = uploadPath.resolve(fileName);

                // Save file to folder
                Files.write(filePath, file.getBytes());

                // Store only filename in DB
                profile.setUpdatePhoto(fileName);
                profile.setLastActive(LocalDateTime.now());
                profileRepository.save(profile);

                return ResponseEntity.ok(Map.of(
                	    "message", "Photo uploaded successfully",
                	    "fileName", fileName,
                	    "updatePhoto", "/profile-photos/" + fileName
                	));

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Failed to store photo");
            }

        }).orElse(ResponseEntity.badRequest().body("Profile not found"));
    }
    
   
    
    
    @PostMapping("/profiles/approve/{id}")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Long id) {
        try {
            profileService.approveProfile(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile approved successfully");
            response.put("profileId", id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }


    @PostMapping("/profiles/reject/{id}")
    public ResponseEntity<?> rejectProfile(
            @PathVariable("id") Long profileId,
            @RequestParam(required = false) String reason) {

        profileService.rejectProfile(profileId, reason);

        return ResponseEntity.ok("Profile rejected and deleted successfully");
    }
    
    
    @GetMapping("/conversation/{senderId}/{receiverId}")
    public Page<ChatMessageDto> getConversation(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size
    ) {
        return chatService.getConversation(senderId, receiverId, page, size);
    }
   

}
