package com.example.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafe.model.User;
import com.example.cafe.repository.UserRepository;
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
	
	@PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }
	 @GetMapping("/login")
	    public String login() {
	        return "Please login with your credentials";
	    }
}
