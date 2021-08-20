package com.project.service.impl;

import com.project.entity.CartDetail;
import com.project.repository.CartDetailRepository;
import com.project.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public CartDetail save(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @Override
    public List<CartDetail> findAllByCartId(Integer id) {
        return cartDetailRepository.findAllByCartId(id);
    }

    @Override
    public  List<CartDetail> saveAll(List<CartDetail> entities) {
        return cartDetailRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Integer id) {
        cartDetailRepository.deleteById(id);
    }

    @Override
    public void deleteCart(Integer id) {
        cartDetailRepository.deleteCart(id);
    }


}
