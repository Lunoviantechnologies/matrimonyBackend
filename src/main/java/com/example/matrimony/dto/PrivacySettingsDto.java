package com.example.matrimony.dto;

public class PrivacySettingsDto {

    private String profileVisibility;
    private Boolean hideProfilePhoto;
    
 // getters & setters
	public String getProfileVisibility() {
		return profileVisibility;
	}
	public void setProfileVisibility(String profileVisibility) {
		this.profileVisibility = profileVisibility;
	}
	public Boolean getHideProfilePhoto() {
		return hideProfilePhoto;
	}
	public void setHideProfilePhoto(Boolean hideProfilePhoto) {
		this.hideProfilePhoto = hideProfilePhoto;
	}
	@Override
	public String toString() {
		return "PrivacySettingsDto [profileVisibility=" + profileVisibility + ", hideProfilePhoto=" + hideProfilePhoto
				+ "]";
	}
     
	
    
}
