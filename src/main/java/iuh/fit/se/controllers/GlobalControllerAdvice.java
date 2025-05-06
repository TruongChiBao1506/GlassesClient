package iuh.fit.se.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import iuh.fit.se.dtos.CartDTO;
import iuh.fit.se.services.CartService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Component
@ControllerAdvice
public class GlobalControllerAdvice {
	@Autowired
	private CartService cartService;
	
	 @ModelAttribute("cartQuantity")
	    public int getCartQuantity(HttpSession session) {
	        Long userId = (Long) session.getAttribute("userId");
	        if (userId != null) {
	            ApiResponse<CartDTO> cartItems = cartService.getCart(userId);
	            if (cartItems.getData() != null && cartItems.getData().getItems() != null) {
	                // Đếm số mặt hàng duy nhất trong giỏ hàng thay vì tổng số lượng
	                return cartItems.getData().getItems().size();
	            }
	        }
	        return 0;
	    }
}
