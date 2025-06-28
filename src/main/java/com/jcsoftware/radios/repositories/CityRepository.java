package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.City;

public interface CityRepository extends JpaRepository<City,Long> {

}
