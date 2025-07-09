package com.sdc.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqModel {
	
	private String ques;
	
	private String ans;

//	public FaqModel(String ques, String ans) {
//		this.ques = ques;
//		this.ans = ans;
//	}
}
