package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.Notification;
import com.recipefinder.model.User;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	public Notification findByUser(User user);
}
