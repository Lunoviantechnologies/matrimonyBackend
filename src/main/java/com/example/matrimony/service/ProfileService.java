package com.example.matrimony.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import java.awt.print.Pageable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

//import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.MatchSearchRequest;
import com.example.matrimony.dto.MatchSearchResponse;
import com.example.matrimony.dto.PrivacySettingsDto;
import com.example.matrimony.dto.ProfileDto;
import com.example.matrimony.dto.RegisterRequest;
import com.example.matrimony.entity.PaymentRecord;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.exception.EmailAlreadyExistsException;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.PaymentRecordRepository;
import com.example.matrimony.repository.ProfilePhotoRepository;
import com.example.matrimony.repository.ProfileRepository;
import com.example.matrimony.dto.ProfileFilterRequest;
import com.example.matrimony.dto.ProfileMatchResponse;
import com.example.matrimony.dto.RecentUserResponse;
import com.example.matrimony.specification.ProfileSpecification;

import io.jsonwebtoken.lang.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
@Service
public class ProfileService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
	@Autowired
	private ProfilePhotoService profilePhotoService;
	private final PaymentRecordRepository paymentRepo;
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private FriendRequestRepository friendRequestRepository;
    private ChatMessageRepository chatMessageRepository;
    private final Notificationadminservice notificationService;
    private PaymentRecordRepository paymentRecordRepository;
    private ProfilePhotoRepository profilePhotoRepository;
    
    

    
 // ===================== GET TOTAL REGISTERED PROFILES =====================
    public long getRegisteredProfilesCount() {
        return profileRepository.count();  // count() from JpaRepository returns long
    }

   
    public ProfileService(ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            Notificationadminservice notificationService,
            FriendRequestRepository friendRequestRepository,
            ChatMessageRepository chatMessageRepository,
            PaymentRecordRepository paymentRecordRepository,
            PaymentRecordRepository paymentRepo,
            ProfilePhotoRepository profilePhotoRepository) {
    	    this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.friendRequestRepository = friendRequestRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.profilePhotoRepository = profilePhotoRepository;
        this.paymentRepo=paymentRepo;
   }



    // REGISTER NEW PROFILE  
    @Transactional
    public Profile register(RegisterRequest req) {

        if (req == null) throw new IllegalArgumentException("Request cannot be null");

        if (req.getEmail() == null || req.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty");

       
     // Check if email already exists
        Optional<Profile> existingEmail = profileRepository.findByEmailId(req.getEmail());

        if (existingEmail.isPresent()) {
            Profile existing = existingEmail.get();

            // 🔒 Banned user cannot register again
            if (existing.isBanned()) {
                throw new RuntimeException("You are permanently banned and cannot register again");
            }

            // Normal duplicate user
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (req.getPassword() == null || req.getPassword().isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        Profile profile = new Profile();

        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());
        profile.setMobileNumber(req.getMobileNumber());
        profile.setEmailId(req.getEmail());
        profile.setCreatePassword(passwordEncoder.encode(req.getPassword()));
        profile.setAge(req.getAge() != null ? req.getAge() : 0);

        if (req.getDateOfBirth() != null) {
            try {
                profile.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid date format (YYYY-MM-DD expected)");
            }
        }

        profile.setGender(req.getGender());
        profile.setReligion(req.getReligion());
   
        profile.setSubCaste(req.getSubCaste());
        profile.setDosham(req.getDosham());
        profile.setMaritalStatus(req.getMaritalStatus());

        // ✅ SAVE PROFILE FIRST
        Profile savedProfile = profileRepository.save(profile);
//     // ✅ SEND EMAIL
//        try {
//            emailService.sendRegistrationSuccessEmail(
//                savedProfile.getEmailId(),
//                savedProfile.getFirstName()
//            );
//        } catch (Exception e) {
//            logger.error("Registration email failed for {}", savedProfile.getEmailId(), e);
//        }

        
        String title = "New User Registered";
        String message = "New profile registered: "
                + savedProfile.getFirstName() + " "
                + savedProfile.getLastName()
                + " (" + savedProfile.getEmailId() + ")";

        notificationService.createAdminNotification(
                title,
                message,
                Instant.now()
        );

        logger.info("Notification sent for new registration: {}", savedProfile.getEmailId());

        return savedProfile;
    }

    
    private ProfileMatchResponse mapToMatchResponse(Profile p) {

        String photo = null;

        try {
            photo = profilePhotoService.getPhotoBase64(p.getId(), 1);
        } catch (Exception e) {
            photo = null;
        }

        return new ProfileMatchResponse(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getHeight(),
                p.getCity(),
                p.getOccupation(),
                p.getHighestEducation(),
                p.getReligion(),
                p.getSubCaste(),
                p.isPremium(),
                p.getUpdatePhoto(),
                p.getUpdatePhoto1(),
                p.getUpdatePhoto2(),
                p.getUpdatePhoto3(),
                p.getUpdatePhoto4(),
                p.getHideProfilePhoto(),
                p.getGender()
        );
    }
    
    
    private Sort buildSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.by("premium").descending()
                       .and(Sort.by("lastActive").descending());
        }

        switch (sort.toUpperCase()) {

            case "NEW":
            case "NEWEST":
                return Sort.by("createdAt").descending();

            case "ACTIVE":
                return Sort.by("lastActive").descending();

            case "PREMIUM":
                return Sort.by("premium").descending()
                        .and(Sort.by("lastActive").descending());

            case "FREE":
                return Sort.by("premium").ascending()
                        .and(Sort.by("lastActive").descending());

            case "RELEVANCE":
            default:
                return Sort.by("premium").descending()
                        .and(Sort.by("lastActive").descending());
        }
    }
    
    
    
    
