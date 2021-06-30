package com.cgmassessment.bowlinggameengineapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Player;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	GameService gameService;
	
	public ObjectNode createNewPlayer(ObjectNode playerValues) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			if(validatePlayerValues(playerValues)) {
				String playerId = UUID.randomUUID().toString();
				Player playerToSave = buildPlayerObjectFromObjectNode(playerId, playerValues);
				playerRepository.save(playerToSave);
				result = buildPlayerObjectNodeFromObject(playerToSave);
			} else {
				result.put(Constant.ERROR, "Player data format input is wrong");
			}
		} catch(Exception e){
			System.out.println("Creation of new player went wrong" + e.toString());
		}
		return result;
	}
	
	public ObjectNode editPlayerNickname(String playerId, String newNickname) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Player playerToEdit = playerRepository.findFirstByPlayerId(playerId);
			if(playerToEdit != null) {
				String playerOldNickname = playerToEdit.getNickname();
				playerToEdit.setNickname(newNickname);
				playerRepository.save(playerToEdit);
				result = buildPlayerObjectNodeFromObject(playerToEdit);
				result.put(Constant.OLD_NICKNAME, playerOldNickname);
			} else {
				result.put(Constant.ERROR, "No player with given player id found");
			}
		} catch(Exception e){
			System.out.println("Changing nickname of player " + playerId + " went wrong" + e.toString());
		}
		return result;
	}
	
	public ObjectNode getPlayerWithHigherScoreAndGame() {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			ObjectNode bestScoreGame = gameService.getGameWithHigherScore();
			if(bestScoreGame != null && bestScoreGame.has(Constant.GAME_ID) && bestScoreGame.has(Constant.PLAYER_ID)) {
				//ObjectNode bestScoreGameObject = JsonHelper.getAsObject(bestScoreGame, Constant.GAME);
				//if(bestScoreGameObject.has(Constant.PLAYER_ID)) {
					String playerId = JsonHelper.getString(bestScoreGame, Constant.PLAYER_ID);
					if(playerId != null) {
						Player playerWithRecord = playerRepository.findFirstByPlayerId(playerId);
						if(playerWithRecord != null) {
							result.set(Constant.PLAYER, buildPlayerObjectNodeFromObject(playerWithRecord));
							result.set(Constant.GAME, bestScoreGame);
						} else {
							result.put(Constant.ERROR, "No player with saved player id in best game");
						}
					} else {
						result.put(Constant.ERROR, "Error on player id");
					}
				//} else {
					//result.put(Constant.ERROR, "Best score game has no player id");
				//}
			} else if(bestScoreGame != null && bestScoreGame.has(Constant.ERROR)) {
				result.put(Constant.ERROR, bestScoreGame.get(Constant.ERROR).toString());
			} else {
				result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
			}
		} catch(Exception e){
			System.out.println("Getting player with higher score went wrong" + e.toString());
		}
		return result;
	}
	
	public ObjectNode getPlayerFromId(String playerId) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Player player = playerRepository.findFirstByPlayerId(playerId);
			if(player != null) {
				result.set(Constant.PLAYER, buildPlayerObjectNodeFromObject(player));
			} else {
				result.put(Constant.ERROR, "No player for given player id");
			}
		} catch(Exception e){
			System.out.println("Getting player with higher score went wrong" + e.toString());
		}
		return result;
	}
	
	private boolean validatePlayerValues(ObjectNode playerValues) {
		if(playerValues.has(Constant.NAME) && JsonHelper.getString(playerValues, Constant.NAME) != null 
				&& playerValues.has(Constant.SURNAME) && JsonHelper.getString(playerValues, Constant.SURNAME) != null 
				&& playerValues.has(Constant.NICKNAME) && JsonHelper.getString(playerValues, Constant.NICKNAME) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private Player buildPlayerObjectFromObjectNode(String playerId, ObjectNode playerValues) {
		Player playerToBuild = new Player();
		playerToBuild.setPlayerId(playerId);
		playerToBuild.setName(JsonHelper.getString(playerValues, Constant.NAME));
		playerToBuild.setSurname(JsonHelper.getString(playerValues, Constant.SURNAME));
		playerToBuild.setNickname(JsonHelper.getString(playerValues, Constant.NICKNAME));
		playerToBuild.setSignUpDate(LocalDateTime.now());
		return playerToBuild;
	}
	
	private ObjectNode buildPlayerObjectNodeFromObject(Player player) {
		ObjectNode playerObjectNode = JsonHelper.getEmptyObjectNode();
		playerObjectNode.put(Constant.PLAYER_ID, player.getPlayerId());
		playerObjectNode.put(Constant.NAME, player.getName());
		playerObjectNode.put(Constant.SURNAME, player.getSurname());
		playerObjectNode.put(Constant.NICKNAME, player.getNickname());
		playerObjectNode.put(Constant.SIGN_UP_DATE, player.getSignUpDate().toString());
		return playerObjectNode;
	}
	
}