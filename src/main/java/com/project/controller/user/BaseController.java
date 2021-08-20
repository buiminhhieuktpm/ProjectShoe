package com.project.controller.user;

import javax.servlet.http.HttpSession;

import com.project.entity.Banner;
import com.project.repository.BannerRepository;
import com.project.repository.RoleRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.entity.Role;
import com.project.entity.User;
import com.project.service.BannerService;
import com.project.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BannerRepository bannerRepository;

	@GetMapping("/login")
	public String getPageLogin(Model model) {
		return "user/login";
	}

	@GetMapping("/register")
	public String getPageRegister(Model model) {
		model.addAttribute("user", new User());
		return "user/register";
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}

	@GetMapping("/")
	public String homePage(Model model, HttpSession session) {
		model.addAttribute("listNews", productService.findNewProductByDate());
		model.addAttribute("listNikeShoes", productService.findByBrand(2, 8));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getPrincipal().equals("anonymousUser")) {
			session.removeAttribute("listCart");
		}
		model.addAttribute("listBanner",bannerService.findAll());
		return "user/index";
	}

	@GetMapping("/category/{id}")
	public String showProduct(Model model, @PathVariable Integer id) {
		return "user/show-product";
	}
	
}
