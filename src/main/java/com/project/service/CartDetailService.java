package com.project.service;

import java.util.List;

import com.project.entity.CartDetail;

public interface CartDetailService {

    CartDetail save(CartDetail cartDetail);

    List<CartDetail> saveAll(List<CartDetail> entities);

    List<CartDetail> findAllByCartId(Integer id);

    void deleteById(Integer id);

    void deleteCart(Integer id);

}
