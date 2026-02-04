package com.karbon.splitmint.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String description;
    private double amount;
    private String paidByParticipantId;

    @ElementCollection
    private List<String> participantIds;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Group group;

    public Expense() {
    }

    public Expense(String description, double amount,
            String paidByParticipantId,
            List<String> participantIds) {
        this.description = description;
        this.amount = amount;
        this.paidByParticipantId = paidByParticipantId;
        this.participantIds = participantIds;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getPaidByParticipantId() {
        return paidByParticipantId;
    }

    public double getAmount() {
        return amount;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }
}
