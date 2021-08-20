package com.project.service.impl;

import com.project.entity.Coupon;
import com.project.repository.CouponRepository;
import com.project.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public Coupon getById(Integer id) {
        return couponRepository.getById(id);
    }

    @Override
    public void deleteAll() {
        couponRepository.deleteAll();
    }

    @Override
    public void delete(Coupon entity) {
        couponRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) {
        couponRepository.deleteById(id);
    }

    @Override
    public long count() {
        return couponRepository.count();
    }

    @Override
    public boolean existsById(Integer id) {
        return couponRepository.existsById(id);
    }

    @Override
    public Optional<Coupon> findById(Integer id) {
        return couponRepository.findById(id);
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon save(Coupon entity) {
        return couponRepository.save(entity);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    @Override
    public Optional<Coupon> findCouponByCode(String code) {
        Optional<Coupon> coupon = couponRepository.findCode(code);
        return coupon;
    }

    @Override
    public void updateAmount(Integer amount, Integer id) {
        couponRepository.updateAmount(amount, id);
    }
}
