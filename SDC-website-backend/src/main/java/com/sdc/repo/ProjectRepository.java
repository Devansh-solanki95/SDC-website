package com.sdc.repo;

import com.sdc.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Projects, Integer> {
}
