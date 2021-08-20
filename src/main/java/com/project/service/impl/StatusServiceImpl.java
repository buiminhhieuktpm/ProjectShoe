package com.project.service.impl;

import com.project.entity.Status;
import com.project.repository.StatusRepository;
import com.project.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public Optional<Status> findById(Integer id) {
        return statusRepository.findById(id);
    }
}
