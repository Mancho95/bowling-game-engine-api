package com.cgmassessment.bowlinggameengineapi.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "attempt")
public class Attempt  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8437476706706877529L;

	@Id
	@Column(name = "attempt_id")
	private String attemptId;
	
	@Column(name = "frame_id")
	private String frameId;
	
	@Column(name = "knocked_pins")
	private int knockedPins;
	
	@Column(name = "attempt_number")
	private int attemptNumber;

	public String getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public int getKnockedPins() {
		return knockedPins;
	}

	public void setKnockedPins(int knockedPins) {
		this.knockedPins = knockedPins;
	}

	public int getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(int attemptNumber) {
		this.attemptNumber = attemptNumber;
	}

}
