package com.cgmassessment.bowlinggameengineapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Game;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.GameRepository;

@Service
public class GameService {

	@Autowired
	GameRepository gameRepository;
	
	public ObjectNode getGameWithHigherScore() {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Game gameWithHigherScore = gameRepository.findFirstOrderByGameScore();
			if(gameWithHigherScore != null) {
				result = buildGameObjectNodeFromObject(gameWithHigherScore);
			} else {
				result.put(Constant.ERROR, "No game with higher score");
			}
		} catch(Exception e) {
			System.out.println("Getting game with higher score went wrong" + e.toString());
		}
		return result;
	}
	
	public ObjectNode getGameWithHigherScoreFromPlayerId(String playerId) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Game gameWithHigherScoreFromPlayerId = gameRepository.findFirstByPlayerIdOrderByGameScore(playerId);
			if(gameWithHigherScoreFromPlayerId != null) {
				result = buildGameObjectNodeFromObject(gameWithHigherScoreFromPlayerId);
			} else {
				result.put(Constant.ERROR, "No game with higher score for that player id");
			}
		} catch(Exception e) {
			System.out.println("Getting game with high score from player id went wrong" + e.toString());
		}
		return result;
	}
	
	public ObjectNode getGamesWithHigherScore() {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			List<Game> gameListOrderedByScore = gameRepository.findAllOrderByGameScore();
			if(gameListOrderedByScore != null) {
				ObjectNode leaderboard = JsonHelper.getEmptyObjectNode();
				for(int i = 0; (i < Constant.LEADERBOARD_ELEMENTS && i < gameListOrderedByScore.size()); i++) {
					if(gameListOrderedByScore.get(i) != null) {
						ObjectNode gameObject = buildGameObjectNodeFromObject(gameListOrderedByScore.get(i));
						leaderboard.set(Constant.POSITION + " " + (i + 1), gameObject);
					}
				}
				result.set(Constant.LEADERBOARD, leaderboard);
			} else {
				result.put(Constant.ERROR, "No games saved");
			}
		} catch(Exception e) {
			System.out.println("Getting games ordered by score went wrong" + e.toString());
		}
		return result;
	}
	
	private ObjectNode buildGameObjectNodeFromObject(Game game) {
		ObjectNode gameObjectNode = JsonHelper.getEmptyObjectNode();
		ObjectNode values = JsonHelper.getEmptyObjectNode();
		values.put(Constant.GAME_ID, game.getGameId());
		values.put(Constant.PLAYER_ID, game.getPlayerId());
		values.put(Constant.MATCH_ID, game.getMatchId());
		values.put(Constant.GAME_SCORE, game.getGameScore());
		gameObjectNode.set(Constant.GAME, values);
		return gameObjectNode;
	}
	
}