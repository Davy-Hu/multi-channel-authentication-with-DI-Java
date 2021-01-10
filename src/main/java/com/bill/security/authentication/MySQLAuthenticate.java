package com.bill.security.authentication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.bill.security.user.UserCredential;
import com.bill.security.util.ResourceLoader;

public class MySQLAuthenticate implements IAuthenticate {
	
	@Override
	public String authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using MySQL Authenticate");
		
		String dbURL = ResourceLoader.getProperty("dbURL");
		String dbUser = ResourceLoader.getProperty("dbUser");
		String dbPassword = ResourceLoader.getProperty("dbPassword");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
	        String sql = "SELECT * FROM users WHERE email = ?";
	        
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, user.getEmail());
		     
	        ResultSet result = statement.executeQuery();        
	        
			if(result.next()) {
				if (BCrypt.checkpw(user.getPassword(), result.getString("password"))) {
					connection.close();
		            return "redirect:/";
		        }
				connection.close();
				return "redirect:/login";
			}else {
				connection.close();
				return "redirect:/login";
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean verifyUser(String code) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
