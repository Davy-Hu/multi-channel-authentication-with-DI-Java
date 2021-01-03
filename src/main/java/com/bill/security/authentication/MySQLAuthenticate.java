package com.bill.security.authentication;

import com.bill.security.user.UserCredential;

public class MySQLAuthenticate implements IAuthenticate {

	@Override
	public void authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using MySQL Authenticate");
	}

}
