package com.gitlab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class GitlabHandler {

	public static void main(String[] args) throws IOException {
		
		Date currentDate = new Date();
		System.out.println("Buscando top 100 issues - " + currentDate.toLocaleString());
		Issue[] issues = getIssuesFromGitlabByProjectId("17085");
		currentDate = new Date();
		System.out.println("Inicio da criacao de docs - "  + currentDate.toLocaleString());
		boolean flgOk = saveIssues(issues);
		currentDate = new Date();
		System.out.println("Fim da criacao de docs - "  + currentDate.toLocaleString());
	}

	public static Issue[] getIssuesFromGitlabByProjectId(String projectId) throws IOException {

		//List<Issue> issues = new Issue();
		Issue[] issues = new Issue[0];
		Issue issue = new Issue();
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		
		//Format current date as integer, to become easier to query by "status_at" field
		String pattern = "yyyyMMdd";
		DateFormat df = new SimpleDateFormat(pattern);
		Date today = Calendar.getInstance().getTime();
		Integer todayAsInt = Integer.parseInt(df.format(today));
		
		try {

			URL url = new URL("https://git.ng.bluemix.net/api/v4/projects/" + projectId + "/issues?per_page=100&page=5&private_token=bs6SNtSqrRJYZWnQD4zb");
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
				issues = new Issue[jsonArray.size()];
				
				for(Integer i=0; i<jsonArray.size(); i++) {
					
					issue = new ObjectMapper().readValue(jsonArray.get(i).toString(), Issue.class);
					
					// Assign the status day at issue objet
					issue.status_at = todayAsInt; 		
					
					issues[i] = issue;
					//String msg = issue.title;
					//if (issue.assignee == null){msg+="-Unassigned";} else { msg+= issue.assignee.name;}
					//System.out.println(msg);
					
				}
				
			}

			con.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();	
		}


		return issues;

	}
	
	public static boolean saveIssues (Issue[] issues) throws IOException {
		
		Gson gson = new Gson();
		String param;
		URL url;
		HttpURLConnection con;		
		
		for(Integer i = 0; i < issues.length; i++) {
			
			param = URLEncoder.encode(gson.toJson(issues[i]), StandardCharsets.UTF_8.toString());
			url = new URL("http://localhost:9080/JavaCloudantApp/api/vinic?jsonData=" + param);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.connect();
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ con.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(con.getInputStream())));
			
			String output = br.readLine();
			//System.out.println(output);
		}		
		
		return true;
	}
}
