package com.bill.security.authentication;

import com.bill.security.user.UserCredential;

public interface IAuthenticate {
	void authenticate(UserCredential user);
}
