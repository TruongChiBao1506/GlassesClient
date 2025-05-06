package iuh.fit.se.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import iuh.fit.se.entities.Order;
import iuh.fit.se.services.OrderService;
import iuh.fit.se.utils.Utils;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	@GetMapping("/orderHistory")
	public String getAllOrderHistory(HttpSession session, Model model) {
		Long userId = (Long) session.getAttribute("userId");
		System.out.println("username:" + userId);
		List<Order> orders = orderService.getOrderByUserId(userId).getData();
		System.out.println(orders);
		model.addAttribute("utils", new Utils());
		model.addAttribute("orders", orders);
		return "OrderHistory";
	}
	
}
