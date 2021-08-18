package com.project.controller.admin;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.Banner;
import com.project.service.BannerService;

@Controller
@RequestMapping("/admin/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;



    @GetMapping("")
    public String categoryHome(Model model) {
        model.addAttribute("list", bannerService.findAll());
        return "admin/banner";
    }

    @GetMapping("/addbanner")
    public String addOrEdit(Model model) {
        Banner banner = new Banner();
        model.addAttribute("banner", banner);
        return "admin/addbanner";
    }

    // Save or Update Banner
    @PostMapping("/savebanner")
    public String addOrUpdate(Model model,@ModelAttribute("banner") Banner banner,@RequestParam("image11") MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                banner.setImage(bannerService.saveImageUrl(image));
            }
            bannerService.save(banner);
        }catch (Exception e) {
            // Error Message
            e.printStackTrace();
        }
        return "redirect:/admin/banner";
    }

    // Delete Banner
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        bannerService.deleteById(id);
        return "redirect:/admin/banner";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        Optional<Banner> ban = bannerService.findById(id);
        // Check Category Exit or not
        if (ban.isPresent()) {
            // Exist
            model.addAttribute("banner", ban.get());
            return "admin/editbanner";
        } else {
            // Not Exist
            // Error Page
            model.addAttribute("mess", "Không tồn tại banner này");
            return "error";
        }


    }
}
