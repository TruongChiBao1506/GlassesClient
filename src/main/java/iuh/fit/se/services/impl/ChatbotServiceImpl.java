package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dtos.ChatRequest;
import iuh.fit.se.dtos.ChatResponse;
import iuh.fit.se.dtos.Conversation;
import iuh.fit.se.entities.User;
import iuh.fit.se.services.ChatbotService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class ChatbotServiceImpl implements ChatbotService {

	private RestClient restClient;
	private ObjectMapper objectMapper;
	@Value("${api.url}")
	private String ENDPOINT;

	@Autowired
	private SessionUtil sessionUtil;

	public ChatbotServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public ApiResponse<ChatResponse> sendMessage(ChatRequest chatRequest) {
		return restClient.post().uri(ENDPOINT + "/chatbot/chat")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.body(chatRequest)
				.exchange((request, response) -> {
					ApiResponse<ChatResponse> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), ChatResponse.class));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response chat bot: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@Override
	public ApiResponse<List<Conversation>> getUserConversations(String userId) {
		try {
			String response = restClient.get().uri(ENDPOINT + "/chatbot/conversations/" + userId)
					.header("Authorization", "Bearer " + sessionUtil.getToken()).retrieve().body(String.class);

			ApiResponse<List<Conversation>> apiResponse = objectMapper.readValue(response,
					new TypeReference<ApiResponse<List<Conversation>>>() {
					});
			return apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null,
					"Lỗi khi lấy danh sách cuộc hội thoại");
		}
	}

	@Override
	public ApiResponse<Conversation> getConversation(String conversationId) {
		try {
			String response = restClient.get().uri(ENDPOINT + "/chatbot/conversation/" + conversationId)
					.header("Authorization", "Bearer " + sessionUtil.getToken()).retrieve().body(String.class);

			ApiResponse<Conversation> apiResponse = objectMapper.readValue(response,
					new TypeReference<ApiResponse<Conversation>>() {
					});
			return apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null,
					"Lỗi khi lấy chi tiết cuộc hội thoại");
		}
	}

	@Override
	public ApiResponse<Void> deleteConversation(String conversationId) {
		try {
			restClient.delete().uri(ENDPOINT + "/chatbot/conversation/" + conversationId)
					.header("Authorization", "Bearer " + sessionUtil.getToken()).retrieve().toBodilessEntity();

			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), null, null, "Đã xóa cuộc hội thoại thành công");
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null,
					"Lỗi khi xóa cuộc hội thoại");
		}
	}

}
