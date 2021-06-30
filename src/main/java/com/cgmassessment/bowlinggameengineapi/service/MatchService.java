package com.cgmassessment.bowlinggameengineapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Match;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.MatchRepository;

@Service
public class MatchService {

	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
	GameService gameService;
	
	public ObjectNode newMatch(ArrayNode players) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Match newMatchWithPlayers = new Match();
			String matchId = UUID.randomUUID().toString();
			newMatchWithPlayers.setMatchId(matchId);
			newMatchWithPlayers.setMatchDate(LocalDateTime.now());
			int gameScoreApp = 0;
			String winnerId = null;
			ObjectNode gameObject = JsonHelper.getEmptyObjectNode();
			for(JsonNode player: players) {
				ObjectNode gamePlayed = gameService.newGame(matchId, player.toString());
				if(gamePlayed != null && gamePlayed.has(Constant.GAME) && JsonHelper.getAsObject(gamePlayed, Constant.GAME).has(Constant.GAME_SCORE)) {
					gameObject = JsonHelper.getAsObject(gamePlayed, Constant.GAME);
					int gameScore = JsonHelper.getInt(gameObject, Constant.GAME_SCORE);
					if(gameScore >= gameScoreApp) {
						gameScoreApp = gameScore;
						winnerId = player.toString();
					}
				} else {
					result.put(Constant.ERROR, "Error on game simulation");
				}
			}
			newMatchWithPlayers.setMatchWinnerId(winnerId);
			matchRepository.save(newMatchWithPlayers);
			result.set(Constant.GAME, gameObject);
			result.set(Constant.MATCH, buildMatchObjectNodeFromObject(newMatchWithPlayers));
		} catch(Exception e) {
			System.out.println("Creating a new match went wrong" + e.toString());
		}
		return result;
	}
	
	private ObjectNode buildMatchObjectNodeFromObject(Match match) {
		ObjectNode matchObjectNode = JsonHelper.getEmptyObjectNode();
		ObjectNode values = JsonHelper.getEmptyObjectNode();
		values.put(Constant.MATCH_ID, match.getMatchId());
		values.put(Constant.MATCH_WINNER_ID, match.getMatchWinnerId());
		values.put(Constant.MATCH_DATE, match.getMatchDate().toString());
		matchObjectNode.set(Constant.MATCH, values);
		return matchObjectNode;
	}
	
}