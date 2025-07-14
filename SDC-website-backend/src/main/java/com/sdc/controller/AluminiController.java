package com.sdc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.entity.Alumini;
import com.sdc.models.AluminiModel;
import com.sdc.repo.AluminiRepository;
import com.sdc.services.AluminiService;
import com.sdc.utils.ApiResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin/alumini")
@PreAuthorize("hasRole('ADMIN')") // applies to all methods
//@CrossOrigin(origins = "http://localhost:5173")
public class AluminiController {
	
	@Autowired
	private AluminiService aluminiService;
	
	@Autowired
	private AluminiRepository aluminiRepo;
	
	@PostMapping(value = "/saveAlumini", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> saveAlumini(@ModelAttribute AluminiModel model) {
		//TODO: process POST request
		Boolean status = aluminiService.saveAlumini(model);
		
		if(status == true)
		{
			return ResponseEntity.ok(new ApiResponse(status, "new Alumini saved", model));
		}
		
		return ResponseEntity.ok(new ApiResponse(false, "alumini not saved" , null));
	}
	
//	  @GetMapping("/getAll-Alumini")
//	    public ResponseEntity<ApiResponse> getAllAlumini() {
//	        List<Alumini> alumniList = aluminiRepo.findAll();
//
//	        List<Map<String, Object>> responseList = alumniList.stream().map(alumini -> {
//	            Map<String, Object> map = new LinkedHashMap<>();
//	            map.put("aluminiId", alumini.getAluminiId());
//	            map.put("aluminiName", alumini.getAluminiName());
//	            map.put("lpa", alumini.getLpa());
//	            map.put("companyName", alumini.getCompanyName());
//	            map.put("content", alumini.getContent());
//
//	            String imageBase64 = (alumini.getImage() != null && alumini.getImage().length > 0)
//	                    ? Base64.getEncoder().encodeToString(alumini.getImage())
//	                    : null;
//
//	            map.put("imageBase64", imageBase64);
//	            return map;
//	        }).collect(Collectors.toList());
//
//	        ApiResponse response = new ApiResponse(true, "Alumini fetched successfully", responseList);
//	        return ResponseEntity.ok(response);
//	    }
	  
	  @PutMapping(value = "/update-Alumini/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	  public ResponseEntity<ApiResponse> updateAlumini(
	          @PathVariable Integer id,
	          @ModelAttribute AluminiModel model) {

	      Optional<Alumini> optional = aluminiRepo.findById(id);

	      if (optional.isEmpty()) {
	          return ResponseEntity
	                  .status(HttpStatus.NOT_FOUND)
	                  .body(new ApiResponse(false, "Alumini with ID " + id + " not found", null));
	      }

	      try {
	          Alumini alumini = optional.get();

	          // ✅ Only update if non-null and not blank
	          if (model.getAluminiName() != null && !model.getAluminiName().isBlank()) {
	              alumini.setAluminiName(model.getAluminiName());
	          }

	          if (model.getLpa() != null && !model.getLpa().isBlank()) {
	              alumini.setLpa(model.getLpa());
	          }

	          if (model.getCompanyName() != null && !model.getCompanyName().isBlank()) {
	              alumini.setCompanyName(model.getCompanyName());
	          }

	          if (model.getContent() != null && !model.getContent().isBlank()) {
	              alumini.setContent(model.getContent());
	          }

	          if (model.getImage() != null && !model.getImage().isEmpty()) {
	              alumini.setImage(model.getImage().getBytes());
	          }

	          aluminiRepo.save(alumini);

	          return ResponseEntity.ok(new ApiResponse(true, "Alumini updated successfully", alumini));

	      } catch (IOException e) {
	          return ResponseEntity
	                  .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body(new ApiResponse(false, "Error updating alumini image", null));
	      }
	  }
	  
	  @DeleteMapping("/delete-alumini/{id}")
	  public ResponseEntity<ApiResponse> deleteAlumini(@PathVariable Integer id) {
	      Optional<Alumini> aluminiOptional = aluminiRepo.findById(id);

	      if (aluminiOptional.isEmpty()) {
	          return ResponseEntity
	                  .status(HttpStatus.NOT_FOUND)
	                  .body(new ApiResponse(false, "Alumini with ID " + id + " not found", null));
	      }

	      aluminiRepo.deleteById(id);

	      return ResponseEntity.ok(new ApiResponse(true, "Alumini deleted successfully", null));
	  }


	  
	}