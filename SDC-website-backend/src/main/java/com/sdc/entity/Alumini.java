package com.sdc.entity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "alumini")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Alumini {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  Integer aluminiId;
	
	@Column
	private String aluminiName;
	
	@Column
	private String lpa;
	
	@Column
	private String CompanyName;
	
	@Column
	private String Content;
	
	// Image stored as byte array in DB
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;

	public Alumini(String aluminiName, String lpa, String companyName, String content, byte[] image) {
		super();
		this.aluminiName = aluminiName;
		this.lpa = lpa;
		CompanyName = companyName;
		Content = content;
		this.image = image;
	}
	
	
}
