package com.karbon.splitmint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karbon.splitmint.model.Group;

public interface GroupRepository extends JpaRepository<Group, String> {

}
