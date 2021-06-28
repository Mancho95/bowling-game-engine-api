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
				playerToEdit.setNickname(newNickname);
				playerRepository.save(playerToEdit);
				result = buildPlayerObjectNodeFromObject(playerToEdit);
			} else {
				result.put(Constant.ERROR, "No player with given player id found");
			}
		} catch(Exception e){
			System.out.println("Changing nickname of player " + playerId + " went wrong" + e.toString());
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
		ObjectNode values = JsonHelper.getEmptyObjectNode();
		values.put(Constant.PLAYER_ID, player.getPlayerId());
		values.put(Constant.NAME, player.getName());
		values.put(Constant.SURNAME, player.getSurname());
		values.put(Constant.NICKNAME, player.getNickname());
		values.put(Constant.SIGN_UP_DATE, player.getSignUpDate().toString());
		playerObjectNode.set(Constant.PLAYER, values);
		return playerObjectNode;
	}
	
}