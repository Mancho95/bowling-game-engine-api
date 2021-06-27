package com.cgmassessment.bowlinggameengineapi.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "frame")
public class Frame  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2218309118027111863L;

	@Id
	@Column(name = "frame_id")
	private String frameId;
	
	@Column(name = "game_id")
	private String gameId;
	
	@Column(name = "frame_score")
	private int frameScore;
	
	@Column(name = "frame_type")
	private String frameType;
	
	@Column(name = "frame_index")
	private int frameIndex;

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getFrameScore() {
		return frameScore;
	}

	public void setFrameScore(int frameScore) {
		this.frameScore = frameScore;
	}

	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}	

}
