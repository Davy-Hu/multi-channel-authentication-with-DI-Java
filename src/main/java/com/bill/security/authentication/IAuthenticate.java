package com.bill.security.authentication;

import java.io.IOException;

import com.bill.security.user.UserCredential;

public interface IAuthenticate {
	String authenticate(UserCredential user);
	boolean verifyUser(String code) throws IOException;
}
