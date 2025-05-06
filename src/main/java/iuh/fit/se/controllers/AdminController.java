package iuh.fit.se.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import iuh.fit.se.dtos.OrderItemDTO;
import iuh.fit.se.entities.Category;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.entities.Notification;
import iuh.fit.se.entities.Order;
import iuh.fit.se.entities.User;
import iuh.fit.se.services.CategoryService;
import iuh.fit.se.services.GlassService;
import iuh.fit.se.services.NotificationService;
import iuh.fit.se.services.OrderItemService;
import iuh.fit.se.services.OrderService;
import iuh.fit.se.services.UserService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;
import iuh.fit.se.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private GlassService glassService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping("/admin-dashboard")
	public String adminDashboard(HttpSession session, Model model) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			
			// Tạo một danh sách để lưu trữ các thông báo lỗi
			List<String> errorMessages = new ArrayList<>();
			
			ApiResponse<List<Notification>> ResponseNotifications = notificationService.findByIsReadFalse();
			ApiResponse<List<Category>> ResponseCategories = categoryService.findAll();
			ApiResponse<List<Glass>> ResponseGlasses = glassService.findAll();
			ApiResponse<List<User>> ResponseUsers = userService.getAllUsers();
			
			if (ResponseNotifications == null || ResponseNotifications.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách thông báo");
			}
			if (ResponseCategories == null || ResponseCategories.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách danh mục");
			}
			if (ResponseGlasses == null || ResponseGlasses.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách kính");
			}
			if (ResponseUsers == null || ResponseUsers.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách người dùng");
			}
			
			// Thêm danh sách lỗi vào model nếu có lỗi
			if (!errorMessages.isEmpty()) {
				model.addAttribute("errorMessages", errorMessages);
			}
			
			// Thêm dữ liệu vào model nếu có
			if (ResponseNotifications != null && ResponseNotifications.getStatus() == 200) {
				model.addAttribute("notifications", ResponseNotifications.getData());
				model.addAttribute("notificationsCount", ResponseNotifications.getData().size());
			} else {
				model.addAttribute("notifications", List.of());
				model.addAttribute("notificationsCount", 0);
			}
			
			if (ResponseCategories != null && ResponseCategories.getStatus() == 200) {
				model.addAttribute("categories", ResponseCategories.getData().size());
			} else {
				model.addAttribute("categories", 0);
			}
			
			if (ResponseGlasses != null && ResponseGlasses.getStatus() == 200) {
				model.addAttribute("glasses", ResponseGlasses.getData().size());
			} else {
				model.addAttribute("glasses", 0);
			}
			
			if (ResponseUsers != null && ResponseUsers.getStatus() == 200) {
				model.addAttribute("users", ResponseUsers.getData().size());
			} else {
				model.addAttribute("users", 0);
			}
			
			model.addAttribute("token", token);
			return "AdminDashboard";
		} else {
			return "accessDenied";
		}
	}

	@GetMapping("/orderList")
	public String manageOrders(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			// Tạo một danh sách để lưu trữ các thông báo lỗi
			List<String> errorMessages = new ArrayList<>();
			
			ApiResponse<List<Order>> ResponseOrders = orderService.findAll();
			ApiResponse<List<Notification>> ResponseNotifications = notificationService.findByIsReadFalse();
			
			if (ResponseOrders == null || ResponseOrders.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách đơn hàng");
			}
			if (ResponseNotifications == null || ResponseNotifications.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách thông báo");
			}
			
			// Thêm danh sách lỗi vào model nếu có lỗi
			if (!errorMessages.isEmpty()) {
				model.addAttribute("errorMessages", errorMessages);
			}
			
			// Thêm dữ liệu vào model nếu có
			if (ResponseOrders != null && ResponseOrders.getStatus() == 200) {
				model.addAttribute("orders", ResponseOrders.getData());
			} else {
				model.addAttribute("orders", List.of());
			}
			
			List<String> statuses = Arrays.asList("PENDING", "PROCESSING", "SHIPPED", "COMPLETED", "CANCELLED", "PAID",
					"FAILED");
			
			if (ResponseNotifications != null && ResponseNotifications.getStatus() == 200) {
				model.addAttribute("notifications", ResponseNotifications.getData());
			} else {
				model.addAttribute("notifications", List.of());
			}
			
			model.addAttribute("utils", new Utils());
			model.addAttribute("statuses", statuses);
			model.addAttribute("token", token);
			return "orderList";
		} else {
			return "accessDenied";
		}
	}

	@GetMapping("/orderList/order/{orderId}")
	public String getOrderItems(@PathVariable Long orderId, Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<Order> ResponseOrder = orderService.findById(orderId);
		ApiResponse<List<OrderItemDTO>> ResponseOrderItems = orderItemService.findByOrderItemIdOrderId(orderId);
		ApiResponse<List<Notification>> ResponseNotifications = notificationService.findByIsReadFalse();
		
		if (ResponseOrder == null || ResponseOrder.getStatus() != 200) {
			errorMessages.add("Không thể lấy thông tin đơn hàng");
		}
		if (ResponseOrderItems == null || ResponseOrderItems.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách sản phẩm trong đơn hàng");
		}
		if (ResponseNotifications == null || ResponseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (ResponseOrder != null && ResponseOrder.getStatus() == 200) {
			model.addAttribute("order", ResponseOrder.getData());
		} else {
			model.addAttribute("order", null);
		}
		
		if (ResponseOrderItems != null && ResponseOrderItems.getStatus() == 200) {
			model.addAttribute("orderItems", ResponseOrderItems.getData());
		} else {
			model.addAttribute("orderItems", List.of());
		}
		
		if (ResponseNotifications != null && ResponseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", ResponseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}
		
		model.addAttribute("utils", new Utils());
		return "OrderDetail";
	}

	@GetMapping("/orderList/delete/{orderId}")
	public String deleteOrder(@PathVariable Long orderId) {
		orderService.deleteById(orderId);
		return "redirect:/orderList";
	}

	@GetMapping("/orderList/update")
	public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
		System.out.println(orderId + " " + status);

		orderService.updateStatus(orderId, status);
		return "redirect:/orderList";
	}

	@GetMapping("/orderList/filter")
	public String filterOrders(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) String sort, Model model) {
		
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<Order>> ResponseOrders = orderService.filterOrders(keyword, status, sort);
		ApiResponse<List<Notification>> ResponseNotifications = notificationService.findByIsReadFalse();
		
		if (ResponseOrders == null || ResponseOrders.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách đơn hàng theo bộ lọc");
		}
		if (ResponseNotifications == null || ResponseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (ResponseOrders != null && ResponseOrders.getStatus() == 200) {
			model.addAttribute("orders", ResponseOrders.getData());
		} else {
			model.addAttribute("orders", List.of());
		}
		
		List<String> statuses = Arrays.asList("PENDING", "PROCESSING", "SHIPPED", "COMPLETED", "CANCELLED", "PAID",
				"FAILED");
		
		if (ResponseNotifications != null && ResponseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", ResponseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}
		
		model.addAttribute("statuses", statuses);
		model.addAttribute("utils", new Utils());
		return "orderList";
	}

	@GetMapping("/report/order-report")
	public String getOrderReport(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			List<Notification> notifications = notificationService.findByIsReadFalse().getData();
			model.addAttribute("token", token);
			model.addAttribute("utils", new Utils());
			model.addAttribute("notifications", notifications);
			return "OrderReport";
		} else {
			return "accessDenied";
		}
	}

	@GetMapping("/report/order-report/export")
	public String exportOrderReport(@RequestParam int year, @RequestParam(required = false) Integer month,
			HttpServletResponse response) throws IOException {
		orderService.exportOrders(year, month, response);
		return "redirect:/report/order-report";
	}
}
