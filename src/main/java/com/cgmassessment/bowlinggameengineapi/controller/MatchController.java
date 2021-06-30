package com.cgmassessment.bowlinggameengineapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.service.MatchService;

@RestController
@RequestMapping("/match/")
public class MatchController {

	@Autowired
	MatchService matchService;
	
	@PostMapping("start")
	public ObjectNode startMatch(@RequestBody Optional<ObjectNode> body){
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		if(body.isPresent() && body.get() != null) {
			if(body.get().has(Constant.PLAYERS)) {
				ArrayNode playersArray = JsonHelper.getAsArray(body.get(), Constant.PLAYERS);
				ObjectNode match = matchService.newMatch(playersArray);
				if(match != null && match.has(Constant.ERROR)) {
					result.put(Constant.ERROR, match.get(Constant.ERROR).toString());
				} else if(match != null && match.has(Constant.MATCH) && match.has(Constant.GAME)) {
					result = match;
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
	
}