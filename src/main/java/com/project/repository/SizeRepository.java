package com.project.repository;

import java.util.List;
import java.util.Optional;

import com.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.entity.Size;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SizeRepository extends JpaRepository<Size, Integer>{
	@Query(value = "Select * from size where size = ?1 and product_id = ?2", nativeQuery = true)
	Optional<Size> findBySizeAndProductId(String size,Integer id);
	
	@Query(value = "Select * from size where product_id = ?1", nativeQuery = true)
	List<Size> findByProduct(Integer id);

	@Query(value = "Select * from size where product_id = ?1 and amount > 0", nativeQuery = true)
	List<Size> findByProductAvailable(Integer id);

	@Query("select s from Size s where s.id = :size_id")
	Integer totalAmount(@Param("size_id") Integer size_id);

	@Transactional
	@Modifying
	@Query("update Size s set s.amount = :amount where s.id = :id")
	void updateAmount(@Param("amount") Integer amount,@Param("id") Integer id);
}
