package com.sdc.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.entity.Alumini;
import com.sdc.entity.Faq;
import com.sdc.entity.Images;
import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.entity.Testimonials;
import com.sdc.repo.AluminiRepository;
import com.sdc.services.FaqService;
import com.sdc.services.ImagesService;
import com.sdc.services.ProjectService;
import com.sdc.services.TeamMemberService;
import com.sdc.services.TestimonialsService;
import com.sdc.utils.ApiResponse;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TeamMemberService teamMemberService;
	
	@Autowired
	private TestimonialsService testimonialsService;
	
	@Autowired
	private AluminiRepository aluminiRepo;
	
	@Autowired
	private FaqService faqService;
	
	@Autowired
	private ImagesService imagesService;
	
	
    @GetMapping("/allproject")
    public ResponseEntity<ApiResponse> getAllProjects() {
        List<Projects> projects = projectService.getAllProjects();

        List<Object> modified = projects.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Projects fetched successfully", modified));
    }

    
    //convert for project fetch
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
    
    
    //get team members
    
    @GetMapping("/teamMember/getAll")
    public ResponseEntity<ApiResponse> getAllMembers() {
        List<TeamMember> members = teamMemberService.getAll();
        List<Map<String, Object>> responseList = members.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(
                true,
                "Team members fetched successfully",
                responseList
            ));
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
    
    
    
    //
    //Get ALL TESTIMONIES
    // ✅ Get all testimonials with base64 images
    @GetMapping("/testimonies/All")
    public ResponseEntity<ApiResponse> getAllTestimonials() {
        List<Testimonials> list = testimonialsService.getAllTestimonials();
        List<Map<String, Object>> response = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Testimonials fetched successfully", response));
    }

    // ✅ Convert to JSON-friendly format with base64 image
    private Map<String, Object> convertToResponse(Testimonials t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("testId", t.getTestId());
        map.put("clientName", t.getClientName());
        map.put("des", t.getDes());

        if (t.getImage() != null && t.getImage().length > 0) {
            map.put("imageBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(t.getImage()));
        } else {
            map.put("imageBase64", null);
        }

        return map;
    }
    
    
    
    
    //
    //Get all aluminies
    @GetMapping("/getAll-Alumini")
    public ResponseEntity<ApiResponse> getAllAlumini() {
        List<Alumini> alumniList = aluminiRepo.findAll();

        List<Map<String, Object>> responseList = alumniList.stream().map(alumini -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("aluminiId", alumini.getAluminiId());
            map.put("aluminiName", alumini.getAluminiName());
            map.put("lpa", alumini.getLpa());
            map.put("companyName", alumini.getCompanyName());
            map.put("content", alumini.getContent());

            String imageBase64 = (alumini.getImage() != null && alumini.getImage().length > 0)
                    ? Base64.getEncoder().encodeToString(alumini.getImage())
                    : null;

            map.put("imageBase64", imageBase64);
            return map;
        }).collect(Collectors.toList());

        ApiResponse response = new ApiResponse(true, "Alumini fetched successfully", responseList);
        return ResponseEntity.ok(response);
    }
    
    
    //to get all faqs
    @GetMapping("/allfaq")
    public ResponseEntity<ApiResponse> getAllFaqs() {
        List<Faq> list = faqService.getAllFaqs();
        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "No FAQs found", list));
        } else {
            return ResponseEntity.ok(new ApiResponse(true, "FAQs fetched successfully", list));
        }
    }
    
    //
    //

    
    @GetMapping("/images/getAll")
    public ResponseEntity<ApiResponse> getAllImages() {
        List<Images> images = imagesService.getAllImages();

        List<Map<String, Object>> response = images.stream().map(img -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", img.getId());
            map.put("title", img.getTitle());

            if (img.getImage() != null && img.getImage().length > 0) {
                map.put("imageBase64", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(img.getImage()));
            } else {
                map.put("imageBase64", null);
            }

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "All images fetched", response));
    }
}
