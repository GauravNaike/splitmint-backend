package com.karbon.splitmint.dto;

import java.util.List;

public class GroupResponseDto {

    private String id;
    private String name;
    private List<ParticipantResponseDto> participants;

    public GroupResponseDto(String id, String name, List<ParticipantResponseDto> participants) {
        this.id = id;
        this.name = name;
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ParticipantResponseDto> getParticipants() {
        return participants;
    }

}
