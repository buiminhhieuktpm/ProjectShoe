package com.project.controller.user;

import com.project.entity.*;
import com.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller(value = "checkoutOfUser")
public class CheckoutController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailService ordersDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private SizeService sizeService;

    @GetMapping("/checkout")
    public String getPage(Authentication authentication, HttpSession session,
                          RedirectAttributes redirectAttributes, Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            Optional<User> user = userService.findUserByUserName(authentication.getName());
            model.addAttribute("user", user.get());

            Optional<Cart> cart = cartService.findCartByUserUserName(authentication.getName());
            List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());

            boolean isFlag = true;
            String message = "";
            for(CartDetail cartDetail : list){
                if (cartDetail.getAmount() > cartDetail.getSize().getAmount()){
                    message = "Sản phẩm "+ cartDetail.getProduct().getName() +" chỉ còn "+ cartDetail.getSize().getAmount();
                    isFlag = false;
                    break;
                }
            }
            if (!isFlag){
                redirectAttributes.addFlashAttribute("message",message);
                return "redirect:/cart?error";
            }

            model.addAttribute("listCart", list);
            model.addAttribute("totalCost", cart.get().getTotalCost());
            return "user/checkout";
        }
        model.addAttribute("user", new User());
        List<CartDetail> listCart = (List<CartDetail>) session.getAttribute("listCart");
        if (listCart == null) {
            model.addAttribute("totalCost", 0);
        } else {
            model.addAttribute("totalCost", cartService.totalCost(listCart));

            boolean isFlag = true;
            String message = "";
            for(CartDetail cartDetail : listCart){
                if (cartDetail.getAmount() > cartDetail.getSize().getAmount()){
                    message = "Sản phẩm "+ cartDetail.getProduct().getName() +" chỉ còn "+ cartDetail.getSize().getAmount();
                    isFlag = false;
                    break;
                }
            }
            if (!isFlag){
                redirectAttributes.addFlashAttribute("message",message);
                return "redirect:/cart?error";
            }
        }
        model.addAttribute("listCart", listCart);
        return "user/checkout";
    }

    @PostMapping(value = {"/api/checkout","/api/checkout/{code}"})
    @ResponseBody
    public ResponseEntity<Orders> addOrders(@PathVariable(name = "code", required = false) Optional<String> code, @RequestBody Orders orders) {
        Optional<Coupon> coupon = null;
        if (code.isPresent()) {
            coupon = couponService.findCouponByCode(code.get());
            orders.setCoupon(coupon.get());
            couponService.updateAmount(coupon.get().getAmount() - 1, coupon.get().getId());

        }else{
            orders.setCoupon(null);
        }

        Status status = statusService.findById(1).orElse(null);
        orders.setStatus(status);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            Optional<User> user = userService.findUserByUserName(authentication.getName());
            Optional<Cart> cart = cartService.findCartByUserUserName(authentication.getName());
            List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());

            orders.setUser(user.get());
            //save order
            save(orders, list, orders.getCoupon());
            //delete
            cartDetailService.deleteCart(cart.get().getId());
            //save
            cart.get().setTotalAmount(null);
            cart.get().setTotalCost(null);
            cartService.save(cart.get());
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession(true);
            List<CartDetail> list = (List<CartDetail>) session.getAttribute("listCart");

            orders.setUser(null);
            //save order
            save(orders, list, orders.getCoupon());
            //remove session
            session.removeAttribute("listCart");
        }

        return new ResponseEntity<>(ordersService.save(orders), HttpStatus.OK);
    }

    private void save(Orders orders, List<CartDetail> list, Coupon coupon){
        orders.setTotalAmount(cartService.totalAmount(list));

        Double totalCost = cartService.totalCost(list);
        orders.setTotalCost(totalCost);
        if (coupon != null) {
            orders.setDiscountPrice(totalCost - (totalCost * coupon.getPercent() * 0.01));
        } else {
            orders.setDiscountPrice(0.0);
        }

        Orders ord = ordersService.save(orders);
        List<OrdersDetail> list1 = new ArrayList<>();
        for (CartDetail cartDetail : list){
            OrdersDetail od = new OrdersDetail();
            od.setAmount(cartDetail.getAmount());
            od.setPrice(cartDetail.getPrice());
            od.setProduct(cartDetail.getProduct());
            od.setSize(cartDetail.getSize());
            od.setOrders(ord);
            list1.add(od);
            sizeService.updateAmount(cartDetail.getSize().getAmount() - cartDetail.getAmount(),cartDetail.getSize().getId());
        }
        ordersDetailService.saveAll(list1);

    }

}
