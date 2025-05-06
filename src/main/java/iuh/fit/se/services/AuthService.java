package iuh.fit.se.services;

import org.springframework.stereotype.Service;

import iuh.fit.se.auth.LoginRequest;
import iuh.fit.se.auth.LoginResponse;
import iuh.fit.se.auth.RegisterRequest;
import iuh.fit.se.dtos.OtpVerificationRequest;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface AuthService {
	public ApiResponse<LoginResponse> login(LoginRequest loginRequest);
	
	public ApiResponse<String> register(RegisterRequest registerRequest);
	
	public ApiResponse<String> handleForgotPassword(String email);
	
	public ApiResponse<String> handleResetPassword(String token, String newPassword);
	
	public ApiResponse<String> verifyOtp(OtpVerificationRequest otpVerificationRequest);
	
	public ApiResponse<String> logout(String refreshToken);
}
