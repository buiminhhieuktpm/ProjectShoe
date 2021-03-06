package com.project.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

	@Query(value = "Select * from product where name Like %?1% ", nativeQuery = true)
	Page<Product> findAllByName(String name, Pageable pageable);
	
	@Query("select p from Product p where datediff(current_date,onCreate) <= 7 and isEnable = true LIMIT 8")
	List<Product> findNewProductByDate();
	
	@Query(value = "select * from product where brand_id = ?1 and is_enable = true  LIMIT ?2", nativeQuery = true)
	List<Product> findByBrand(Integer id, int number);
	
	@Query(value = "select * from product where category_id = ?1 and is_enable = true", nativeQuery = true)
	Page<Product> findByCategory(Integer id, Pageable pageable);
	
	@Query(value = "select * from product where category_id Like ?1 and brand_id Like ?2 and name Like ?3 and price between ?4 and ?5 and is_enable = true", nativeQuery = true)
	Page<Product> filterProduct(String category_id, String brand_id, String keyword, String min, String max, Pageable pageable);

}
