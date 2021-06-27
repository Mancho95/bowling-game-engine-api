package com.cgmassessment.bowlinggameengineapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Attempt;


public interface AttemptRepository extends JpaRepository<Attempt, String> {
	
	List<Attempt> findAllByFrameIdOrderByAttemptIndex(String frameId);
	
	Attempt findFirstByAttemptId(String attemptId);
	
  }