package com.cgmassessment.bowlinggameengineapi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Game;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.GameRepository;
import com.cgmassessment.bowlinggameengineapi.service.FrameService;

@Service
public class GameService {

	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	FrameService frameService;
	
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
	
	public ObjectNode newGame(String matchId, String playerId) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ArrayNode frames = JsonHelper.getEmptyArrayNode();
		Game newGameFromPlayerId = new Game();
		String gameId = UUID.randomUUID().toString();
		newGameFromPlayerId.setGameId(gameId);
		newGameFromPlayerId.setMatchId(matchId);
		newGameFromPlayerId.setPlayerId(playerId);
		for(int i = 0; i < Constant.NUMBER_OF_FRAMES; i++) {
			ObjectNode framePlayed = JsonHelper.getEmptyObjectNode();
			framePlayed = frameService.newFrame(gameId, i + 1);
			if(framePlayed != null && framePlayed.has(Constant.FRAME) && framePlayed.has(Constant.ATTEMPTS)) {
				frames.add(framePlayed);
			} else if (framePlayed != null && framePlayed.has(Constant.ERROR)) {
				result.put(Constant.ERROR, "Error on generating frame for game");
			} else {
				result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
			}
		}
		int score = calculateScore(newGameFromPlayerId, frames);
		newGameFromPlayerId.setGameScore(score);
		gameRepository.save(newGameFromPlayerId);
		result.set(Constant.FRAMES, frames);
		result.set(Constant.GAME, buildGameObjectNodeFromObject(newGameFromPlayerId));
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
		gameObjectNode.put(Constant.GAME_ID, game.getGameId());
		gameObjectNode.put(Constant.PLAYER_ID, game.getPlayerId());
		gameObjectNode.put(Constant.MATCH_ID, game.getMatchId());
		gameObjectNode.put(Constant.GAME_SCORE, game.getGameScore());
		return gameObjectNode;
	}
	
	private int calculateScore(Game gamePlayed, ArrayNode frames) {
		int score = 0;
		try {
			for(int i = 0; i < Constant.NUMBER_OF_FRAMES; i ++) {
				if(frames.get(i).has(Constant.ATTEMPTS)) {
					int frameScore = 0;
					ArrayNode attempts = JsonHelper.getAsArray(frames.get(i), Constant.ATTEMPTS);
					ObjectNode frame = JsonHelper.getAsObject(frames.get(i), Constant.FRAME);
					ObjectNode nextFrame = JsonHelper.getAsObject(frames.get(i + 1), Constant.FRAME);
					if(frame.has(Constant.FRAME_TYPE) && 
							(JsonHelper.getString(frame, Constant.FRAME_TYPE).equals(Constant.NORMAL) || JsonHelper.getString(frame, Constant.FRAME_TYPE).equals(Constant.TRIPLE))) {
						for(JsonNode attempt: attempts) {
							if(attempt.has(Constant.KNOCKED_PINS)) {
								int attemptScore = JsonHelper.getInt(attempt, Constant.KNOCKED_PINS);
								frameScore = frameScore + attemptScore;
							}
						}
						if(frame.has(Constant.FRAME_ID)) {
							frameService.saveFrameScore(JsonHelper.getString(frame, Constant.FRAME_ID), frameScore);
							frame.put(Constant.FRAME_SCORE, frameScore);
						}
					}
					else if(frame.has(Constant.FRAME_TYPE) && JsonHelper.getString(frame, Constant.FRAME_TYPE).equals(Constant.STRIKE)) {
						if(nextFrame.has(Constant.FRAME_TYPE) && !(JsonHelper.getString(nextFrame, Constant.FRAME_TYPE).equals(Constant.STRIKE) 
								|| JsonHelper.getString(nextFrame, Constant.FRAME_TYPE).equals(Constant.SPARE))) {
							attempts = JsonHelper.getAsArray(frames.get(i + 1), Constant.ATTEMPTS);
							frameScore = 10;
							for(JsonNode attempt: attempts) {
								if(attempt.has(Constant.KNOCKED_PINS)) {
									int attemptScore = JsonHelper.getInt(attempt, Constant.KNOCKED_PINS);
									frameScore = frameScore + attemptScore;
								}
							}
							if(frame.has(Constant.FRAME_ID)) {
								frameService.saveFrameScore(JsonHelper.getString(frame, Constant.FRAME_ID), frameScore);
								frame.put(Constant.FRAME_SCORE, frameScore);
							}
						}
						else {
							frame.put(Constant.FRAME_SCORE, Constant.RECALCULATE);
						}
					}
					else if(frame.has(Constant.FRAME_TYPE) && JsonHelper.getString(frame, Constant.FRAME_TYPE).equals(Constant.SPARE)) {
						if(nextFrame.has(Constant.FRAME_TYPE) && !(JsonHelper.getString(nextFrame, Constant.FRAME_TYPE).equals(Constant.STRIKE) 
								|| JsonHelper.getString(nextFrame, Constant.FRAME_TYPE).equals(Constant.SPARE))) {
							attempts = JsonHelper.getAsArray(frames.get(i + 1), Constant.ATTEMPTS);
							frameScore = 10;
							if(attempts.get(0).has(Constant.KNOCKED_PINS)) {
								int attemptScore = JsonHelper.getInt(attempts.get(0), Constant.KNOCKED_PINS);
								frameScore = frameScore + attemptScore;
							}
							if(frame.has(Constant.FRAME_ID)) {
								frameService.saveFrameScore(JsonHelper.getString(frame, Constant.FRAME_ID), frameScore);
								frame.put(Constant.FRAME_SCORE, frameScore);
							}
						} else {
							frame.put(Constant.FRAME_SCORE, Constant.RECALCULATE);
						}
					}
					score = score + frameScore;
				}
			}
			int recalculateFrameScore = 0;
			for(int j = Constant.NUMBER_OF_FRAMES - 1; j >= 0; j--) {
				ObjectNode frame = JsonHelper.getAsObject(frames.get(j), Constant.FRAME);
				ArrayNode attempts = JsonHelper.getAsArray(frames.get(j), Constant.ATTEMPTS);
				if(frames.get(j).has(Constant.ATTEMPTS) && frame.has(Constant.FRAME_SCORE)) {
					String frameScore = JsonHelper.getString(frame, Constant.FRAME_SCORE);
					String frameType = JsonHelper.getString(frame, Constant.FRAME_TYPE);
					if(frameScore != null && frameScore.equals(Constant.RECALCULATE) && frameType.equals(Constant.STRIKE)) {
						attempts = JsonHelper.getAsArray(frames.get(j + 1), Constant.ATTEMPTS);
						recalculateFrameScore = 10;
						for(JsonNode attempt: attempts) {
							if(attempt.has(Constant.KNOCKED_PINS)) {
								int attemptScore = JsonHelper.getInt(attempt, Constant.KNOCKED_PINS);
								recalculateFrameScore = recalculateFrameScore + attemptScore;
							}
						}
						if(frame.has(Constant.FRAME_ID)) {
							frameService.saveFrameScore(JsonHelper.getString(frame, Constant.FRAME_ID), recalculateFrameScore);
							frame.put(Constant.FRAME_SCORE, recalculateFrameScore);
						}
					}
					if(frameScore != null && frameScore.equals(Constant.RECALCULATE) && frameType.equals(Constant.SPARE)) {
						attempts = JsonHelper.getAsArray(frames.get(j + 1), Constant.ATTEMPTS);
						recalculateFrameScore = 10;
						if(attempts.get(0).has(Constant.KNOCKED_PINS)) {
							int attemptScore = JsonHelper.getInt(attempts.get(0), Constant.KNOCKED_PINS);
							recalculateFrameScore = recalculateFrameScore + attemptScore;
						}
						if(frame.has(Constant.FRAME_ID)) {
							frameService.saveFrameScore(JsonHelper.getString(frame, Constant.FRAME_ID), recalculateFrameScore);
							frame.put(Constant.FRAME_SCORE, recalculateFrameScore);
						}
					} 
				}
			}
		} catch(Exception e) {
			System.out.println("Calculating game score went wrong" + e.toString());
		}
		return score;
		
	}
	
}