package com.project.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.entity.Cart;
import com.project.entity.CartDetail;
import com.project.entity.Product;
import com.project.entity.Size;
import com.project.entity.User;
import com.project.service.CartDetailService;
import com.project.service.CartService;
import com.project.service.ProductService;
import com.project.service.SizeService;
import com.project.service.UserService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public String getPage(Authentication authentication, HttpSession session, Model model){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")){
            Optional<User> user = userService.findUserByUserName(authentication.getName());

            Optional<Cart> cart = cartService.findCartByUserId(user.get().getId());
            if (cart.isPresent()) {
                List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());
                model.addAttribute("listCart",list);
                model.addAttribute("totalCost",cart.get().getTotalCost());
            }
        }else{
            List<CartDetail> listCart = (List<CartDetail>) session.getAttribute("listCart");
            if (listCart == null) {
                model.addAttribute("totalCost", 0);
            } else {
                model.addAttribute("totalCost", cartService.totalCost(listCart));
            }
            if (model.asMap().get("message") != null){
                model.addAttribute("erroramount", model.asMap().get("message").toString());
            }
            model.addAttribute("listCart", listCart);
        }
        return "user/shop-cart";
    }

    @PostMapping(value = "/add")
    public String addCartTets(@RequestParam Integer productId, @RequestParam Integer amount,
                              @RequestParam Integer sizeId, Authentication authentication,
                              HttpSession session, Model model) {

        Product product = productService.findById(productId).orElse(null);
        Optional<Size> size = sizeService.findById(sizeId);

        authentication = SecurityContextHolder.getContext().getAuthentication();
        //xu ly database
        if (!authentication.getPrincipal().equals("anonymousUser")){

            Optional<User> user = userService.findUserByUserName(authentication.getName());

            Optional<Cart> cart = cartService.findCartByUserId(user.get().getId());
            if (cart.isPresent()){
                List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());

                boolean existItem = false;
                if (!list.isEmpty()){
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getProduct().getId() == productId) {
                            if (list.get(i).getSize().getId() == sizeId){
                                list.get(i).setAmount(list.get(i).getAmount() + amount);
                                existItem = true;
                                break;
                            }
                        }
                    }
                }

                if (!existItem){
                    CartDetail cartDetail = addCart(product,size,amount);
                    cartDetail.setCart(cart.get());
                    list.add(cartDetail);
                }
                cart.get().setTotalAmount(cartService.totalAmount(list));
                cart.get().setTotalCost(cartService.totalCost(list));
                cartService.save(cart.get());
                cartDetailService.saveAll(list);
                model.addAttribute("listCart",cartDetailService.findAllByCartId(cart.get().getId()));
                model.addAttribute("totalCost",cart.get().getTotalCost());
            }else{
                Cart cartNew = new Cart();
                cartNew.setUser(user.orElse(null));
                cartNew.setTotalCost(product.getPrice() * amount);
                cartNew.setTotalAmount(amount);
                Cart newCart = cartService.save(cartNew);

                CartDetail cartDetail = addCart(product,size,amount);
                cartDetail.setCart(newCart);

                model.addAttribute("listCart",cartDetailService.findAllByCartId(newCart.getId()));
                model.addAttribute("totalCost",cartNew.getTotalCost());
                cartDetailService.save(cartDetail);
            }

            return "user/shop-cart";
        }

        //xu ly session
        List<CartDetail> listCart = (List<CartDetail>) session.getAttribute("listCart");
        if (listCart == null) {
            //init map
            listCart = new ArrayList<>();
            //add
            listCart.add(addCart(product, size, amount));
        } else {
            boolean existItem = false;

            for (int i = 0; i < listCart.size(); i++) {
                if (listCart.get(i).getProduct().getId() == productId) {
                    if (listCart.get(i).getSize().getId() == sizeId){
                        listCart.get(i).setAmount(listCart.get(i).getAmount() + amount);
                        existItem = true;
                        break;
                    }
                }
            }

            if (!existItem)
                listCart.add(addCart(product, size, amount));
        }
        session.setAttribute("listCart", listCart);
        session.setAttribute("totalCost", cartService.totalCost(listCart));
        model.addAttribute("listCart",listCart);
        model.addAttribute("totalCost",cartService.totalCost(listCart));

        return "user/shop-cart";
    }

    private CartDetail addCart(Product product, Optional<Size> size, Integer amount){
        CartDetail cartDetail = new CartDetail();
        cartDetail.setPrice(product.getPrice());
        cartDetail.setAmount(amount);
        cartDetail.setProduct(product);
        if (size.isPresent())
            cartDetail.setSize(size.orElse(null));
        return cartDetail;
    }

    @GetMapping(value = "/update")
    public String updateCart(Authentication authentication,HttpSession session, HttpServletRequest request, Model model) {

        String[] itemUpdate = request.getParameterValues("amount");

        authentication = SecurityContextHolder.getContext().getAuthentication();
        //authenticated
        if (!authentication.getPrincipal().equals("anonymousUser")){
            Optional<User> user = userService.findUserByUserName(authentication.getName());

            Optional<Cart> cart = cartService.findCartByUserId(user.get().getId());
            if (cart.isPresent()){
                List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());
                if (!list.isEmpty()){
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setAmount(Integer.parseInt(itemUpdate[i]));
                    }

                    cartDetailService.saveAll(list);
                    cart.get().setTotalAmount(cartService.totalAmount(list));
                    cart.get().setTotalCost(cartService.totalCost(list));
                    cartService.save(cart.get());

                    model.addAttribute("listCart",list);
                    model.addAttribute("totalCost",cart.get().getTotalCost());
                }else{
                    model.addAttribute("totalCost",cartService.totalCost(list));
                }
            }
            return "user/shop-cart";
        }

        //non-authenticated
        List<CartDetail> listCart = (List<CartDetail>) session.getAttribute("listCart");
        if(listCart != null) {
            for (int i = 0; i < listCart.size(); i++) {
                listCart.get(i).setAmount(Integer.parseInt(itemUpdate[i]));
            }
            session.setAttribute("listCart", listCart);
            model.addAttribute("totalCost", cartService.totalCost(listCart));
        }else {
            model.addAttribute("totalCost", 0);
        }
        model.addAttribute("listCart", listCart);
        return "user/shop-cart";
    }

    @RequestMapping(value = "/remove/{id}")
    public String deleteItems(@PathVariable(value = "id") int id, @RequestParam(name = "size", required = false) Integer size_id,
                            Authentication authentication, Model model, HttpSession session) {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        //authenticated
        if (!authentication.getPrincipal().equals("anonymousUser")){
            cartDetailService.deleteById(id);

            Optional<Cart> cart = cartService.findCartByUserUserName(authentication.getName());
            List<CartDetail> list = cartDetailService.findAllByCartId(cart.get().getId());
            cart.get().setTotalCost(cartService.totalCost(list));
            cart.get().setTotalAmount(cartService.totalAmount(list));
            cartService.save(cart.get());

            return "redirect:/cart";
        }

        //process session
        List<CartDetail> listCart = (List<CartDetail>) session.getAttribute("listCart");
        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).getProduct().getId() == id && listCart.get(i).getSize().getId() == size_id) {
                listCart.remove(i);
                break;
            }
        }
        session.setAttribute("listCart", listCart);

        return "redirect:/cart";
    }


}
