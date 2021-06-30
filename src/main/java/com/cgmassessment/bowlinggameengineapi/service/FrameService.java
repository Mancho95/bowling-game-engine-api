package com.cgmassessment.bowlinggameengineapi.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cgmassessment.bowlinggameengineapi.Constant;
import com.cgmassessment.bowlinggameengineapi.domain.Frame;
import com.cgmassessment.bowlinggameengineapi.helper.JsonHelper;
import com.cgmassessment.bowlinggameengineapi.repository.FrameRepository;

@Service
public class FrameService {

	@Autowired
	FrameRepository frameRepository;
	
	@Autowired
	AttemptService attemptService;
	
	public ObjectNode newFrame(String gameId, int frameIndex) {
		ObjectNode result = JsonHelper.getEmptyObjectNode();
		ArrayNode attempts = JsonHelper.getEmptyArrayNode();
		Frame newFrameForGameId = new Frame();
		String frameId = UUID.randomUUID().toString();
		newFrameForGameId.setFrameIndex(frameIndex);
		newFrameForGameId.setFrameId(frameId);
		newFrameForGameId.setGameId(gameId);
		if(frameIndex == Constant.NUMBER_OF_FRAMES) {
			newFrameForGameId.setFrameType(Constant.TRIPLE);
			ObjectNode attempt1 = attemptService.newAttempt(frameId, 0, 1);
			attempts.add(attempt1);
			if(attempt1 != null && attempt1.has(Constant.KNOCKED_PINS)) {
				int previousKnockedPins = JsonHelper.getInt(attempt1, Constant.KNOCKED_PINS);
				ObjectNode attempt2 = JsonHelper.getEmptyObjectNode();
				if(previousKnockedPins == Constant.NUMBER_OF_PINS) {
					attempt2 = attemptService.newAttempt(frameId, 0, 2);
					attempts.add(attempt2);
				} else {
					attempt2 = attemptService.newAttempt(frameId, previousKnockedPins, 2);
					attempts.add(attempt2);
				}
				if(attempt2 != null && attempt2.has(Constant.KNOCKED_PINS)) {
					int actualKnockedPins = JsonHelper.getInt(attempt2, Constant.KNOCKED_PINS);
					ObjectNode attempt3 = JsonHelper.getEmptyObjectNode();
					if((actualKnockedPins + previousKnockedPins) == Constant.NUMBER_OF_PINS || actualKnockedPins == Constant.NUMBER_OF_PINS && previousKnockedPins == Constant.NUMBER_OF_PINS) {
						attempt3 = attemptService.newAttempt(frameId, 0, 3);
						attempts.add(attempt3);
					} else if(previousKnockedPins == Constant.NUMBER_OF_PINS && actualKnockedPins < Constant.NUMBER_OF_PINS) {
						attempt3 = attemptService.newAttempt(frameId, actualKnockedPins, 0);
						attempts.add(attempt3);
					}
				} else {
					result.put(Constant.ERROR, "Error on making second shot");
				}
				
			} else {
				result.put(Constant.ERROR, "Error on making first shot");
			}
		} else {
			ObjectNode attempt1 = attemptService.newAttempt(frameId, 0, 1);
			attempts.add(attempt1);
			if(attempt1 != null && attempt1.has(Constant.KNOCKED_PINS)) {
				int previousKnockedPins = JsonHelper.getInt(attempt1, Constant.KNOCKED_PINS);
				if(previousKnockedPins == Constant.NUMBER_OF_PINS) {
					newFrameForGameId.setFrameType(Constant.STRIKE);
				} else {
					ObjectNode attempt2 = attemptService.newAttempt(frameId, previousKnockedPins, 2);
					attempts.add(attempt2);
					if(attempt2 != null && attempt2.has(Constant.KNOCKED_PINS)) {
						int actualKnockedPins = JsonHelper.getInt(attempt2, Constant.KNOCKED_PINS);
						if((actualKnockedPins + previousKnockedPins) == Constant.NUMBER_OF_PINS) {
							newFrameForGameId.setFrameType(Constant.SPARE);
						} else {
							newFrameForGameId.setFrameType(Constant.NORMAL);
						}
					} else {
						result.put(Constant.ERROR, "Error on making second shot");
					}
				}
			} else {
				result.put(Constant.ERROR, "Error on making first shot");
			}
		}
		frameRepository.save(newFrameForGameId);
		result.set(Constant.FRAME, buildFrameObjectNodeFromObject(newFrameForGameId));
		result.set(Constant.ATTEMPTS, attempts);
		return result;
	}
	
	public void saveFrameScore(String frameId, int score) {
		try {
			Frame frameToSave = frameRepository.findFirstByFrameId(frameId);
			frameToSave.setFrameScore(score);
			frameRepository.save(frameToSave);
		} catch(Exception e) {
			System.out.println("Error on saving frame score" + e.toString());
		}
	}
	
	private ObjectNode buildFrameObjectNodeFromObject(Frame frame) {
		ObjectNode frameObjectNode = JsonHelper.getEmptyObjectNode();
		frameObjectNode.put(Constant.FRAME_ID, frame.getFrameId());
		frameObjectNode.put(Constant.GAME_ID, frame.getGameId());
		frameObjectNode.put(Constant.FRAME_SCORE, frame.getFrameScore());
		frameObjectNode.put(Constant.FRAME_TYPE, frame.getFrameType());
		frameObjectNode.put(Constant.FRAME_INDEX, frame.getFrameIndex());;
		return frameObjectNode;
	}
	
}