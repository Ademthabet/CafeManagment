package com.example.cafe.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.cafe.model.User;
import com.example.cafe.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
    private UserRepository userRepository;
	
	//private com.example.cafe.model.User userDetails;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		 if (user == null) {
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }
	        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
	                new ArrayList<>());
	}
	/*public com.example.cafe.model.User getUserDetail(){
		return userDetails;
	}*/
	public User getUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmail(email);
        }
        return null;
    }

}
