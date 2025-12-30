package com.example.matrimony.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_message")

public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    @JsonManagedReference("sentMessages")
//    private Profile sender;
//
//    @ManyToOne
//    @JoinColumn(name = "receiver_id")
//    @JsonManagedReference("receivedMessages")
//    private Profile receiver;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonBackReference("sentMessages")
    private Profile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @JsonBackReference("receivedMessages")
    private Profile receiver;


    @Column(length = 1000)
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();

	public synchronized Long getId() {
		return id;
	}

	public synchronized void setId(Long id) {
		this.id = id;
	}

	public synchronized Profile getSender() {
		return sender;
	}

	public synchronized void setSender(Profile sender) {
		this.sender = sender;
	}

	public synchronized Profile getReceiver() {
		return receiver;
	}

	public synchronized void setReceiver(Profile receiver) {
		this.receiver = receiver;
	}

	public synchronized String getMessage() {
		return message;
	}

	public synchronized void setMessage(String message) {
		this.message = message;
	}

	public synchronized LocalDateTime getTimestamp() {
		return timestamp;
	}

	public synchronized void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", message=" + message
				+ ", timestamp=" + timestamp + "]";
	}

   
}

