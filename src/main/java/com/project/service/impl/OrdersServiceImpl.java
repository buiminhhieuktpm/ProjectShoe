package com.project.service.impl;

import com.project.entity.Orders;
import com.project.repository.OrdersRepository;
import com.project.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Orders save(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Orders> findById(Integer id) {
        Optional<Orders> orders = ordersRepository.findById(id);
        if (orders.isPresent()){
            orders.get().getOrdersDetails().forEach(ordersDetail -> {
                ordersDetail.getProduct();
                ordersDetail.getSize();
            });
        }
        return orders;
    }

    @Override
    public Page<Orders> findAllByStatus(Integer status, Pageable pageable) {
        return ordersRepository.findAllByStatusId(status, pageable);
    }

    @Override
    public Page<Orders> findOrdersByTime(Integer status,Timestamp startdate, Timestamp enddate, Pageable pageable) {
        return ordersRepository.findOrdersByTime(status,startdate,enddate,pageable);
    }

    @Override
    public void cancelOrder(Integer ordersId, Integer statusId) {
        ordersRepository.cancelOrder(ordersId,statusId);
    }

    public String toDate(String str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String startDateString = sdf2.format(sdf.parse(str));
            return startDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
