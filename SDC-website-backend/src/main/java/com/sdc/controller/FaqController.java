package com.sdc.controller;

import com.sdc.entity.Faq;
import com.sdc.models.FaqModel;
import com.sdc.services.FaqService;
import com.sdc.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faq")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')") 
public class FaqController {

    @Autowired
    private FaqService faqService;

    //to add faqs
    @PostMapping("/addfaq")
    public ResponseEntity<ApiResponse> createFaq(@RequestBody FaqModel faqModel) {
        try {
            if (faqModel.getQues() == null || faqModel.getAns() == null ||
                    faqModel.getQues().isEmpty() || faqModel.getAns().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "Question and Answer cannot be empty", null));
            }

            Faq saved = faqService.addFaq(faqModel);
            System.err.println(saved);
            return ResponseEntity.ok(new ApiResponse(true, "FAQ created successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse(false, "Something went wrong while creating FAQ", null));
        }
    }

    @GetMapping("/allfaq")
    public ResponseEntity<ApiResponse> getAllFaqs() {
        List<Faq> list = faqService.getAllFaqs();
        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "No FAQs found", list));
        } else {
            return ResponseEntity.ok(new ApiResponse(true, "FAQs fetched successfully", list));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFaq(@PathVariable Integer id) {
        try {
            Optional<Faq> optional = faqService.getFaqById(id);

            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "FAQ not found", null));
            }

            faqService.deleteFaq(id);
            return ResponseEntity.ok(new ApiResponse(true, "FAQ deleted successfully", null));
        } catch (Exception e) {
            e.printStackTrace(); // helpful for debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Something went wrong while deleting", null));
        }
    }


    @PutMapping("/updatefaq/{id}")
    public ResponseEntity<ApiResponse> updateFaq(@PathVariable Integer id, @RequestBody Faq updatedFaq) {
        Optional<Faq> optional = faqService.getFaqById(id); // ✅ Service use

        if (optional.isPresent()) {
            Faq existing = optional.get();

            //  If ques is not null or empty, then update
            if (updatedFaq.getQues() != null && !updatedFaq.getQues().trim().isEmpty()) {
                existing.setQues(updatedFaq.getQues());
            }

            //  If ans is not null or empty, then update
            if (updatedFaq.getAns() != null && !updatedFaq.getAns().trim().isEmpty()) {
                existing.setAns(updatedFaq.getAns());
            }

            Faq saved = faqService.savefaq(existing); //  Save via service
            return ResponseEntity.ok(new ApiResponse(true, "FAQ updated successfully", saved));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse(false, "FAQ not found", null));
        }
    }
}