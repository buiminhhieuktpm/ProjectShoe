package com.project.repository;

import com.project.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
}
