package com.sdc.repo;

import com.sdc.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeammemberRepository extends JpaRepository<TeamMember, Integer> {
}

