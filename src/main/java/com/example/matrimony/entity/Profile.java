package com.example.matrimony.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profiles")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String profileFor;

	@NotBlank
	private String firstName;
	private String lastName;

	@PrePersist
	protected void onCreate() {
		if (this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
	}

	@NotBlank
	@Column(unique = true)
	private String mobileNumber;

	// @JsonIgnore
	@NotBlank
	private String createPassword;

	private String role = "USER";

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = true)
	private Boolean active = Boolean.TRUE;

	@Min(18)
	private int age;

	private LocalDate dateOfBirth;

	@Email
	@Column(unique = true)
	private String emailId;

	private String gender;

	@Column(name = "about_yourself", length = 500, nullable = true)
	private String aboutYourself;

	// community
	private String religion;
	private String caste;
	private String subCaste;
	private String dosham;
	private String motherTongue;

	// membership/account
	@Column(name = "membership_type", length = 50)
	private String membershipType = "Free";

	@Column(name = "account_status", length = 50)
	private String accountStatus = "Pending Verification";

	@Column(name = "last_active")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastActive;

	// Marital & family
	private String maritalStatus;
	@Column(nullable = true)
	private Integer noOfChildren;
	@Column(nullable = true)
	private Boolean isChildrenLivingWithYou;
	private String height;
	private String familyStatus;
	private String familyType;

	// store filenames/urls (controller writes filename)
	@Column(name = "update_photo", length = 512)
	private String updatePhoto;

	@Column(name = "document_file", length = 512)
	private String documentFile;

	// family details
	private String fatherName;
	private String motherName;

	@Column(name = "siblings", length = 200)
	private String siblings;

	@Column(name = "father_status", length = 100)
	private String fatherStatus;

	@Column(name = "mother_status", length = 100)
	private String motherStatus;

	private Integer numberOfBrothers;
	private Integer numberOfSisters;

	@Column(name = "ancestral_origin", length = 200)
	private String ancestralOrigin;

	@Column(name = "living_with", length = 100)
	private String livingWith;

	@Column(name = "children_details", length = 500)
	private String childrenDetails;

	// Education & career
	private String highestEducation;
	private String collegeName;
	private String employedIn;
	private String sector;
	private String occupation;
	private String companyName;
	private String annualIncome;
	private String workLocation;
	@Column(name="spiritualPath", length = 50, nullable = true )
	private String spiritualPath;

	private String state;
	private String country;
	private String city;

	// Astrology
	private String rashi;
	private String nakshatra;
	private String ascendant;
	@Lob
	@Column(name = "basic_planetary_position", columnDefinition = "LONGTEXT")
	private String basicPlanetaryPosition;

	// Preferences
	private String yourHobbies;
	private String partnerAgeRange;
	private String partnerReligion;
	private String partnerEducation;
	private String partnerWork;
	private String partnerHobbies;
	@Column(name="vegiterian", nullable = false)
	private String vegiterian;

	// Additional fields from your other file
	private String hobbies;
	private String weight;
	private String bodyType;
	private String complexion;
	private String experience;
	private String manglik;
	private String partnerLocationPref;
	private String partnerWorkStatus;
	private boolean premium;

	// for payment experiration
	@Column(name = "premium_start")
	private LocalDateTime premiumStart;

	@Column(name = "premium_end")
	private LocalDateTime premiumEnd;

	// ==============
	@Column(name = "profile_visibility")
	private String profileVisibility; // EVERYONE | ONLY_MATCHES | PREMIUM_MEMBERS

	@Column(name = "hide_profile_photo")
	private Boolean hideProfilePhoto;

	@Column(name = "delete_requested")
	private Boolean deleteRequested = false;

	@Column(name = "delete_requested_at")
	private LocalDateTime deleteRequestedAt;

	private String sports;
	
	@Column(nullable = false)
    private Boolean approved = Boolean.FALSE;   // admin approval
	// ============

	public synchronized String getWeight() {
		return weight;
	}

	public synchronized String getSports() {
		return sports;
	}

	public synchronized void setSports(String sports) {
		this.sports = sports;
	}

	public synchronized void setWeight(String weight) {
		this.weight = weight;
	}

	public synchronized String getProfileVisibility() {
		return profileVisibility;
	}

	public synchronized void setProfileVisibility(String profileVisibility) {
		this.profileVisibility = profileVisibility;
	}

	public synchronized Boolean getHideProfilePhoto() {
		return hideProfilePhoto;
	}

	public synchronized void setHideProfilePhoto(Boolean hideProfilePhoto) {
		this.hideProfilePhoto = hideProfilePhoto;
	}

	public synchronized Boolean getDeleteRequested() {
		return deleteRequested;
	}

	public synchronized void setDeleteRequested(Boolean deleteRequested) {
		this.deleteRequested = deleteRequested;
	}

	public synchronized LocalDateTime getDeleteRequestedAt() {
		return deleteRequestedAt;
	}

	public synchronized void setDeleteRequestedAt(LocalDateTime deleteRequestedAt) {
		this.deleteRequestedAt = deleteRequestedAt;
	}
	
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<ProfileView> profileViews = new ArrayList<>();


	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore // ignore during registration/fetch
	private List<FriendRequest> sentRequests = new ArrayList<>();

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<FriendRequest> receivedRequests = new ArrayList<>();


	@OneToMany(mappedBy = "sender",  cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore // ignore during registration/fetch
	private List<ChatMessage> sentMessages = new ArrayList<>();

	@OneToMany(mappedBy = "receiver",  cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ChatMessage> receivedMessages = new ArrayList<>();


	@OneToMany(mappedBy = "profile", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<PaymentRecord> payments = new ArrayList<>();

	@ManyToMany
	@jakarta.persistence.JoinTable(name = "profile_friends", joinColumns = @jakarta.persistence.JoinColumn(name = "profile_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "friend_id"))
	@JsonIgnore
	private List<Profile> friends = new ArrayList<>();

	// Payments helpers
	public List<PaymentRecord> getPayments() {
		return payments;
	}

	public void addPayment(com.example.matrimony.entity.PaymentRecord p) {
		if (p == null)
			return;
		payments.add(p);
		p.setProfile(this);
	}

	public void removePayment(PaymentRecord p) {
		if (p == null)
			return;
		payments.remove(p);
		p.setProfile(null);
	}

	// --- getters & setters (important ones included) ---

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
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
	

	public String getVegiterian() {
		return vegiterian;
	}

	public void setVegiterian(String vegiterian) {
		this.vegiterian = vegiterian;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
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

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getYourHobbies() {
		return yourHobbies;
	}

	public void setYourHobbies(String yourHobbies) {
		this.yourHobbies = yourHobbies;
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

	public void setPartnerReligion(String partnerReligion) {
		this.partnerReligion = partnerReligion;
	}

	public String getPartnerEducation() {
		return partnerEducation;
	}

	public void setPartnerEducation(String partnerEducation) {
		this.partnerEducation = partnerEducation;
	}

	public String getPartnerWork() {
		return partnerWork;
	}

	public void setPartnerWork(String partnerWork) {
		this.partnerWork = partnerWork;
	}

	public String getPartnerHobbies() {
		return partnerHobbies;
	}

	public void setPartnerHobbies(String partnerHobbies) {
		this.partnerHobbies = partnerHobbies;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
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

	public String getManglik() {
		return manglik;
	}

	public void setManglik(String manglik) {
		this.manglik = manglik;
	}

	public String getPartnerLocationPref() {
		return partnerLocationPref;
	}

	public void setPartnerLocationPref(String partnerLocationPref) {
		this.partnerLocationPref = partnerLocationPref;
	}

	public String getPartnerWorkStatus() {
		return partnerWorkStatus;
	}

	public void setPartnerWorkStatus(String partnerWorkStatus) {
		this.partnerWorkStatus = partnerWorkStatus;
	}

	public List<com.example.matrimony.entity.FriendRequest> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(List<com.example.matrimony.entity.FriendRequest> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public List<com.example.matrimony.entity.FriendRequest> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(List<com.example.matrimony.entity.FriendRequest> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	public List<com.example.matrimony.entity.ChatMessage> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<com.example.matrimony.entity.ChatMessage> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<com.example.matrimony.entity.ChatMessage> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<com.example.matrimony.entity.ChatMessage> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}

	public List<Profile> getFriends() {
		return friends;
	}

	public void setFriends(List<Profile> friends) {
		this.friends = friends;
	}

	public void setPayments(List<com.example.matrimony.entity.PaymentRecord> payments) {
		this.payments = payments;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProfileFor() {
		return profileFor;
	}

	public void setProfileFor(String profileFor) {
		this.profileFor = profileFor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCreatePassword() {
		return createPassword;
	}

	public void setCreatePassword(String createPassword) {
		this.createPassword = createPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/** Null-safe convenience for code expecting primitive boolean */
	public boolean isActiveFlag() {
		return Boolean.TRUE.equals(this.active);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAboutYourself() {
		return aboutYourself;
	}

	public void setAboutYourself(String aboutYourself) {
		this.aboutYourself = aboutYourself;
	}

	public String getUpdatePhoto() {
		return updatePhoto;
	}

	public void setUpdatePhoto(String updatePhoto) {
		this.updatePhoto = updatePhoto;
	}

	public String getDocumentFile() {
		return documentFile;
	}

	public void setDocumentFile(String documentFile) {
		this.documentFile = documentFile;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public LocalDateTime getPremiumStart() {
		return premiumStart;
	}

	public void setPremiumStart(LocalDateTime premiumStart) {
		this.premiumStart = premiumStart;
	}

	public LocalDateTime getPremiumEnd() {
		return premiumEnd;
	}

	public void setPremiumEnd(LocalDateTime premiumEnd) {
		this.premiumEnd = premiumEnd;
	}
	
	

	public String getSpiritualPath() {
		return spiritualPath;
	}

	public void setSpiritualPath(String spiritualPath) {
		this.spiritualPath = spiritualPath;
	}

	public List<ProfileView> getProfileViews() {
		return profileViews;
	}

	public void setProfileViews(List<ProfileView> profileViews) {
		this.profileViews = profileViews;
	}

	public synchronized Boolean getApproved() {
		return approved;
	}

	public synchronized void setApproved(Boolean approved) {
		this.approved = approved;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", profileFor=" + profileFor + ", firstName=" + firstName + ", lastName="
				+ lastName + ", mobileNumber=" + mobileNumber + ", createPassword=" + createPassword + ", role=" + role
				+ ", createdAt=" + createdAt + ", active=" + active + ", age=" + age + ", dateOfBirth=" + dateOfBirth
				+ ", emailId=" + emailId + ", gender=" + gender + ", aboutYourself=" + aboutYourself + ", religion="
				+ religion + ", caste=" + caste + ", subCaste=" + subCaste + ", dosham=" + dosham + ", motherTongue="
				+ motherTongue + ", membershipType=" + membershipType + ", accountStatus=" + accountStatus
				+ ", lastActive=" + lastActive + ", maritalStatus=" + maritalStatus + ", noOfChildren=" + noOfChildren
				+ ", isChildrenLivingWithYou=" + isChildrenLivingWithYou + ", height=" + height + ", familyStatus="
				+ familyStatus + ", familyType=" + familyType + ", updatePhoto=" + updatePhoto + ", documentFile="
				+ documentFile + ", fatherName=" + fatherName + ", motherName=" + motherName + ", siblings=" + siblings
				+ ", fatherStatus=" + fatherStatus + ", motherStatus=" + motherStatus + ", numberOfBrothers="
				+ numberOfBrothers + ", numberOfSisters=" + numberOfSisters + ", ancestralOrigin=" + ancestralOrigin
				+ ", livingWith=" + livingWith + ", childrenDetails=" + childrenDetails + ", highestEducation="
				+ highestEducation + ", collegeName=" + collegeName + ", employedIn=" + employedIn + ", sector="
				+ sector + ", occupation=" + occupation + ", companyName=" + companyName + ", annualIncome="
				+ annualIncome + ", workLocation=" + workLocation + ", state=" + state + ", country=" + country
				+ ", city=" + city + ", rashi=" + rashi + ", nakshatra=" + nakshatra + ", ascendant=" + ascendant
				+ ", basicPlanetaryPosition=" + basicPlanetaryPosition + ", yourHobbies=" + yourHobbies
				+ ", partnerAgeRange=" + partnerAgeRange + ", partnerReligion=" + partnerReligion
				+ ", partnerEducation=" + partnerEducation + ", partnerWork=" + partnerWork + ", partnerHobbies="
				+ partnerHobbies + ", hobbies=" + hobbies + ", weight=" + weight + ", bodyType=" + bodyType
				+ ", complexion=" + complexion + ", experience=" + experience + ", manglik=" + manglik
				+ ", partnerLocationPref=" + partnerLocationPref + ", partnerWorkStatus=" + partnerWorkStatus
				+ ", premium=" + premium + ", sentRequests=" + sentRequests + ", receivedRequests=" + receivedRequests
				+ ", sentMessages=" + sentMessages + ", receivedMessages=" + receivedMessages + ", payments=" + payments
				+ ", friends=" + friends + "]";
	}
}