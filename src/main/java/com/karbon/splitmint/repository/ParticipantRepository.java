package com.karbon.splitmint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karbon.splitmint.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, String> {

}
