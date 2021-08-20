package com.project.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.entity.User;
import com.project.exception.CustomNotFoundException;
import com.project.repository.UserRepository;
import com.project.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findAllUser(Pageable pageable) {
        return userRepository.findAllUser(pageable);
    }

    @Override
    public Page<User> findByUserNameLike(String username, Pageable pageable) {
        return userRepository.findByUserNameLike(username, pageable);
    }

    @Override
    public Page<User> findByIsEnable(Boolean isEnable, Pageable pageable) {
        return userRepository.findByIsEnable(isEnable, pageable);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User findUserByUserNameAndIsEnable(String userName, Boolean isEnable) throws CustomNotFoundException {
        return userRepository.findUserByUserNameAndIsEnable(userName, isEnable)
                .orElseThrow(() -> new CustomNotFoundException("Could not find any user with " + userName));
    }

    @Override
    public Optional<User> findUserByTokenAndIsEnable(String token, Boolean isEnable) {
        return userRepository.findUserByTokenAndIsEnable(token,isEnable);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null){
            if (userRepository.findUserByUserName(user.getUserName()).isPresent()
                    || userRepository.findUserByEmail(user.getEmail()).isPresent()){
                return null;
            }

            String token = UUID.randomUUID().toString();
            user.setToken(token);

            user.setPassword(passwordEncoder.encode(user.getPsw()));
            user.setIsEnable(false);

            return userRepository.save(user);
        }

        User u = userRepository.findUserByUserNameAndIsEnable(user.getUserName(), true).orElse(null);
        if (u != null){
            u.setFullName(user.getFullName());
            u.setEmail(user.getEmail());
            u.setPhone(user.getPhone());
            u.setAddress(user.getAddress());
            return userRepository.save(u);
        }
        return null;
    }

    @Override
    public User postUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()){
            if (userOptional.get().getId() == user.getId()){
                return userRepository.save(user);
            }else{
                return null;
            }
        }else {
            return userRepository.save(user);
        }
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        return userRepository.findUserByUserName(username);
    }

    @Override
    public User findByEmail(String email) throws CustomNotFoundException{
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomNotFoundException("Could not find any user with email "+email));
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public void enableUser(Boolean isEnable, Integer id) {
        userRepository.enableUser(isEnable, id);
    }

    @Override
    public void updateToken(String token, String email) {
        userRepository.updateToken(token, email);
    }
}
