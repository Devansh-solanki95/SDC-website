package com.sdc.models;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
public class TeamMemberModel {
	
	private String name;
    private String branch;
    private String position;
    private String linkdin_url;
    private String github_url;
    private String insta_url;
    private MultipartFile image;  //  Added for image upload
    
    private List<Integer> projectIds;
  //  private List<ProjectModel> projects;
    
}
