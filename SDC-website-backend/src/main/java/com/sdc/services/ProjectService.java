package com.sdc.services;

import com.sdc.entity.Projects;
import com.sdc.repo.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    public Projects saveProject(Projects project)
    {
        return projectRepo.save(project);
    }


    public List<Projects> getAllProjects() {
        return projectRepo.findAll();
    }

    public Projects getProjectById(Integer id) {
        return projectRepo.findById(id).orElse(null);
    }

    public boolean deleteProjectById(Integer id) {
        if (projectRepo.existsById(id)) {
            projectRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}
