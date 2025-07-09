package com.sdc.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Admin {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    
    @Column
    private String name;
    
    @Column(name="email" , nullable = false, unique = true)
    private String email;
    
    @Column
    private String contactNo;
    
    @Column
    private String password;

	public Admin(String name, String email, String contactNo, String password) {
		super();
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.password = password;
	}
    

}