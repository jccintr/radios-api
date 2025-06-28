package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.Radio;

public interface RadioRepository extends JpaRepository<Radio,Long> {

}
