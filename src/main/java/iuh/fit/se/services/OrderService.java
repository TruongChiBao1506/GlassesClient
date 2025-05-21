package iuh.fit.se.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import iuh.fit.se.entities.Order;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface OrderService {
	public ApiResponse<List<Order>> findAll();
	
	public ApiResponse<Map<String, Object>> findAllPaginated(int page, int size);
	
	public ApiResponse<Order> findById(Long id);
	
	public ApiResponse<Boolean> deleteById(Long id);
	
	public ApiResponse<String> updateStatus(Long id, String status);
		public ApiResponse<List<Order>> filterOrders(String keyword, String status, String sort);
	
	public ApiResponse<Map<String, Object>> filterOrdersPaginated(String keyword, String status, String sort, int page, int size);
	
	public ApiResponse<Void> exportOrders(int year, Integer month, HttpServletResponse response) throws IOException;
	
	public ApiResponse<List<Order>> getOrderByUserId(Long userId);
}
