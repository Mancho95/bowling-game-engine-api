package com.cgmassessment.bowlinggameengineapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cgmassessment.bowlinggameengineapi.domain.Game;


public interface GameRepository extends JpaRepository<Game, String> {
	
	Game findFirstByGameId(String gameId);
	
	Game findFirstByPlayerId(String playerId);
	
	Game findFirstByPlayerIdOrderByGameScore(String playerId);
	
	@Query(nativeQuery = true, value ="select * from game order by game_score desc limit 1")
	Game findFirstOrderByGameScore();
	
	@Query(nativeQuery = true, value ="select * from game order by game_score desc")
	List<Game> findAllOrderByGameScore();
	
	List<Game> findAllByMatchIdOrderByGameScore(String matchId);
	
  }