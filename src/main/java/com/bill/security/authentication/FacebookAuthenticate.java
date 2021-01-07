package com.bill.security.authentication;

import com.bill.security.user.UserCredential;
import com.bill.security.util.ResourceLoader;

public class FacebookAuthenticate implements IAuthenticate {

	@Override
	public String authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using Facebook Authenticate");
		return "redirect:" + getRedirectURL();
	}

	public String getRedirectURL() {
		return "http://www.facebook.com/dialog/oauth?client_id=" + ResourceLoader.getProperty("facebookId")
				+ "&redirect_uri=http://localhost:8080/&scope=email";
	}
}
