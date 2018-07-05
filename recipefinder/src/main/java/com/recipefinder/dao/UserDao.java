package com.recipefinder.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.User;
@Repository
public class UserDao {
	@Autowired
	private UserRepository userRepository;	
	
	public User getUserByUserId(int userId) {
		User user = null;
		try {
			user = userRepository.findUserByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User getUserByEmailId(String emailId) {
		User user = userRepository.findUserByEmailId(emailId);
		return user;
	}
	
	public boolean addUser(String emailId, int uid) {
		boolean status = false;
		try {			
			userRepository.save(new User(emailId, uid));
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}		
		return status;
	}
}
