package com.sdc.services;

import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.models.ProjectModel;
import com.sdc.repo.ProjectRepository;
import com.sdc.repo.TeamMemberRepository;

import ch.qos.logback.core.model.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

//    // Save project (create)
//    public Projects saveProject(Projects project) {
//        return projectRepo.save(project);
//    }

    // Get all projects
    public List<Projects> getAllProjects() {
        return projectRepo.findAll();
    }

    // Get single project by ID
    public Projects getProjectById(Integer id) {
        return projectRepo.findById(id).orElse(null);
    }

    // Delete project
    public boolean deleteProjectById(Integer id) {
        if (projectRepo.existsById(id)) {
            projectRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    //  Assign team members to a project (for bidirectional mapping)
    public boolean assignTeamMembers(Integer projectId, List<Integer> memberIds) {
        Optional<Projects> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isEmpty()) return false;

        Projects project = optionalProject.get();
        List<TeamMember> members = teamMemberRepository.findAllById(memberIds);

        // Set team members to the project
        project.setTeamMembers(members);

        // Optional: maintain reverse mapping to avoid issues
        for (TeamMember member : members) {
            if (!member.getProjects().contains(project)) {
                member.getProjects().add(project);
            }
        }

        projectRepo.save(project);  // Persist both sides
        return true;
    }

public Projects saveProject(ProjectModel model) {
    Projects project = new Projects();
    project.setTitle(model.getTitle());
    project.setDescription(model.getDescription());
    project.setLink(model.getLink());
    
    System.err.println("project object created");
  
    try {
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            project.setImage(model.getImage().getBytes());
    
        System.err.println("in the try block of image ");
        }
        
    } catch (IOException e) {
    	System.out.println("in project service");
        throw new RuntimeException("Error reading image file", e);
    }

System.err.println("after the try block ");
   List<Integer> ids = model.getTeamMemberIds();
   if(ids != null && !ids.isEmpty())
   {
    List<TeamMember> members = teamMemberRepository.findAllById(model.getTeamMemberIds());
    if(members.size() != ids.size() )
    {
    System.err.println(123445);
    }
    
    project.setTeamMembers(members);
    System.out.println(members);
   }
   
 
    
  
    return projectRepo.save(project);
}
}