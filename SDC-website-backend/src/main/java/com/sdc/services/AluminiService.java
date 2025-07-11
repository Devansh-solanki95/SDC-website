package com.sdc.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc.entity.Alumini;
import com.sdc.models.AluminiModel;
import com.sdc.repo.AluminiRepository;

@Service
public class AluminiService {
	
	
     @Autowired
     private AluminiRepository aluminiRepo;

	public Boolean saveAlumini(AluminiModel model) {
	
		Alumini newAlumini = new Alumini();
		
		newAlumini.setAluminiName(model.getAluminiName());
		newAlumini.setCompanyName(model.getCompanyName());
		newAlumini.setLpa(model.getLpa());
		newAlumini.setContent(model.getContent());
		
	      try {
	            if (model.getImage() != null && !model.getImage().isEmpty()) {
	                newAlumini.setImage(model.getImage().getBytes());
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("Error reading image file", e);
	        }

	      aluminiRepo.save(newAlumini);

		return true;
	}

	
	
}
