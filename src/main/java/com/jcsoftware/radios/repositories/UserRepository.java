package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
	public User findByEmail(String email);

}
