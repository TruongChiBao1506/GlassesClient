package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.auth.LoginRequest;
import iuh.fit.se.auth.LoginResponse;
import iuh.fit.se.auth.RegisterRequest;
import iuh.fit.se.dtos.OtpVerificationRequest;
import iuh.fit.se.services.AuthService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {
	private ObjectMapper objectMapper;
	private RestClient restClient;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	public AuthServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
		return restClient.post().uri(ENDPOINT + "/auth/login").body(loginRequest).exchange((request, response) -> {
			ApiResponse<LoginResponse> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);
				apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), LoginResponse.class));
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Lỗi đọc response: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> register(RegisterRequest registerRequest) {
		return restClient.post().uri(ENDPOINT + "/auth/register").body(registerRequest)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> handleForgotPassword(String email) {
		Map<String, String> body = new HashMap();
		body.put("email", email);
		return restClient.post().uri(ENDPOINT + "/auth/forgot-password").body(body).exchange((request, response) -> {
			ApiResponse<String> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
				
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Lỗi đọc response: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> handleResetPassword(String token, String newPassword) {
		Map<String, String> body = new HashMap();
		body.put("token", token);
		body.put("newPassword", newPassword);
		return restClient.post().uri(ENDPOINT + "/auth/reset-password").body(body).exchange((request, response) -> {
			ApiResponse<String> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Lỗi đọc response: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> verifyOtp(OtpVerificationRequest otpVerificationRequest) {

		return restClient.post().uri(ENDPOINT + "/auth/verify-otp").body(otpVerificationRequest)
				.exchange((request, response) -> {
					ApiResponse<String> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
					} catch (IOException e) {
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@Override
	public ApiResponse<String> logout(String refreshToken) {
		return restClient.post().uri(ENDPOINT + "/auth/logout").body(refreshToken).exchange((request, response) -> {
			System.out.println("Đã gọi API logout");
			ApiResponse<String> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse.setMessage(objectMapper.convertValue(apiResponse.getMessage(), String.class));
			} catch (IOException e) {
				System.err.println("Lỗi đọc response: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}

}
