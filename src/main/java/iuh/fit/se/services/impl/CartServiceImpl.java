package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.auth.LoginResponse;
import iuh.fit.se.dtos.CartDTO;
import iuh.fit.se.services.CartService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private RestClient restClient;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SessionUtil sessionUtil;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	@Override
	public ApiResponse<CartDTO> getCart(Long userId) {
		return restClient.get().uri(ENDPOINT + "/carts/{userId}", userId)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<CartDTO> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), CartDTO.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response từ cart: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@Override
	public ApiResponse<String> addToCart(Long userId, Long productId, int quantity) {
		return restClient.post()
				.uri(ENDPOINT + "/carts/{userId}/add?productId={productId}&quantity={quantity}", userId, productId,
						quantity)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});

	}

	@Override
	public ApiResponse<String> clearCart(Long userId) {
		return restClient.delete().uri(ENDPOINT + "/carts/{userId}/clear", userId)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
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

	@Override
	public ApiResponse<String> removeCart(Long userId, Long productId) {
		return restClient.delete().uri(ENDPOINT + "/carts/{userId}/remove?productId={productId}", userId, productId)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
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

	@Override
	public ApiResponse<String> updateCart(Long userId, Long productId, int quantity) {
		return restClient.post()
				.uri(ENDPOINT + "/carts/{userId}/update?productId={productId}&quantity={quantity}", userId, productId,
						quantity)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
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
