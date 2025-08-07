package com.sdc.controller;

import com.sdc.entity.Admin;
import com.sdc.entity.ApplicationForm;
import com.sdc.entity.Contact;
import com.sdc.entity.Images;
import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.models.TeamMemberModel;
import com.sdc.models.AdminDTO;
import com.sdc.models.AdminModel;
import com.sdc.models.ForgetPasswordModel;
import com.sdc.repo.ContactRepository;
import com.sdc.services.AdminService;
import com.sdc.services.ApplicationFormService;
import com.sdc.services.ProjectService;
import com.sdc.services.TeamMemberService;
import com.sdc.utils.ApiResponse;

import jakarta.annotation.security.PermitAll;

import com.sdc.models.UpdateAdminModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // applies to all methods
//@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ApplicationFormService applicationFormService;

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
    
    
    @PutMapping("/updateAdmin")
    public ResponseEntity<ApiResponse> updateAdminProfile(
            @RequestBody UpdateAdminModel model,
            Principal principal
    ) {
        // Get logged-in admin's email from JWT via Principal
        String email = principal.getName();

        // Find the admin using email
        Admin admin = adminService.findByUsername(email);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Unauthorized or admin not found", null));
        }

        // Update profile fields
        Admin updatedAdmin = adminService.updateAdminProfile(admin.getAdminId(), model);

        if (updatedAdmin != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Admin profile updated successfully", updatedAdmin));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update profile", null));
        }
    }


    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody Map<String, String> body,
            Principal principal
    ) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Old and new passwords are required", null)
            );
        }

        String email = principal.getName(); // authenticated user's email from JWT
        Admin admin = adminService.findByUsername(email);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse(false, "Admin not found", null)
            );
        }

        String currentPassword = admin.getPassword();

        boolean passwordMatches;

        // Determine if stored password is encoded (typically starts with $2a$ or $2b$ for BCrypt)
        if (currentPassword.startsWith("$2a$") || currentPassword.startsWith("$2b$")) {
            // Encoded password
            passwordMatches = passwordEncoder.matches(oldPassword, currentPassword);
        } else {
            // Plain text stored in DB (not recommended)
            passwordMatches = oldPassword.equals(currentPassword);
        }

        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse(false, "Old password is incorrect", null)
            );
        }

        // Always save new password in encoded form
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminService.save(admin);

        return ResponseEntity.ok(
                new ApiResponse(true, "Password updated successfully", null)
        );
    }
    
    
    @GetMapping("/application-form/getAll")
    public ResponseEntity<ApiResponse> getAllForms() {
        List<ApplicationForm> list = applicationFormService.getAllForms();

        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "No applications found", list));
        }

        // Convert each ApplicationForm to response map
        List<Map<String, Object>> responseData = list.stream().map(form -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", form.getId());
            map.put("name", form.getName());
            map.put("contactNumber", form.getContactNumber());
            map.put("email", form.getEmail());
            map.put("year", form.getYear());
            map.put("branch", form.getBranch());
            map.put("enrollmentNumber", form.getEnrollmentNumber());
            map.put("position", form.getPosition());
            map.put("pastExperience", form.getPastExperience());

            // ✅ Only file name, not full path
            String resumePath = form.getResumePath();
            String fileName = (resumePath != null) ? Paths.get(resumePath).getFileName().toString() : null;
            map.put("resumePath", fileName);

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Applications fetched successfully", responseData));
    }

    @GetMapping("/application-form/get/{id}")
    public ResponseEntity<ApiResponse> getFormById(@PathVariable Long id) {
        return applicationFormService.getFormById(id)
                .map(form -> ResponseEntity.ok(new ApiResponse(true, "Form found", form)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Application form not found", null)));
    }

    // ❌ Delete
    @DeleteMapping("/application-form/delete/{id}")
    public ResponseEntity<ApiResponse> deleteForm(@PathVariable Long id) {
        try {
            Optional<ApplicationForm> optional = applicationFormService.getFormById(id);
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Application form not found", null));
            }
            applicationFormService.deleteForm(id);
            return ResponseEntity.ok(new ApiResponse(true, "Application form deleted successfully", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Something went wrong while deleting", null));
        }
    }
    

	@GetMapping("/all-admins")    
	public ResponseEntity<ApiResponse> getAllAdmins()
	{
	
		try {
            // Fetch all admins
            List<Admin> admins = adminService.findAllAdmins();

            // Convert to DTO to avoid exposing sensitive data like password
            List<AdminDTO> adminDTOs = admins.stream()
                .map(admin -> new AdminDTO(
                    admin.getAdminId(),
                    admin.getName(),
                    admin.getEmail(),
                    admin.getContact_no(),
                    admin.getPassword()
                ))
                .collect(Collectors.toList());

            // Create API response
            ApiResponse response = new ApiResponse(
                true,
                "Admins retrieved successfully",
                adminDTOs
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse(
                false,
                "Error retrieving admins: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
		
	}
   



