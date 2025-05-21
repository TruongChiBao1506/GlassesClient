package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.services.PaymentService;
import iuh.fit.se.utils.ApiResponse;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	public PaymentServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> getPayment() {
		return restClient.get().uri(ENDPOINT + "/orders/vnpay-return").exchange((request, response) -> {
			ApiResponse<String> apiResponse = null;
			try (InputStream is = response.getBody()){
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), String.class));
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
