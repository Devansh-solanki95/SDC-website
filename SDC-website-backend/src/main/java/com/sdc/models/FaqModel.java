package com.sdc.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FaqModel {

	private String ques;

	private String ans;
}


//	public FaqModel(String ques, String ans) {
//		this.ques = ques;
//		this.ans = ans;
//	}

