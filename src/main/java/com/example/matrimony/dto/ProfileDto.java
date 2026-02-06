package com.example.matrimony.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;

public class ProfileDto {

    private Long id;
    private String profileFor;

    // Personal
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String createPassword;
    private String role;
    private Integer age;
    private LocalDate dateOfBirth;
    private String emailId;
    private String gender;
    private String aboutYourself;

    // Identity / physical
    private String height;
    private String weight;
    private String bodyType;
    private String complexion;
    private String experience;

    // Religion / community
    private String religion;
    private String caste;
    private String subCaste;
    private String dosham;
    private String motherTongue;
    private String manglik;

    // Membership & account
    private String membershipType;
    private String accountStatus;
    private Boolean active;
    private Boolean premium;

    // Timing / metadata
    private LocalDateTime lastActive;
    private LocalDateTime createdAt;

    // Marital / family
    private String maritalStatus;
    private Integer noOfChildren;
    private Boolean isChildrenLivingWithYou;
    private String familyStatus;
    private String familyType;
    private String spiritualPath;
    private String UpdatePhoto1;
    private String UpdatePhoto2;
    private String UpdatePhoto3;
    private String UpdatePhoto4;
    

    // Family details
    private String fatherName;
    private String motherName;
    private String siblings;
    private String fatherStatus;
    private String motherStatus;
    private Integer numberOfBrothers;
    private Integer numberOfSisters;
    private String ancestralOrigin;
    private String livingWith;
    private String childrenDetails;
    private String vegiterian;
    private String gothram;
	
    // Education & career
    private String highestEducation;
    private String collegeName;
    private String employedIn;
    private String sector;
    private String occupation;
    private String companyName;
    private String annualIncome;
    private String workLocation;
 // Location IDs
    private Long countryId;
    private Long stateId;
    private Long cityId;
    
    private String countryName;
    private String stateName;
    private String cityName;

    // Astrology
    private String rashi;
    private String nakshatra;
    private String ascendant;
    private String basicPlanetaryPosition;

    // Preferences
    private String Hobbies;
    private String partnerAgeRange;
    private String partnerReligion;
    private String partnerEducation;
    private String partnerLocationPref;
    private String partnerWork;
    private String partnerWorkStatus;
    private String partnerHobbies;
    private String habbits;
    
    // getters & setters

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = Boolean.FALSE;

    // Files / photos
    private String updatePhoto;

   
    private String documentFile;
    private Integer documentFilePresent;

    // Payments
    private List<PaymentDto> payments = new ArrayList<>();
    
    private String sports;


    // Relationships (kept as entity types for simplicity — consider mapping to DTOs)
//    @JsonIgnore
//    private List<FriendRequest> sentRequests = new ArrayList<>();
//    @JsonIgnore
//    private List<FriendRequest> receivedRequests = new ArrayList<>();
//    @JsonIgnore
//    private List<ChatMessage> sentMessages = new ArrayList<>();
//    @JsonIgnore
//    private List<ChatMessage> receivedMessages = new ArrayList<>();
//
//    // Friends as DTO
//    private List<ProfileDto> friends = new ArrayList<>();
    
    

    public synchronized String getSports() {
		return sports;
	}
	public String getUpdatePhoto1() {
		return UpdatePhoto1;
	}
	public void setUpdatePhoto1(String updatePhoto1) {
		UpdatePhoto1 = updatePhoto1;
	}
	public String getUpdatePhoto2() {
		return UpdatePhoto2;
	}
	public void setUpdatePhoto2(String updatePhoto2) {
		UpdatePhoto2 = updatePhoto2;
	}
	public String getUpdatePhoto3() {
		return UpdatePhoto3;
	}
	public void setUpdatePhoto3(String updatePhoto3) {
		UpdatePhoto3 = updatePhoto3;
	}
	public String getUpdatePhoto4() {
		return UpdatePhoto4;
	}
	public void setUpdatePhoto4(String updatePhoto4) {
		UpdatePhoto4 = updatePhoto4;
	}
	public synchronized void setSports(String sports) {
		this.sports = sports;
	}

