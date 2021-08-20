package com.project.controller.user;

import com.project.entity.Role;
import com.project.entity.User;
import com.project.exception.CustomNotFoundException;
import com.project.service.RoleService;
import com.project.service.UserService;
import com.project.service.sendmail.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller(value = "userOfUser")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SendMail sendMail;

    @GetMapping("/{username}")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public String getUser(@PathVariable String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser")){
            try {
                User user = userService.findUserByUserNameAndIsEnable(username, true);
                model.addAttribute("user", user);
                return "user/edit-user";
            }catch (CustomNotFoundException ex){
                model.addAttribute("user", new User());
                model.addAttribute("error", ex.getMessage());
                return "user/edit-user";
            }
        }
        return "redirect:http://localhost:8080/perform_logout";
    }

    @PostMapping
    public String saveUser(@ModelAttribute @Valid User user, BindingResult result) {
        if (user.getId() == null) {
            //create
            if (result.hasErrors()) {
                return "redirect:/register?invalid";
            }

            User u = userService.save(user);
            if (u == null)
                return "redirect:/register?existed";

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = "<a href=\"http://localhost:8080/user/actived/" + u.getToken() +
                                "\">Actice account !</a>";
                        sendMail.confirmMail(u.getEmail(), url, user.getUserName());
                        Thread.sleep(15 * 60 * 1000);
                        if (userService.findUserByTokenAndIsEnable(u.getToken(), false).isPresent()) {
                            userService.delete(u.getId());
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();

            return "redirect:/login";

        } else {
            //update
            if (result.hasErrors()) {
                return "redirect:/user/" + user.getUserName() + "?invalid";
            }
            userService.save(user);
            return "redirect:/user/" + user.getUserName();
        }
    }

    @GetMapping("/actived/{token}")
    public String activeUser(@PathVariable(value = "token") String token) {
        User user = userService.findUserByTokenAndIsEnable(token, false).orElse(null);
        if (user != null) {
            Role role = roleService.findRoleByRole("USER");
            user.addRole(role);
            user.setIsEnable(true);
            userService.postUser(user);
            return "redirect:/login?actived";
        }
        return "redirect:/login?expired";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "user/forgotpsw";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        try {
            User user = userService.findByEmail(email);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = "<a href=\"http://localhost:8080/user/reset-password/" + user.getToken() +
                                "\">Change Password !</a>";
                        sendMail.confirmMail(email, url, user.getUserName());
                        Thread.sleep(15 * 60 * 1000);
                        if (userService.findUserByTokenAndIsEnable(user.getToken(), true).isPresent()) {
                            String token = UUID.randomUUID().toString();
                            userService.updateToken(token, email);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (CustomNotFoundException e) {
            model.addAttribute("error",e.getMessage());
        }

        return "user/forgotpsw";
    }

    @GetMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable String token,Model model){
        if (userService.findUserByTokenAndIsEnable(token, true).isPresent()){
            model.addAttribute("token", token);
            return "user/resetpassword";
        }
        return "redirect:/user/forgot-password?expired";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String psw){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userService.findUserByTokenAndIsEnable(token,true).orElse(null);
        if (user != null){
            user.setPassword(passwordEncoder.encode(psw));
            userService.postUser(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    @Secured("ROLE_USER")
    public String getPageChangePsw(){
        return "user/changepsw";
    }

    @PostMapping("/update-password")
    @Secured({"ROLE_USER"})
    public String changePassword(@RequestParam(name = "pswOld") String pswOld, @RequestParam(name = "pswNew") String pswNew){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")){
            try {
                User user = userService.findUserByUserNameAndIsEnable(authentication.getName(), true);
                if (passwordEncoder.matches(pswOld, user.getPassword())){
                    user.setPassword(passwordEncoder.encode(pswNew));
                    userService.postUser(user);
                    return "redirect:http://localhost:8080/perform_logout";
                }
                return "redirect:/user/change-password?error";
            } catch (CustomNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "redirect:http://localhost:8080/perform_logout";
    }
}