//    public Profile register(RegisterRequest req) {
//
//        if (req == null) throw new IllegalArgumentException("Request cannot be null");
//
//        if (req.getEmail() == null || req.getEmail().isBlank())
//            throw new IllegalArgumentException("Email cannot be empty");
//
//        if (profileRepository.existsByEmailId(req.getEmail()))
//            throw new IllegalArgumentException("Email already registered");
//
//        if (profileRepository.existsByEmailId(req.getEmail())) {
//            throw new EmailAlreadyExistsException("Email already registered");
//        }
//        
//        if (req.getPassword() == null || req.getPassword().isBlank())
//            throw new IllegalArgumentException("Password cannot be empty");
//
//        Profile profile = new Profile();
//
//        profile.setFirstName(req.getFirstName());
//        profile.setLastName(req.getLastName());
//        profile.setMobileNumber(req.getMobileNumber());
//        profile.setEmailId(req.getEmail());
//        profile.setCreatePassword(passwordEncoder.encode(req.getPassword()));
//        profile.setAge(req.getAge() != null ? req.getAge() : 0);
//
//        if (req.getDateOfBirth() != null) {
//            try {
//                profile.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
//            } catch (Exception ex) {
//                throw new IllegalArgumentException("Invalid date format (YYYY-MM-DD expected)");
//            }
//        }
//
//        profile.setGender(req.getGender());
//        profile.setReligion(req.getReligion());
//        profile.setCaste(req.getCaste());
//        profile.setSubCaste(req.getSubCaste());
//        profile.setDosham(req.getDosham());
//        profile.setMaritalStatus(req.getMaritalStatus());
//        
//        String title = "New User Registered";
//        String message = "New profile registered: " 
//                + Profile.getFirstName() + " " 
//                + Profile.getLastName() 
//                + " (" + Profile.getEmailId() + ")";
//
//        notificationService.createAdminNotification(
//                title,
//                message,
//                LocalDateTime.now()
//        );
//
//        logger.info("Notification sent for new registration: {}", savedProfile.getEmailId());
//
//
//        return profileRepository.save(profile);
//    }


    // FIND PROFILE BY ID
    public Optional<Profile> findById(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Invalid ID");

        return profileRepository.findById(id);
    }


    // FIND PROFILE BY EMAIL
    public Optional<Profile> findByEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be empty");

        return profileRepository.findByEmailId(email);
    }


    // LIST ALL PROFILES
    public Page<Profile> listAll(Pageable pageable) {

        Page<Profile> profiles = profileRepository.findAll(pageable);

        if (profiles.isEmpty()) {
            throw new IllegalStateException("No profiles found");
        }

        return profiles;
    }
    public Page<Profile> listAll(ProfileFilterRequest req, Pageable pageable) {

        Specification<Profile> spec = ProfileSpecification.filter(req);

        Page<Profile> profiles = profileRepository.findAll(spec, pageable);

        if (profiles.isEmpty()) {
            throw new IllegalStateException("No profiles found");
        }

        return profiles;
    }


    // DELETE PROFILE
 // ============================
    // DELETE PROFILE (robust)
    // ============================

    @Transactional
    public void deleteById(Long id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        //  1. delete friend join-table mappings (most important)
        profileRepository.deleteFriendMappings(id);

        //  2. delete friend requests
        friendRequestRepository.deleteBySenderOrReceiver(id);

        //  3. delete chat messages
        chatMessageRepository.deleteByProfileId(id);

        //  4. delete payments
        paymentRecordRepository.deleteByProfileId(id);

        //  5. delete profile pictures (if table exists)
        profilePhotoRepository.deleteByProfileId(id);

        //  6. finally delete profile
        profileRepository.delete(profile);
    }


 // ============================
    // USER UPDATE PROFILE (partial)
    // ============================
 
    @Transactional
    public Profile userUpdateProfile(Long id, Profile data) {
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Basic personal
        if (data.getFirstName() != null) existing.setFirstName(data.getFirstName());
        if (data.getLastName() != null) existing.setLastName(data.getLastName());

        if (data.getMobileNumber() != null) {
            if (!data.getMobileNumber().equals(existing.getMobileNumber()) &&
                    profileRepository.existsByMobileNumber(data.getMobileNumber())) {
                throw new IllegalArgumentException("Mobile number already exists");
            }
            existing.setMobileNumber(data.getMobileNumber());
        }

        if (data.getEmailId() != null) {
            if (!data.getEmailId().equals(existing.getEmailId()) &&
                    profileRepository.existsByEmailId(data.getEmailId())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existing.setEmailId(data.getEmailId());
        }

        if (data.getGender() != null) existing.setGender(data.getGender());
        if (data.getAboutYourself() != null) existing.setAboutYourself(data.getAboutYourself());

        // Family / personal extended fields
        if (data.getFatherName() != null) existing.setFatherName(data.getFatherName());
        if (data.getMotherName() != null) existing.setMotherName(data.getMotherName());
        if (data.getSiblings() != null) existing.setSiblings(data.getSiblings());
        if (data.getFatherStatus() != null) existing.setFatherStatus(data.getFatherStatus());
        if (data.getMotherStatus() != null) existing.setMotherStatus(data.getMotherStatus());
        if (data.getNumberOfBrothers() != null) existing.setNumberOfBrothers(data.getNumberOfBrothers());
        if (data.getNumberOfSisters() != null) existing.setNumberOfSisters(data.getNumberOfSisters());
        if (data.getAncestralOrigin() != null) existing.setAncestralOrigin(data.getAncestralOrigin());
        if (data.getLivingWith() != null) existing.setLivingWith(data.getLivingWith());
        if (data.getChildrenDetails() != null) existing.setChildrenDetails(data.getChildrenDetails());
        if (data.getVegiterian() != null) existing.setVegiterian(data.getVegiterian());

        // Photo (byte[]) - overwrite only when provided (null-safe)
        if (data.getUpdatePhoto() != null) {
            existing.setUpdatePhoto(data.getUpdatePhoto());
        }

        // Education & career
        if (data.getHighestEducation() != null) existing.setHighestEducation(data.getHighestEducation());
        if (data.getOccupation() != null) existing.setOccupation(data.getOccupation());
        if (data.getCollegeName() != null) existing.setCollegeName(data.getCollegeName());
        if (data.getSector() != null) existing.setSector(data.getSector());
        if (data.getCompanyName() != null) existing.setCompanyName(data.getCompanyName());
        if (data.getAnnualIncome() != null) existing.setAnnualIncome(data.getAnnualIncome());

        // Location
        if (data.getState() != null) existing.setState(data.getState());
        if (data.getCity() != null) existing.setCity(data.getCity());
        if (data.getCountry() != null) existing.setCountry(data.getCountry());
        if (data.getWorkLocation() != null) existing.setWorkLocation(data.getWorkLocation());

        // Age & DOB
        if (data.getAge() > 0) existing.setAge(data.getAge());
        if (data.getDateOfBirth() != null) existing.setDateOfBirth(data.getDateOfBirth());

        // Astrology
        if (data.getRashi() != null) existing.setRashi(data.getRashi());
        if (data.getNakshatra() != null) existing.setNakshatra(data.getNakshatra());
        
        // Hobbies + partner preferences
        if (data.getYourHobbies() != null) existing.setYourHobbies(data.getYourHobbies());
        if (data.getPartnerAgeRange() != null) existing.setPartnerAgeRange(data.getPartnerAgeRange());
        if (data.getPartnerReligion() != null) existing.setPartnerReligion(data.getPartnerReligion());
        if (data.getPartnerEducation() != null) existing.setPartnerEducation(data.getPartnerEducation());
        if (data.getPartnerWork() != null) existing.setPartnerWork(data.getPartnerWork());
        if (data.getPartnerHobbies() != null) existing.setPartnerHobbies(data.getPartnerHobbies());

        // Family summary/status
        if (data.getFamilyStatus() != null) existing.setFamilyStatus(data.getFamilyStatus());
        if (data.getFamilyType() != null) existing.setFamilyType(data.getFamilyType());

        return profileRepository.save(existing);
    }



    // ===================== UPDATE PROFILE =====================
    public Profile updateProfile(Profile updatedProfile) {

        Profile existingProfile = profileRepository.findById(updatedProfile.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + updatedProfile.getId()));

        // ---------- BASIC DETAILS ----------
        existingProfile.setProfileFor(updatedProfile.getProfileFor());
        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setGender(updatedProfile.getGender());
        existingProfile.setAge(updatedProfile.getAge());
        existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        existingProfile.setAboutYourself(updatedProfile.getAboutYourself());

        // ---------- CONTACT ----------
        if (!existingProfile.getMobileNumber().equals(updatedProfile.getMobileNumber())) {
            if (profileRepository.existsByMobileNumber(updatedProfile.getMobileNumber())) {
                throw new RuntimeException("Mobile number already exists");
            }
            existingProfile.setMobileNumber(updatedProfile.getMobileNumber());
        }

        if (updatedProfile.getEmailId() != null &&
                !updatedProfile.getEmailId().equals(existingProfile.getEmailId())) {
            if (profileRepository.existsByEmailId(updatedProfile.getEmailId())) {
                throw new RuntimeException("Email already exists");
            }
            existingProfile.setEmailId(updatedProfile.getEmailId());
        }

        // ---------- COMMUNITY ----------
        existingProfile.setReligion(updatedProfile.getReligion());
       
        existingProfile.setSubCaste(updatedProfile.getSubCaste());
        existingProfile.setDosham(updatedProfile.getDosham());
        existingProfile.setMotherTongue(updatedProfile.getMotherTongue());
        existingProfile.setSpiritualPath(updatedProfile.getSpiritualPath());

        // ---------- ACCOUNT ----------
        existingProfile.setMembershipType(updatedProfile.getMembershipType());
        existingProfile.setAccountStatus(updatedProfile.getAccountStatus());
        existingProfile.setLastActive(updatedProfile.getLastActive());
        existingProfile.setActive(updatedProfile.getActive());
        existingProfile.setPremium(updatedProfile.isPremium());

        // ---------- MARITAL & FAMILY ----------
        existingProfile.setMaritalStatus(updatedProfile.getMaritalStatus());
        existingProfile.setNoOfChildren(updatedProfile.getNoOfChildren());
        existingProfile.setIsChildrenLivingWithYou(updatedProfile.getIsChildrenLivingWithYou());
        existingProfile.setHeight(updatedProfile.getHeight());
        existingProfile.setFamilyStatus(updatedProfile.getFamilyStatus());
        existingProfile.setFamilyType(updatedProfile.getFamilyType());

        existingProfile.setFatherName(updatedProfile.getFatherName());
        existingProfile.setMotherName(updatedProfile.getMotherName());
        existingProfile.setSiblings(updatedProfile.getSiblings());
        existingProfile.setFatherStatus(updatedProfile.getFatherStatus());
        existingProfile.setMotherStatus(updatedProfile.getMotherStatus());
        existingProfile.setNumberOfBrothers(updatedProfile.getNumberOfBrothers());
        existingProfile.setNumberOfSisters(updatedProfile.getNumberOfSisters());
        existingProfile.setAncestralOrigin(updatedProfile.getAncestralOrigin());
        existingProfile.setLivingWith(updatedProfile.getLivingWith());
        existingProfile.setChildrenDetails(updatedProfile.getChildrenDetails());
        existingProfile.setHabbits(updatedProfile.getHabbits());
        // ---------- EDUCATION & CAREER ----------
        existingProfile.setHighestEducation(updatedProfile.getHighestEducation());
        existingProfile.setCollegeName(updatedProfile.getCollegeName());
        existingProfile.setEmployedIn(updatedProfile.getEmployedIn());
        existingProfile.setSector(updatedProfile.getSector());
        existingProfile.setOccupation(updatedProfile.getOccupation());
        existingProfile.setCompanyName(updatedProfile.getCompanyName());
        existingProfile.setAnnualIncome(updatedProfile.getAnnualIncome());
        existingProfile.setWorkLocation(updatedProfile.getWorkLocation());

        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setCity(updatedProfile.getCity());

        // ---------- ASTROLOGY ----------
        existingProfile.setRashi(updatedProfile.getRashi());
        existingProfile.setNakshatra(updatedProfile.getNakshatra());
        existingProfile.setAscendant(updatedProfile.getAscendant());
        existingProfile.setBasicPlanetaryPosition(updatedProfile.getBasicPlanetaryPosition());

        // ---------- PREFERENCES ----------
        existingProfile.setYourHobbies(updatedProfile.getYourHobbies());
        existingProfile.setPartnerAgeRange(updatedProfile.getPartnerAgeRange());
        existingProfile.setPartnerReligion(updatedProfile.getPartnerReligion());
        existingProfile.setPartnerEducation(updatedProfile.getPartnerEducation());
        existingProfile.setPartnerWork(updatedProfile.getPartnerWork());
        existingProfile.setPartnerHobbies(updatedProfile.getPartnerHobbies());
        existingProfile.setHabbits(updatedProfile.getHabbits());

        existingProfile.setHobbies(updatedProfile.getHobbies());
        existingProfile.setWeight(updatedProfile.getWeight());
        existingProfile.setBodyType(updatedProfile.getBodyType());
        existingProfile.setComplexion(updatedProfile.getComplexion());
        existingProfile.setExperience(updatedProfile.getExperience());
        existingProfile.setManglik(updatedProfile.getManglik());
        existingProfile.setPartnerLocationPref(updatedProfile.getPartnerLocationPref());
        existingProfile.setPartnerWorkStatus(updatedProfile.getPartnerWorkStatus());
        existingProfile.setVegiterian(updatedProfile.getVegiterian());

        // ---------- FILES ----------
//        existingProfile.setUpdatePhoto(updatedProfile.getUpdatePhoto());
        existingProfile.setDocumentFile(updatedProfile.getDocumentFile());

        // ⚠️ DO NOT update:
        // createdAt, password, role, relationships

        return profileRepository.save(existingProfile);
    }

    // ===================== GET PROFILE =====================
    
    
    
 // ============================================================
    // FIX: helper method for controllers (READ)
    // ============================================================
    public Profile getProfile(Long profileId) {

        if (profileId == null || profileId <= 0) {
            throw new IllegalArgumentException("Invalid profileId");
        }

        return profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }


    // ============================================================
    // UPDATE: privacy settings (WRITE)
    // ============================================================
    @Transactional
    public void updatePrivacySettings(Long profileId, PrivacySettingsDto dto) {

        if (profileId == null || profileId <= 0) {
            throw new IllegalArgumentException("Invalid profileId");
        }

        if (dto == null) {
            throw new IllegalArgumentException("Privacy settings cannot be null");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Update visibility only if provided
        if (dto.getProfileVisibility() != null) {
            profile.setProfileVisibility(dto.getProfileVisibility());
        }

        // Null-safe boolean handling
        profile.setHideProfilePhoto(Boolean.TRUE.equals(dto.getHideProfilePhoto()));

        // Explicit save (clear + safe)
        profileRepository.save(profile);
    }
    
    // profile deletion 30 days logic
    
    @Transactional
    public void requestAccountDeletion(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mark deletion request
        profile.setDeleteRequested(true);
        profile.setDeleteRequestedAt(LocalDateTime.now());

        // Disable account immediately
        profile.setActive(false);

        profileRepository.save(profile);

        // Send confirmation email
        emailService.sendAccountDeletionEmail(  
                profile.getEmailId(),
                profile.getFirstName()
        );
    }

    // ====Logic Account recover deletion after 30 days======
    @Transactional
    public void recoverAccount(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(profile.getDeleteRequested())) {
            throw new RuntimeException("Account is not scheduled for deletion");
        }

        LocalDateTime expiry =
                profile.getDeleteRequestedAt().plusDays(30);

        if (LocalDateTime.now().isAfter(expiry)) {
            throw new RuntimeException("Recovery period expired");
        }

        // Restore account
        profile.setDeleteRequested(false);
        profile.setDeleteRequestedAt(null);
        profile.setActive(true);

        profileRepository.save(profile);
    }
   
  ///============================================================
