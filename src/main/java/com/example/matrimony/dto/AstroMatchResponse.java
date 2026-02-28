package com.example.matrimony.dto;


public class AstroMatchResponse {

    private int score;
    private String nakshatraOne;
    private String nakshatraTwo;
    private String message;

    public AstroMatchResponse(int score, String n1, String n2, String message) {
        this.score = score;
        this.nakshatraOne = n1;
        this.nakshatraTwo = n2;
        this.message = message;
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}
