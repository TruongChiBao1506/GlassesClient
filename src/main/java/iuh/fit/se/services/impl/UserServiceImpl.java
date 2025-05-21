package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dtos.ChangePasswordRequest;
import iuh.fit.se.dtos.UserProfileDTO;
import iuh.fit.se.entities.Role;
import iuh.fit.se.entities.User;
import iuh.fit.se.services.UserService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class UserServiceImpl implements UserService {

	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	@Autowired
	private SessionUtil sessionUtil;

	public UserServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<User> findByUsername(String username) {
		return restClient.get().uri(ENDPOINT + "/users/" + username)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<User> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), User.class));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response users: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> changePassword(ChangePasswordRequest changePasswordRequest) {
		return restClient.post().uri(ENDPOINT + "/users/change-password")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).body(changePasswordRequest)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), String.class));
					} catch (Exception e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> updateProfile(UserProfileDTO userProfileDTO) {
		return restClient.post().uri(ENDPOINT + "/users/update-profile")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).body(userProfileDTO)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), String.class));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<User>> getAllUsers() {
		return restClient.get().uri(ENDPOINT + "/users/all-by-role/"+ sessionUtil.getUsername())
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<User>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), 
							new TypeReference<List<User>>() {}));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response users: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> deleteUser(Long id) {
		return restClient.delete().uri(ENDPOINT + "/users/delete/" + id)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = new ApiResponse<>();
					if (response.getBody() != null && response.getBody().available() > 0) {
						apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);
						if (apiResponse.getData() != null) {
							apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), String.class));
						}
					} else {
						apiResponse.setStatus(response.getStatusCode().value());
						apiResponse.setMessage("Xóa người dùng thành công!");
						apiResponse.setData("Xóa thành công");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> addUser(User user) {
		return restClient.post().uri(ENDPOINT + "/users/add")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).body(user)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
//						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), String.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<User> getUserById(Long userId) {
		return restClient.get().uri(ENDPOINT + "/users/user/" + userId)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<User> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), User.class));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@Override
	public ApiResponse<List<Role>> findAll() {
		return restClient.get().uri(ENDPOINT + "/roles/all").header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<Role>> apiResponse = new ApiResponse<>();
					if (response.getBody() != null && response.getBody().available() > 0) {
						JsonNode rootNode = objectMapper.readTree(response.getBody());
						apiResponse.setStatus(rootNode.path("status").asInt());
						apiResponse.setMessage(rootNode.path("message").asText());
						if (!rootNode.path("data").isMissingNode()) {
							apiResponse.setData(
									objectMapper.convertValue(rootNode.path("data"), new TypeReference<List<Role>>() {
									}));
						}
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> updateUser(User user) {
		return restClient.put().uri(ENDPOINT + "/users/update/" + user.getId())
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).body(user)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
//						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), User.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<User>> filterUsers(String keyword, String gender, String role) {
		// Xử lý các tham số null để tránh lỗi khi truyền vào URL
		keyword = (keyword != null) ? keyword : "";
		gender = (gender != null) ? gender : "";
		role = (role != null) ? role : "";
		
		StringBuilder urlBuilder = new StringBuilder(ENDPOINT + "/users/filter?");
		
		// Chỉ thêm các tham số không rỗng vào URL
		if (!keyword.isEmpty()) {
			urlBuilder.append("keyword=").append(keyword);
		}
		if (!gender.isEmpty()) {
			if (urlBuilder.toString().endsWith("?")) {
				urlBuilder.append("gender=").append(gender);
			} else {
				urlBuilder.append("&gender=").append(gender);
			}
		}
		if (!role.isEmpty()) {
			if (urlBuilder.toString().endsWith("?")) {
				urlBuilder.append("role=").append(role);
			} else {
				urlBuilder.append("&role=").append(role);
			}
		}
		
		return restClient.get()
				.uri(urlBuilder.toString())
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<User>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), List.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> getAllUsersPaginated(int page, int size) {
		return restClient.get().uri(ENDPOINT + "/users/all-by-role/" + sessionUtil.getUsername() + "?page=" + page + "&size=" + size)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
					ApiResponse<Map<String, Object>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						// Đọc response dạng Map
						Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
						
						// Tạo ApiResponse mới
						apiResponse = new ApiResponse<>();
						apiResponse.setStatus((Integer) responseMap.get("status"));
						apiResponse.setMessage("Success");
						
						// Tạo Map chứa thông tin phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<User>>() {}));
						paginationData.put("currentPage", responseMap.get("currentPage"));
						paginationData.put("totalItems", responseMap.get("totalItems"));
						paginationData.put("totalPages", responseMap.get("totalPages"));
						paginationData.put("hasMore", responseMap.get("hasMore"));
						
						apiResponse.setData(paginationData);
					} catch (IOException e) {
						System.err.println("Lỗi đọc response users: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> filterUsersPaginated(String keyword, String gender, String role, int page, int size) {
		// Xử lý các tham số null để tránh lỗi khi truyền vào URL
		keyword = (keyword != null) ? keyword : "";
		gender = (gender != null) ? gender : "";
		role = (role != null) ? role : "";
		
		StringBuilder urlBuilder = new StringBuilder(ENDPOINT + "/users/filter?");
		urlBuilder.append("page=").append(page).append("&size=").append(size);
		
		// Chỉ thêm các tham số không rỗng vào URL
		if (!keyword.isEmpty()) {
			urlBuilder.append("&keyword=").append(keyword);
		}
		if (!gender.isEmpty()) {
			urlBuilder.append("&gender=").append(gender);
		}
		if (!role.isEmpty()) {
			urlBuilder.append("&role=").append(role);
		}
		
		return restClient.get()
				.uri(urlBuilder.toString())
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
					ApiResponse<Map<String, Object>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						// Đọc response dạng Map
						Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
						
						// Tạo ApiResponse mới
						apiResponse = new ApiResponse<>();
						apiResponse.setStatus((Integer) responseMap.get("status"));
						apiResponse.setMessage("Success");
						
						// Tạo Map chứa thông tin phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<User>>() {}));
						paginationData.put("currentPage", responseMap.get("currentPage"));
						paginationData.put("totalItems", responseMap.get("totalItems"));
						paginationData.put("totalPages", responseMap.get("totalPages"));
						paginationData.put("hasMore", responseMap.get("hasMore"));
						
						apiResponse.setData(paginationData);
					} catch (IOException e) {
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
}
