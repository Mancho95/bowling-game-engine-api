package com.cgmassessment.bowlinggameengineapi.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "match")
public class Match  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5971372164028363643L;

	@Id
	@Column(name = "match_id")
	private String matchId;
	
	@Column(name = "match_winner_id")
	private String matchWinnerId;
	
	@Column(name = "match_date")
	private LocalDateTime matchDate;

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	
	public String getMatchWinnerId() {
		return matchId;
	}

	public void setMatchWinnerId(String matchWinnerId) {
		this.matchWinnerId = matchWinnerId;
	}

	public LocalDateTime getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDateTime matchDate) {
		this.matchDate = matchDate;
	}

}
