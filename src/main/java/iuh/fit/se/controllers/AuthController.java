package iuh.fit.se.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iuh.fit.se.auth.LoginRequest;
import iuh.fit.se.auth.LoginResponse;
import iuh.fit.se.auth.RegisterRequest;
import iuh.fit.se.dtos.OtpVerificationRequest;
import iuh.fit.se.services.AuthService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/admin-login")
	public String adminLogin() {
	    return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
		String refreshToken = (String) session.getAttribute("refreshToken");
		System.out.println("refreshToken: " + refreshToken);
        // Xóa toàn bộ thông tin trong session
		if(session.getAttribute("token") != null && session.getAttribute("role").equals("ADMIN") || session.getAttribute("role").equals("SUPER")) {
			// Xóa cookie
			authService.logout(refreshToken);
			session.invalidate(); 
			return "redirect:/login";
		} 
		// Xóa cookie
		authService.logout(refreshToken);
		session.invalidate();  
		
		
        return "redirect:/home";
    }
	
	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
		ApiResponse<LoginResponse> response = authService.login(loginRequest);
		System.out.println(response.getStatus());
		if (response != null && response.getData() != null && response.getStatus() == 200) {
			session.setAttribute("username", response.getData().getUsername());
			session.setAttribute("userId", response.getData().getUserId());
			session.setAttribute("token", response.getData().getAccessToken());
			session.setAttribute("refreshToken", response.getData().getRefreshToken());
			session.setAttribute("role", response.getData().getRole());
			if(response.getData().getRole().equals("ADMIN"))
                return "redirect:/admin-dashboard";
            else if(response.getData().getRole().equals("SUPER"))
                return "redirect:/admin-dashboard";
            else 
            	return "redirect:/home";
		} else {
			System.out.println("message:"+ response.getMessage());
			model.addAttribute("error", response.getMessage());
			return "login";
		}
    }
	
    @GetMapping("/verify-otp")
	public String verifyOtpScreen(@RequestParam(required = false) String email, Model model) {
        // Nếu có email từ query parameter, sử dụng nó
        if (email != null && !email.isEmpty()) {
            model.addAttribute("email", email);
        }
        return "Verify-otp";
    }
	
	@PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAttributes, Model model) {
		ApiResponse<String> response = authService.register(registerRequest);
		
		if (response != null) {
			// Kiểm tra status code của response
			if (response.getStatus() != 200) {
				// Trường hợp lỗi (username đã tồn tại, email không hợp lệ, v.v.)
				model.addAttribute("error", response.getMessage());
				// Giữ lại dữ liệu người dùng đã nhập để họ không phải nhập lại
				model.addAttribute("registerRequest", registerRequest);
				System.out.println("Registration error: " + response.getMessage());
				return "register"; // Hiển thị lỗi trực tiếp ở trang đăng ký
			}
			
			// Trường hợp đăng ký thành công
			if (response.getMessage() != null) {
				model.addAttribute("message", response.getMessage());
				model.addAttribute("email", registerRequest.getEmail());
				return "Verify-otp"; // Chuyển đến trang xác thực OTP
			}
		}
		
		// Trường hợp lỗi khác
		if (response != null && response.getErrors() != null) {
			model.addAttribute("error", response.getErrors());
			model.addAttribute("registerRequest", registerRequest);
			return "register";
		} else {
			System.out.println("No response");
			model.addAttribute("error", "Đăng ký thất bại!");
			return "register";
		}
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@ModelAttribute OtpVerificationRequest otpVerificationRequest, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("otpVerificationRequest: " + otpVerificationRequest.toString());
		ApiResponse<String> response = authService.verifyOtp(otpVerificationRequest);
		
		// Luôn lưu email để người dùng không phải nhập lại khi có lỗi
		model.addAttribute("email", otpVerificationRequest.getEmail());
		
		if (response != null) {
			if (response.getStatus() != 200) {
				// Trường hợp có lỗi, status khác 200
				model.addAttribute("error", response.getMessage());
				return "Verify-otp"; // Trả về trực tiếp trang verify-otp với thông báo lỗi
			} else {
				// Trường hợp thành công, status = 200
				// Sử dụng redirectAttributes để truyền thông báo đến trang login
				redirectAttributes.addFlashAttribute("success", response.getMessage());
				// Thêm dòng log để debug
				System.out.println("OTP verification successful, redirecting to login with success message: " + response.getMessage());
				return "redirect:/login";
			}
		} else {
			// Trường hợp không có response từ service
			System.out.println("No response from auth service");
			model.addAttribute("error", "Xác thực OTP thất bại. Vui lòng thử lại!");
			return "Verify-otp"; // Trả về trực tiếp trang verify-otp với thông báo lỗi
		}
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot-password";
	}
	
	@GetMapping("/reset-password")
	public String resetPassword(@RequestParam("token") String token, Model model) {
		model.addAttribute("token", token); // Gửi token tới view
		return "reset-password";
	}
	
	@PostMapping("/forgot-password")
	public String handleForgotPassword(@RequestParam("email") String email, Model model) {
		ApiResponse<String> response = authService.handleForgotPassword(email);
		if (response != null && response.getMessage() != null) {
			model.addAttribute("message", response.getMessage());
			return "forgot-password";
		} else {
			System.out.println("No response");
			model.addAttribute("error", "Gửi email thất bại!");
			return "forgot-password";
		}
	}
	@PostMapping("/reset-password")
	public String handleResetPassword(@RequestParam("token") String token,
			@RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes, Model model) {
		ApiResponse<String> response = authService.handleResetPassword(token, newPassword);
		if (response != null && response.getMessage() != null) {
			redirectAttributes.addFlashAttribute("message", response.getMessage());
			return "login";
		} else {
			model.addAttribute("error", "Thay đổi mật khẩu thất bại!");
			return "reset-password";
		}
	}
}
