package com.sdc.controller;

import com.sdc.entity.ApplicationForm;
import com.sdc.models.ApplicationFormModel;
import com.sdc.services.ApplicationFormService;
import com.sdc.services.EmailService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin("*")
public class ApplicationFormController {

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private EmailService emailService;

    // ✅ Create + Resume Upload + Send Email
    @PostMapping(value = "/createWithResume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createFormWithResume(
            @ModelAttribute ApplicationFormModel formModel,
            @RequestParam("file") MultipartFile file) throws MessagingException {

        try {
            // Step 1: Save file to disk
            byte[] arr = file.getBytes();
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uploadFile = UUID.randomUUID().toString() + extension;

            File dir = new File("src/main/resources/static/assets/resume");
            dir.mkdirs(); // ensure directory exists

            File fileObj = new File(dir, uploadFile);
            FileOutputStream fos = new FileOutputStream(fileObj);
            fos.write(arr);
            fos.flush();
            fos.close();

            String resumePath = fileObj.getAbsolutePath();

            // Step 2: Map model to entity
            ApplicationForm form = new ApplicationForm();
            form.setName(formModel.getName());
            form.setEmail(formModel.getEmail());
            form.setContactNumber(formModel.getContactNumber());
            form.setYear(formModel.getYear());
            form.setBranch(formModel.getBranch());
            form.setEnrollmentNumber(formModel.getEnrollmentNumber());
            form.setPosition(formModel.getPosition());
            form.setPastExperience(formModel.getPastExperience());
            form.setResumePath(uploadFile);  // ✅ only file name


            // Step 3: Save to DB
            ApplicationForm savedForm = applicationFormService.saveForm(form);

            // ✅ Step 4: Send email to admin with all form data
            emailService.sendApplicationFormEmailWithAttachment(
                    savedForm.getName(),
                    savedForm.getEmail(),
                    savedForm.getContactNumber(),
                    savedForm.getYear(),
                    savedForm.getBranch(),
                    savedForm.getEnrollmentNumber(),
                    savedForm.getPosition(),
                    savedForm.getPastExperience(),
                    new File(resumePath) // ✅ Correct: File object passed
            );

            // Step 5: Return response
            ApiResponse response = new ApiResponse(true, "Form and resume uploaded successfully!", savedForm);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            ApiResponse response = new ApiResponse(false, "Failed to upload resume or save form.", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    // 🔍 Get All
    @GetMapping("/all")
    public ResponseEntity<List<ApplicationForm>> getAllForms() {
        List<ApplicationForm> forms = applicationFormService.getAllForms();
        return ResponseEntity.ok(forms);
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationForm> getFormById(@PathVariable Long id) {
        return applicationFormService.getFormById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable Long id) {
        applicationFormService.deleteForm(id);
        return ResponseEntity.ok("Application form deleted successfully.");
    }
}
