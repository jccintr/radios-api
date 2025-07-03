package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.ListItem;

public interface ListItemRepository extends JpaRepository<ListItem,Long>{
	
	

}
