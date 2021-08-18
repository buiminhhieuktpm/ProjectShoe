package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.Product;

public interface ProductService {


	void deleteAll();

	void delete(Product entity);

	void deleteById(Integer id);

	long count();

	boolean existsById(Integer id);

	Optional<Product> findById(Integer id);

	List<Product> findAll();

	Product save(Product entity);
	
	String saveImageUrl(MultipartFile file);
	
	
	Page<Product> findPaginated(int pageNo, int pageSize);
	
	Page<Product> findAllByName(String name, int pageNo, int pageSize);

	List<Product> findNewProductByDate();
	
	List<Product> findByBrand(Integer id,int number);
	
	Page<Product> findByCategory(Integer id, int pageNo, int pageSize);
	
	Page<Product> filterProduct(String category_id, String brand_id, String keyword, String min, String max,int pageNo, int pageSize);


}
