package com.karbon.splitmint.dto;

public class BalanceDto {

    private String participantId;
    private double amount;

    public BalanceDto(String participantId, double amount) {
        this.participantId = participantId;
        this.amount = amount;
    }

    public String getParticipantId() {
        return participantId;
    }

    public double getAmount() {
        return amount;
    }
}
