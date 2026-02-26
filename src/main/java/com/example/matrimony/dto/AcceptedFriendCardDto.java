package com.example.matrimony.dto;

public class AcceptedFriendCardDto {

    private Long friendId;
    private String friendName;
    private String friendPhoto;
    private Boolean hideProfilePhoto;
    private String gender;

    public AcceptedFriendCardDto(Long friendId,
                                 String friendName,
                                 String friendPhoto,
                                 String gender,
                                 Boolean hideProfilePhoto) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendPhoto = friendPhoto;
        this.hideProfilePhoto = hideProfilePhoto;
        this.gender=gender;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendPhoto() {
        return friendPhoto;
    }

    public Boolean getHideProfilePhoto() {
        return hideProfilePhoto;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setFriendPhoto(String friendPhoto) {
        this.friendPhoto = friendPhoto;
    }

    public void setHideProfilePhoto(Boolean hideProfilePhoto) {
        this.hideProfilePhoto = hideProfilePhoto;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
    
}