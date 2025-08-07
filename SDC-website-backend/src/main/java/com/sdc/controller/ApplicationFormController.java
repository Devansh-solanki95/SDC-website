package com.sdc.controller;

import com.sdc.entity.ApplicationForm;
import com.sdc.models.ApplicationFormModel;
import com.sdc.services.ApplicationFormService;
import com.sdc.services.EmailService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/public/form")

public class ApplicationFormController {

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private EmailService emailService;

    // ✅ Create + Resume Upload + Send Email
    @PostMapping(value = "/application-form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
            System.err.println("application submitted");

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

//    @GetMapping("/getAll")
//    public ResponseEntity<ApiResponse> getAllForms() {
//        List<ApplicationForm> list = applicationFormService.getAllForms();
//        if (list.isEmpty()) {
//            return ResponseEntity.ok(new ApiResponse(false, "No applications found", list));
//        } else {
//            return ResponseEntity.ok(new ApiResponse(true, "Applications fetched successfully", list));
//        }
//    }
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<ApiResponse> getFormById(@PathVariable Long id) {
//        return applicationFormService.getFormById(id)
//                .map(form -> ResponseEntity.ok(new ApiResponse(true, "Form found", form)))
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse(false, "Application form not found", null)));
//    }
//
//    // ❌ Delete
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ApiResponse> deleteForm(@PathVariable Long id) {
//        try {
//            Optional<ApplicationForm> optional = applicationFormService.getFormById(id);
//            if (optional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse(false, "Application form not found", null));
//            }
//            applicationFormService.deleteForm(id);
//            return ResponseEntity.ok(new ApiResponse(true, "Application form deleted successfully", null));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse(false, "Something went wrong while deleting", null));
//        }
//    }
//    
    
	/*
	 * @PutMapping(value = "/update/{id}", consumes =
	 * MediaType.MULTIPART_FORM_DATA_VALUE) public ResponseEntity<ApiResponse>
	 * updateFormWithResume(
	 * 
	 * @PathVariable Long id,
	 * 
	 * @ModelAttribute ApplicationFormModel formModel,
	 * 
	 * @RequestParam(value = "file", required = false) MultipartFile file) throws
	 * MessagingException {
	 * 
	 * Optional<ApplicationForm> optional = applicationFormService.getFormById(id);
	 * if (optional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND)
	 * .body(new ApiResponse(false, "Application form not found", null)); }
	 * 
	 * ApplicationForm existing = optional.get();
	 * 
	 * try { // ✅ Update file only if present if (file != null && !file.isEmpty()) {
	 * byte[] arr = file.getBytes(); String fileName = file.getOriginalFilename();
	 * String extension = fileName.substring(fileName.lastIndexOf(".")); String
	 * uploadFile = UUID.randomUUID().toString() + extension;
	 * 
	 * File dir = new File("src/main/resources/static/assets/resume"); dir.mkdirs();
	 * 
	 * File fileObj = new File(dir, uploadFile); FileOutputStream fos = new
	 * FileOutputStream(fileObj); fos.write(arr); fos.flush(); fos.close();
	 * 
	 * existing.setResumePath(uploadFile); }
	 * 
	 * // ✅ Update other fields if not null if (formModel.getName() != null)
	 * existing.setName(formModel.getName()); if (formModel.getEmail() != null)
	 * existing.setEmail(formModel.getEmail()); if (formModel.getContactNumber() !=
	 * null) existing.setContactNumber(formModel.getContactNumber()); if
	 * (formModel.getYear() != null) existing.setYear(formModel.getYear()); if
	 * (formModel.getBranch() != null) existing.setBranch(formModel.getBranch()); if
	 * (formModel.getEnrollmentNumber() != null)
	 * existing.setEnrollmentNumber(formModel.getEnrollmentNumber()); if
	 * (formModel.getPosition() != null)
	 * existing.setPosition(formModel.getPosition()); if
	 * (formModel.getPastExperience() != null)
	 * existing.setPastExperience(formModel.getPastExperience());
	 * 
	 * ApplicationForm saved = applicationFormService.saveForm(existing); return
	 * ResponseEntity.ok(new ApiResponse(true,
	 * "Application form updated successfully", saved));
	 * 
	 * } catch (IOException e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
	 * ApiResponse(false, "Something went wrong while updating", null)); } }
	 */

}
