package iuh.fit.se.services;

import iuh.fit.se.dtos.CartDTO;
import iuh.fit.se.utils.ApiResponse;

public interface CartService {
	public ApiResponse<CartDTO> getCart(Long userId);

	public ApiResponse<String> addToCart(Long userId, Long productId, int quantity);

	public ApiResponse<String> clearCart(Long userId);
	
	public ApiResponse<String> removeCart(Long userId, Long productId);
	
	public ApiResponse<String> updateCart(Long userId, Long productId, int quantity);
}
