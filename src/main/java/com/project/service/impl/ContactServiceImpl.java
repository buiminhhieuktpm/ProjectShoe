package com.project.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.entity.Contact;
import com.project.repository.ContactRepository;
import com.project.service.ContactService;
import com.project.service.sendmail.SendMail;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private SendMail sendMail;

    @Override
    public Page<Contact> findAllContact(Pageable pageable) {
        return contactRepository.findAllContact(pageable);
    }

    @Override
    public Page<Contact> findAllByName(String name, Pageable pageable) {
        return contactRepository.findAllByNameLike(name, pageable);
    }

    @Override
    public Optional<Contact> findById(Integer id) {
        return contactRepository.findById(id);
    }

    @Override
    public Optional<Contact> findContactByPhone(String phone) {
        return contactRepository.findContactByPhone(phone);
    }

    @Override
    public Boolean existByPhone(String phone) {
        return contactRepository.existsByPhone(phone);
    }

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() == null) {
            if (contactRepository.existsByPhone(contact.getPhone())) {
                return null;
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMail.sendMail(contact.getEmail(), "Shoe Store", contact.getName());
                }
            });
            thread.start();

            return contactRepository.save(contact);
        }else{
            Optional<Contact> c = contactRepository.findContactByPhone(contact.getPhone());
            if (c.isPresent()){
                if (c.get().getId() == contact.getId()){
                    return contactRepository.save(contact);
                }else{
                    return null;
                }
            }
            return contactRepository.save(contact);
        }
    }

    @Override
    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }
}
