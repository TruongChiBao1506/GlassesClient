package iuh.fit.se.services;

import java.io.IOException;
import java.util.List;

import iuh.fit.se.entities.Order;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface OrderService {
	public ApiResponse<List<Order>> findAll();
	
	public ApiResponse<Order> findById(Long id);
	
	public ApiResponse<Boolean> deleteById(Long id);
	
	public ApiResponse<String> updateStatus(Long id, String status);
	
	public ApiResponse<List<Order>> filterOrders(String keyword, String status, String sort);
	
	public ApiResponse<Void> exportOrders(int year, Integer month, HttpServletResponse response) throws IOException;
	
	public ApiResponse<List<Order>> getOrderByUserId(Long userId);
}
