package com.sdc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdc.entity.Alumini;

@Repository
public interface AluminiRepository extends JpaRepository<Alumini, Integer> {

}
