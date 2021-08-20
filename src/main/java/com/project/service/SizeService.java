package com.project.service;

import java.util.List;
import java.util.Optional;

import com.project.entity.Size;

public interface SizeService{


	void deleteAll();

	void delete(Size entity);

	void deleteById(Integer id);

	long count();

	Optional<Size> findById(Integer id);

	List<Size> findAll();

	Size save(Size entity);

	List<Size> findByProductId(Integer id);
	
	Optional<Size> findBySizeAndProductId(String size, Integer id);
	
	List<Size> findByProductAvailable(Integer id);

	Integer totalAmount(Integer size_id);

	void updateAmount(Integer amount, Integer id);
}
