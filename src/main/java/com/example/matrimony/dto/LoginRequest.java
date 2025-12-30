package com.example.matrimony.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("createPassword")
    private String createPassword;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCreatePassword() {
        return createPassword;
    }

    public void setCreatePassword(String createPassword) {
        this.createPassword = createPassword;
    }

    @Override
    public String toString() {
        return "LoginRequest [emailId=" + emailId + ", createPassword=" + createPassword + "]";
    }
}
