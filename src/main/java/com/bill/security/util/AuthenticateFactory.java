package com.bill.security.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.bill.security.authentication.IAuthenticate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticateFactory {
	
	private static Map<String, IAuthenticate> authenticationClassMap;
	
	public static void constructClasses() {
		String classNames = ResourceLoader.getProperty("authenticationclasses");
		String[] classNameArray = classNames.split(",");
		for(String className:classNameArray) {
			try {
				Object o = Class.forName(className).getDeclaredConstructor().newInstance();
				IAuthenticate clas= (IAuthenticate)o;
				
				if(authenticationClassMap==null) {
					authenticationClassMap = new HashMap<String, IAuthenticate>();
				}
				String key = DataUtil.getFrom(className);
				authenticationClassMap.put(key, clas);
			}catch(Exception e) {
				e.printStackTrace();
			} 
			
		}
		
	}
	
	public static IAuthenticate getAuthenticationClass(String source) {
		if(authenticationClassMap==null) {
			constructClasses();
		}
		if(!authenticationClassMap.containsKey(source)) {
			return null;
		}else {
			return authenticationClassMap.get(source);
		}
		
	}
	
}
//1.
//if(from=="google"){
//	return new GoogleAuthentiaon();
//}
//
//2.
//current