package com.sdc.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    private String name;
    private String email;
    private String contact_no;
    private String password;
	
	
	  public Admin(String name, String email, String contact_no, String password) {
	  this.adminId = adminId;
	  this.name = name; 
	  this.email = email; 
	  this.contact_no = contact_no;
	  this.password = password; 
	  }
	  
    
   
}
