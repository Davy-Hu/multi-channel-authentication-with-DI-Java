package com.bill.security.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bill.security.user.User;
import com.bill.security.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class AppController {
	
	@Autowired
    private UserRepository userRepo;
	
	@GetMapping(path = "/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new User());
	     
	    return "register";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	     
	    userRepo.save(user);
	     
	    return "register_success";
	}
	
}