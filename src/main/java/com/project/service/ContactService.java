package com.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.entity.Contact;

public interface ContactService {

    Page<Contact> findAllContact(Pageable pageable);

    Page<Contact> findAllByName(String name, Pageable pageable);

    Optional<Contact> findById(Integer id);

    Optional<Contact> findContactByPhone(String phone);

    Boolean existByPhone(String phone);

    Contact save(Contact contact);

    void delete(Contact contact);
}
