package com.example.matrimony.dto;

public class FriendRequestCardDto {

    private Long requestId;
    private String status;
    private ProfileCardDto profile;
    private Long senderId;
    private Long receiverId;
    private Boolean hideProfilePhoto;
    
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProfileCardDto getProfile() {
		return profile;
	}
	public void setProfile(ProfileCardDto profile) {
		this.profile = profile;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public Boolean getHideProfilePhoto() {
		return hideProfilePhoto;
	}
	public void setHideProfilePhoto(Boolean hideProfilePhoto) {
		this.hideProfilePhoto = hideProfilePhoto;
	}

   
}