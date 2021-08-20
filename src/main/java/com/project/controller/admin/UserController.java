package com.project.controller.admin;

import com.project.entity.User;
import com.project.exception.CustomNotFoundException;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller(value = "userOfAdmin")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/profile")
    public String getAdmin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")){
            try {
                User user = userService.findUserByUserNameAndIsEnable(authentication.getName(), true);
                model.addAttribute("user",user);
            } catch (CustomNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "admin/profileadmin";
    }

    @GetMapping("/admin/user")
    public String getPage(){
        return "admin/user";
    }

    @GetMapping("/admin/user/{id}")
    public String getUser(@PathVariable Integer id, Model model){
        User user = userService.findById(id).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin/edituser";
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/api/user/{pageNo}")
    @ResponseBody
    public Page<User> getAllUser(@PathVariable(value = "pageNo") Optional<Integer> pageNo){
        int page = pageNo.orElse(1);
        Pageable pageable = PageRequest.of(page - 1,8, Sort.by("userName").ascending());
        Page<User> listUser = userService.findAllUser(pageable);
        return listUser;
    }

    @GetMapping("/api/user/{pageNo}/{name}")
    @ResponseBody
    public Page<User> getAllUserName(@PathVariable(value = "pageNo") Optional<Integer> pageNo,
                                           @PathVariable(value = "name") String userName){
        int page = pageNo.orElse(1);
        Pageable pageable = PageRequest.of(page - 1,8, Sort.by("userName").ascending());
        Page<User> listUser = userService.findByUserNameLike("%"+userName+"%", pageable);
        return listUser;
    }

    @PutMapping("/api/user/{id}")
    @ResponseBody
    public ResponseEntity<User> editUser(@PathVariable Integer id, @RequestBody User user){
        User u = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found User with " + id));
        u.setFullName(user.getFullName());
        u.setEmail(user.getEmail());
        u.setPhone(user.getPhone());
        u.setAddress(user.getAddress());
        u.setIsEnable(user.getIsEnable());

        User updateUser = userService.postUser(u);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/api/user/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> deleteUserApi(@PathVariable Integer id){
        User u = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found User with " + id));

        userService.enableUser(false, u.getId());
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
