package iuh.fit.se.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import iuh.fit.se.dtos.ChatRequest;
import iuh.fit.se.dtos.ChatResponse;
import iuh.fit.se.dtos.Conversation;
import iuh.fit.se.services.ChatbotService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {
	@Autowired
	private ChatbotService chatbotService;

	@GetMapping
	public String showChatbot(Model model, HttpSession session) {
		// Tạo một conversation rỗng để tránh lỗi null
		Conversation emptyConversation = new Conversation();
		emptyConversation.setMessages(new ArrayList<>());
		model.addAttribute("conversation", emptyConversation);
		model.addAttribute("conversations", Collections.emptyList());

		return "chatbot";
	}


	@GetMapping("/{conversationId}")
	public String loadConversation(@PathVariable String conversationId, Model model) {
		ApiResponse<Conversation> response = chatbotService.getConversation(conversationId);
		if (response.getData() != null) {
			model.addAttribute("conversation", response.getData());
		}
		model.addAttribute("chatRequest", new ChatRequest());
		return "chatbot";
	}

	@PostMapping("/send")
	@ResponseBody
	public ChatResponse sendMessage(@ModelAttribute ChatRequest chatRequest, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId != null) {
			chatRequest.setUserId(userId.toString());
		}

		if (chatRequest.getLanguage() == null) {
			chatRequest.setLanguage("vi"); // Mặc định tiếng Việt
		}

		ApiResponse<ChatResponse> response = chatbotService.sendMessage(chatRequest);
		return response.getData();
	}

	@PostMapping("/delete/{conversationId}")
	@ResponseBody
	public ApiResponse<Void> deleteConversation(@PathVariable String conversationId) {
		return chatbotService.deleteConversation(conversationId);
	}
}
