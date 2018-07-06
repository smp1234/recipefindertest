package com.recipefinder.model;

import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection="Vote")
public class Vote {
	@Id
	private String Id;
	
	private User voter;
	private HashSet<UndetectedItem> items;
	
	public Vote() {
		this.items = new HashSet<>();
	}
	
	public Vote(User user, UndetectedItem item) {
		this.voter = user;		
		this.items = new HashSet<>();
		this.items.add(item);
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public User getVoter() {
		return voter;
	}
	public void setVoter(User voter) {
		this.voter = voter;
	}
	public HashSet<UndetectedItem> getItems() {
		return items;
	}
	public void setItems(HashSet<UndetectedItem> items) {
		this.items = items;
	}
}
