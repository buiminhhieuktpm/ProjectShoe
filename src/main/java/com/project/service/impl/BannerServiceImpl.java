package com.project.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.entity.Banner;
import com.project.firebase.StorageStrategy;
import com.project.repository.BannerRepository;
import com.project.service.BannerService;

@Service
public class BannerServiceImpl  implements BannerService {
    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private StorageStrategy storageStrategy;

    @Override
    public Banner getById(Integer id) {
        return bannerRepository.getById(id);
    }

    @Override
    public void deleteAll() {
        bannerRepository.deleteAll();
    }

    @Override
    public void delete(Banner entity) {
        bannerRepository.delete(entity);
    }

    @Override
    public void deleteById(Integer id) {
        bannerRepository.deleteById(id);
    }

    @Override
    public long count() {
        return bannerRepository.count();
    }

    @Override
    public boolean existsById(Integer id) {
        return bannerRepository.existsById(id);
    }

    @Override
    public Optional<Banner> findById(Integer id) {
        return bannerRepository.findById(id);
    }

    @Override
    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    @Override
    public Banner save(Banner entity) {
        return bannerRepository.save(entity);
    }

    @Override
    public Optional<Banner> findByTitle(String title) {
        return bannerRepository.findByTitle(title);
    }

    @Override
    public String saveImageUrl(MultipartFile file) {

        StringBuilder imageUrl = new StringBuilder();
        try {
            String fileName = storageStrategy.saveImage(file, "banner");

            if (fileName != null || fileName.trim().length() != 0) {
                String tokens = StringUtils.substringBeforeLast(fileName, ".");
                imageUrl.append("https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/");
                imageUrl.append("banner%2F");
                imageUrl.append(fileName);
                imageUrl.append("?alt=media&token=");
                imageUrl.append(tokens);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageUrl.toString();
    }


}
