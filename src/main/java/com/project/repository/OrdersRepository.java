package com.project.repository;

import com.project.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    Page<Orders> findAllByStatusId(Integer status, Pageable pageable);

    @Query("select distinct o from Orders o where o.status.id = :status and o.timeOrder between :startdate and :enddate")
    Page<Orders> findOrdersByTime(@Param("status") Integer status,@Param("startdate") Timestamp startdate,@Param("enddate") Timestamp enddate, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status.id = :statusId WHERE o.id = :ordersId")
    void cancelOrder(@Param("ordersId") Integer ordersId, @Param("statusId") Integer statusId);
}
