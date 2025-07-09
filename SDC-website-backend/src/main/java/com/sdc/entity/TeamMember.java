package com.sdc.entity;

import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="teamMember")
@ToString(exclude = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TeamMember {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;
	
	@Column
	private String name;
	
	@Column
	private String branch;
	
	@Column
	private String position;
	
	@Column
	private String linkdin_url;
	
	@Column
	private String github_url;

	@Column
	private String insta_url;
	
    // Image stored as byte array in DB
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;
	
	
	@ManyToMany(mappedBy = "teamMembers")
	@JsonInclude(JsonInclude.Include.ALWAYS)
	@JsonIgnoreProperties("teamMembers") 
	private List<Projects> projects = new ArrayList<>() ;
	
}
