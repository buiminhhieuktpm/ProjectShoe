package com.project.service;

import com.project.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    Contact findById(Integer id);

    Contact findByPhone(String phone);

    Boolean existByEmailAndPhone(String email, String phone);

    List<Contact> findAll();

    Page<Contact> findAllPaging(Pageable pageable);

    Contact save(Contact contact);

    void delete(Integer id);
}
