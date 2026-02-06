package com.example.matrimony.dto;

public class StateDto {

    private Long id;
    private String name;

    public StateDto() {
    }

    public StateDto(Long id, String name) {
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