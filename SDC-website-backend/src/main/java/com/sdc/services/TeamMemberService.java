package com.sdc.services;

import com.sdc.entity.Projects;
import com.sdc.entity.TeamMember;
import com.sdc.models.TeamMemberModel;
import com.sdc.repo.ProjectRepo;
import com.sdc.repo.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ProjectRepo projectRepo;

    // Add Team Member with image
    public TeamMember addTeamMemberWithImage(TeamMemberModel model) {
        TeamMember member = mapToEntity(model);

        try {
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                member.setImage(model.getImage().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading image file", e);
        }

        List<Integer> ids = model.getProjectIds();
        if (ids != null && !ids.isEmpty()) {
            List<Projects> projects = projectRepo.findAllById(ids);
            if (projects.size() != ids.size()) {
                System.err.println("Some project IDs not found!");
            }
            member.setProjects(projects);

            for (Projects project : projects) {
                project.getTeamMembers().add(member);
            }
        }

        System.out.println("TeamMember Created: " + member.getName());
        return teamMemberRepository.save(member);
    }

    // Update only non-null fields
    public Optional<TeamMember> updateTeamMemberWithImage(Integer id, TeamMemberModel model) {
        return teamMemberRepository.findById(id).map(existing -> {

            if (StringUtils.hasText(model.getName())) {
                existing.setName(model.getName());
            }

            if (StringUtils.hasText(model.getBranch())) {
                existing.setBranch(model.getBranch());
            }

            if (StringUtils.hasText(model.getPosition())) {
                existing.setPosition(model.getPosition());
            }

            if (StringUtils.hasText(model.getLinkdin_url())) {
                existing.setLinkdin_url(model.getLinkdin_url());
            }

            if (StringUtils.hasText(model.getGithub_url())) {
                existing.setGithub_url(model.getGithub_url());
            }

            if (StringUtils.hasText(model.getInsta_url())) {
                existing.setInsta_url(model.getInsta_url());
            }

            try {
                MultipartFile imageFile = model.getImage();
                if (imageFile != null && !imageFile.isEmpty()) {
                    existing.setImage(imageFile.getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error updating image file", e);
            }

            List<Integer> ids = model.getProjectIds();
            if (ids != null && !ids.isEmpty()) {
                List<Projects> projects = projectRepo.findAllById(ids);

                if (projects.size() != ids.size()) {
                    System.err.println("Some project IDs not found.");
                }

                existing.setProjects(projects);

                for (Projects project : projects) {
                    if (!project.getTeamMembers().contains(existing)) {
                        project.getTeamMembers().add(existing);
                    }
                }
            }

            return teamMemberRepository.save(existing);
        });
    }

    // Delete
    public boolean deleteTeamMember(Integer id) {
        if (teamMemberRepository.existsById(id)) {
            teamMemberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Helper mapping (for add only)
    private TeamMember mapToEntity(TeamMemberModel model) {
        TeamMember member = new TeamMember();
        member.setName(model.getName());
        member.setBranch(model.getBranch());
        member.setPosition(model.getPosition());
        member.setLinkdin_url(model.getLinkdin_url());
        member.setGithub_url(model.getGithub_url());
        member.setInsta_url(model.getInsta_url());
        return member;
    }

    // Get All Members
    public List<TeamMember> getAll() {
        return teamMemberRepository.findAll();
    }
}
