package com.project.service.impl;

import com.project.entity.OrdersDetail;
import com.project.repository.OrdersDetailRepository;
import com.project.service.OrdersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersDetailServiceImpl implements OrdersDetailService {

    @Autowired
    private OrdersDetailRepository ordersDetailRepository;

    @Override
    public List<OrdersDetail> saveAll(List<OrdersDetail> entities) {
        return ordersDetailRepository.saveAll(entities);
    }
}
