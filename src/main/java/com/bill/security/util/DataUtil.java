package com.bill.security.util;

import java.util.Map;

import com.bill.security.authentication.GoogleAuthenticate;

import java.util.HashMap; 

public class DataUtil {

	private static Map<String, String> authentication_class_map= new HashMap<String, String>() {{
        put("com.bill.security.authentication.MySQLAuthenticate", "mySQL");
        put("com.bill.security.authentication.GoogleAuthenticate", "google");
        put("com.bill.security.authentication.FacebookAuthenticate", "facebook");
    }};
    
	
	public static String getFrom(String s) {
		return authentication_class_map.get(s);
	}
	
	public static void main(String [] args) {
		GoogleAuthenticate g = new GoogleAuthenticate();
		System.out.println(g.getClass().getName());
	}
	
}