////ADMIN: APPROVE PROFILE
////============================================================
//    @Transactional
//    public void approveProfile(Long profileId) {
//
//        Profile profile = profileRepository.findById(profileId)
//                .orElseThrow(() -> new RuntimeException("Profile not found"));
//
//        // mark approved + activate account
//        profile.setApproved(true);
//        profile.setActive(true);
//
//        profileRepository.save(profile);
//
//        // ✅ Send notification to profile owner
//        NotificationDto notification = new NotificationDto();
//        notification.setType("PROFILE_APPROVED");
//        notification.setMessage("Your profile has been approved successfully 🎉");
//        notification.setSenderId(0L); // system/admin
//        notification.setReceiverId(profile.getId());
//        notification.setData(Map.of(
//                "profileId", profile.getId(),
//                "status", "APPROVED"
//        ));
//
//        notificationService.sendToUserAndSave(notification);
//
//        // ✅ send approval email
//        emailService.sendApprovalEmail(
//                profile.getEmailId(),
//                profile.getFirstName()
//        );
//    }

    
    @Transactional
    public void approveProfile(Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // mark approved + activate account
        profile.setApproved(true);
        profile.setActive(true);
        profile.setAccountStatus("APPROVED"); // explicitly update account_status

        profileRepository.save(profile);
        profileRepository.flush(); // ensure immediate DB update

//        // ✅ Send notification
//        NotificationDto notification = new NotificationDto();
//        notification.setType("PROFILE_APPROVED");
//        notification.setMessage("Your profile has been approved successfully 🎉");
//        notification.setSenderId(0L); // system/admin
//        notification.setReceiverId(profile.getId());
//        notification.setData(Map.of(
//                "profileId", profile.getId(),
//                "status", "APPROVED"
//        ));
//        notificationService.sendToUserAndSave(notification);

        // ✅ send approval email
        emailService.sendApprovalEmail(
                profile.getEmailId(),
                profile.getFirstName()
        );
    }

