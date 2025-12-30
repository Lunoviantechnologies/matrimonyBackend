package com.example.matrimony.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_id", nullable = false)
//    @JsonManagedReference("sentRequests")
//    private Profile sender;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "receiver_id", nullable = false)
//    @JsonManagedReference("receivedRequests")
//    private Profile receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference("sentRequests")
    private Profile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonBackReference("receivedRequests")
    private Profile receiver;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt; 

 // if the friend accepted 
    @ManyToOne
  	@JoinColumn(name = "friend_request_id")
  	private FriendRequest friendRequest;

    // Constructors
    public FriendRequest() {}
    public FriendRequest(Profile sender, Profile receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Profile getSender() { return sender; }
    public void setSender(Profile sender) { this.sender = sender; }

    public Profile getReceiver() { return receiver; }
    public void setReceiver(Profile receiver) { this.receiver = receiver; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
	public synchronized LocalDateTime getSentAt() {
		return sentAt;
	}
	public synchronized void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
    
    
    
	
}
