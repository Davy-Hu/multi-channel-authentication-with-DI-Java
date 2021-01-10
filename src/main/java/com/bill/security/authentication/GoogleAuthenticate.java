package com.bill.security.authentication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.bill.security.user.UserCredential;
import com.bill.security.util.ResourceLoader;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONValue;

public class GoogleAuthenticate implements IAuthenticate {

	@Override
	public String authenticate(UserCredential user) {
		// TODO Auto-generated method stub
		System.out.println("Using Google Authenticate");
		return "redirect:" + getRedirectURL();
	}

	public String getRedirectURL() {
		return "https://accounts.google.com/o/oauth2/v2/auth?" + "scope=email%20profile&"
				+ "response_type=code&state=security_token%3D138r5719ru3e1%26url%3Dhttps%3A%2F%2Foauth2.example.com%2Ftoken"
				+ "&redirect_uri=http://localhost:8080/google/callback" + "&client_id="
				+ ResourceLoader.getProperty("googleId");
	}

	public boolean verifyUser(String code) throws IOException {
		String email = null;

		JSONObject google_params = new JSONObject();
		google_params.put("code", code);
		google_params.put("client_id", "316649002787-pj5ruij4ppogj900cppl4sjjn4ov06c2.apps.googleusercontent.com");
		google_params.put("client_secret", "zKr-gkjsVnxut-zTZoSxO0aN");
		google_params.put("redirect_uri", "http://localhost:8080/google/callback");
		google_params.put("grant_type", "authorization_code");

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost post_request = new HttpPost("https://oauth2.googleapis.com/token");
			StringEntity params = new StringEntity(google_params.toString());
			post_request.addHeader("content-type", "application/json");
			post_request.setEntity(params);
			HttpResponse post_response = httpClient.execute(post_request);
			String returned_json = EntityUtils.toString(post_response.getEntity());
			JSONObject object = (JSONObject) JSONValue.parse(returned_json);
			String id_token = (String) object.get("id_token");

			HttpGet get_request = new HttpGet("https://oauth2.googleapis.com/tokeninfo?id_token=" + id_token);
			get_request.addHeader("content-type", "application/json");
			HttpResponse get_response = httpClient.execute(get_request);
			String returned_json_2 = EntityUtils.toString(get_response.getEntity());
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
