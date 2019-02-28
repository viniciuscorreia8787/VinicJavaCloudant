package com.gitlab;

public class Issue {

	public String _id;
	public String _rev;
	public Integer id;
	public Integer iid;
	public Integer project_id;
	public String title;
	public String description;
	public String state;
	public String created_at;
	public String updated_at;
	public String closed_at;

	public Integer user_notes_count;
	public Integer upvotes;
	public Integer downvotes;
	public String due_date;
	public boolean confidential;
	public String discussion_locked;
	public String web_url;
	  
	
	
	
	public String[] labels;
	public Assignee assignee;
	public Milestone milestone;
	public TimeStats time_stats;
	
}
