package com.sdc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applicationform")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactNumber;
    private String email;
    private String year;
    private String branch;
    private String enrollmentNumber;
    private String position;
    private String pastExperience;
    private String resumePath;  // path of uploaded resume


}
