package com.sdc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.config.JwtService;
import com.sdc.entity.Admin;
import com.sdc.models.AdminModel;
import com.sdc.models.LoginModel;
import com.sdc.services.AdminService;
import com.sdc.services.CustomUserDetailsService;
import com.sdc.utils.AdminUserDetails;
import com.sdc.utils.ApiResponse;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
	
	   @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtService jwtService;
	    
	    @Autowired
	    private AdminService adminService;
	    
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private CustomUserDetailsService customUserDetailsService;

	    @PostMapping("/login")
	    public ResponseEntity<ApiResponse> login(@RequestBody LoginModel request) {
	        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

	        String rawPassword = request.getPass();
	        String storedPassword = userDetails.getPassword(); // could be encoded or plain text

	        boolean passwordMatches;

	        // Determine if stored password is encoded (e.g., BCrypt format)
	        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
	            passwordMatches = passwordEncoder.matches(rawPassword, storedPassword);
	        } else {
	            passwordMatches = rawPassword.equals(storedPassword);
	        }

	        if (!passwordMatches) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse(false, "Invalid credentials", null));
	        }

	        // ✅ Set authentication in SecurityContext
	        UsernamePasswordAuthenticationToken authToken =
	            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(authToken);

	        System.err.println("came  in controller");

	        // ✅ Generate JWT token
	        String token = jwtService.generateToken(userDetails);
	        System.out.println("Generated token: " + token);

	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("token", token);

	        if (userDetails instanceof AdminUserDetails adminUserDetails) {
	            Admin admin = adminUserDetails.getAdmin();
	            responseData.put("adminId", admin.getAdminId());
	            responseData.put("name", admin.getName());
	            responseData.put("email", admin.getEmail());
	        }

	        return ResponseEntity.ok(new ApiResponse(true, "Login successful", responseData));
	    }


	    
	  
	}



