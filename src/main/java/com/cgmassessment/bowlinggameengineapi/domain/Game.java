package com.cgmassessment.bowlinggameengineapi.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "game")
public class Game  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3024877538391463529L;

	@Id
	@Column(name = "game_id")
	private String gameId;
	
	@Column(name = "player_id")
	private String playerId;
	
	@Column(name = "game_score")
	private int gameScore;
	
	@Column(name = "game_date")
	private LocalDateTime gameDate;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	public LocalDateTime getGameDate() {
		return gameDate;
	}

	public void setGameDate(LocalDateTime gameDate) {
		this.gameDate = gameDate;
	}
	
}
