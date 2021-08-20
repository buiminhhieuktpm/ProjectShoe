package com.project.controller.admin;

import com.project.entity.Coupon;
import com.project.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller(value = "couponOfAdmin")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @GetMapping("/admin/coupon")
    public String categoryHome(Model model) {
        model.addAttribute("list", couponService.findAll());
        return "admin/coupon";
    }

    @GetMapping("/admin/coupon/addcoupon")
    public String addOrEdit(Model model) {
        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);
        return "admin/addcoupon";
    }


    @PostMapping("/admin/coupon/savecoupon")
    public String addOrUpdate(Model model, @ModelAttribute("coupon") Coupon coupon, @RequestParam String start, @RequestParam String end) {
        try {
            start = start + " 00:00:00";
            end = end + " 00:00:00";
            java.sql.Timestamp startD = java.sql.Timestamp.valueOf(start);
            java.sql.Timestamp endD = java.sql.Timestamp.valueOf(end);
            coupon.setStartDate(startD);
            coupon.setEndDate(endD);
            // them ma giam gia
            if (coupon.getId() == null) {
                if (couponService.findCouponByCode(coupon.getCode()).isPresent()) {
                    // da ton tai ma code = > k save được
                    model.addAttribute("messAdd", "Thêm mã giảm giá thất bại");
                    return "redirect:/admin/coupon/addcoupon";
                } else {
                    couponService.save(coupon);
                }
            }
            // sua ma giam gia
            else {
                couponService.save(coupon);
            }
        } catch (Exception e) {
            return "redirect:/admin/coupon";
        }
        return "redirect:/admin/coupon";
    }

    // Delete Category
    @GetMapping("/admin/coupon/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        couponService.deleteById(id);
        return "redirect:/admin/coupon";
    }

    @GetMapping("/admin/coupon/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        Optional<Coupon> cou = couponService.findById(id);
        // Check Category Exit or not
        if (cou.isPresent()) {
            // Exist
            String statDate = cou.get().getStartDate().toString().split(" ")[0];
            String endDate = cou.get().getEndDate().toString().split(" ")[0];
            model.addAttribute("coupon", cou.get());
            model.addAttribute("startD", statDate);
            model.addAttribute("endD", endDate);
            return "admin/editcoupon";
        } else {
            // Not Exist
            // Error Page
            model.addAttribute("mess", "Không tồn tại mã giảm giá này");
            return "error";
        }
    }

    @GetMapping("/api/coupon/{code}")
    @ResponseBody
    public ResponseEntity<Coupon> checkCoupon(@PathVariable(value = "code") String code){
        Optional<Coupon> coupon = couponService.findCouponByCode(code);
        return coupon.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
