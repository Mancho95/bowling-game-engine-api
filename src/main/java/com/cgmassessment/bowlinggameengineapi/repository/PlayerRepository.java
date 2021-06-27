package com.cgmassessment.bowlinggameengineapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Player;


public interface PlayerRepository extends JpaRepository<Player, String> {
	
	Player findFirstByPlayerId(String playerId);
	
	Player findFirstByGameId(String gameId);
	
  }