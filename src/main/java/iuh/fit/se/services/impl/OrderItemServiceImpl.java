package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dtos.OrderItemDTO;
import iuh.fit.se.services.OrderItemService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class OrderItemServiceImpl implements OrderItemService{
	private RestClient restClient;
	private ObjectMapper objectMapper;
	@Value("${api.url}")
	private String ENDPOINT;
	@Autowired
	private SessionUtil sessionUtil;

	public OrderItemServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<OrderItemDTO>> findByOrderItemIdOrderId(Long orderId) {
		return restClient.get().uri(ENDPOINT + "/orders/order?id=" + orderId)
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.exchange((request, response) -> {
			ApiResponse<List<OrderItemDTO>> apiResponse = null;
			try (InputStream is = response.getBody()){
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

}
