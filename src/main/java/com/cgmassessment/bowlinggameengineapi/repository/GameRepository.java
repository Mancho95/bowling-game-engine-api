package com.cgmassessment.bowlinggameengineapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Game;


public interface GameRepository extends JpaRepository<Game, String> {
	
	Game findFirstByGameId(String gameId);
	
	Game findFirstByPlayerId(String playerId);
	
	Game findFirstByPlayerIdOrderByGameScore(String playerId);
	
	Game findFirstOrderByGameScore();
	
	List<Game> findAllOrderByGameScore();
	
	List<Game> findAllByMatchIdOrderByGameScore(String matchId);
	
  }