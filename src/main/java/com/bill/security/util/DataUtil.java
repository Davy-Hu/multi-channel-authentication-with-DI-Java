package com.bill.security.util;

import java.util.HashMap;
import java.util.Map; 

public class DataUtil {

	private static Map<String, String> authentication_class_map= new HashMap<String, String>() {{
        put("com.bill.security.authentication.MySQLAuthenticate", "mySQL");
        put("com.bill.security.authentication.GoogleAuthenticate", "google");
        put("com.bill.security.authentication.FacebookAuthenticate", "facebook");
    }};
    
	
	public static String getFrom(String s) {
		return authentication_class_map.get(s);
	}

}
