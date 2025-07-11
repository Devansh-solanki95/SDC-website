package com.sdc.services;

import com.sdc.entity.Images;
import com.sdc.models.ImagesModel;
import com.sdc.repo.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImagesService {

    @Autowired
    private ImagesRepository imagesRepository;

    // ✅ Save new image
    public Images saveImage(ImagesModel model) {
        Images img = new Images();
        img.setTitle(model.getTitle());

        MultipartFile file = model.getImage();
        try {
            if (file != null && !file.isEmpty()) {
                img.setImage(file.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading image file", e);
        }

        return imagesRepository.save(img);
    }

    // ✅ Update existing image
    public Optional<Images> updateImage(Integer id, ImagesModel model) {
        return imagesRepository.findById(id).map(existing -> {

            if (model.getTitle() != null) {
                existing.setTitle(model.getTitle());
            }

            MultipartFile file = model.getImage();
            try {
                if (file != null && !file.isEmpty()) {
                    existing.setImage(file.getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error updating image file", e);
            }

            return imagesRepository.save(existing);
        });
    }

    // ✅ Get all images
    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }

    // ✅ Delete image
    public boolean deleteImage(Integer id) {
        if (imagesRepository.existsById(id)) {
            imagesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Get single image
    public Optional<Images> getImageById(Integer id) {
        return imagesRepository.findById(id);
    }
}
