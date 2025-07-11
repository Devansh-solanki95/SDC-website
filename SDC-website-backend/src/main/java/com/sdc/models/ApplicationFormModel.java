package com.sdc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFormModel {

    private String name;
    private String contactNumber;
    private String email;
    private String year;
    private String branch;
    private String enrollmentNumber;
    private String position;
    private String pastExperience;
    private String resumePath;
}