package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.recipefinder.model.User;

public interface UserRepository extends MongoRepository<User, String>{
	@Query("{'userId' : ?0}")
	public User findByUserId(Integer userId);
	public User findByEmailId(String emailId);
	
}
