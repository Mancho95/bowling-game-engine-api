package com.cgmassessment.bowlinggameengineapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.service.PlayerService;

@RestController
@RequestMapping("/player/")
public class PlayerController {

	@Autowired
	PlayerService playerService;
	
	@PostMapping("signup")
	public ObjectNode signUpNewPlayer(@RequestBody Optional<ObjectNode> body){
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		if(body.isPresent() && body.get() != null) {
			if(body.get().has(Constant.PLAYER)) {
				ObjectNode playerValues = JsonHelper.getAsObject(body.get(), Constant.PLAYER);
				ObjectNode savedPlayer = playerService.createNewPlayer(playerValues);
				if(savedPlayer != null && savedPlayer.has(Constant.ERROR)) {
					result.put(Constant.ERROR, savedPlayer.get(Constant.ERROR).toString());
				} else if(savedPlayer != null && savedPlayer.has(Constant.PLAYER)) {
					result = savedPlayer;
				} else {
					result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
				}
			} else {
				result.put(Constant.ERROR, "Body of request has no player object");
			}
		} else {
			result.put(Constant.ERROR, "Body of request is empty");
		}
		return result;
	}
	
	@GetMapping("change/{playerId}/{newNickname}")
	public ObjectNode editPlayerNickname(@PathVariable String playerId, @PathVariable String newNickname){
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ObjectNode editedPlayer = playerService.editPlayerNickname(playerId, newNickname);
		if(editedPlayer != null && editedPlayer.has(Constant.ERROR)) {
			result.put(Constant.ERROR, editedPlayer.get(Constant.ERROR).toString());
		} else if(editedPlayer != null && editedPlayer.has(Constant.PLAYER)) {
			result = editedPlayer;
		} else {
			result.put(Constant.ERROR, Constant.UNEXPECTED_ERROR);
		}
		return result;
	}
	
}