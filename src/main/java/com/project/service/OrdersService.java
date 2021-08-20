package com.project.service;

import com.project.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Optional;

public interface OrdersService {

    Orders save(Orders orders);

    Optional<Orders> findById(Integer id);

    Page<Orders> findAllByStatus(Integer status, Pageable pageable);

    Page<Orders> findOrdersByTime(Integer status,Timestamp startdate, Timestamp enddate, Pageable pageable);

    void cancelOrder(Integer ordersId,Integer statusId);

    String toDate(String str);

}
