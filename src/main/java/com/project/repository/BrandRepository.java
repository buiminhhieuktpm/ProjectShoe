package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
	// Query tim kieu theo ten
		@Query(value = "select * from brand where name = ?1", nativeQuery = true)
			public Optional<Brand> findByName(String name);

}
