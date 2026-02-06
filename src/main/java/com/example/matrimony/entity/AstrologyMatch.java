package com.example.matrimony.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "astrology_matches")
public class AstrologyMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_one_id")
    private Profile profileOne;

    @ManyToOne
    @JoinColumn(name = "profile_two_id")
    private Profile profileTwo;

    private String nakshatraOne;
    private String nakshatraTwo;

    private int score;

    private LocalDateTime matchedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfileOne() {
		return profileOne;
	}

	public void setProfileOne(Profile profileOne) {
		this.profileOne = profileOne;
	}

	public Profile getProfileTwo() {
		return profileTwo;
	}

	public void setProfileTwo(Profile profileTwo) {
		this.profileTwo = profileTwo;
	}

	public String getNakshatraOne() {
		return nakshatraOne;
	}

	public void setNakshatraOne(String nakshatraOne) {
		this.nakshatraOne = nakshatraOne;
	}

	public String getNakshatraTwo() {
		return nakshatraTwo;
	}

	public void setNakshatraTwo(String nakshatraTwo) {
		this.nakshatraTwo = nakshatraTwo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public LocalDateTime getMatchedAt() {
		return matchedAt;
	}

	public void setMatchedAt(LocalDateTime matchedAt) {
		this.matchedAt = matchedAt;
	}
    
}
