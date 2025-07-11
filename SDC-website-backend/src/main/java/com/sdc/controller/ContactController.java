package com.sdc.controller;

import com.sdc.entity.Contact;
import com.sdc.repo.ContactRepository;
import com.sdc.services.EmailService;
import com.sdc.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/contact")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveContact(@RequestBody Contact contact) {
        contactRepository.save(contact);
        emailService.sendContactEmail(
                contact.getName(),
                contact.getEmail(),
                contact.getContactNo(),
                contact.getQuery(),
                contact.getMessage()
        );
        return ResponseEntity.ok(new ApiResponse(true, "contact saved and email send", contact));
    }

//    
//    @PostMapping("/save")
//    public Contact createEmployee(@RequestBody Contact contact){
//        return contactRepository.save(contact);
//    }
//    
    
    
    
}