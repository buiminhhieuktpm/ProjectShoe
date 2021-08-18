package com.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Cart;
import com.project.entity.CartDetail;
import com.project.repository.CartRepository;
import com.project.service.CartService;

@Service
public class CartImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;

	@Override
	public double totalCost(List<CartDetail> listCart) {
		float totalCost = 0;
		for (CartDetail cartDetail : listCart) {
			totalCost += cartDetail.getAmount()*cartDetail.getPrice();
		}
		return totalCost;
	}

	@Override
	public int totalAmount(List<CartDetail> listCart) {
		int totalAmount = 0;
		for (CartDetail cartDetail : listCart) {
			totalAmount += cartDetail.getAmount();
		}
		return totalAmount;
	}

	@Override
	public Optional<Cart> findCartByUserId(Integer id) {
		return cartRepository.findCartByUserId(id);
	}

	@Override
	public Optional<Cart> findCartByUserUserName(String username) {
		return cartRepository.findCartByUserUserName(username);
	}

	@Override
	public Cart save(Cart cart) {
		return cartRepository.save(cart);
	}

}
