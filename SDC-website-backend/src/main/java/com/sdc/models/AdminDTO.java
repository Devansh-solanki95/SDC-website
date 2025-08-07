package com.sdc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor	
public class AdminDTO {
	
	private Long adminId;
	private String name;
	private String email;
	private String contact_no;
	private String password;

}
