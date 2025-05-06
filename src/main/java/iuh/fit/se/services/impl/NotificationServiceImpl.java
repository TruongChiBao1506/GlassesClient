package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.entities.Notification;
import iuh.fit.se.services.NotificationService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private SessionUtil sessionUtil;

	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://localhost:8080/api";

	public NotificationServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Notification>> findByIsReadFalse() {
		String token = sessionUtil.getToken();
		return restClient.get().uri(ENDPOINT + "/notifications/unread")
				.header("Authorization", "Bearer " + token)
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
					ApiResponse<List<Notification>> apiResponse = null;
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
	public ApiResponse<String> markAsRead(Long id) {
		String token = sessionUtil.getToken();
		System.out.println(token);
		return restClient.post().uri(ENDPOINT + "/admin/notifications/" + id + "/mark-as-read")
				.header("Authorization", "Bearer " + token)
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

}
