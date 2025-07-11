package com.sdc.controller;

import com.sdc.entity.Projects;
import com.sdc.models.ProjectModel;
import com.sdc.services.ProjectService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/projects")
@CrossOrigin(origins = "http://localhost:5173")

public class ProjectController {	

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadProject(@ModelAttribute ProjectModel model) {
        try {
            Projects saved = projectService.saveProject(model);
            return ResponseEntity.ok(new ApiResponse(true, "Project saved", convertToResponse(saved)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Project not saved", null));
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateProjectWithImage(
            @PathVariable Integer id,
            @ModelAttribute ProjectModel model) {

        try {
            return projectService.updateProjectWithImage(id, model)
                    .map(updated -> ResponseEntity.ok(new ApiResponse(true, "Project updated", convertToResponse(updated))))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(false, "Project not found", null)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating project", null));
        }
    }

    @DeleteMapping("/deleteproject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok("Project deleted successfully");
    }

//    @GetMapping("/allproject")
//    public ResponseEntity<ApiResponse> getAllProjects() {
//        List<Projects> projects = projectService.getAllProjects();
//
//        List<Object> modified = projects.stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(new ApiResponse(true, "Projects fetched successfully", modified));
//    }

    
    
    private Map<String, Object> convertToResponse(Projects project) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("projectID", project.getProjectID());
        map.put("title", project.getTitle());
        map.put("description", project.getDescription());
        map.put("link", project.getLink());

        // ✅ Project image base64 (match frontend key)
        if (project.getImage() != null && project.getImage().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(project.getImage());
            map.put("imageBase64", base64Image); // ✅ Correct key
        } else {
            map.put("imageBase64", null);
        }

        // Team Members with image
        map.put("teamMembers", project.getTeamMembers().stream().map(member -> {
            Map<String, Object> tm = new LinkedHashMap<>();
            tm.put("memberId", member.getMemberId());
            tm.put("name", member.getName());
            tm.put("branch", member.getBranch());
            tm.put("position", member.getPosition());
            tm.put("linkdin_url", member.getLinkdin_url());
            tm.put("github_url", member.getGithub_url());
            tm.put("insta_url", member.getInsta_url());

            if (member.getImage() != null && member.getImage().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(member.getImage());
                tm.put("imageBase64", base64Image); // ✅ for team member too
            } else {
                tm.put("imageBase64", null);
            }

            return tm;
        }).collect(Collectors.toList()));

        return map;
    }
}
