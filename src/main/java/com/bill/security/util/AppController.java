package com.bill.security.util;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bill.security.authentication.IAuthenticate;
import com.bill.security.user.User;
import com.bill.security.user.UserCredential;
import com.bill.security.user.UserRepository;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login/{source}")
	public String processLogin(@PathVariable String source, @RequestParam(required = false) String email,
			@RequestParam(required = false) String password) throws SQLException, ClassNotFoundException {
		UserCredential user = new UserCredential();
		if (source.equals("mySQL")) {
			user.setEmail(email);
			user.setPassword(password);
		}

		IAuthenticate authenticate = AuthenticateFactory.getAuthenticationClass(source);
		return authenticate.authenticate(user);
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepo.save(user);

		return "register_success";
	}

	@GetMapping("/{source}/callback")
	public String callback(@PathVariable String source, @RequestParam(required = false) String code)
			throws IOException {
		IAuthenticate authenticate = AuthenticateFactory.getAuthenticationClass(source);
		if (authenticate.verifyUser(code)) {
			return "redirect:/";
		} else {
			return "redirect:/login";
		}

	}

	@GetMapping("/getproperty/{property}")
	@ResponseBody
	public String getProperty(@PathVariable String property) {
		return ResourceLoader.getProperty(property);
	}

}