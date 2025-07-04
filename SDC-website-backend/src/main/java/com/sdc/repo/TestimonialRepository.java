package com.sdc.repo;

import com.sdc.entity.Testimonials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonials,Long > {
    //all crud database methods
}

