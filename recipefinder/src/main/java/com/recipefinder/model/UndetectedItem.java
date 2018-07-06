package com.recipefinder.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.PriorityQueue;

import org.springframework.data.annotation.Id;
@Component
@Document(collection="UndetectedItem")
public class UndetectedItem {
	@Id
	private String Id;	
	
	private String fileName;
	private User creator;
	private PriorityQueue<UnknownEntry> guessedItems;
	private Date entryDate;
	
	public UndetectedItem() {
		this.guessedItems = new PriorityQueue<>(new UnknownEntry());
	}
	
	public UndetectedItem(String fileName, User creator) {
		this.fileName = fileName;
		this.creator = creator;
		this.guessedItems = new PriorityQueue<>(new UnknownEntry());
		this.entryDate = new Date();
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public PriorityQueue<UnknownEntry> getGuessedItems() {
		return guessedItems;
	}

	public void setGuessedItems(PriorityQueue<UnknownEntry> guessedItems) {
		this.guessedItems = guessedItems;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	
}