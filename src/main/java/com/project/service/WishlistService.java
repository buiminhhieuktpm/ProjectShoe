package com.project.service;

import java.util.List;
import java.util.Optional;

import com.project.entity.User;
import com.project.entity.Wishlist;

public interface WishlistService {

	void deleteAll();

	void delete(Wishlist entity);

	void deleteById(Integer id);

	Optional<Wishlist> findById(Integer id);

	Wishlist save(Wishlist entity);
	
	Optional<Wishlist> findByProductAndUser(Integer product_id, Integer user_id);
	
	List<Wishlist> findByUser(User user);

}
