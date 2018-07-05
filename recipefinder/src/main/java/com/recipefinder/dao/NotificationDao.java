package com.recipefinder.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.Notification;
import com.recipefinder.model.User;

@Repository
public class NotificationDao {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public boolean createEntry(User user, long notifications) {
		boolean status = false;
		try {			
			notificationRepository.save(new Notification(user, notifications));
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public long getNotificationCount(User user) {
		return notificationRepository.findByUser(user).getNoOfNotifications();
	}
}
