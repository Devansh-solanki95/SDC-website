package com.sdc.controller;

import com.sdc.entity.Contact;
import com.sdc.repo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/contactdetails")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public List<Contact> getAllContacts(){

        return contactRepository.findAll();
    }

    
    @PostMapping
    public Contact createEmployee(@RequestBody Contact contact){

        return contactRepository.save(contact);
    }
}