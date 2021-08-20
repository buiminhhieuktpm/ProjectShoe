package com.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Size;
import com.project.repository.SizeRepository;
import com.project.service.SizeService;

@Service
public class SizeServiceImpl implements SizeService {
	
	@Autowired
	SizeRepository sizeRepository;
	
	@Override
	public Size save(Size entity) {
		return sizeRepository.save(entity);
	}

	@Override
	public List<Size> findAll() {
		return sizeRepository.findAll();
	}

	@Override
	public Optional<Size> findById(Integer id) {
		return sizeRepository.findById(id);
	}

	@Override
	public long count() {
		return sizeRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		sizeRepository.deleteById(id);
	}

	@Override
	public void delete(Size entity) {
		sizeRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		sizeRepository.deleteAll();
	}

	@Override
	public Optional<Size> findBySizeAndProductId(String size, Integer id) {
		return sizeRepository.findBySizeAndProductId(size, id);
	}

	@Override
	public List<Size> findByProductId(Integer id) {		
		return sizeRepository.findByProduct(id);
	}

	@Override
	public List<Size> findByProductAvailable(Integer id) {
		return sizeRepository.findByProductAvailable(id);
	}

	@Override
	public Integer totalAmount(Integer size_id) {
		return sizeRepository.totalAmount(size_id);
	}

	@Override
	public void updateAmount(Integer amount,Integer id) {
		sizeRepository.updateAmount(amount,id);
	}

}
