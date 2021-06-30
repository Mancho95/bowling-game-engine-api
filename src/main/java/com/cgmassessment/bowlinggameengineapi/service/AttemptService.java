package com.cgmassessment.bowlinggameengineapi.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Attempt;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.AttemptRepository;

@Service
public class AttemptService {

	@Autowired
	AttemptRepository attemptRepository;
	
	public ObjectNode newAttempt(String frameId, int previousKnockedPins, int attemptIndex) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		try {
			Attempt newAttemptForFrameId = new Attempt();
			Random random = new Random();
			String attemptId = UUID.randomUUID().toString();
		    int shot = random.nextInt(Constant.NUMBER_OF_PINS - previousKnockedPins + 1);
		    newAttemptForFrameId.setAttemptId(attemptId);
		    newAttemptForFrameId.setAttemptIndex(attemptIndex);
		    newAttemptForFrameId.setFrameId(frameId);
		    newAttemptForFrameId.setKnockedPins(shot);
		    attemptRepository.save(newAttemptForFrameId);
		    result = buildAttemptObjectNodeFromObject(newAttemptForFrameId);
		} catch(Exception e) {
			System.out.println("Creating a new attempt went wrong" + e.toString());
		}
		return result;
	}
	
	private ObjectNode buildAttemptObjectNodeFromObject(Attempt attempt) {
		ObjectNode attemptObjectNode = JsonHelper.getEmptyObjectNode();
		attemptObjectNode.put(Constant.ATTEMPT_ID, attempt.getAttemptId());
		attemptObjectNode.put(Constant.FRAME_ID, attempt.getFrameId());
		attemptObjectNode.put(Constant.KNOCKED_PINS, attempt.getKnockedPins());
		attemptObjectNode.put(Constant.ATTEMPT_INDEX, attempt.getAttemptIndex());
		return attemptObjectNode;
	}
	
}