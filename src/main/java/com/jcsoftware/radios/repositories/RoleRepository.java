package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	
	Role findByAuthority(String authority); 

}