//============================================================
//ADMIN: REJECT / DELETE PROFILE
//============================================================
  @Transactional
  public void rejectProfile(Long profileId, String reason) {

	  Profile profile = profileRepository.findById(profileId)
          .orElseThrow(() -> new RuntimeException("Profile not found"));

	  // 1️⃣ Send rejection email BEFORE deletion
	  emailService.sendRejectionEmail(
          profile.getEmailId(),
          profile.getFirstName(),
          reason
  );

  // 2️⃣ Permanently delete ONLY this profile
  deleteById(profileId);
}
  
  public Page<Profile> filterProfiles(ProfileFilterRequest req) {
	  
	  
	  if (req.getSearch() == null || req.getSearch().trim().isEmpty()) {
	        return Page.empty();
	    }
	    Pageable pageable = PageRequest.of(
	            req.getPage(),
	            req.getSize(),
	            Sort.by("createdAt").descending()
	    );
	    return profileRepository.findAll(
	            ProfileSpecification.filter(req),
	            pageable
	    );
	}
  
  public Page<Profile> filterProfiles(ProfileFilterRequest req, Pageable pageable) {

	    Sort sort = Sort.by("createdAt").descending(); 

	    if (req.getSortBy() != null) {

	        switch (req.getSortBy().toLowerCase()) {

	            case "newest":
	            case "newest_first":
	                sort = Sort.by("createdAt").descending();
	                break;

	            case "recently_active":
	                sort = Sort.by("lastActive").descending();
	                break;

	            case "premium":
	                sort = Sort.by("premium").descending()
	                           .and(Sort.by("lastActive").descending());
	                break;

	            case "free":
	                sort = Sort.by("premium").ascending()
	                           .and(Sort.by("lastActive").descending());
	                break;

	            case "relevance":
	            default:
	                sort = Sort.by("premium").descending()
	                           .and(Sort.by("lastActive").descending());
	                break;
	        }
	    }

	    Pageable sortedPageable = PageRequest.of(
	            req.getPage(),
	            req.getSize(),
	            sort
	    );

	    return profileRepository.findAll(
	            ProfileSpecification.filter(req),
	            sortedPageable
	    );
	}
  
  public RecentUserResponse getRecentUsers(int page, int size) {

	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            Sort.by("createdAt").descending()
	    );

	    LocalDateTime now = LocalDateTime.now();

	    long totalUsers = profileRepository.count();

	    long joinedIn24Hours =
	            profileRepository.countByCreatedAtAfter(
	                    now.minusHours(24)
	            );

	    long joinedIn7Days =
	            profileRepository.countByCreatedAtAfter(
	                    now.minusDays(7)
	            );

	    long joinedIn30Days =
	            profileRepository.countByCreatedAtAfter(
	                    now.minusDays(30)
	            );

	    Page<Profile> users =
	            profileRepository.findAll(pageable);

	    return new RecentUserResponse(
	            totalUsers,
	            joinedIn24Hours,
	            joinedIn7Days,
	            joinedIn30Days,
	            users
	    );
	}
  
  public ResponseEntity<ProfileDto> getOtherProfile(Long loginUserId, Long targetProfileId) {

	    Optional<Profile> optional = profileRepository.findById(targetProfileId);

	    if (optional.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    Profile profile = optional.get();

	    
	    if (profile.getId().equals(loginUserId)) {
	        return ResponseEntity.badRequest().build();
	    }

	    ProfileDto dto = mapToDto(profile);  

	    return ResponseEntity.ok(dto);
	}
  private ProfileDto mapToDto(Profile p) {

	    ProfileDto dto = new ProfileDto();

	    // ===== BASIC =====
	    dto.setId(p.getId());
	    dto.setProfileFor(p.getProfileFor());
	    dto.setFirstName(p.getFirstName());
	    dto.setLastName(p.getLastName());
	    dto.setMobileNumber(p.getMobileNumber());
	    dto.setEmailId(p.getEmailId());
	    dto.setGender(p.getGender());
	    dto.setAge(p.getAge());
	    dto.setDateOfBirth(p.getDateOfBirth());
	    dto.setAboutYourself(p.getAboutYourself());
	    dto.setRole(p.getRole());
	    dto.setCreatedAt(p.getCreatedAt());

	    // ===== ACCOUNT =====
	    dto.setMembershipType(p.getMembershipType());
	    dto.setAccountStatus(p.getAccountStatus());
	    dto.setActive(p.getActive());
	    dto.setPremium(p.isPremium());
	    dto.setLastActive(p.getLastActive());

	    // ===== PERSONAL =====
	    dto.setHeight(p.getHeight());
	    dto.setweight(p.getWeight());
	    dto.setBodyType(p.getBodyType());
	    dto.setComplexion(p.getComplexion());
	    dto.setExperience(p.getExperience());

	    // ===== COMMUNITY =====
	    dto.setReligion(p.getReligion());
	    dto.setCaste(null); // you removed caste column
	    dto.setSubCaste(p.getSubCaste());
	    dto.setDosham(p.getDosham());
	    dto.setMotherTongue(p.getMotherTongue());
	    dto.setGothram(p.getGothram());
	    dto.setManglik(p.getManglik());

	    // ===== FAMILY =====
	    dto.setMaritalStatus(p.getMaritalStatus());
	    dto.setNoOfChildren(p.getNoOfChildren());
	    dto.setIsChildrenLivingWithYou(p.getIsChildrenLivingWithYou());
	    dto.setFamilyStatus(p.getFamilyStatus());
	    dto.setFamilyType(p.getFamilyType());
	    dto.setFatherName(p.getFatherName());
	    dto.setMotherName(p.getMotherName());
	    dto.setSiblings(p.getSiblings());
	    dto.setFatherStatus(p.getFatherStatus());
	    dto.setMotherStatus(p.getMotherStatus());
	    dto.setNumberOfBrothers(p.getNumberOfBrothers());
	    dto.setNumberOfSisters(p.getNumberOfSisters());
	    dto.setAncestralOrigin(p.getAncestralOrigin());
	    dto.setLivingWith(p.getLivingWith());
	    dto.setChildrenDetails(p.getChildrenDetails());
	    dto.setVegiterian(p.getVegiterian());
	    dto.setHabbits(p.getHabbits());
	    dto.setSports(p.getSports());
	    dto.setSpiritualPath(p.getSpiritualPath());

	    // ===== EDUCATION =====
	    dto.setHighestEducation(p.getHighestEducation());
	    dto.setCollegeName(p.getCollegeName());
	    dto.setEmployedIn(p.getEmployedIn());
	    dto.setSector(p.getSector());
	    dto.setOccupation(p.getOccupation());
	    dto.setCompanyName(p.getCompanyName());
	    dto.setAnnualIncome(p.getAnnualIncome());
	    dto.setWorkLocation(p.getWorkLocation());
	    dto.setSpiritualPath(p.getSpiritualPath());

	    // ===== LOCATION =====
	    dto.setCountry(p.getCountry());
	    dto.setState(p.getState());
	    dto.setCity(p.getCity());
	    dto.setDistrict(p.getDistrict());
	    dto.setResidenceStatus(p.getResidenceStatus());

	    // ===== ASTRO =====
	    dto.setRashi(p.getRashi());
	    dto.setNakshatra(p.getNakshatra());
	    dto.setAscendant(p.getAscendant());
	    dto.setBasicPlanetaryPosition(p.getBasicPlanetaryPosition());

	    // ===== PREFERENCES =====
	    dto.setHobbies(p.getHobbies());
	    dto.setPartnerAgeRange(p.getPartnerAgeRange());
	    dto.setPartnerReligion(p.getPartnerReligion());
	    dto.setPartnerEducation(p.getPartnerEducation());
	    dto.setPartnerLocationPref(p.getPartnerLocationPref());
	    dto.setPartnerWork(p.getPartnerWork());
	    dto.setPartnerWorkStatus(p.getPartnerWorkStatus());
	    dto.setPartnerHobbies(p.getPartnerHobbies());

	    // ===== PRIVACY =====
	    dto.setProfileVisibility(p.getProfileVisibility());
	    dto.setHideProfilePhoto(p.getHideProfilePhoto());

	    // ===== REFERRAL =====
	    dto.setReferralCode(p.getReferralCode());
	    dto.setReferralRewardBalance(p.getReferralRewardBalance());

	    // ===== PHOTOS =====
	    try {
	        dto.setUpdatePhoto1(profilePhotoService.getPhotoBase64(p.getId(),1));
	        dto.setUpdatePhoto2(profilePhotoService.getPhotoBase64(p.getId(),2));
	        dto.setUpdatePhoto3(profilePhotoService.getPhotoBase64(p.getId(),3));
	        dto.setUpdatePhoto4(profilePhotoService.getPhotoBase64(p.getId(),4));
	    } catch (Exception e) {}

	    dto.setUpdatePhoto(p.getUpdatePhoto());
	    dto.setDocumentFile(p.getDocumentFile());
	    return dto;
	}
  
  @Transactional(readOnly = true)
  public MatchSearchResponse search(MatchSearchRequest request) {

      ProfileFilterRequest filter = request.getFilters();
      if (filter == null) {
          filter = new ProfileFilterRequest();
      }

      // ===== GET MY PROFILE =====
      Long myId = filter.getMyId();
      Profile myProfile = profileRepository.findById(myId)
              .orElseThrow(() -> new RuntimeException("My profile not found"));

      filter.setMyId(myId);
      filter.setMyGender(myProfile.getGender());
      filter.setHiddenIds(getHiddenProfileIds(myId));
   // ===== GLOBAL hidden filter (apply for ALL match types) =====
      filter.setHiddenIds(getHiddenProfileIds(myId));


      // ===== MATCH TYPE LOGIC =====
      if (request.getMatchType() != null) {

          switch (request.getMatchType().toUpperCase()) {

              case "NEW":
                  // profiles created last 3 days
                  filter.setCreatedAfter(
                          java.time.LocalDateTime.now().minusDays(3)
                  );
                  break;

              case "NEAR":
                  // same city
                  filter.setCity(myProfile.getCity());
                  break;

              case "MY":
                  // score handled later (>=70%)
                  break;

              case "MORE":
                  // show all (no extra filter)
                  break;

              default:
                  break;
          }
      }

      Pageable pageable = PageRequest.of(
              request.getPage(),
              request.getSize(),
              buildSort(request.getSort())
      );

      Page<Profile> page = profileRepository.findAll(
              ProfileSpecification.filter(filter),
              pageable
      );
      
      
      

      // ===== MATCH SCORE FILTER =====
      List<ProfileMatchResponse> content = page.getContent()
              .stream()
              .filter(p -> !p.getId().equals(myId))
              .map(p -> {

                  int score =
                          com.example.util.MatchScoreUtil
                                  .calculate(myProfile, p);

                 
                  if ("MY".equalsIgnoreCase(request.getMatchType())
                          && score < 70) {
                      return null;
                  }

                  return mapToMatchResponse(p);

              })
              .filter(java.util.Objects::nonNull)
              .toList();

      return new MatchSearchResponse(
              content,
              page.getTotalPages(),
              page.getTotalElements(),
              page.getNumber()
      );
  }
  
  private List<Long> getHiddenProfileIds(Long myId) {

	    List<Long> hidden = new ArrayList<>();

	    // accepted friends
	    hidden.addAll(friendRequestRepository.findAcceptedFriendIds(myId));

	    // sent requests
	    hidden.addAll(friendRequestRepository.findSentRequestIds(myId));

	    // received requests
	    hidden.addAll(friendRequestRepository.findReceivedRequestIds(myId));

	    // rejected
	    hidden.addAll(friendRequestRepository.findRejectedIds(myId));

	    return hidden.stream().distinct().toList();
	}
  @Transactional
  public void syncMembershipFromPayments(Long profileId){

      List<PaymentRecord> payments =
          paymentRepo.findByUserIdAndStatus(profileId,"PAID");

      PaymentRecord latest = payments.stream()
          .filter(p -> p.getPremiumEnd()!=null &&
                       p.getPremiumEnd().isAfter(LocalDateTime.now()))
          .max(Comparator.comparing(PaymentRecord::getPremiumEnd))
          .orElse(null);

      Profile profile = profileRepository  
              .findById(profileId)
              .orElseThrow(() -> new RuntimeException("Profile not found"));

      if(latest == null){
          profile.setPremium(false);
          profile.setMembershipType("Free");
          profile.setPremiumEnd(null);
      }else{
          profile.setPremium(true);
          profile.setMembershipType(latest.getPlanName());
          profile.setPremiumEnd(latest.getPremiumEnd());
      }

      profileRepository.save(profile);
  }

}
