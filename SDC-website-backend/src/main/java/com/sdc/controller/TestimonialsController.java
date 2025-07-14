package com.sdc.controller;

import com.sdc.entity.Testimonials;
import com.sdc.models.TestimonialsModel;
import com.sdc.services.TestimonialsService;
import com.sdc.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@RestController
@RequestMapping("/admin/testimonials")
@PreAuthorize("hasRole('ADMIN')") // Optional if secured
//@CrossOrigin(origins = "http://localhost:5173")
public class TestimonialsController {

    @Autowired
    private TestimonialsService testimonialsService;

    // ✅ Add testimonial
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> addTestimonial(@ModelAttribute TestimonialsModel model) {
        Testimonials saved = testimonialsService.addTestimonial(model);
        return ResponseEntity.ok(new ApiResponse(true, "Testimonial added successfully", convertToResponse(saved)));
    }

    // ✅ Update testimonial
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateTestimonial(@PathVariable Integer id, @ModelAttribute TestimonialsModel model) {
        return testimonialsService.updateTestimonial(id, model)
                .map(updated -> ResponseEntity.ok(new ApiResponse(true, "Testimonial updated successfully", convertToResponse(updated))))
                .orElseGet(() -> ResponseEntity.ok(new ApiResponse(false, "Testimonial not found", null)));
    }

//    //  Get all testimonials with base64 images
//    @GetMapping("/getAll")
//    public ResponseEntity<ApiResponse> getAllTestimonials() {
//        List<Testimonials> list = testimonialsService.getAllTestimonials();
//        List<Map<String, Object>> response = list.stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(new ApiResponse(true, "Testimonials fetched successfully", response));
//    }

    // ❌ Delete testimonial
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTestimonial(@PathVariable Integer id) {
        boolean deleted = testimonialsService.deleteTestimonial(id);
        return ResponseEntity.ok(new ApiResponse(deleted, deleted ? "Testimonial deleted successfully" : "Testimonial not found", null));
    }

    // ✅ Convert to JSON-friendly format with base64 image
    private Map<String, Object> convertToResponse(Testimonials t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("testId", t.getTestId());
        map.put("clientName", t.getClientName());
        map.put("des", t.getDes());

        if (t.getImage() != null && t.getImage().length > 0) {
            map.put("imageBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(t.getImage()));
        } else {
            map.put("imageBase64", null);
        }

        return map;
    }
}
