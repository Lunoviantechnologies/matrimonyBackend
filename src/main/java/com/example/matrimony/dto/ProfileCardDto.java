package com.example.matrimony.dto;

public class ProfileCardDto {

    private Long id;
    private String name;
    private Integer age;
    private String city;
    private String profession;
    private String updatePhoto;
    private boolean premium;
    private String gender;
    private Boolean hideProfilePhoto;
    private String motherTongue;
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getUpdatePhoto() {
        return updatePhoto;
    }

    public void setUpdatePhoto(String updatePhoto) {
        this.updatePhoto = updatePhoto;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getHideProfilePhoto() {
		return hideProfilePhoto;
	}

	public void setHideProfilePhoto(Boolean hideProfilePhoto) {
		this.hideProfilePhoto = hideProfilePhoto;
	}

	public String getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
    
}