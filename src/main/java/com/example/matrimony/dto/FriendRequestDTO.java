package com.example.matrimony.dto;

import com.example.matrimony.entity.FriendRequest;

public class FriendRequestDTO {
    private Long requestId;
    private Long receiverId;
    private Long senderId;
    private String senderName;
    private String receiverName;
    private String senderEmail;
    
    private FriendRequest.Status status;

    public FriendRequestDTO(Long requestId,Long senderId, Long receiverId, String senderName,String receiverName, String senderEmail, FriendRequest.Status status) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderEmail = senderEmail; 
        this.status = status;
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
    
    
}


