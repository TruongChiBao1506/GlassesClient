package iuh.fit.se.services;


import java.util.List;

import iuh.fit.se.dtos.ChatRequest;
import iuh.fit.se.dtos.ChatResponse;
import iuh.fit.se.dtos.Conversation;
import iuh.fit.se.utils.ApiResponse;

public interface ChatbotService {
	ApiResponse<ChatResponse> sendMessage(ChatRequest request);
    ApiResponse<List<Conversation>> getUserConversations(String userId);
    ApiResponse<Conversation> getConversation(String conversationId);
    ApiResponse<Void> deleteConversation(String conversationId);
}
