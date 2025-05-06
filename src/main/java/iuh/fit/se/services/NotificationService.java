package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.entities.Notification;
import iuh.fit.se.utils.ApiResponse;


public interface NotificationService {
	public ApiResponse<List<Notification>> findByIsReadFalse();
	
	public ApiResponse<String> markAsRead(Long id);
}
