package com.sdc.services;


import com.sdc.models.ApplicationFormModel;
import com.sdc.repo.ApplicationFormRepository;
import com.sdc.entity.ApplicationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationFormService {

    @Autowired
    private ApplicationFormRepository applicationFormRepository;

    public ApplicationForm saveForm(ApplicationForm form) {
        return applicationFormRepository.save(form);
    }

    public List<ApplicationForm> getAllForms() {
        return applicationFormRepository.findAll();
    }

    public Optional<ApplicationForm> getFormById(Long id) {
        return applicationFormRepository.findById(id);
    }

    public void deleteForm(Long id) {
        applicationFormRepository.deleteById(id);
    }

    public ApplicationForm updateForm(Long id, ApplicationForm updatedForm) {
        return applicationFormRepository.findById(id).map(existingForm -> {
            if (updatedForm.getName() != null) {
                existingForm.setName(updatedForm.getName());
            }
            if (updatedForm.getContactNumber() != null) {
                existingForm.setContactNumber(updatedForm.getContactNumber());
            }
            if (updatedForm.getEmail() != null) {
                existingForm.setEmail(updatedForm.getEmail());
            }
            if (updatedForm.getYear() != null) {
                existingForm.setYear(updatedForm.getYear());
            }
            if (updatedForm.getBranch() != null) {
                existingForm.setBranch(updatedForm.getBranch());
            }
            if (updatedForm.getEnrollmentNumber() != null) {
                existingForm.setEnrollmentNumber(updatedForm.getEnrollmentNumber());
            }
            if (updatedForm.getPosition() != null) {
                existingForm.setPosition(updatedForm.getPosition());
            }
            if (updatedForm.getPastExperience() != null) {
                existingForm.setPastExperience(updatedForm.getPastExperience());
            }
            if (updatedForm.getResumePath() != null) {
                existingForm.setResumePath(updatedForm.getResumePath());
            }

            return applicationFormRepository.save(existingForm);
        }).orElse(null);
    }

    public void updateResumePath(Long id, String path) {
        applicationFormRepository.findById(id).ifPresent(form -> {
            form.setResumePath(path);
            applicationFormRepository.save(form);
        });
    }


}

