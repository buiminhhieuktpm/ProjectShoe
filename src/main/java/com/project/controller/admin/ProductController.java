package com.project.controller.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.Product;
import com.project.service.BrandService;
import com.project.service.CategoryService;
import com.project.service.ProductService;

@Controller
@RequestMapping("/admin")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	BrandService brandService;

	@GetMapping("/product")
	public String productHome(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
		return findPaginated(1, keyword, model);
	}

	@GetMapping("/product/addproduct")
	public String addProduct(Model model) {
		Product product = new Product();

		// set enable
		product.setIsEnable(true);

		// set time stamp
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		product.setOnCreate(timestamp);
		product.setOnUpdate(timestamp);

		// set view to 0
		product.setView(0);

		model.addAttribute("category", categoryService.findAll());
		model.addAttribute("brand", brandService.findAll());
		model.addAttribute("product", product);
		return "admin/addproduct";
	}

	@PostMapping("product/saveproduct")
	public String saveProduct(Model model, @RequestParam("image11") MultipartFile image1,
			@RequestParam("image22") MultipartFile image2, @RequestParam("image33") MultipartFile image3,
			@RequestParam("image44") MultipartFile image4, @ModelAttribute("product") Product product) {

		try {
			// set image 1
			if (!image1.isEmpty()) {
				product.setImage1(productService.saveImageUrl(image1));
			}

			// set image 2
			if (!image2.isEmpty()) {
				product.setImage2(productService.saveImageUrl(image2));
			}

			// set image 3
			if (!image3.isEmpty()) {
				product.setImage3(productService.saveImageUrl(image3));
			}

			// set image 4
			if (!image4.isEmpty()) {
				product.setImage4(productService.saveImageUrl(image4));
			}

			// save or update Product
			productService.save(product);

		} catch (Exception e) {
			// Error Message
			e.printStackTrace();
		}
		return "redirect:/admin/product";
	}

	@GetMapping("/product/edit/{id}")
	public String edit(Model model, @PathVariable Integer id) {
		Optional<Product> product = productService.findById(id);
		// Check Category Exit or not
		if (product.isPresent()) {
			// Exist
			// get Product
			Product pd = product.get();
			// set time update
			Date date = new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			pd.setOnUpdate(timestamp);
				
			model.addAttribute("category", categoryService.findAll());
			model.addAttribute("brand", brandService.findAll());
			model.addAttribute("product", pd);
			return "admin/editproduct";
		} else {
			// Not Exist
			// Error Page
			return "redirect:/admin/product";
		}
	}

	// Delete Category
	@GetMapping("/product/delete/{id}")
	public String delete(Model model, @PathVariable Integer id) {
		productService.deleteById(id);
		return "redirect:/admin/product";
	}

	// paginated
	@GetMapping("product/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		int pageSize = 8;

		Page<Product> page;

		if (StringUtils.hasText(keyword)) {
			
			keyword.trim();
			page = productService.findAllByName(keyword, pageNo, pageSize);

		} else {
			page = productService.findPaginated(pageNo, pageSize);
		}

		List<Product> listProducts = page.getContent();

		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("list", listProducts);

		return "admin/product";

	}

}
