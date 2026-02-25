package com.example.matrimony.dto;

import com.example.matrimony.entity.FriendRequest;

public class FriendRequestDTO {
    private Long requestId;
    private Long receiverId;
    private Long senderId;
    private String senderName;
    private String receiverName;
    private String senderEmail;
    
    private String senderCity;
    private Integer senderAge;
    private String senderPhoto;

    private String receiverCity;
    private Integer receiverAge;
    private String receiverPhoto;
    
    private String receiverGender;
    private String senderGender;
    
    private FriendRequest.Status status;

    public FriendRequestDTO(Long requestId,Long senderId, Long receiverId, String senderName,String receiverName, String senderEmail,
    		Integer senderAge,String senderPhoto,String senderGender,String receiverGender,
    		String receiverCity,Integer receiverAge,String receiverPhoto,String senderCity, FriendRequest.Status status) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderEmail = senderEmail; 
        this.status = status;
        this.senderCity=senderCity;
        this.senderAge = senderAge;
        this.senderPhoto = senderPhoto;
        this.receiverGender=receiverGender;
        this.senderGender=senderGender;
        this.receiverCity = receiverCity;
        this.receiverAge = receiverAge;
        this.receiverPhoto = receiverPhoto;
    }
    
    public FriendRequestDTO() {
    	
    }

	
	public synchronized Long getRequestId() {
		return requestId;
	}

	public synchronized void setRequestId(Long requestId) {
		this.requestId = requestId;
	}


	public synchronized Long getReceiverId() {
		return receiverId;
	}

	public synchronized void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public synchronized String getSenderName() {
		return senderName;
	}

	public synchronized void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public synchronized String getSenderEmail() {
		return senderEmail;
	}

	public synchronized void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public synchronized FriendRequest.Status getStatus() {
		return status;
	}

	public synchronized void setStatus(FriendRequest.Status status) {
		this.status = status;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public Integer getSenderAge() {
		return senderAge;
	}

	public void setSenderAge(Integer senderAge) {
		this.senderAge = senderAge;
	}

	public String getSenderPhoto() {
		return senderPhoto;
	}

	public void setSenderPhoto(String senderPhoto) {
		this.senderPhoto = senderPhoto;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public Integer getReceiverAge() {
		return receiverAge;
	}

	public void setReceiverAge(Integer receiverAge) {
		this.receiverAge = receiverAge;
	}

	public String getReceiverPhoto() {
		return receiverPhoto;
	}

	public void setReceiverPhoto(String receiverPhoto) {
		this.receiverPhoto = receiverPhoto;
	}

	public String getReceiverGender() {
		return receiverGender;
	}

	public void setReceiverGender(String receiverGender) {
		this.receiverGender = receiverGender;
	}

	public String getSenderGender() {
		return senderGender;
	}

	public void setSenderGender(String senderGender) {
		this.senderGender = senderGender;
	}
    
    
}


