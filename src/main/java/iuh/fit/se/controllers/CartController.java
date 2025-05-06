package iuh.fit.se.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iuh.fit.se.dtos.CartDTO;
import iuh.fit.se.dtos.CartItemView;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.entities.Notification;
import iuh.fit.se.entities.User;
import iuh.fit.se.services.CartService;
import iuh.fit.se.services.GlassService;
import iuh.fit.se.services.NotificationService;
import iuh.fit.se.services.UserService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private GlassService glassService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@GetMapping("/cart")
	public String showCart(Model model, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam(value = "redirectTo", required = false) String redirectTo) {
		String username = (String) session.getAttribute("username");
		String token = (String) session.getAttribute("token");
		String url = redirectTo;
		if (username == null) {
			redirectAttributes.addFlashAttribute("popup", "true");
			return url != null ? "redirect:" + url : "redirect:/home";

		} else {
			ApiResponse<User> response = userService.findByUsername(username);
			User user = response.getData();
			Long userId = (Long) session.getAttribute("userId");
			user.setId(userId);
			model.addAttribute("user", user);
			ApiResponse<CartDTO> cartItems = cartService.getCart(userId);
			List<CartItemView> cartItemViews = new ArrayList<>();
			for (Map.Entry<Long, Integer> entry : cartItems.getData().getItems().entrySet()) {
				Long productId = entry.getKey();
				Integer quantity = entry.getValue();
				Glass product = glassService.findById(productId).getData();
				CartItemView view = new CartItemView();
				view.setProductId(productId);
				view.setName(product.getName());
				view.setPrice(product.getPrice());
				view.setQuantity(quantity);
				view.setImageUrl(product.getImageSideUrl());
				view.setColorCode(product.getColorCode());
				view.setColorName(product.getColorName());
				cartItemViews.add(view);
			}
			System.out.println("Cart items: " + cartItemViews.size() + " items");
			model.addAttribute("cartItems", cartItemViews);
			model.addAttribute("token", token);
			return "cart";
		}
	}

	@PostMapping("/cart/add")
	public String addToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute("userId");
		System.out.println("User ID: " + userId + ", Product ID: " + productId + ", Quantity: " + quantity);
		if (userId == null) {
			redirectAttributes.addFlashAttribute("popup", "true");
			return "redirect:/login"; // hoặc redirect về trang cũ
		}

		ApiResponse<String> response = cartService.addToCart(userId, productId, quantity);

		if (response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("success", "Added to cart");
		} else {
			redirectAttributes.addFlashAttribute("error", "Add to cart failed!");
		}

		return "redirect:/products/glasses/" + productId; // hoặc redirect về trang đang xem
	}

	@PostMapping("/cart/update")
	public String updateToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute("userId");
		System.out.println("User ID: " + userId + ", Product ID: " + productId + ", Quantity: " + quantity);
		if (userId == null) {
			redirectAttributes.addFlashAttribute("popup", "true");
			return "redirect:/login"; // hoặc redirect về trang cũ
		}

		ApiResponse<String> response = cartService.updateCart(userId, productId, quantity);

		if (response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("message", "Đã cập nhật giỏ hàng");
		} else {
			redirectAttributes.addFlashAttribute("message", "Cập nhật giỏ hàng thất bại");
		}

		return "redirect:/cart"; // hoặc redirect về trang đang xem
	}

	@PostMapping("/cart/remove")
	public String removeFromCart(@RequestParam("productId") Long productId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			redirectAttributes.addFlashAttribute("popup", "true");
			return "redirect:/login"; // hoặc redirect về trang cũ
		}

		ApiResponse<String> response = cartService.removeCart(userId, productId);

		if (response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("message", "Đã xóa khỏi giỏ hàng");
		} else {
			redirectAttributes.addFlashAttribute("message", "Xóa khỏi giỏ hàng thất bại");
		}

		return "redirect:/cart";
	}

}