	// Optional relation ids
    private Long senderId;
    private Long receiverId;

    // --- Getters / Setters (generate as needed) ---
    
    public Long getId() { return id; }
    public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getweight() {
		return weight;
	}
	public void setweight(String weight) {
		this.weight = weight;
	}
	
	public String getBodyType() {
		return bodyType;
	}
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	public String getComplexion() {
		return complexion;
	}
	public void setComplexion(String complexion) {
		this.complexion = complexion;
	}
	public String getExperience() {
		return experience;
	}
	
	
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getSubCaste() {
		return subCaste;
	}
	public void setSubCaste(String subCaste) {
		this.subCaste = subCaste;
	}
	public String getDosham() {
		return dosham;
	}
	public void setDosham(String dosham) {
		this.dosham = dosham;
	}
	public String getMotherTongue() {
		return motherTongue;
	}
	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}
	public String getManglik() {
		return manglik;
	}
	public void setManglik(String manglik) {
		this.manglik = manglik;
	}
	public String getMembershipType() {
		return membershipType;
	}
	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public LocalDateTime getLastActive() {
		return lastActive;
	}
	public void setLastActive(LocalDateTime lastActive) {
		this.lastActive = lastActive;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime localDateTime) {
		this.createdAt = localDateTime;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public Integer getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(Integer noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public Boolean getIsChildrenLivingWithYou() {
		return isChildrenLivingWithYou;
	}
	public void setIsChildrenLivingWithYou(Boolean isChildrenLivingWithYou) {
		this.isChildrenLivingWithYou = isChildrenLivingWithYou;
	}
	public String getFamilyStatus() {
		return familyStatus;
	}
	public void setFamilyStatus(String familyStatus) {
		this.familyStatus = familyStatus;
	}
	public String getFamilyType() {
		return familyType;
	}
	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getSiblings() {
		return siblings;
	}
	public void setSiblings(String siblings) {
		this.siblings = siblings;
	}
	public String getFatherStatus() {
		return fatherStatus;
	}
	public void setFatherStatus(String fatherStatus) {
		this.fatherStatus = fatherStatus;
	}
	public String getMotherStatus() {
		return motherStatus;
	}
	public void setMotherStatus(String motherStatus) {
		this.motherStatus = motherStatus;
	}
	public Integer getNumberOfBrothers() {
		return numberOfBrothers;
	}
	
	public void setNumberOfBrothers(Integer numberOfBrothers) {
		this.numberOfBrothers = numberOfBrothers;
	}
	public Integer getNumberOfSisters() {
		return numberOfSisters;
	}
	public void setNumberOfSisters(Integer numberOfSisters) {
		this.numberOfSisters = numberOfSisters;
	}
	public String getAncestralOrigin() {
		return ancestralOrigin;
	}
	public void setAncestralOrigin(String ancestralOrigin) {
		this.ancestralOrigin = ancestralOrigin;
	}
	public String getLivingWith() {
		return livingWith;
	}
	public void setLivingWith(String livingWith) {
		this.livingWith = livingWith;
	}
	public String getChildrenDetails() {
		return childrenDetails;
	}
	public void setChildrenDetails(String childrenDetails) {
		this.childrenDetails = childrenDetails;
	}
	public String getHighestEducation() {
		return highestEducation;
	}
	public void setHighestEducation(String highestEducation) {
		this.highestEducation = highestEducation;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public String getEmployedIn() {
		return employedIn;
	}
	public void setEmployedIn(String employedIn) {
		this.employedIn = employedIn;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getWorkLocation() {
		return workLocation;
	}
	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}
	
	public String getRashi() {
		return rashi;
	}
	public void setRashi(String rashi) {
		this.rashi = rashi;
	}
	public String getNakshatra() {
		return nakshatra;
	}
	public void setNakshatra(String nakshatra) {
		this.nakshatra = nakshatra;
	}
	public String getAscendant() {
		return ascendant;
	}
	public void setAscendant(String ascendant) {
		this.ascendant = ascendant;
	}
	public String getBasicPlanetaryPosition() {
		return basicPlanetaryPosition;
	}
	public void setBasicPlanetaryPosition(String basicPlanetaryPosition) {
		this.basicPlanetaryPosition = basicPlanetaryPosition;
	}
	public String getHobbies() {
		return Hobbies;
	}
	public void setHobbies(String Hobbies) {
		this.Hobbies = Hobbies;
	}
	public String getPartnerAgeRange() {
		return partnerAgeRange;
	}
	public void setPartnerAgeRange(String partnerAgeRange) {
		this.partnerAgeRange = partnerAgeRange;
	}
	public String getPartnerReligion() {
		return partnerReligion;
	}

	public String getSpiritualPath() {
		return spiritualPath;
	}
	public void setSpiritualPath(String spiritualPath) {
		this.spiritualPath = spiritualPath;
	}
	public void setPartnerReligion(String partnerReligion) {
		this.partnerReligion = partnerReligion;
	}
	public String getPartnerEducation() {
		return partnerEducation;
	}
	public void setPartnerEducation(String partnerEducation) {
		this.partnerEducation = partnerEducation;
	}
	public String getPartnerLocationPref() {
		return partnerLocationPref;
	}
	public void setPartnerLocationPref(String partnerLocationPref) {
		this.partnerLocationPref = partnerLocationPref;
	}
	public String getPartnerWork() {
		return partnerWork;
	}
	public void setPartnerWork(String partnerWork) {
		this.partnerWork = partnerWork;
	}
	public String getPartnerWorkStatus() {
		return partnerWorkStatus;
	}
	public void setPartnerWorkStatus(String partnerWorkStatus) {
		this.partnerWorkStatus = partnerWorkStatus;
	}
	public String getPartnerHobbies() {
		return partnerHobbies;
	}
	public void setPartnerHobbies(String partnerHobbies) {
		this.partnerHobbies = partnerHobbies;
	}
	public String getDocumentFile() {
		return documentFile;
	}
	public void setDocumentFile(String documentFile) {
		this.documentFile = documentFile;
	}
	public Integer getDocumentFilePresent() {
		return documentFilePresent;
	}
	public void setDocumentFilePresent(Integer documentFilePresent) {
		this.documentFilePresent = documentFilePresent;
	}
	public void setId(Long id) { this.id = id; }

    public String getProfileFor() { return profileFor; }
    public void setProfileFor(String profileFor) { this.profileFor = profileFor; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getCreatePassword() { return createPassword; }
    public void setCreatePassword(String createPassword) { this.createPassword = createPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAboutYourself() { return aboutYourself; }
    public void setAboutYourself(String aboutYourself) { this.aboutYourself = aboutYourself; }

    public String getUpdatePhoto() { return updatePhoto; }
    public void setUpdatePhoto(String updatePhoto) { this.updatePhoto = updatePhoto; }

    public List<PaymentDto> getPayments() { return payments; }
    public void setPayments(List<PaymentDto> payments) { this.payments = payments; }

//    public List<FriendRequest> getSentRequests() { return sentRequests; }
//    public void setSentRequests(List<FriendRequest> sentRequests) { this.sentRequests = sentRequests; }
//
//    public List<FriendRequest> getReceivedRequests() { return receivedRequests; }
//    public void setReceivedRequests(List<FriendRequest> receivedRequests) { this.receivedRequests = receivedRequests; }
//
//    public List<ChatMessage> getSentMessages() { return sentMessages; }
//    public void setSentMessages(List<ChatMessage> sentMessages) { this.sentMessages = sentMessages; }
//
//    public List<ChatMessage> getReceivedMessages() { return receivedMessages; }
//    public void setReceivedMessages(List<ChatMessage> receivedMessages) { this.receivedMessages = receivedMessages; }
//
//    public List<ProfileDto> getFriends() { return friends; }
//    public void setFriends(List<ProfileDto> friends) { this.friends = friends; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Boolean getPremium() { return premium; }
    public void setPremium(Boolean premium) { this.premium = premium; }
    
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	// to string method
	@Override
	public String toString() {
		return "ProfileDto [id=" + id + ", profileFor=" + profileFor + ", firstName=" + firstName + ", lastName="
				+ lastName + ", mobileNumber=" + mobileNumber + ", createPassword=" + createPassword + ", role=" + role
				+ ", age=" + age + ", dateOfBirth=" + dateOfBirth + ", emailId=" + emailId + ", gender=" + gender
				+ ", aboutYourself=" + aboutYourself + ", height=" + height + ", weight=" + weight + ", bodyType="
				+ bodyType + ", complexion=" + complexion + ", experience=" + experience + ", religion=" + religion
				+ ", caste=" + caste + ", subCaste=" + subCaste + ", dosham=" + dosham + ", motherTongue="
				+ motherTongue + ", manglik=" + manglik + ", membershipType=" + membershipType + ", accountStatus="
				+ accountStatus + ", active=" + active + ", premium=" + premium + ", lastActive=" + lastActive
				+ ", createdAt=" + createdAt + ", maritalStatus=" + maritalStatus + ", noOfChildren=" + noOfChildren
				+ ", isChildrenLivingWithYou=" + isChildrenLivingWithYou + ", familyStatus=" + familyStatus
				+ ", familyType=" + familyType + ", fatherName=" + fatherName + ", motherName=" + motherName
				+ ", siblings=" + siblings + ", fatherStatus=" + fatherStatus + ", motherStatus=" + motherStatus
				+ ", numberOfBrothers=" + numberOfBrothers + ", numberOfSisters=" + numberOfSisters
				+ ", ancestralOrigin=" + ancestralOrigin + ", livingWith=" + livingWith + ", childrenDetails="
				+ childrenDetails + ", highestEducation=" + highestEducation + ", collegeName=" + collegeName
				+ ", employedIn=" + employedIn + ", sector=" + sector + ", occupation=" + occupation + ", companyName="
				+ companyName + ", annualIncome=" + annualIncome + ", workLocation=" + workLocation + ", state=" + stateName
				+ ", country=" + countryName + ", city=" + cityName + ", rashi=" + rashi + ", nakshatra=" + nakshatra
				+ ", ascendant=" + ascendant + ", basicPlanetaryPosition=" + basicPlanetaryPosition + ",Hobbies="
				+ Hobbies + ", partnerAgeRange=" + partnerAgeRange + ", partnerReligion=" + partnerReligion
				+ ", partnerEducation=" + partnerEducation + ", partnerLocationPref=" + partnerLocationPref
				+ ", partnerWork=" + partnerWork + ", partnerWorkStatus=" + partnerWorkStatus + ", partnerHobbies="
				+ partnerHobbies + ", updatePhoto=" + updatePhoto + ", documentFile=" + documentFile
				+ ", documentFilePresent=" + documentFilePresent + ", payments=" + payments + ",  senderId=" + senderId
				+ ", receiverId=" + receiverId + "]";
	}
	public String getVegiterian() {
		return vegiterian;
	}
	public void setVegiterian(String vegiterian) {
		this.vegiterian = vegiterian;
	}
	public String getGothram() {
		return gothram;
	}
	public void setGothram(String gothram) {
		this.gothram = gothram;
	}
	public String getHabbits() {
		return habbits;
	}
	public void setHabbits(String habbits) {
		this.habbits = habbits;
	}
	
    
}
