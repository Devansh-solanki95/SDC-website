package com.sdc.controller;

import com.sdc.entity.Admin;

import com.sdc.entity.Contact;
import com.sdc.entity.Images;
import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.models.TeamMemberModel;
import com.sdc.models.AdminModel;
import com.sdc.models.ForgetPasswordModel;
import com.sdc.repo.ContactRepository;
import com.sdc.services.AdminService;
import com.sdc.services.ProjectService;
import com.sdc.services.TeamMemberService;
import com.sdc.utils.ApiResponse;

import jakarta.annotation.security.PermitAll;

import com.sdc.models.UpdateAdminModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // applies to all methods
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("/saveAdmin")
    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody AdminModel model) {
        Boolean status = adminService.saveAdmin(model);
        return ResponseEntity.ok(new ApiResponse(status, status ? "Admin saved successfully" : "Admin not saved", model));
    }

    // ✅ Add Team Member (return Base64 image)
    @PostMapping(value = "/teamMember/add", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> addTeamMember(@ModelAttribute TeamMemberModel model) {
        TeamMember saved = teamMemberService.addTeamMemberWithImage(model);
        return ResponseEntity.ok(new ApiResponse(true, "Team Member added successfully", convertToResponse(saved)));
    }

    // ✅ Update Team Member (return Base64 image)
    @PutMapping(value = "/teamMember/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateTeamMember(@PathVariable Integer id, @ModelAttribute TeamMemberModel model) {
        Optional<TeamMember> updated = teamMemberService.updateTeamMemberWithImage(id, model);
        return updated
                .map(member -> ResponseEntity.ok(new ApiResponse(true, "Team Member updated successfully", convertToResponse(member))))
                .orElseGet(() -> ResponseEntity.ok(new ApiResponse(false, "Team Member not found", null)));
    }

//    @GetMapping("/teamMember/getAll")
//    public ResponseEntity<ApiResponse> getAllMembers() {
//        List<TeamMember> members = teamMemberService.getAll();
//        List<Map<String, Object>> responseList = members.stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(new ApiResponse(
//                true,
//                "Team members fetched successfully",
//                responseList
//            ));
//    }

    
    

    // ❌ Delete Team Member (no image needed here)
    @DeleteMapping("/teamMember/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTeamMember(@PathVariable Integer id) {
        boolean deleted = teamMemberService.deleteTeamMember(id);
        return ResponseEntity.ok(new ApiResponse(deleted, deleted ? "Team Member deleted successfully" : "Team Member not found", null));
    }

    // Assign Members to Project (no image needed here)
    @PostMapping("/project/{projectId}/assignMembers")
    public ResponseEntity<ApiResponse> assignTeamMembersToProject(
            @PathVariable Integer projectId,
            @RequestBody List<Integer> memberIds) {
        boolean success = projectService.assignTeamMembers(projectId, memberIds);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Team members assigned successfully" : "Project or members not found", memberIds));
    }
  
 // ✅ Convert TeamMember to JSON-friendly format with image and project titles
    private Map<String, Object> convertToResponse(TeamMember member) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("memberId", member.getMemberId());
        map.put("name", member.getName());
        map.put("branch", member.getBranch());
        map.put("position", member.getPosition());
        map.put("linkdin_url", member.getLinkdin_url());
        map.put("github_url", member.getGithub_url());
        map.put("insta_url", member.getInsta_url());
        map.put("imageBase64", member.getImage() != null ? Base64.getEncoder().encodeToString(member.getImage()) : null);

        // ✅ Add only the titles of associated projects
        List<String> projectTitles = member.getProjects() != null
                ? member.getProjects().stream()
                    .map(Projects::getTitle)
                    .collect(Collectors.toList())
                : new ArrayList<>();

        map.put("projectTitles", projectTitles);
        return map;
    }

    
    
    //contact api
    @GetMapping("/getAllContacts")
    public List<Contact> getAllContacts(){
        return contactRepository.findAll();
    }
    
    
    @PutMapping("/updateAdmin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateAdminProfile(@PathVariable Long id, @RequestBody UpdateAdminModel model) {
        Admin updatedAdmin = adminService.updateAdminProfile(id, model);

        if (updatedAdmin != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Admin profile updated successfully", updatedAdmin));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Admin not found with this ID", null));
        }
    }

    @PutMapping("/updatePassword/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse> forgetPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        Admin updatedAdmin = adminService.updatePasswordById(id, newPassword);

        if (updatedAdmin != null) {
            return ResponseEntity.ok(
                new ApiResponse(true, "Password updated successfully", updatedAdmin)
            );
        } else {
            return ResponseEntity.ok(
                new ApiResponse(false, "Admin not found with this ID", null)
            );
        }
    }
   


}
