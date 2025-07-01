package com.jcsoftware.radios.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.RadioList;

public interface RadioListRepository extends JpaRepository<RadioList,Long>{
	
	List<RadioList> findByOwnerId(Long userId);

}
