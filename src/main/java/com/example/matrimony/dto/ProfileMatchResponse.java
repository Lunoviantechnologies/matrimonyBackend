package com.example.matrimony.dto;

import jakarta.persistence.Transient;

public class ProfileMatchResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String height;
    private String city;
    private String occupation;
    private String highestEducation;
    private String religion;
    private String subCaste;
    private boolean premium;
    private String updatePhoto;
	private String updatePhoto1;
	private String updatePhoto2;
	private String updatePhoto3;
	private String updatePhoto4;
	private String gender;
	private Boolean hideProfilePhoto;
    public ProfileMatchResponse(Long id,
                                String firstName,
                                String lastName,
                                Integer age,
                                String height,
                                String city,
                                String occupation,
                                String highestEducation,
                                String religion,
                                String subCaste,
                                boolean premium,
                                String updatePhoto,
                                String updatePhoto1,
                                String updatePhoto2,
                                String updatePhoto3,
                                String updatePhoto4,
                                Boolean hideProfilePhoto,
                                String gender
                                ) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.city = city;
        this.occupation = occupation;
        this.highestEducation = highestEducation;
        this.religion = religion;
        this.subCaste = subCaste;
        this.premium = premium;
        this.updatePhoto = updatePhoto;
        this.updatePhoto1=updatePhoto1;
        this.updatePhoto2=updatePhoto2;
        this.updatePhoto3=updatePhoto3;
        this.updatePhoto4=updatePhoto4;
        this.gender=gender;
        this.hideProfilePhoto=hideProfilePhoto;
    }


	public String getUpdatePhoto() {
		return updatePhoto;
	}


	public void setUpdatePhoto(String updatePhoto) {
		this.updatePhoto = updatePhoto;
	}


	public String getUpdatePhoto1() {
		return updatePhoto1;
	}


	public void setUpdatePhoto1(String updatePhoto1) {
		this.updatePhoto1 = updatePhoto1;
	}


	public String getUpdatePhoto2() {
		return updatePhoto2;
	}


	public void setUpdatePhoto2(String updatePhoto2) {
		this.updatePhoto2 = updatePhoto2;
	}


	public String getUpdatePhoto3() {
		return updatePhoto3;
	}


	public void setUpdatePhoto3(String updatePhoto3) {
		this.updatePhoto3 = updatePhoto3;
	}


	public String getUpdatePhoto4() {
		return updatePhoto4;
	}


	public void setUpdatePhoto4(String updatePhoto4) {
		this.updatePhoto4 = updatePhoto4;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Boolean getHideProfilePhoto() {
		return hideProfilePhoto;
	}


	public void setHideProfilePhoto(Boolean hideProfilePhoto) {
		this.hideProfilePhoto = hideProfilePhoto;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getOccupation() {
		return occupation;
	}


	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}


	public String getHighestEducation() {
		return highestEducation;
	}


	public void setHighestEducation(String highestEducation) {
		this.highestEducation = highestEducation;
	}


	public String getReligion() {
		return religion;
	}


	public void setReligion(String religion) {
		this.religion = religion;
	}


	public String getSubCaste() {
		return subCaste;
	}


	public void setSubCaste(String subCaste) {
		this.subCaste = subCaste;
	}


	public boolean isPremium() {
		return premium;
	}


	public void setPremium(boolean premium) {
		this.premium = premium;
	}
    
}