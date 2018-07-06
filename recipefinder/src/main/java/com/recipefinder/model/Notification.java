package com.recipefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection="Notifications")
public class Notification {
	@Id
	private String Id;
	
	@Indexed(unique=true)
	private User user;
	private long noOfNotifications;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getNoOfNotifications() {
		return noOfNotifications;
	}
	public void setNoOfNotifications(long noOfNotifications) {
		this.noOfNotifications = noOfNotifications;
	}
	
	public Notification() {}
	
	public Notification(User user, long notifications) {
		this.user = user;
		noOfNotifications = notifications;
	}
}
