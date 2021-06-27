package com.cgmassessment.bowlinggameengineapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Match;


public interface MatchRepository extends JpaRepository<Match, String> {
	
	Match findFirstByMatchId(String matchId);
	
  }