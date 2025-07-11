package com.sdc.models;
import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import com.sdc.entity.TeamMember;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectModel {
	

	private String title;
    private String link;
    private String description;
	private MultipartFile image;
    
    private List<Integer> teamMemberIds;
  //  private List<TeamMember> projects;
    
}
