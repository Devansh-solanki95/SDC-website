package com.sdc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

import jakarta.servlet.http.HttpServletResponse;

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
	    public ResponseEntity<ApiResponse> login(@RequestBody LoginModel request, HttpServletResponse response) {
	        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

	        String rawPassword = request.getPass();
	        String storedPassword = userDetails.getPassword();

	        boolean passwordMatches;

	        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
	            passwordMatches = passwordEncoder.matches(rawPassword, storedPassword);
	        } else {
	            passwordMatches = rawPassword.equals(storedPassword);
	        }

	        if (!passwordMatches) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse(false, "Invalid credentials", null));
	        }

	        UsernamePasswordAuthenticationToken authToken =
	            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(authToken);

	        System.err.println("came  in controller");

	        // ✅ Generate token
	        String token = jwtService.generateToken(userDetails);
	        System.out.println("Generated token: " + token);

	        // ✅ Set token in HttpOnly cookie
	        ResponseCookie cookie = ResponseCookie.from("jwt", token)
	            .httpOnly(true)
	            .secure(false) // make sure you use HTTPS in production
	            .path("/")
	            .maxAge(3600) // 1 hour
	            .sameSite("Lax") // Or "Lax" for dev with cross-origin
	            .build();

	        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

	        // ✅ Optionally return only admin details, no token
	        Map<String, Object> responseData = new HashMap<>();
	        if (userDetails instanceof AdminUserDetails adminUserDetails) {
	            Admin admin = adminUserDetails.getAdmin();
	            responseData.put("adminId", admin.getAdminId());
	            responseData.put("name", admin.getName());
	            responseData.put("email", admin.getEmail());
	        }

	        return ResponseEntity.ok(new ApiResponse(true, "Login successfull", responseData));
	    }

	    
	    @GetMapping("/verify-token")
	    public ResponseEntity<ApiResponse> verifyToken(@CookieValue(name = "jwt", required = false) String token) {
	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new ApiResponse(false, "Token is missing", null));
	        }

	        String username = jwtService.extractUsername(token);
	        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

	        if (!jwtService.isTokenValid(token, userDetails)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                                 .body(new ApiResponse(false,"Invalid Token",null));
	        }


	        // (Optional) check user's roles if needed
	        Map<String, Object> data = new HashMap<>();
	        data.put("email", userDetails.getUsername());

	        return ResponseEntity.ok(new ApiResponse(true, "Token is valid", data));
	    }

	    
	    
//	    
//	    @PostMapping("/logout")
//	    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
//	        // 🔒 Invalidate the JWT cookie by setting maxAge=0
//	        ResponseCookie cookie = ResponseCookie.from("jwt", "")
//	            .httpOnly(true)
//	            .secure(false) // change to true in production with HTTPS
//	            .path("/")
//	            .maxAge(0) // expires immediately
//	            .sameSite("Strict")
//	            .build();
//
//	        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//	        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully", null));
//	    }

	  
	}



