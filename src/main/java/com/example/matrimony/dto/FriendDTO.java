package com.example.matrimony.dto;

public class FriendDTO {

	private Long senderId;
    private Long requestId;  
    private Long receiverId;
    private String receiverName;      
    private String status;
  
    public FriendDTO(Long requestId,Long senderId, Long receiverId, String receiverName, String status) {
    	this.setSenderId(senderId);
        this.requestId = requestId;
        this.receiverId = receiverId;      
        this.receiverName = receiverName;    
        this.status = status;
    }

    // Getters and Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    
    public synchronized Long getReceiverId() {
		return receiverId;
	}

	public synchronized void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

  
	public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

   
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}


}
