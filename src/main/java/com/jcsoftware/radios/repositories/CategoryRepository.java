package com.jcsoftware.radios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.radios.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
