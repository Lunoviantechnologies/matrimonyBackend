package com.example.matrimony.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProfileFilterRequest {

    private List<String> age;
    private List<String> profileFor;
    private List<String> maritalStatus;
    private List<String> religion;
    private List<String> country;
    private List<String> profession;
    private List<String> education;
    private List<String> lifestyle;
    private List<String> habbits;
    private String matchType; 
    private LocalDateTime createdAfter;
    private Integer minMatchScore;
    private String city;   // for NEAR filter

    private String caste;
    private String height;

    private String sortBy;
    private int page = 0;
    private int size = 10;
    private Integer ageMin;
    private Integer ageMax;
    
    private Map<String, String> otherValues;
    private String customCaste;
    public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	private Long myId;
    private String myGender;
    private String search;

    private List<Long> hiddenIds;
     
	public List<String> getAge() {
		return age;
	}

	public void setAge(List<String> age) {
		this.age = age;
	}

	public List<String> getProfileFor() {
		return profileFor;
	}

	public void setProfileFor(List<String> profileFor) {
		this.profileFor = profileFor;
	}

	public List<String> getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(List<String> maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public List<String> getReligion() {
		return religion;
	}

	public void setReligion(List<String> religion) {
		this.religion = religion;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}

	public List<String> getProfession() {
		return profession;
	}

	public void setProfession(List<String> profession) {
		this.profession = profession;
	}

	public List<String> getEducation() {
		return education;
	}

	public void setEducation(List<String> education) {
		this.education = education;
	}

	public List<String> getLifestyle() {
		return lifestyle;
	}

	public void setLifestyle(List<String> lifestyle) {
		this.lifestyle = lifestyle;
	}

	public List<String> getHabbits() {
		return habbits;
	}

	public void setHabbits(List<String> habbits) {
		this.habbits = habbits;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;  
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getMyId() {
		return myId;
	}

	public void setMyId(Long myId) {
		this.myId = myId;
	}

	public String getMyGender() {
		return myGender;
	}

	public void setMyGender(String myGender) {
		this.myGender = myGender;
	}

	public List<Long> getHiddenIds() {
		return hiddenIds;
	}

	public void setHiddenIds(List<Long> hiddenIds) {
		this.hiddenIds = hiddenIds;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public LocalDateTime getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(LocalDateTime createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Integer getMinMatchScore() {
		return minMatchScore;
	}

	public void setMinMatchScore(Integer minMatchScore) {
		this.minMatchScore = minMatchScore;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public Map<String, String> getOtherValues() {
		return otherValues;
	}

	public void setOtherValues(Map<String, String> otherValues) {
		this.otherValues = otherValues;
	}

	public String getCustomCaste() {
		return customCaste;
	}

	public void setCustomCaste(String customCaste) {
		this.customCaste = customCaste;
	}
    
    
}