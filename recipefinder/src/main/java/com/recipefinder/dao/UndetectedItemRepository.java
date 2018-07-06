package com.recipefinder.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.User;

public interface UndetectedItemRepository extends MongoRepository<UndetectedItem, String>{
	
	public UndetectedItem findUndetectedItemByFileName(String fileName);
	public void deleteByFileName(String fileName);
	public List<UndetectedItem> findAllByCreator(User creator);
	
}
