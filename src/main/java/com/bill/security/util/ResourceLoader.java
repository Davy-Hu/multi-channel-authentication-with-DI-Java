package com.bill.security.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {


	private static Map<String, String> properties;
	private static String path = "C:\\Users\\willi\\eclipse-workspace\\multi-channel-authentication\\src\\main\\resources\\application.yml";
	
	private static void LoadProperty(String path){
		try (InputStream input = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(input);
            if(properties == null) {
            	properties = new HashMap<>();
            }
            
            Enumeration<Object> allkeys = prop.keys();
            while(allkeys.hasMoreElements()) {
            	String key = (String) allkeys.nextElement();
            	String value = prop.getProperty(key);
            	properties.put(key, value);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public static String getProperty(String key) {
		if(properties == null) {
			LoadProperty(path);
		}
		if(!properties.containsKey(key)) {
			return null;
		}else {
			return properties.get(key);
		}
	}
	
	public static void main(String[]args) {
		System.out.println(getProperty("authenticationurls"));
	}
	
}

//1.
//get/set
//
//2. 
//getProperty(key)
//String DBuserID = prop.getProperty("DBuserID");
//String DBpwd = prop.getProperty("DBpwd");
//String DBURL = prop.getProperty("DBURL");
//String authenticationclasses = prop.getProperty("authenticationclasses");
//properties.put("DBuserID", DBuserID);
//properties.put("DBpwd", DBpwd);
//properties.put("DBURL", DBURL);
//properties.put("authenticationclasses", authenticationclasses);
//
//3. current
