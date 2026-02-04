package com.karbon.splitmint.dto;

public class ParticipantResponseDto {

    private String id;
    private String name;

    public ParticipantResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
