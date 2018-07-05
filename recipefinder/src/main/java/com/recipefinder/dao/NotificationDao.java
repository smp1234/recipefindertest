package com.recipefinder.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.Notification;

@Repository
public class NotificationDao {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public boolean createEntry(int userId, long notifications) {
		boolean status = false;
		try {
			notificationRepository.save(new Notification(new UserDao().getUserByUserId(userId), notifications));
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public long getNotificationCount(int userId) {
		return notificationRepository.findByUser(new UserDao().getUserByUserId(userId)).getNoOfNotifications();
	}
}
