package com.sdc.controller;

import com.sdc.entity.Faq;
import com.sdc.repo.FaqRepository;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/api/faq")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    // 🔹 Get all FAQs
    @GetMapping("/api/faq/getallfaq")
    public ResponseEntity<ApiResponse> getAllFaqs() {
        List<Faq> list = faqRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "All FAQs fetched successfully", list));
    }

    // 🔹 Get FAQ by ID
    @GetMapping("/api/faq/getbyfaqbyid}")
    public ResponseEntity<ApiResponse> getFaqById(@PathVariable int id) {
        Optional<Faq> optional = faqRepository.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "FAQ found", optional.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false ,"FAQ not found",null));
        }
    }

    // 🔹 Create FAQ
    @PostMapping("/addfaq")
    public ResponseEntity<ApiResponse> createFaq(@RequestBody Faq faq) {
        Faq saved = faqRepository.save(faq);
        return ResponseEntity.ok(new ApiResponse(true, "FAQ created successfully", saved));
    }

    // 🔹 Update FAQ
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateFaq(@PathVariable int id, @RequestBody Faq updatedFaq) {
        Optional<Faq> optional = faqRepository.findById(id);
        if (optional.isPresent()) {
            Faq existing = optional.get();
            existing.setQues(updatedFaq.getQues());
            existing.setAns(updatedFaq.getAns());
            // Update other fields if any
            faqRepository.save(existing);
            return ResponseEntity.ok(new ApiResponse(true, "FAQ updated successfully", existing));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "FAQ not found",null));
        }
    }

    // 🔹 Delete FAQ
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFaq(@PathVariable int id) {
        if (faqRepository.existsById(id)) {
            faqRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "FAQ deleted successfully",null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "FAQ not found",null));
        }
    }
}