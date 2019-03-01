package com.gitlab;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Issue implements List<Issue> {

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


	public Issue() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String get_rev() {
		return _rev;
	}


	public void set_rev(String _rev) {
		this._rev = _rev;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getIid() {
		return iid;
	}


	public void setIid(Integer iid) {
		this.iid = iid;
	}


	public Integer getProject_id() {
		return project_id;
	}


	public void setProject_id(Integer project_id) {
		this.project_id = project_id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCreated_at() {
		return created_at;
	}


	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


	public String getUpdated_at() {
		return updated_at;
	}


	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}


	public String getClosed_at() {
		return closed_at;
	}


	public void setClosed_at(String closed_at) {
		this.closed_at = closed_at;
	}


	public Integer getUser_notes_count() {
		return user_notes_count;
	}


	public void setUser_notes_count(Integer user_notes_count) {
		this.user_notes_count = user_notes_count;
	}


	public Integer getUpvotes() {
		return upvotes;
	}


	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}


	public Integer getDownvotes() {
		return downvotes;
	}


	public void setDownvotes(Integer downvotes) {
		this.downvotes = downvotes;
	}


	public String getDue_date() {
		return due_date;
	}


	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}


	public boolean isConfidential() {
		return confidential;
	}


	public void setConfidential(boolean confidential) {
		this.confidential = confidential;
	}


	public String getDiscussion_locked() {
		return discussion_locked;
	}


	public void setDiscussion_locked(String discussion_locked) {
		this.discussion_locked = discussion_locked;
	}


	public String getWeb_url() {
		return web_url;
	}


	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}


	public String[] getLabels() {
		return labels;
	}


	public void setLabels(String[] labels) {
		this.labels = labels;
	}


	public Assignee getAssignee() {
		return assignee;
	}


	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}


	public Milestone getMilestone() {
		return milestone;
	}


	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}


	public TimeStats getTime_stats() {
		return time_stats;
	}


	public void setTime_stats(TimeStats time_stats) {
		this.time_stats = time_stats;
	}


	@Override
	public boolean add(Issue e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void add(int arg0, Issue arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean addAll(Collection<? extends Issue> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean addAll(int arg0, Collection<? extends Issue> arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Issue get(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Iterator<Issue> iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public ListIterator<Issue> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListIterator<Issue> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Issue remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Issue set(int arg0, Issue arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public List<Issue> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

}
