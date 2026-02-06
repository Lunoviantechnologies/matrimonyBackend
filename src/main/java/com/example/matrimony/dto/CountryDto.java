package com.example.matrimony.dto;

public class CountryDto {

    private Long id;
    private String name;

    public CountryDto() {
        // required for serialization
    }

    public CountryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }
}