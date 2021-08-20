package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.entity.User;
import com.project.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
	
	@Query(value = "Select * from wishlist where product_id = ?1 and user_id = ?2 ", nativeQuery = true)
	Optional<Wishlist> findByProductAndUser(Integer product_id, Integer user_id);
	
	List<Wishlist> findByUser(User user);
}
