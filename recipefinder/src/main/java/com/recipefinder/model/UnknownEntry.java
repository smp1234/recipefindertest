package com.recipefinder.model;

import java.util.Comparator;

public class UnknownEntry implements Comparator<UnknownEntry>, Comparable<UnknownEntry>{
	private String name;
	private int votes;
	
	public UnknownEntry(String name, int votes) {		
		this.name = name;
		this.votes = votes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public UnknownEntry() {}

	@Override
	public int compare(UnknownEntry o1, UnknownEntry o2) {
		
		return o2.votes - o1.votes;
	}
	
	@Override
	public String toString() {
		return this.name + " " +this.votes;
	}

	@Override
	public int compareTo(UnknownEntry o) {
		
		return o.votes - this.votes;
	}
}
