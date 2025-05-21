package iuh.fit.se.services;

import java.util.List;
import java.util.Map;

import iuh.fit.se.dtos.ChangePasswordRequest;
import iuh.fit.se.dtos.UserProfileDTO;
import iuh.fit.se.entities.Role;
import iuh.fit.se.entities.User;
import iuh.fit.se.utils.ApiResponse;

public interface UserService {
	public ApiResponse<User> findByUsername(String username);
	
	public ApiResponse<String> changePassword(ChangePasswordRequest changePasswordRequest);
	
	public ApiResponse<String> updateProfile(UserProfileDTO userProfileDTO);
	
	public ApiResponse<List<User>> getAllUsers();
	
	public ApiResponse<String> deleteUser(Long id);
	
	ApiResponse<String> addUser(User user);
	
	ApiResponse<String> updateUser(User user);
	
	ApiResponse<User> getUserById(Long userId); 
	
	ApiResponse<List<Role>> findAll();
	
	ApiResponse<List<User>> filterUsers(String keyword, String gender, String role);
	
	ApiResponse<Map<String, Object>> getAllUsersPaginated(int page, int size);
	
	ApiResponse<Map<String, Object>> filterUsersPaginated(String keyword, String gender, String role, int page, int size);

}
