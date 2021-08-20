package com.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.User;
import com.project.entity.Wishlist;
import com.project.repository.WishlistRepository;
import com.project.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {
	
	@Autowired
	WishlistRepository wishlistRepository;

	@Override
	public Wishlist save(Wishlist entity) {
		return wishlistRepository.save(entity);
	}

	@Override
	public Optional<Wishlist> findById(Integer id) {
		return wishlistRepository.findById(id);
	}

	@Override
	public void deleteById(Integer id) {
		wishlistRepository.deleteById(id);
	}

	@Override
	public void delete(Wishlist entity) {
		wishlistRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		wishlistRepository.deleteAll();
	}

	@Override
	public Optional<Wishlist> findByProductAndUser(Integer product_id, Integer user_id) {
		return wishlistRepository.findByProductAndUser(product_id, user_id);
	}

	@Override
	public List<Wishlist> findByUser(User user) {
		return wishlistRepository.findByUser(user);
	}
	
	
}
