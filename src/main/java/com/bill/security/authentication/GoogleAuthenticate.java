package com.bill.security.authentication;

import com.bill.security.user.UserCredential;
import com.bill.security.util.ResourceLoader;

public class GoogleAuthenticate implements IAuthenticate {

	@Override
	public String authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using Google Authenticate");
		return "redirect:"+getRedirectURL();
	}

	public String getRedirectURL() {
		return "https://accounts.google.com/o/oauth2/v2/auth?" + "scope=email%20profile&"
				+ "response_type=code&state=security_token%3D138r5719ru3e1%26url%3Dhttps%3A%2F%2Foauth2.example.com%2Ftoken"
				+ "&redirect_uri=http://localhost:8080/" + "&client_id=" + ResourceLoader.getProperty("googleId");
	}

}
