package com.project.controller.user;

import com.project.entity.Contact;
import com.project.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller(value = "contactOfUser")
@RequestMapping("user/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("")
    public String getFormContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "user/contact";
    }

    @PostMapping("/save")
    public String addContact(@ModelAttribute @Valid Contact contact, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/user/contact?invalid";
        }

        if (contactService.save(contact) == null)
            return "redirect:/user/contact?existed";

        return "redirect:/user/contact";
    }

}
