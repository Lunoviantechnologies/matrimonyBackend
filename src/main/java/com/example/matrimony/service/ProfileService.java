package com.example.matrimony.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.PrivacySettingsDto;
import com.example.matrimony.dto.RegisterRequest;
import com.example.matrimony.entity.Profile;
import com.example.matrimony.exception.EmailAlreadyExistsException;
import com.example.matrimony.repository.ChatMessageRepository;
import com.example.matrimony.repository.FriendRequestRepository;
import com.example.matrimony.repository.ProfileRepository;

@Service
public class ProfileService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    @Autowired
    private ChatMessageRepository chatmessageREpository;
    @Autowired
    private final Notificationadminservice notificationService;
    
 // ===================== GET TOTAL REGISTERED PROFILES =====================
    public long getRegisteredProfilesCount() {
        return profileRepository.count();  // count() from JpaRepository returns long
    }

   
    public ProfileService(ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            Notificationadminservice notificationService) {
    	this.profileRepository = profileRepository;
    	this.passwordEncoder = passwordEncoder;
    	this.emailService = emailService;
    	this.notificationService = notificationService;
   }



    // REGISTER NEW PROFILE  
    @Transactional
    public Profile register(RegisterRequest req) {

        if (req == null) throw new IllegalArgumentException("Request cannot be null");

        if (req.getEmail() == null || req.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty");

        if (profileRepository.existsByEmailId(req.getEmail())) {
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
    public List<Profile> listAll() {
        List<Profile> profiles = profileRepository.findAll();
        if (profiles.isEmpty())
            throw new IllegalStateException("No profiles found");
        
        return profiles;
    }


    // DELETE PROFILE
 // ============================
    // DELETE PROFILE (robust)
    // ============================

    public void deleteById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
       
        for (Profile friend : profile.getFriends()) {
            friend.getFriends().remove(profile);
        }       
        profile.getSentRequests().clear();
        profile.getReceivedRequests().clear();
       
        profile.getSentMessages().clear();
        profile.getReceivedMessages().clear();
       
        profile.getPayments().clear();
      
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
        emailService.sendAccountDeletionEmail(  // error occur here final keyword of email service
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
}
