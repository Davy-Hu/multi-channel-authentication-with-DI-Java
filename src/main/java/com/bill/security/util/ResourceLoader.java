package com.bill.security.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ResourceLoader {

	private static Map<String, String> properties;
	private static String path = "C:\\Users\\willi\\eclipse-workspace\\multi-channel-authentication\\src\\main\\resources\\application.yml";

	private static void LoadProperty(String path) {
		try (InputStream input = new FileInputStream(path)) {
			Properties prop = new Properties();
			prop.load(input);
			if (properties == null) {
				properties = new HashMap<>();
			}

			Enumeration<Object> allkeys = prop.keys();
			while (allkeys.hasMoreElements()) {
				String key = (String) allkeys.nextElement();
				String value = prop.getProperty(key);
				properties.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		if (properties == null) {
			LoadProperty(path);
		}
		if (!properties.containsKey(key)) {
			return null;
		} else {
			return properties.get(key);
		}
	}

	public static void main(String[] args) {
		System.out.println(getProperty("authenticationurls"));
	}

}
