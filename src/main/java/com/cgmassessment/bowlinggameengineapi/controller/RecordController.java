package com.cgmassessment.bowlinggameengineapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.service.GameService;
import com.cgmassessment.bowlinggameengineapi.service.PlayerService;

@RestController
@RequestMapping("/record/")
public class RecordController {

	@Autowired
	PlayerService playerService;
	
	@Autowired
	GameService gameService;
	
	@GetMapping("bestRecord")
	public ObjectNode getHighestRecordPlayerAndGame(){
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ObjectNode highestRecordPlayerAndGame = playerService.getPlayerWithHigherScoreAndGame(); 
		if(highestRecordPlayerAndGame != null && highestRecordPlayerAndGame.has(Constant.ERROR)) {
			result.put(Constant.ERROR, highestRecordPlayerAndGame.get(Constant.ERROR).toString());
		} else if(highestRecordPlayerAndGame != null && highestRecordPlayerAndGame.has(Constant.PLAYER) && highestRecordPlayerAndGame.has(Constant.GAME)) {
			result = highestRecordPlayerAndGame;
		} else {
			result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
		}
		return result;
	}
	
	@GetMapping("bestRecord/{playerId}")
	public ObjectNode getHighestRecordGameFromPlayerId(@PathVariable String playerId) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ObjectNode highestRecordGameFromPlayerId = gameService.getGameWithHigherScoreFromPlayerId(playerId);
		if(highestRecordGameFromPlayerId != null && highestRecordGameFromPlayerId.has(Constant.ERROR)) {
			result.put(Constant.ERROR, highestRecordGameFromPlayerId.get(Constant.ERROR).toString());
		} else if(highestRecordGameFromPlayerId != null && highestRecordGameFromPlayerId.has(Constant.GAME)) {
			result = highestRecordGameFromPlayerId;
		} else {
			result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
		}	
		return result;
	}
	
	@GetMapping("leaderboard")
	public ObjectNode getLeaderboard() {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ObjectNode leaderboard = gameService.getGamesWithHigherScore();
		if(leaderboard != null && leaderboard.has(Constant.ERROR)) {
			result.put(Constant.ERROR, leaderboard.get(Constant.ERROR).toString());
		} else if(leaderboard != null && leaderboard.has(Constant.LEADERBOARD)) {
			result = leaderboard;
		} else {
			result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
		}	
		return result;
	}
	
}