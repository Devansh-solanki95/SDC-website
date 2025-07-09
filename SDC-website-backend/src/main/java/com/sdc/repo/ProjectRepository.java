package com.sdc.repo;

import com.sdc.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Projects, Integer> {
    @Query("SELECT p FROM Projects p LEFT JOIN FETCH p.teamMembers WHERE p.projectID = :id")
    Optional<Projects> findByIdWithMembers(@Param("id") Integer id);

    @Query("SELECT DISTINCT p FROM Projects p LEFT JOIN FETCH p.teamMembers")
    List<Projects> findAllTeamMembers();
}
