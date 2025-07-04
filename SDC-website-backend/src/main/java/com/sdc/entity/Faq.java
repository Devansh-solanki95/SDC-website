package com.sdc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="faq")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Faq {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String ques;

	@Column
	private String ans;

	public Faq(String ques, String ans) {
		super();
		this.ques = ques;
		this.ans = ans;
	}
	
	
	
}
