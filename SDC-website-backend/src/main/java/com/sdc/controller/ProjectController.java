package com.sdc.controller;

import com.sdc.entity.Projects;
import com.sdc.models.ProjectModel;
import com.sdc.services.ProjectService;
import com.sdc.utils.ApiResponse;

import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController

@RequestMapping("/admin/projects")
@PreAuthorize("hasRole('ADMIN')") 
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/upload",  consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadProject(@ModelAttribute ProjectModel model) {

        try {
            Projects saved = projectService.saveProject(model);
            
            return ResponseEntity.ok(new ApiResponse(true,"project saved",saved));
        } 
        catch (Exception e) {
        	System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false,"project not saved",null));
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable Integer id) {
        Projects project = projectService.getProjectById(id);

        if (project == null || project.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        // You can customize this MIME type based on file type if needed
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG) // or IMAGE_PNG, etc.
                .body(project.getImage());
    }

    @DeleteMapping("/deleteproject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok("project deleted successfully");
    }
//    @PutMapping("/update")
//    public ResponseEntity<String> updateProject(@RequestBody Projects project) {
//       String res= projectService.updateProject(project);
//        return ResponseEntity.ok(res);
//    }
    @GetMapping("/allproject")
    public ResponseEntity<ApiResponse> getAllProject(ServletResponse servletResponse) {
        return ResponseEntity.ok(new ApiResponse( true,"project fetched",projectService.getAllProjects()));
    }
}
