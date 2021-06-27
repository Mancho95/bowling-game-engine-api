package com.cgmassessment.bowlinggameengineapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgmassessment.bowlinggameengineapi.domain.Frame;


public interface FrameRepository extends JpaRepository<Frame, String> {
	
	List<Frame> findAllByGameIdOrderByFrameIndex(String gameId);
	
	Frame findFirstByFrameId(String frameId);
	
  }