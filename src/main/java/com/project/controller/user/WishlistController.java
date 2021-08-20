package com.project.controller.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.entity.Product;
import com.project.entity.User;
import com.project.entity.Wishlist;
import com.project.service.ProductService;
import com.project.service.UserService;
import com.project.service.WishlistService;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public String addWishlist(@PathVariable Integer id,  Authentication authentication, Model model) {

		authentication = SecurityContextHolder.getContext().getAuthentication();
		// check dang nhap
		// neu chua thi khong add vao wishlist
		if (!authentication.getPrincipal().equals("anonymousUser")) {

			Optional<Product> product = productService.findById(id);
			Optional<User> user = userService.findUserByUserName(authentication.getName());

			if(product.isPresent()) {
				
				Optional<Wishlist> wishlist = wishlistService.findByProductAndUser(product.get().getId(), user.get().getId());
				if(!wishlist.isPresent()) {
					Wishlist wish = new Wishlist();
					wish.setProduct(product.get());
					wish.setUser(user.get());
					wishlistService.save(wish);
				}			
			}else {
				model.addAttribute("mess", "Không tồn tại thể loại này");
				return "error";
			}
		}
		return "redirect:/wishlist";
	}
	
	@GetMapping
	@Secured("ROLE_USER")
	public String showWishlist(Authentication authentication, Model model) {
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
		// check dang nhap
		if (!authentication.getPrincipal().equals("anonymousUser")) {
			
			Optional<User> user = userService.findUserByUserName(authentication.getName());			
			model.addAttribute("listWishlist", wishlistService.findByUser(user.get()));
			return "user/wishlist";
		}
		return "redirect:/";
	}
	
	@GetMapping("/remove/{id}")
	public String removeWishlist(@PathVariable Integer id,  Authentication authentication, Model model) {
						
		authentication = SecurityContextHolder.getContext().getAuthentication();
			
		Optional<Wishlist> wishlist = wishlistService.findById(id);		
			if(wishlist.isPresent()) {
				
				wishlistService.deleteById(id);
				
			}else {
				
				model.addAttribute("mess", "Không tồn tại sản phẩm này này ");
				return "error";
				
			}

		return "redirect:/wishlist";
	}

}
