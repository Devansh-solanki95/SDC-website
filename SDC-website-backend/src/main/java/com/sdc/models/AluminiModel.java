package com.sdc.models;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AluminiModel {
	
	private String aluminiName;
	private String lpa;
	private String companyName;
	private String content;
	
	private MultipartFile image;	

}
