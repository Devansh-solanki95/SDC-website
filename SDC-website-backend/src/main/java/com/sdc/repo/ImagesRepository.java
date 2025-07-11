package com.sdc.repo;

import com.sdc.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Integer> {

    
    Images findByTitle(String title);
}
