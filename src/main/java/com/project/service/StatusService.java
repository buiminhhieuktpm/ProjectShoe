package com.project.service;

import com.project.entity.Status;

import java.util.Optional;

public interface StatusService {
    Optional<Status> findById(Integer id);
}
