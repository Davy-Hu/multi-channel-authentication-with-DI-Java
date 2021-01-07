package com.bill.security.authentication;

import com.bill.security.user.UserCredential;

public interface IAuthenticate {
	String authenticate(UserCredential user);
}
