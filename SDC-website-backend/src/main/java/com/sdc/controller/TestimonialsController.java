package com.sdc.controller;

import com.sdc.entity.Testimonials;
import com.sdc.repo.TestimonialRepository;
import com.sdc.utils.ApiResponse;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialsController {

    @Autowired
    private TestimonialRepository testimonialsRepository;

    // 🔹 GET all testimonials
    @GetMapping("/getall")
    public ResponseEntity<ApiResponse> getAllTestimonials(ServletResponse response) {
        List<Testimonials> list = testimonialsRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Fetched all testimonials", list));
    }

    // 🔹 GET testimonial by ID
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<ApiResponse> getTestimonialById(@PathVariable Long id) {
        Optional<Testimonials> optional = testimonialsRepository.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial found", optional.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
        }
    }

    // 🔹 CREATE a testimonial
    @PostMapping("/addtestimonial")
    public ResponseEntity<ApiResponse> createTestimonial(@RequestBody Testimonials testimonial) {
        Testimonials saved = testimonialsRepository.save(testimonial);
        return ResponseEntity.ok(new ApiResponse(true, "Testimonial created successfully", saved));
    }

    // 🔹 UPDATE a testimonial
    @PutMapping("/updatetestimonial/{id}")
    public ResponseEntity<ApiResponse> updateTestimonial(@PathVariable Long id, @RequestBody Testimonials updatedData) {
        Optional<Testimonials> optional = testimonialsRepository.findById(id);
        if (optional.isPresent()) {
            Testimonials existing = optional.get();
            existing.setClientName(updatedData.getClientName());
            existing.setDes(updatedData.getDes());
            // update other fields as needed
            testimonialsRepository.save(existing);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial updated", existing));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
        }
    }

    // 🔹 DELETE a testimonial
    @DeleteMapping("/deletetestimonial/{id}")
    public ResponseEntity<ApiResponse> deleteTestimonial(@PathVariable Long id) {
        if (testimonialsRepository.existsById(id)) {
            testimonialsRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial deleted successfully",null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
        }
    }
}