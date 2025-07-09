package com.sdc.controller;


import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;

import com.sdc.models.AdminModel;
import com.sdc.models.TeamMemberModel;
import com.sdc.services.AdminService;
import com.sdc.services.ProjectService;
import com.sdc.services.TeamMemberService;
import com.sdc.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // applies to all methods
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeamMemberService teamMemberService;
    
    @Autowired
    private ProjectService projectService;


   
    @PostMapping("/saveAdmin")
    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody AdminModel model) {
        Boolean status = adminService.saveAdmin(model);

        if (status) {
            return ResponseEntity.ok(new ApiResponse(true, "Admin saved successfully", model));
        }
        return ResponseEntity.ok(new ApiResponse(false, "Admin not saved", model));
    }


    //  Add Team Member with Image
    @PostMapping(value = "/teamMember/add", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> addTeamMember(@ModelAttribute TeamMemberModel model) {
        TeamMember saved = teamMemberService.addTeamMemberWithImage(model);
        return ResponseEntity.ok(new ApiResponse(true, "Team Member added successfully", saved));
    }

    // Update Team Member with optional image
    @PutMapping(value = "/teamMember/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateTeamMember(@PathVariable Integer id, @ModelAttribute TeamMemberModel model) {
        Optional<TeamMember> updated = teamMemberService.updateTeamMemberWithImage(id, model);
        if (updated.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Team Member updated successfully", updated.get()));
        }
        return ResponseEntity.ok(new ApiResponse(false, "Team Member not found", null));
    }

    //  Delete
    @DeleteMapping("/teamMember/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTeamMember(@PathVariable Integer id) {
        boolean deleted = teamMemberService.deleteTeamMember(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse(true, "Team Member deleted successfully", null));
        }
        return ResponseEntity.ok(new ApiResponse(false, "Team Member not found", null));
    }
    
 // Get All Team Members
    @GetMapping("/teamMember/getAll")
    public ResponseEntity<List<TeamMember>> getAllMembers() {
        return ResponseEntity.ok(teamMemberService.getAll());
    }
    
	/*
	 * @PostMapping("/project/add") public ResponseEntity<ApiResponse>
	 * addProject(@RequestBody Projects project) { Projects saved =
	 * projectService.saveProject(project); return ResponseEntity.ok(new
	 * ApiResponse(true, "Project added successfully", saved)); }
	 */

    @PostMapping("/project/{projectId}/assignMembers")
    public ResponseEntity<ApiResponse> assignTeamMembersToProject(
            @PathVariable Integer projectId,
            @RequestBody List<Integer> memberIds) {
        
        boolean success = projectService.assignTeamMembers(projectId, memberIds);
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Team members assigned successfully", memberIds ));
        }
        return ResponseEntity.ok(new ApiResponse(false, "Project or members not found", null));
    }

    
}