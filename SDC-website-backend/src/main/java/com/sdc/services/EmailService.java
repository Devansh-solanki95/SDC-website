package com.sdc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactEmail(String name, String email, String phone, String query, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("devanshcloud2024@gmail.com"); // Replace with your admin's email
        mail.setSubject("New Contact Query from " + name);
        mail.setText(
                "Name: " + name +
                "\nEmail: " + email +
                "\nPhone: " + phone +
                "\nQuery: " + query +
                "\nMessage:\n" + message
        );
        mailSender.send(mail);
        
    }
}
