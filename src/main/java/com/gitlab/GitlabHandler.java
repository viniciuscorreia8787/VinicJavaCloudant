package com.gitlab;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GitlabHandler {


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		getIssuesFromGitlab("17085");

	}

	public static List<Issue> getIssuesFromGitlab(String projectId) throws IOException {

		List<Issue> issues = new Issue();
		Issue issue;
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		
		try {

			URL url = new URL("https://git.ng.bluemix.net/api/v4/projects/" + projectId + "/issues?per_page=100&private_token=bs6SNtSqrRJYZWnQD4zb");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			if (con.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ con.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(con.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				jsonArray = gson.fromJson(output, JsonArray.class);
				
				//System.out.println(jsonArray.get(0));
				
				for(Integer i=0; i<jsonArray.size(); i++) {
					//issues.add(gson.fromJson(jsonArray.get(i), Issue.class));
					//issue = gson.fromJson(jsonArray.get(i).toString(), Issue.class);
					issue = new ObjectMapper().readValue(jsonArray.get(i).toString(), Issue.class);
					
					System.out.println(issue);
				}
				
			}

			con.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();	
		}


		return issues;

	}
}
