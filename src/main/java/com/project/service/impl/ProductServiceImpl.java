package com.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.Product;
import com.project.firebase.StorageStrategy;
import com.project.repository.ProductRepository;
import com.project.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	StorageStrategy storageStrategy;
	
	@Override
	public Product save(Product entity) {
		return productRepository.save(entity);
	}

	@Override
	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return productRepository.existsById(id);
	}

	@Override
	public long count() {
		return productRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		productRepository.deleteById(id);
	}

	@Override
	public void delete(Product entity) {
		productRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}

	@Override
	public String saveImageUrl(MultipartFile file) {			
		StringBuilder imageUrl = new StringBuilder();
		 try {
	            String fileName = storageStrategy.saveImage(file, "product");
	                      
	            if (fileName != null || fileName.trim().length() != 0) {
	                String tokens = StringUtils.substringBeforeLast(fileName, ".");
	                imageUrl.append("https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/");
	                imageUrl.append("product%2F");
	                imageUrl.append(fileName);
	                imageUrl.append("?alt=media&token=");
	                imageUrl.append(tokens);
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		return imageUrl.toString();
	}




	@Override
	public Page<Product> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		return this.productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> findAllByName(String name, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		return this.productRepository.findAllByName(name, pageable);
	}

	@Override
	public List<Product> findNewProductByDate() {	
		return productRepository.findNewProductByDate();
	}

	@Override
	public List<Product> findByBrand(Integer id, int number) {
		return productRepository.findByBrand(id,number);
	}

	@Override
	public Page<Product> findByCategory(Integer id, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		return productRepository.findByCategory(id, pageable);
	}

	@Override
	public Page<Product> filterProduct(String category_id, String brand_id, String keyword, String min, String max,int pageNo, int pageSize) {
		

		category_id = checkNumber(category_id, "%%");
		brand_id = checkNumber(brand_id,"%%");
		keyword = checkName(keyword);	
		min = checkNumber(min, "0");
		max = checkNumber(max, "50000000");
		
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		return productRepository.filterProduct(category_id, brand_id, keyword, min, max,pageable);
	}
	
	
	public String checkName(String str) {
		if(org.springframework.util.StringUtils.hasText(str)) {
			 return "%"+str+"%";
		}else {
			return "%%";
		}
	}
	
	public String checkNumber(String str, String result) {
		if(org.springframework.util.StringUtils.hasText(str)) {
			 return str;
		}else {
			return result;
		}
	}
	




	
	
}
