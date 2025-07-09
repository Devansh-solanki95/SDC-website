package com.sdc.entity;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "projects")
@ToString(exclude = "teamMembers")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Projects {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectID;

	@Column
	private String title;

	// Image stored as byte array in DB
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;

	@Column(length = 1000)
	private String description;

	@Column
	private String link;

	
	@ManyToMany
	@JoinTable(
			name = "project_team_member",
			joinColumns = @JoinColumn(name = "projectID"),
			inverseJoinColumns = @JoinColumn(name = "memberId")
	)

	@JsonInclude(JsonInclude.Include.ALWAYS)
	@JsonIgnoreProperties("projects")   
	private List<TeamMember> teamMembers = new ArrayList<>();
}
