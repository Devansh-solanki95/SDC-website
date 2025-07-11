package com.sdc.services;

import com.sdc.entity.Testimonials;
import com.sdc.models.TestimonialsModel;
import com.sdc.repo.TestimonialsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TestimonialsService {

    @Autowired
    private TestimonialsRepository testimonialsRepository;

    // ✅ Add new testimonial with image
    public Testimonials addTestimonial(TestimonialsModel model) {
        Testimonials testimonial = new Testimonials();
        testimonial.setClientName(model.getClientName());
        testimonial.setDes(model.getDes());

        MultipartFile image = model.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                testimonial.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read image", e);
            }
        }

        return testimonialsRepository.save(testimonial);
    }

    // ✅ Update testimonial
    public Optional<Testimonials> updateTestimonial(Integer id, TestimonialsModel model) {
        return testimonialsRepository.findById(id).map(existing -> {
            if (model.getClientName() != null) existing.setClientName(model.getClientName());
            if (model.getDes() != null) existing.setDes(model.getDes());

            MultipartFile image = model.getImage();
            if (image != null && !image.isEmpty()) {
                try {
                    existing.setImage(image.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read image", e);
                }
            }

            return testimonialsRepository.save(existing);
        });
    }

    // ✅ Delete testimonial
    public boolean deleteTestimonial(Integer id) {
        if (testimonialsRepository.existsById(id)) {
            testimonialsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Get all testimonials
    public List<Testimonials> getAllTestimonials() {
        return testimonialsRepository.findAll();
    }

    // ✅ Get testimonial by ID
    public Optional<Testimonials> getTestimonialById(Integer id) {
        return testimonialsRepository.findById(id);
    }
}
