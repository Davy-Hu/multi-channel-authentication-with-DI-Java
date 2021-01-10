package com.bill.security.authentication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.bill.security.user.UserCredential;
import com.bill.security.util.ResourceLoader;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONValue;

public class FacebookAuthenticate implements IAuthenticate {

	@Override
	public String authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using Facebook Authenticate");
		return "redirect:" + getRedirectURL();
	}

	public String getRedirectURL() {
		return "http://www.facebook.com/dialog/oauth?client_id=" + ResourceLoader.getProperty("facebookId")
				+ "&redirect_uri=http://localhost:8080/facebook/callback&scope=email";
	}

	public boolean verifyUser(String code) throws IOException {
		String email = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpGet get_request = new HttpGet("https://graph.facebook.com/oauth/access_token?client_id="
					+ ResourceLoader.getProperty("facebookId")
					+ "&redirect_uri=http://localhost:8080/facebook/callback&client_secret="
					+ ResourceLoader.getProperty("facebookSecret") + "&code=" + code);
			get_request.addHeader("content-type", "application/json");
			HttpResponse get_response = httpClient.execute(get_request);
			String returned_json = EntityUtils.toString(get_response.getEntity());
			JSONObject object = (JSONObject) JSONValue.parse(returned_json);
			String access_token = (String) object.get("access_token");

			HttpGet get_request_2 = new HttpGet(
					"https://graph.facebook.com/me?fields=email&access_token=" + access_token);
			get_request_2.addHeader("content-type", "application/json");
			HttpResponse get_response_2 = httpClient.execute(get_request_2);
			String returned_json_2 = EntityUtils.toString(get_response_2.getEntity());
			JSONObject object_2 = (JSONObject) JSONValue.parse(returned_json_2);
			email = (String) object_2.get("email");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		if (email != null) {
			String dbURL = ResourceLoader.getProperty("dbURL");
			String dbUser = ResourceLoader.getProperty("dbUser");
			String dbPassword = ResourceLoader.getProperty("dbPassword");

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
				String sql = "SELECT * FROM users WHERE email = ?";

				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, email);

				ResultSet result = statement.executeQuery();

				if (result.next()) {
					return true;
				} else {
					connection.close();
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
