package com.cgmassessment.bowlinggameengineapi.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "player")
public class Player  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 167909633533686686L;

	@Id
	@Column(name = "player_id")
	private String playerId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "surname")
	private Integer surname;
	
	@Column(name = "birth_date")
	private LocalDateTime birthDate;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSurname() {
		return surname;
	}

	public void setSurname(Integer surname) {
		this.surname = surname;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.birthDate = birthDate;
	}

}
