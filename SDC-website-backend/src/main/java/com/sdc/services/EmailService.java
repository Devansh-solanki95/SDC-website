package com.sdc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Contact Email
    public void sendContactEmail(String name, String email, String phone, String query, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("devanshcloud2024@gmail.com");
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

    // 📩 Application Form Email 
    public void sendApplicationFormEmailWithAttachment(
            String name, String email, String phone, String year,
            String branch, String enrollNo, String position,
            String experience, File resumePath) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

        helper.setTo("devanshcloud2024@gmail.com");
        helper.setSubject("New Application from " + name);

        String body = "📄 New application form details:\n\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Year: " + year + "\n" +
                "Branch: " + branch + "\n" +
                "Enrollment No: " + enrollNo + "\n" +
                "Position: " + position + "\n" +
                "Experience: " + experience + "\n";

        helper.setText(body);

        // Attach resume
        FileSystemResource file = new FileSystemResource(resumePath);

        String fileName = resumePath.getName();  // Gets original file name like abc.png, xyz.docx
        helper.addAttachment(fileName, file);
 

        mailSender.send(message);
    }
}
