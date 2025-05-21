package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
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
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

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
	public ApiResponse<Map<String, Object>> findAllPaginated(int page, int size) {
		return restClient.get().uri(ENDPOINT + "/orders/all?page=" + page + "&size=" + size)
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
						apiResponse.setMessage((String) responseMap.get("message"));
						
						// Tạo Map chứa thông tin phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<Order>>() {}));
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
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> filterOrdersPaginated(String keyword, String status, String sort, int page, int size) {
		// Xử lý các tham số null để tránh lỗi khi truyền vào URL
		keyword = (keyword != null) ? keyword : "";
		status = (status != null) ? status : "";
		sort = (sort != null) ? sort : "";
		
		StringBuilder urlBuilder = new StringBuilder(ENDPOINT + "/orders/filter?");
		urlBuilder.append("page=").append(page).append("&size=").append(size);
		
		// Chỉ thêm các tham số không rỗng vào URL
		if (!keyword.isEmpty()) {
			urlBuilder.append("&keyword=").append(keyword);
		}
		if (!status.isEmpty()) {
			urlBuilder.append("&status=").append(status);
		}
		if (!sort.isEmpty()) {
			urlBuilder.append("&sort=").append(sort);
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
						apiResponse.setMessage((String) responseMap.get("message"));
						
						// Tạo Map chứa thông tin phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<Order>>() {}));
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
