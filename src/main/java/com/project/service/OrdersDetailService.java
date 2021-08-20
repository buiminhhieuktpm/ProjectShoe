package com.project.service;

import com.project.entity.OrdersDetail;

import java.util.List;

public interface OrdersDetailService {

    List<OrdersDetail> saveAll(List<OrdersDetail> entities);
}
