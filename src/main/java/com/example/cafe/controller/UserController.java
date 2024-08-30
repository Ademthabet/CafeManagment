package com.example.cafe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafe.model.User;
import com.example.cafe.repository.UserRepository;
import com.example.cafe.security.JwtUtils;
import com.example.cafe.service.CustomUserDetailsService;
@RequestMapping(path ="/user")
@RestController
public class UserController {
	@Autowired
	CustomUserDetailsService userService;
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtils jwtUtil;
	
	@PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
		if(userRepository.findByEmail(user.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
		}
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap) {
	    try {
	        Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
	        );

	        if (auth.isAuthenticated()) {
	            // Directly retrieve the user from the repository after successful authentication
	            User user = userRepository.findByEmail(requestMap.get("email"));
	            if (user.getStatus().equalsIgnoreCase("true")) {
	                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
	                return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("{\"message\":\"Wait for admin approval.\"}", HttpStatus.BAD_REQUEST);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return new ResponseEntity<>("{\"message\":\"Bad credentials.\"}", HttpStatus.BAD_REQUEST);
	}
	/*@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap) {
	    try {
	        // Log the request map for debugging
	        System.out.println("Request Map: " + requestMap);

	        // Extract and validate email and password
	        String email = requestMap.get("email");
	        String password = requestMap.get("password");

	        if (email == null || password == null) {
	            return new ResponseEntity<>("{\"message\":\"Email or password is missing.\"}", HttpStatus.BAD_REQUEST);
	        }

	        // Perform authentication
	        Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(email, password)
	        );

	        if (auth.isAuthenticated()) {
	            User user = userRepository.findByEmail(email);
	            if (user != null && user.getStatus().equalsIgnoreCase("true")) {
	                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
	                return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
	            } else if (user != null) {
	                return new ResponseEntity<>("{\"message\":\"Wait for admin approval.\"}", HttpStatus.BAD_REQUEST);
	            } else {
	                return new ResponseEntity<>("{\"message\":\"User not found.\"}", HttpStatus.BAD_REQUEST);
	            }
	        }
	    } catch (BadCredentialsException e) {
	        return new ResponseEntity<>("{\"message\":\"Invalid email or password.\"}", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("{\"message\":\"An error occurred during login.\"}", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return new ResponseEntity<>("{\"message\":\"Bad credentials.\"}", HttpStatus.BAD_REQUEST);
	}*/
}
