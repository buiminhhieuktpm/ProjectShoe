package com.project.service.impl;

import com.project.entity.Contact;
import com.project.repository.ContactRepository;
import com.project.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact findById(Integer id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public Contact findByPhone(String phone) {
        return null;
    }

    @Override
    public Boolean existByEmailAndPhone(String email, String phone) {
        return contactRepository.existsByEmailAndPhone(email, phone);
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public Page<Contact> findAllPaging(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            if (contactRepository.existsByEmailAndPhone(contact.getEmail(), contact.getPhone())) {
                return null;
            }
            return contactRepository.save(contact);
        }
        if (contactRepository.existsByEmailAndPhone(contact.getEmail(), contact.getPhone())) {
            return null;
        }
        return contactRepository.save(contact);
    }

    @Override
    public void delete(Integer id) {
        contactRepository.deleteById(id);
    }
}
