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
	
	@Column(name = "match_date")
	private LocalDateTime birthDate;

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.birthDate = birthDate;
	}

}
