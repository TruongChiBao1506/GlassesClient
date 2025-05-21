package iuh.fit.se.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
		String refreshToken = (String) session.getAttribute("refreshToken");
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
			model.addAttribute("refreshToken", refreshToken);
			return "AdminDashboard";
		} else {
			return "accessDenied";
		}
	}

	@GetMapping("/orderList")
	public String manageOrders(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");

		if (role == null || (!role.equals("ADMIN") && !role.equals("SUPER"))) {
			return "accessDenied";
		}

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();

		// Điều chỉnh page để phù hợp với API (chuyển từ 1-based sang 0-based)
		int pageForApi = page > 0 ? page - 1 : 0;

		ApiResponse<Map<String, Object>> ResponseOrders = orderService.findAllPaginated(pageForApi, size);
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
			Map<String, Object> paginationData = ResponseOrders.getData();

			// Thêm dữ liệu cho danh sách đơn hàng
			model.addAttribute("orders", paginationData.get("data"));

			// Thêm thông tin phân trang
			model.addAttribute("currentPage", paginationData.get("currentPage"));
			model.addAttribute("totalItems", paginationData.get("totalItems"));
			model.addAttribute("totalPages", paginationData.get("totalPages"));
			model.addAttribute("hasMore", paginationData.get("hasMore"));
			model.addAttribute("currentSize", size);
		} else {
			model.addAttribute("orders", List.of());
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalItems", 0);
			model.addAttribute("totalPages", 0);
			model.addAttribute("hasMore", false);
			model.addAttribute("currentSize", size);
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
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model, HttpSession session) {

		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");

		if (role == null || (!role.equals("ADMIN") && !role.equals("SUPER"))) {
			return "accessDenied";
		}

		// Xử lý các tham số rỗng
		keyword = (keyword != null && !keyword.isBlank()) ? keyword : null;
		status = (status != null && !status.isBlank()) ? status : null;
		sort = (sort != null && !sort.isBlank()) ? sort : null;
		// Nếu tất cả các tham số đều null, chuyển hướng đến danh sách đơn hàng thông
		// thường
		if (keyword == null && status == null && sort == null) {
			return "redirect:/orderList?page=" + page + "&size=" + size;
		}

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		// Điều chỉnh page để phù hợp với API (chuyển từ 1-based sang 0-based)
		int pageForApi = page > 0 ? page - 1 : 0;

		// Sử dụng filterOrdersPaginated để tận dụng phân trang phía server
		ApiResponse<Map<String, Object>> ResponseOrders = orderService.filterOrdersPaginated(keyword, status, sort,
				pageForApi, size);
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
			Map<String, Object> paginationData = ResponseOrders.getData();

			// Thêm dữ liệu cho danh sách đơn hàng từ kết quả phân trang server
			model.addAttribute("orders", paginationData.get("data"));

			// Thêm thông tin phân trang
			model.addAttribute("currentPage", paginationData.get("currentPage"));
			model.addAttribute("totalItems", paginationData.get("totalItems"));
			model.addAttribute("totalPages", paginationData.get("totalPages"));
			model.addAttribute("hasMore", paginationData.get("hasMore"));
			model.addAttribute("currentSize", size);

			// Lưu lại thông tin bộ lọc để dùng cho các liên kết phân trang
			model.addAttribute("keyword", keyword);
			model.addAttribute("selectedStatus", status);
			model.addAttribute("sort", sort);
		} else {
			model.addAttribute("orders", List.of());
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalItems", 0);
			model.addAttribute("totalPages", 0);
			model.addAttribute("hasMore", false);
			model.addAttribute("currentSize", size);
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
		model.addAttribute("token", token);
		return "orderList";
	}

	@GetMapping("/report/order-report")
	public String getOrderReport(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");
		String refreshToken = (String) session.getAttribute("refreshToken");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			List<Notification> notifications = notificationService.findByIsReadFalse().getData();
			model.addAttribute("token", token);
			model.addAttribute("refreshToken", refreshToken);
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
