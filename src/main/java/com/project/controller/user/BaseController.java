package com.project.controller.user;

import com.project.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    @GetMapping("/login")
    public String getPageLogin(Model model){
   	
//    	model.addAttribute("menu",categoryService.findByIsDisplay(true));
        return "user/login";
    }

    @GetMapping("/register")
    public String getPageRegister(Model model){
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/contact")
    public String getContact(){
        return "redirect:/user/contact";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "403";
    }
    

}
