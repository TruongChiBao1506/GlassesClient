package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.entities.Order;
import iuh.fit.se.services.OrderService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class OrderServiceImpl implements OrderService {
	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://localhost:8080/api";

	@Autowired
	private SessionUtil sessionUtil;

	public OrderServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Order>> findAll() {
		return restClient.get().uri(ENDPOINT + "/orders/all")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<Order>> apiResponse = null;
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
	public ApiResponse<Order> findById(Long id) {
		return restClient.get().uri(ENDPOINT + "/orders/full/" + id)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<Order> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Order.class));
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
	public ApiResponse<Boolean> deleteById(Long id) {
		return restClient.delete().uri(ENDPOINT + "/orders/delete/" + id)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				 .exchange((request, response) -> {
					ApiResponse<Boolean> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Boolean.class));
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
	public ApiResponse<String> updateStatus(Long id, String status) {
		return restClient.get().uri(ENDPOINT + "/orders/update-status/" + id + "?status=" + status)
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

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Order>> filterOrders(String keyword, String status, String sort) {
		return restClient.get()
				.uri(ENDPOINT + "/orders/filter?keyword=" + keyword + "&status=" + status + "&sort=" + sort)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<Order>> apiResponse = null;
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

	@Override
	public ApiResponse<Void> exportOrders(int year, Integer month, HttpServletResponse response) throws IOException {

		String url = month == null ? ENDPOINT + "/orders/orders-export?year=" + year
				: ENDPOINT + "/orders/orders-export?year=" + year + "&month=" + month;
		return restClient.get().uri(url).header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response1) -> {
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
					response.getOutputStream().write(response1.getBody().readAllBytes());
					return null;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Order>> getOrderByUserId(Long userId) {
		return restClient.get().uri(ENDPOINT + "/orders/orders-history?userId=" + userId)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken()).exchange((request, response) -> {
					ApiResponse<List<Order>> apiResponse = null;
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

}
