package com.project.controller.admin;

import com.project.entity.*;
import com.project.service.CouponService;
import com.project.service.OrdersService;
import com.project.service.SizeService;
import com.project.service.StatusService;
import com.project.service.sendmail.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller(value = "ordersOfAdmin")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private SendMail sendMail;

    @GetMapping("/admin/orders")
    private String getPage() {
        return "admin/orders";
    }

    @GetMapping("/api/order/{orderId}")
    @ResponseBody
    public ResponseEntity<Orders> getOrders(@PathVariable(name = "orderId") Integer orderId) {
        Optional<Orders> orders = ordersService.findById(orderId);
        return orders.map(orders1 -> new ResponseEntity<>(orders1, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/api/orders/{pageNo}/{status}")
    @ResponseBody
    public Page<Orders> getAllOrders(@PathVariable(name = "pageNo") Optional<Integer> pageNo, @PathVariable(name = "status") Optional<Integer> statusId) {
        int page = pageNo.orElse(1);
        int status = statusId.orElse(1);
        Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("timeOrder").descending());
        Page<Orders> listOrders = ordersService.findAllByStatus(status, pageable);
        return listOrders;
    }

    @GetMapping("/api/orders/{pageNo}")
    @ResponseBody
    public Page<Orders> getAllDate(@PathVariable(name = "pageNo") Optional<Integer> pageNo, @RequestParam(name = "statusid") Integer statusId,
                                   @RequestParam(name = "startdate") String startdate, @RequestParam(name = "enddate") String enddate) {
        int page = pageNo.orElse(1);
        Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("timeOrder").descending());

        startdate = ordersService.toDate(startdate) + " 00:00:00";
        enddate = ordersService.toDate(enddate) + " 23:59:59";
        Timestamp startD = Timestamp.valueOf(startdate);
        Timestamp endD = Timestamp.valueOf(enddate);

        Page<Orders> listOrders = ordersService.findOrdersByTime(statusId, startD, endD, pageable);
        return listOrders;
    }

    @PutMapping("/api/orders/{ordersId}/{statusId}")
    @ResponseBody
    public ResponseEntity<Orders> updateOrder(@PathVariable(name = "ordersId") Integer ordersId,
                                              @PathVariable(name = "statusId") Integer statusId) {

        Optional<Orders> orders = ordersService.findById(ordersId);
        if (!orders.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if (statusId == 5) {
            List<OrdersDetail> list = orders.get().getOrdersDetails();
            for (OrdersDetail detail : list) {
                sizeService.updateAmount(detail.getSize().getAmount() + detail.getAmount(), detail.getSize().getId());
            }
            if (orders.get().getCoupon() != null) {
                Coupon coupon = orders.get().getCoupon();
                coupon.setAmount(coupon.getAmount() + 1);
                couponService.save(coupon);
            }
        }
        Optional<Status> status = statusService.findById(statusId);
        orders.get().setStatus(status.get());

        if (statusId != 1) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Double tien = (orders.get().getDiscountPrice() == 0) ? orders.get().getTotalCost() : orders.get().getDiscountPrice();
                        String body = "<p> Mã đơn hàng : " + orders.get().getId() + "</p>"
                                + "<p> Trạng thái : " + status.get().getName() + "</p>"
                                + "<p> Tổng thanh toán : " + tien + " VNĐ </p>";
                        sendMail.sendOrders(orders.get().getEmail(), body, orders.get().getName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        ordersService.save(orders.get());

        return new ResponseEntity<>(orders.get(), HttpStatus.OK);
    }

    @DeleteMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> deleteContact(@PathVariable Integer id){
        Orders orders = ordersService.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found Order with " + id));

        List<OrdersDetail> list = orders.getOrdersDetails();
        for (OrdersDetail detail : list) {
            sizeService.updateAmount(detail.getSize().getAmount() + detail.getAmount(), detail.getSize().getId());
        }
        if (orders.getCoupon() != null) {
            Coupon coupon = orders.getCoupon();
            coupon.setAmount(coupon.getAmount() + 1);
            couponService.save(coupon);
        }

        ordersService.cancelOrder(orders.getId(),5);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
