package com.karbon.splitmint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karbon.splitmint.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, String> {

}
