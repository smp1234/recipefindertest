package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.User;

public interface UserRepository extends MongoRepository<User, String>{
	
	public User findByUserId(int userId);
	public User findByEmailId(String emailId);
	
}
