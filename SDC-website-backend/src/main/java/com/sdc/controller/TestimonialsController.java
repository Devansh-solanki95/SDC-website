package com.sdc.controller;

import com.sdc.entity.Testimonials;
<<<<<<< HEAD
import com.sdc.repo.TestimonialRepository;
=======
>>>>>>> origin/anshika
import com.sdc.utils.ApiResponse;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/testimonials")
@CrossOrigin("*")
public class TestimonialsController {

    @Autowired
    private TestimonialRepository testimonialsRepository;

    // 🔹 GET all testimonials
    @GetMapping
=======
//@RequestMapping("/api/testimonials")
public class TestimonialsController {

    @Autowired
    private TestimonialsRepository testimonialsRepository;

    // 🔹 GET all testimonials
    @GetMapping("/api/testimonials/getall")
>>>>>>> origin/anshika
    public ResponseEntity<ApiResponse> getAllTestimonials(ServletResponse response) {
        List<Testimonials> list = testimonialsRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Fetched all testimonials", list));
    }

    // 🔹 GET testimonial by ID
<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTestimonialById(@PathVariable Long id, ServletResponse response) {
=======
    @GetMapping("/api/testimonials/getbyid/{id}")
    public ResponseEntity<ApiResponse> getTestimonialById(@PathVariable Long id) {
>>>>>>> origin/anshika
        Optional<Testimonials> optional = testimonialsRepository.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial found", optional.get()));
        } else {
<<<<<<< HEAD
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found", null));
=======
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
>>>>>>> origin/anshika
        }
    }

    // 🔹 CREATE a testimonial
<<<<<<< HEAD
    @PostMapping("/addtestimonial")
    public ResponseEntity<ApiResponse> createTestimonial(@RequestBody Testimonials testimonial, ServletResponse response) {
=======
    @PostMapping("/api/testimonials/addtestimonial")
    public ResponseEntity<ApiResponse> createTestimonial(@RequestBody Testimonials testimonial) {
>>>>>>> origin/anshika
        Testimonials saved = testimonialsRepository.save(testimonial);
        return ResponseEntity.ok(new ApiResponse(true, "Testimonial created successfully", saved));
    }

    // 🔹 UPDATE a testimonial
<<<<<<< HEAD
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTestimonial(@PathVariable Long id, @RequestBody Testimonials updatedData, ServletResponse response) {
=======
    @PutMapping("/api/testimonials/updatetestimonial/{id}")
    public ResponseEntity<ApiResponse> updateTestimonial(@PathVariable Long id, @RequestBody Testimonials updatedData) {
>>>>>>> origin/anshika
        Optional<Testimonials> optional = testimonialsRepository.findById(id);
        if (optional.isPresent()) {
            Testimonials existing = optional.get();
            existing.setClientName(updatedData.getClientName());
            existing.setDes(updatedData.getDes());
<<<<<<< HEAD
            // Update other fields if needed
            testimonialsRepository.save(existing);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial updated successfully", existing));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found", null));
=======
            // update other fields as needed
            testimonialsRepository.save(existing);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial updated", existing));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
>>>>>>> origin/anshika
        }
    }

    // 🔹 DELETE a testimonial
<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTestimonial(@PathVariable Long id, ServletResponse response) {
        if (testimonialsRepository.existsById(id)) {
            testimonialsRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found", null));
        }
    }
}
=======
    @DeleteMapping("/api/testimonials/deletetestimonial/{id}")
    public ResponseEntity<ApiResponse> deleteTestimonial(@PathVariable Long id) {
        if (testimonialsRepository.existsById(id)) {
            testimonialsRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Testimonial deleted successfully",null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "Testimonial not found",null));
        }
    }
}
>>>>>>> origin/anshika
