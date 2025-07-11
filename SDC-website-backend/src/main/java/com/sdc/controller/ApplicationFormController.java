package com.sdc.controller;
import com.sdc.entity.ApplicationForm;
import com.sdc.models.ApplicationFormModel;
import com.sdc.services.ApplicationFormService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin("*") // if you're calling from frontend like React/Angular
public class ApplicationFormController {

    @Autowired
    private  ApplicationFormService applicationFormService;
    private Long id;
    private MultipartFile file;


    // Create + Resume Upload Together
    @PostMapping(value = "/createWithResume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createFormWithResume(
            @ModelAttribute ApplicationFormModel formModel,
            @RequestParam("file") MultipartFile file) {

        try {
            // 1. Save file to disk
            byte[] arr = file.getBytes();
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uploadFile = UUID.randomUUID().toString() + extension;

            File dir = new File("C:\\Users\\lenovo\\OneDrive\\Documents\\GitHub\\SDC-website\\SDC-website-backend\\src\\main\\resources\\static\\assets\\resume");
            dir.mkdirs(); // ensures directory exists

            File fileObj = new File(dir, uploadFile);
            FileOutputStream fos = new FileOutputStream(fileObj);
            fos.write(arr);
            fos.flush();
            fos.close();

            // 2. Map model to entity
            ApplicationForm form = new ApplicationForm();
            form.setName(formModel.getName());
            form.setEmail(formModel.getEmail());
            form.setContactNumber(formModel.getContactNumber());
            form.setYear(formModel.getYear());
            form.setBranch(formModel.getBranch());
            form.setEnrollmentNumber(formModel.getEnrollmentNumber());
            form.setPosition(formModel.getPosition());
            form.setPastExperience(formModel.getPastExperience());
            form.setResumePath(fileObj.getAbsolutePath());

            // 3. Save to DB
            ApplicationForm savedForm = applicationFormService.saveForm(form);

            ApiResponse response = new ApiResponse(true, "Form and resume uploaded successfully!", savedForm);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            ApiResponse response = new ApiResponse(false, "Failed to upload resume or save form.", null);
            return ResponseEntity.status(500).body(response);

    }
}
    // Read All
    @GetMapping("/all")
    public ResponseEntity<List<ApplicationForm>> getAllForms() {
        List<ApplicationForm> forms = applicationFormService.getAllForms();
        return ResponseEntity.ok(forms);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationForm> getFormById(@PathVariable Long id) {
        return applicationFormService.getFormById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    // Update
//    @PutMapping("/update/{id}")
//    public ResponseEntity<ApplicationForm> updateForm(@PathVariable Long id,
//                                                      @RequestBody ApplicationForm form) {
//        ApplicationForm updated = applicationFormService.updateForm(id, form);
//        if (updated != null) {
//            return ResponseEntity.ok(updated);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable Long id) {
        applicationFormService.deleteForm(id);
        return ResponseEntity.ok("Application form deleted successfully.");
    }
}
