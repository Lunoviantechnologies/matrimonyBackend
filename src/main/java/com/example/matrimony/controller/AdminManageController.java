package com.example.matrimony.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.matrimony.dto.ChatMessageDto;
import com.example.matrimony.dto.DynamicYearlyReportResponse;
import com.example.matrimony.dto.PaymentDto;
import com.example.matrimony.dto.ProfileDto;
import com.example.matrimony.dto.RecentUserResponse;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.service.AdminReportService;
import com.example.matrimony.service.ChatService;
import com.example.matrimony.service.ProfileService;

import jakarta.persistence.criteria.Predicate;

import com.example.matrimony.repository.PaymentRecordRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminManageController {

	private static final Logger logger = LoggerFactory.getLogger(AdminManageController.class);

    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ChatService chatService;
    private final PaymentRecordRepository paymentRepository;
    private final AdminReportService adminReportService;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminManageController(ProfileRepository profileRepository,AdminReportService adminReportService, ProfileService profileService, ChatService chatService,PaymentRecordRepository paymentRepository) {
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.chatService = chatService;
        this.paymentRepository=paymentRepository;
        this.adminReportService = adminReportService;
    }


  

    @GetMapping("/profiles")
    public ResponseEntity<Page<Profile>> listProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String accountStatus,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) String search
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        Specification<Profile> spec = Specification.where(null);

        // Account Status filter
        if (accountStatus != null && !accountStatus.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("accountStatus"), accountStatus)
            );
        }

        // Active filter
        if (active != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("active"), active)
            );
        }

        // Banned filter
        if (banned != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("banned"), banned)
            );
        }

        // Search filter (ID, firstName, lastName)
        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> {

                String likePattern = "%" + search.toLowerCase() + "%";

                Predicate idMatch = null;

                // If search is number → match by id
                if (search.matches("\\d+")) {
                    idMatch = cb.equal(root.get("id"), Long.parseLong(search));
                }

                Predicate firstNameMatch = cb.like(
                        cb.lower(root.get("firstName")),
                        likePattern
                );

                Predicate lastNameMatch = cb.like(
                        cb.lower(root.get("lastName")),
                        likePattern
                );

                if (idMatch != null) {
                    return cb.or(idMatch, firstNameMatch, lastNameMatch);
                } else {
                    return cb.or(firstNameMatch, lastNameMatch);
                }
            });
        }

        Page<Profile> profilePage = profileRepository.findAll(spec, pageable);

        
        List<ProfileDto> dtoList = profilePage.getContent()
                .stream()
                .map(profile -> {

                    ProfileDto dto = profileService.mapToDto(profile);

                    Map<Integer, String> photoMap =
                            profileService.getPhotoMap(profile.getId());

                    dto.setUpdatePhoto(photoMap.get(0));
                    dto.setUpdatePhoto1(photoMap.get(1));
                    dto.setUpdatePhoto2(photoMap.get(2));
                    dto.setUpdatePhoto3(photoMap.get(3));
                    dto.setUpdatePhoto4(photoMap.get(4));

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(profilePage);
    }
    
    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {

        long totalUsers = profileRepository.count();
        long activeUsers = profileRepository.countByActiveTrue();
        long inactiveUsers = profileRepository.countByActiveFalse();
        long premiumUsers = profileRepository.countByPremiumTrue();
        long nonPremiumUsers = profileRepository.countByPremiumFalse();

        Double totalRevenue = paymentRepository.getTotalRevenue();
        Double todayRevenue = paymentRepository.getTodayRevenue();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("inactiveUsers", inactiveUsers);
        stats.put("premiumUsers", premiumUsers);
        stats.put("nonPremiumUsers", nonPremiumUsers);
        stats.put("totalRevenue", totalRevenue);
        stats.put("todayRevenue", todayRevenue);

        return ResponseEntity.ok(stats);
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
      dto.setCountry(profile.getCountry());
      dto.setState(profile.getState());
        dto.setCity(profile.getCity());
        dto.setDistrict(profile.getDistrict());
        dto.setResidenceStatus(profile.getResidenceStatus());
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
            dto.setDocumentFile("/documents/" + profile.getDocumentFile());
        } else {
            dto.setDocumentFile(null);
        }
        Map<Integer, String> photoMap =
                profileService.getPhotoMap(profile.getId());

        dto.setUpdatePhoto(photoMap.getOrDefault(0, null));
        dto.setUpdatePhoto1(photoMap.getOrDefault(1, null));
        dto.setUpdatePhoto2(photoMap.getOrDefault(2, null));
        dto.setUpdatePhoto3(photoMap.getOrDefault(3, null));
        dto.setUpdatePhoto4(photoMap.getOrDefault(4, null));
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
        if (updatedDto.getCity() != null) {
			existing.setCity(updatedDto.getCity());
		}
       if (updatedDto.getState() != null) {
			existing.setState(updatedDto.getState());
		}
        if (updatedDto.getCountry() != null) {
			existing.setCountry(updatedDto.getCountry());
		}
        if(updatedDto.getDistrict()!=null) {
        	existing.setDistrict(updatedDto.getDistrict());
        }

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
        if(updatedDto.getResidenceStatus()!=null) {
        	existing.setResidenceStatus(updatedDto.getResidenceStatus());
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

            String fileName = Paths
                    .get(updatedDto.getDocumentFile())
                    .getFileName()
                    .toString();

            existing.setDocumentFile(fileName);
        }
        existing.setLastActive(LocalDateTime.now());

        // Save
        Profile saved = profileRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

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
    public ResponseEntity<?> updatePhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        return profileRepository.findById(id).map(profile -> {

            try {
                // ================= FILE VALIDATION =================

                if (file == null || file.isEmpty()) {
                    return ResponseEntity.badRequest().body("File is required");
                }

                //  1MB limit
                long maxSize = 10 * 1024 * 1024; // 10MB 
                if (file.getSize() > maxSize) {
                    return ResponseEntity
                            .status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File too large! Max allowed is 10MB");
                }

                //  Allow only images
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity
                            .badRequest()
                            .body("Only image files are allowed");
                }

                // ================= FILE SAVE =================

                Path uploadPath = Paths.get("uploads/profile-photos");
                Files.createDirectories(uploadPath);

                // Get extension dynamically
                String originalName = file.getOriginalFilename();
                String extension = ".jpg"; // default

                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }

                // Custom filename
                String fileName = "profile_" + id + "_" + System.currentTimeMillis() + extension;

                Path filePath = uploadPath.resolve(fileName);

                // Save file
                Files.write(filePath, file.getBytes());

                // Save in DB
               
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
   
    @GetMapping("/recent-users")
    public ResponseEntity<RecentUserResponse> getRecentUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                profileService.getRecentUsers(page, size)
        );
    }
    
    @GetMapping("/yearly-dashboard")
    public ResponseEntity<DynamicYearlyReportResponse> getYearlyDashboard(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                adminReportService.getYearlyDashboard(year)
        );
    }

}
