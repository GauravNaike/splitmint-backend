package com.karbon.splitmint.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "group_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Group group;

    public Participant() {
    }

    public Participant(String name) {
        this();
        this.name = name;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

}
