package com.project.controller.admin;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.entity.Coupon;
import com.project.service.CouponService;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController {
        @Autowired
        private CouponService couponService;

        @GetMapping("")
        public String categoryHome(Model model) {
            model.addAttribute("list", couponService.findAll());
            return "admin/coupon";
        }

    @GetMapping("/addcoupon")
    public String addOrEdit(Model model) {
        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);
        return "admin/addcoupon";
    }



    @PostMapping("/savecoupon")
    public String addOrUpdate(Model model, @ModelAttribute("coupon") Coupon coupon, @RequestParam String start,@RequestParam String end) {

            try {
                start = start+" 00:00:00" ;
                end = end+" 00:00:00" ;
                java.sql.Timestamp startD = java.sql.Timestamp.valueOf( start);
                java.sql.Timestamp endD = java.sql.Timestamp.valueOf( end ) ;
                coupon.setStartDate(startD);
                coupon.setEndDate(endD);
                // them ma giam gia
                if(coupon.getId() == null){
                    if(couponService.findByCode(coupon.getCode()).isPresent()){
                        // da ton tai ma code = > k save được
                        model.addAttribute("messAdd","Thêm mã giảm giá thất bại");
                        return "redirect:/admin/coupon/addcoupon";
                    }else{
                        couponService.save(coupon);
                    }
                }
                // sua ma giam gia
                else{
                     couponService.save(coupon);
                }
            }catch (Exception e){
                return "redirect:/admin/coupon";
            }
            return "redirect:/admin/coupon";
    }

    // Delete Category
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        couponService.deleteById(id);
        return "redirect:/admin/coupon";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        Optional<Coupon> cou = couponService.findById(id);
        // Check Category Exit or not
        if (cou.isPresent()) {
            // Exist
            String statDate = cou.get().getStartDate().toString().split(" ")[0];
            String endDate = cou.get().getEndDate().toString().split(" ")[0];
            model.addAttribute("coupon", cou.get());
            model.addAttribute("startD",statDate);
            model.addAttribute("endD",endDate);
            return "admin/editcoupon";
        } else {
            // Not Exist
            // Error Page
            model.addAttribute("mess", "Không tồn tại mã giảm giá này");
            return "error";
        }
    }
}
