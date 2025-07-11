package com.sdc.services;

import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.models.ProjectModel;
import com.sdc.repo.ProjectRepo;
import com.sdc.repo.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    // Get all projects with members
    public List<Projects> getAllProjects() {
        return projectRepo.findAllWithTeamMembers();
    }

    // Get single project
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

    // Assign team members
    public boolean assignTeamMembers(Integer projectId, List<Integer> memberIds) {
        Optional<Projects> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isEmpty()) return false;

        Projects project = optionalProject.get();
        List<TeamMember> members = teamMemberRepository.findAllById(memberIds);

        project.setTeamMembers(members);

        for (TeamMember member : members) {
            if (!member.getProjects().contains(project)) {
                member.getProjects().add(project);
            }
        }

        projectRepo.save(project);
        return true;
    }

    // Save new project with image and team
    public Projects saveProject(ProjectModel model) {
        Projects project = new Projects();

        project.setTitle(model.getTitle());
        project.setDescription(model.getDescription());
        project.setLink(model.getLink());

        try {
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                project.setImage(model.getImage().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading image file", e);
        }

        List<Integer> ids = model.getTeamMemberIds();
        if (ids != null && !ids.isEmpty()) {
            List<TeamMember> members = teamMemberRepository.findAllById(ids);
            if (members.size() != ids.size()) {
                System.err.println("Some team member IDs not found!");
            }
            project.setTeamMembers(members);
        }

        return projectRepo.save(project);
    }

    // Update project with only non-null fields
    public Optional<Projects> updateProjectWithImage(Integer id, ProjectModel model) {
        return projectRepo.findById(id).map(existing -> {

            // Safe updates — only overwrite non-null and non-empty values
            if (StringUtils.hasText(model.getTitle())) {
                existing.setTitle(model.getTitle());
            }

            if (StringUtils.hasText(model.getDescription())) {
                existing.setDescription(model.getDescription());
            }

            if (StringUtils.hasText(model.getLink())) {
                existing.setLink(model.getLink());
            }

            try {
                if (model.getImage() != null && !model.getImage().isEmpty()) {
                    existing.setImage(model.getImage().getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error updating project image", e);
            }

            // Update team members only if given
            List<Integer> ids = model.getTeamMemberIds();
            if (ids != null && !ids.isEmpty()) {
                List<TeamMember> members = teamMemberRepository.findAllById(ids);

                if (members.size() != ids.size()) {
                    System.err.println("Some team member IDs not found!");
                }

                existing.setTeamMembers(members);

                for (TeamMember member : members) {
                    if (!member.getProjects().contains(existing)) {
                        member.getProjects().add(existing);
                    }
                }
            }

            // Save and return
            projectRepo.save(existing);
            return projectRepo.findByIdWithMembers(existing.getProjectID())
                    .orElse(existing);
        });
    }
}
