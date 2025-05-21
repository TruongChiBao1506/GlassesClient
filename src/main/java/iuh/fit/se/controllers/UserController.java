package iuh.fit.se.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iuh.fit.se.dtos.ChangePasswordRequest;
import iuh.fit.se.dtos.UserProfileDTO;
import iuh.fit.se.entities.Notification;
import iuh.fit.se.entities.Role;
import iuh.fit.se.entities.User;
import iuh.fit.se.services.NotificationService;
import iuh.fit.se.services.UserService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/changePassword")
	public String changePassword(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
		return "ChangePassword";
	}

	@GetMapping("/profile")
	public String profile(Model model, HttpSession session) {

		String username = (String) session.getAttribute("username");
		System.out.println("username: " + username);

		ApiResponse<User> response = userService.findByUsername(username);

		if (response == null || response.getStatus() != 200) {
			model.addAttribute("error", "Không thể tải thông tin người dùng");
		}

		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getStatus() == 200) {
			model.addAttribute("user", response.getData());
		} else {
			model.addAttribute("user", null);
		}

		return "profile";
	}

	@PostMapping("/changePassword")
	public String changePassword(@ModelAttribute ChangePasswordRequest changePasswordRequest, HttpSession session,
			Model model, RedirectAttributes redirectAttributes) {
		String username = session.getAttribute("username").toString();
		model.addAttribute("username", username);
		ApiResponse<String> response = userService.changePassword(changePasswordRequest);
		if (response != null && response.getStatus() == 200) {
			// Thay vì thêm thông báo thường, thêm attribute để hiển thị SweetAlert2
			redirectAttributes.addFlashAttribute("sweetalert", true);
			redirectAttributes.addFlashAttribute("alertType", "success");
			redirectAttributes.addFlashAttribute("message", response.getMessage());
			return "redirect:/home";
		} else {
			// Thêm attribute để hiển thị SweetAlert2 cho lỗi
			model.addAttribute("sweetalert", true);
			model.addAttribute("alertType", "error");
			model.addAttribute("error", response.getMessage());
			return "ChangePassword";
		}
	}

	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserProfileDTO userProfileDTO, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) {
		ApiResponse<String> response = userService.updateProfile(userProfileDTO);
		if (response != null && response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("success", response.getMessage());
			return "redirect:/profile";
		} else {
			redirectAttributes.addFlashAttribute("error", response.getMessage());
			return "redirect:/profile";
		}
	}

	@GetMapping("/userList")
	public String showUserList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			// Tạo một danh sách để lưu trữ các thông báo lỗi
			List<String> errorMessages = new ArrayList<>();

			// Điều chỉnh page để phù hợp với API (chuyển từ 1-based sang 0-based)
			int pageForApi = page > 0 ? page - 1 : 0;

			ApiResponse<Map<String, Object>> response = userService.getAllUsersPaginated(pageForApi, size);
			ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();

			if (response == null || response.getStatus() != 200) {
				errorMessages.add("Không thể tải dữ liệu người dùng");
			}
			if (responseNotifications == null || responseNotifications.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách thông báo");
			}

			// Thêm danh sách lỗi vào model nếu có lỗi
			if (!errorMessages.isEmpty()) {
				model.addAttribute("errorMessages", errorMessages);
			}

			// Thêm dữ liệu vào model nếu có
			if (response != null && response.getStatus() == 200) {
				Map<String, Object> paginationData = response.getData();

				// Thêm dữ liệu cho danh sách người dùng
				model.addAttribute("users", paginationData.get("data"));

				// Thêm thông tin phân trang
				model.addAttribute("currentPage", paginationData.get("currentPage"));
				model.addAttribute("totalItems", paginationData.get("totalItems"));
				model.addAttribute("totalPages", paginationData.get("totalPages"));
				model.addAttribute("hasMore", paginationData.get("hasMore"));
				model.addAttribute("currentSize", size);
			} else {
				model.addAttribute("users", List.of());
				model.addAttribute("currentPage", 0);
				model.addAttribute("totalItems", 0);
				model.addAttribute("totalPages", 0);
				model.addAttribute("hasMore", false);
				model.addAttribute("currentSize", size);
			}
			if (responseNotifications != null && responseNotifications.getStatus() == 200) {
				model.addAttribute("notifications", responseNotifications.getData());
			} else {
				model.addAttribute("notifications", List.of());
			}

			model.addAttribute("role", session.getAttribute("role"));
			model.addAttribute("token", token);
			return "userList";
		} else {
			return "accessDenied";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		ApiResponse<String> response = userService.deleteUser(id);
		if (response != null && response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa người dùng!");
		}
		return "redirect:/userList";
	}

	@GetMapping("/usersAdd")
	public String showAddUserForm(Model model) {
		if (!model.containsAttribute("errors")) {
			model.addAttribute("errors", null);
		}
		model.addAttribute("user", new User());
		model.addAttribute("roles", List.of("USER", "ADMIN", "SUPER"));
		List<Notification> notifications = notificationService.findByIsReadFalse().getData();
		model.addAttribute("notifications", notifications);
		return "userAdd";
	}

	@PostMapping("/users/add")
	public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
		System.out.println(user);
		try {
//	        user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setPassword(user.getPassword());
			ApiResponse<String> response = userService.addUser(user);

			if (response != null && response.getStatus() == 200) {
				redirectAttributes.addFlashAttribute("success", "User added successfully!");
				return "redirect:/userList";

			} else if (response != null && response.getErrors() != null) {
				redirectAttributes.addFlashAttribute("errors", response.getErrors());
//				model.addAttribute("user", new User());
//			    model.addAttribute("roles", List.of("USER", "ADMIN", "SUPER"));
//			    List<Notification> notifications = notificationService.findByIsReadFalse().getData();
//				model.addAttribute("notifications", notifications);
				return "redirect:/usersAdd";
			} else {
				redirectAttributes.addFlashAttribute("error", "Failed to add user: " + response.getMessage());
				return "redirect:/users/add";
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error adding user: " + e.getMessage());
			return "redirect:/users/add";
		}
	}

	@GetMapping("/users/update/{userId}")
	public String showUpdateUserForm(@PathVariable("userId") long userId, Model model) {
		ApiResponse<User> response = userService.getUserById(userId);

		System.out
				.println("response: " + response.getData() + " " + response.getStatus() + " " + response.getMessage());
		List<Notification> notifications = notificationService.findByIsReadFalse().getData();

		if (response != null && response.getStatus() == 200) {
			User user = response.getData();
			user.setId(userId); // Set the user ID for the form)
			model.addAttribute("user", user);
			model.addAttribute("roles", List.of("USER", "ADMIN", "SUPER"));
			model.addAttribute("notifications", notifications);
			return "userUpdate";
		} else {
			model.addAttribute("error", "Failed to get user: " + response.getMessage());
			return "redirect:/userList";
		}
	}

	@PostMapping("/users/update/{userId}")
	public String updateUser(@PathVariable("userId") Long userId, @ModelAttribute User user,
			RedirectAttributes redirectAttributes) {
		user.setId(userId);
		System.out.println("updateUser: " + user);
		ApiResponse<String> response = userService.updateUser(user);
		System.out.println(response.getErrors());
		if (response != null && response.getStatus() == 200) {
			redirectAttributes.addFlashAttribute("success", "User updated successfully!");
			return "redirect:/users/update/" + userId;
		} else if (response != null && response.getErrors() != null) {
			redirectAttributes.addFlashAttribute("errors", response.getErrors());
			return "redirect:/users/update/" + userId;
		} else {
			redirectAttributes.addFlashAttribute("error", "Failed to update user: " + response.getMessage());
		}
		return "redirect:/userList";
	}

	@GetMapping("/users/details/{userId}")
	public String showUserDetails(@PathVariable("userId") long userId, Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();

		ApiResponse<User> response = userService.getUserById(userId);
		ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();

		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy thông tin người dùng");
		}
		if (responseNotifications == null || responseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}

		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}

		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getStatus() == 200) {
			User user = response.getData();
			model.addAttribute("user", user);
		} else {
			model.addAttribute("user", null);
			model.addAttribute("error", "Không thể lấy thông tin người dùng");
			return "redirect:/userList";
		}

		if (responseNotifications != null && responseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", responseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}

		return "userDetails";
	}

	@GetMapping("/users/filter")
	public String filterUsers(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model, HttpSession session) {
		String userRole = (String) session.getAttribute("role");
		String token = (String) session.getAttribute("token");

		if (userRole == null || (!userRole.equals("ADMIN") && !userRole.equals("SUPER"))) {
			return "accessDenied";
		}

		// Xử lý các tham số rỗng
		keyword = (keyword != null && !keyword.isBlank()) ? keyword : null;
		gender = (gender != null && !gender.isBlank()) ? gender : null;
		role = (role != null && !role.isBlank()) ? role : null;

		// Nếu tất cả các tham số đều null, chuyển hướng đến danh sách người dùng thông
		// thường
		if (keyword == null && gender == null && role == null) {
			return "redirect:/userList?page=" + page + "&size=" + size;
		}

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();

		// Điều chỉnh page để phù hợp với API (chuyển từ 1-based sang 0-based)
		int pageForApi = page > 0 ? page - 1 : 0;

		ApiResponse<Map<String, Object>> responseUsers = userService.filterUsersPaginated(keyword, gender, role,
				pageForApi, size);
		ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();

		if (responseUsers == null || responseUsers.getStatus() != 200) {
			errorMessages.add("Không thể tải dữ liệu người dùng theo bộ lọc");
		}
		if (responseNotifications == null || responseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}

		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}

		// Thêm dữ liệu vào model nếu có
		if (responseUsers != null && responseUsers.getStatus() == 200) {
			Map<String, Object> paginationData = responseUsers.getData();

			// Thêm dữ liệu cho danh sách người dùng
			model.addAttribute("users", paginationData.get("data"));

			// Thêm thông tin phân trang
			model.addAttribute("currentPage", paginationData.get("currentPage"));
			model.addAttribute("totalItems", paginationData.get("totalItems"));
			model.addAttribute("totalPages", paginationData.get("totalPages"));
			model.addAttribute("hasMore", paginationData.get("hasMore"));
			model.addAttribute("currentSize", size);

			// Lưu lại thông tin bộ lọc để dùng cho các liên kết phân trang
			model.addAttribute("keyword", keyword);
			model.addAttribute("selectedGender", gender);
			model.addAttribute("selectedRole", role);
		} else {
			model.addAttribute("users", List.of());
			model.addAttribute("currentPage", 0);
			model.addAttribute("totalItems", 0);
			model.addAttribute("totalPages", 0);
			model.addAttribute("hasMore", false);
			model.addAttribute("currentSize", size);
		}

		if (responseNotifications != null && responseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", responseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}

		model.addAttribute("role", userRole);
		model.addAttribute("token", token);
		return "userList";
	}
}
