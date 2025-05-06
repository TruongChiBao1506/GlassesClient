package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.dtos.OrderItemDTO;
import iuh.fit.se.utils.ApiResponse;

public interface OrderItemService {

	public ApiResponse<List<OrderItemDTO>> findByOrderItemIdOrderId(Long orderId);
}
