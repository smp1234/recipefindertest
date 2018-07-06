package com.recipefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection="User")
public class User {
	@Id
	private String Id;
	
	private int userId;
	private String emailId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public User(String emailId) {	
		this.emailId = emailId;
	}
	
	public User() {
		
	}
	
	public User(String emailId, int userId) {		
		this.userId = userId;
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return this.emailId;
	}
	
}
