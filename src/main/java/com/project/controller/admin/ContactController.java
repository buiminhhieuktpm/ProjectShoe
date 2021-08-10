package com.project.controller.admin;

import com.project.entity.Contact;
import com.project.service.ContactService;
import com.project.utils.GenericModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller(value = "contactOfAdmin")
@RequestMapping("admin/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("")
    public String getContact(@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNo,
                             @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                             @RequestParam(value = "sortField", defaultValue = "name", required = false) String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
                             Model model) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit, ("asc".equals(sortDir) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()));
        Page<Contact> page = contactService.findAllPaging(pageable);

        GenericModel<Contact> contactModel = new GenericModel<>();
        contactModel.setPageNo(pageNo);
        contactModel.setLimit(limit);
        contactModel.setTotalPage(page.getTotalPages());
        contactModel.setTotalItem(page.getTotalElements());
        contactModel.setSortField(sortField);
        contactModel.setSortDir(sortDir);
        contactModel.setList(page.toList());

        model.addAttribute("contacts", contactModel);
        return "admin/contact";
    }

    @GetMapping("/edit/{id}")
    public String getUpdateContact(@PathVariable Integer id, Model model) {
        Contact contact = contactService.findById(id);
        if (contact != null) {
            model.addAttribute("contact", contact);
            return "admin/editcontact";
        }
        return "redirect:/admin/contact";
    }

    @PostMapping("/update")
    public String updateContact(@ModelAttribute @Valid Contact contact, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/admin/contact/edit/" + contact.getId() + "?invalid";
        }

        if (contactService.save(contact) == null)
            return "redirect:/admin/contact/edit/" + contact.getId() + "?existed";

        return "redirect:/admin/contact/edit/" + contact.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable Integer id) {
        contactService.delete(id);
        return "redirect:/admin/contact";
    }
}
