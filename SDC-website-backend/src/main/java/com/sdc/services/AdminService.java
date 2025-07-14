package com.sdc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdc.entity.Admin;
import com.sdc.models.AdminModel;
import com.sdc.models.ForgetPasswordModel;
import com.sdc.models.UpdateAdminModel;
import com.sdc.repo.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Boolean saveAdmin(AdminModel model) {
		// TODO Auto-generated method stub
		
		String rawPassword = model.getPassword();
		String hashedPass = passwordEncoder.encode(rawPassword);
		
		//Admin newAdmin = new Admin();
		 Admin newAdmin = new Admin(model.getName(), model.getEmail(), model.getContact_no(), hashedPass);
		/*
		 * newAdmin.setContact_no(model.getContact_no());
		 * newAdmin.setEmail(model.getEmail()); newAdmin.setName(model.getName());
		 * newAdmin.setPassword(model.getPassword());
		 */
		
		adminRepo.save(newAdmin);
		
		System.out.println("admin saved successfully");
		
		return true;
	}
	
	public Admin updatePasswordById(Long id, String newPassword) {
	    Optional<Admin> optionalAdmin = adminRepo.findById(id);
	    if (optionalAdmin.isPresent()) {
	        Admin admin = optionalAdmin.get();
	        admin.setPassword(passwordEncoder.encode(newPassword));
	        return adminRepo.save(admin);
	    }
	    return null;
	}

	public Admin updateAdminProfile(Long adminId, UpdateAdminModel model) {
	    Optional<Admin> optionalAdmin = adminRepo.findById(adminId);

	    if (optionalAdmin.isPresent()) {
	        Admin admin = optionalAdmin.get();

	        if (model.getName() != null)
	            admin.setName(model.getName());

	        if (model.getEmail() != null)
	            admin.setEmail(model.getEmail());

	        if (model.getContact_no() != null)
	            admin.setContact_no(model.getContact_no());

//	        if (model.getPassword() != null && !model.getPassword().isEmpty())
//	            admin.setPassword(passwordEncoder.encode(model.getPassword()));

	        return adminRepo.save(admin);
	    }

	    return null;
	}

	public Admin findByUsername(String email) {
	    return adminRepo.findByEmail(email).orElse(null);
	}

	public void save(Admin admin) {
	    adminRepo.save(admin);
	}
}
