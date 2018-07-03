package com.recipefinder.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.User;
@Repository
public class UndetectedItemDao {
	
	@Autowired
	private UndetectedItemRepository undetectedItemRepository;
	
	public boolean addItem(String fileName, User creator) {
		boolean status = false;
		try {
			undetectedItemRepository.save(new UndetectedItem(fileName, creator));
			status = true;
		} catch (Exception e) {
			status = false;
		}
		return status;	
	}
}
