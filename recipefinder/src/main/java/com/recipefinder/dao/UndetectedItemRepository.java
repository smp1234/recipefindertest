package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.UndetectedItem;

public interface UndetectedItemRepository extends MongoRepository<UndetectedItem, String>{
	
	public UndetectedItem findUndetectedItemByFileName(String fileName);
	
}
