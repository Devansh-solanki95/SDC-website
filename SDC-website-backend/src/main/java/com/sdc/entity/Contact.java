package com.sdc.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Contact {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer contactId;
		
	    @Column
		private String name;
		 
		 @Column 
	    private String email;
		 
		 @Column
	    private String contactNo;
		 
		 @Column
		private String query;
		 
		 @Column(columnDefinition = "MEDIUMTEXT")
		private String message;

	public Contact(String name, String email, String contactNo, String query, String message) {
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.query = query;
		this.message = message;
	}
}

