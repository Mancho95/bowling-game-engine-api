package com.cgmassessment.bowlinggameengineapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Game;


public interface GameRepository extends JpaRepository<Game, String> {
	
	Game findFirstByGameId(String gameId);
	
	Game findFirstByPlayerId(String playerId);
	
	Game findFirstOrderByGameScore();
	
  }