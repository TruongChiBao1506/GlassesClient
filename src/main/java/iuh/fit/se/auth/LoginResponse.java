package iuh.fit.se.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private String username;
	private Long userId;
	private String role;
	
	

	
	public LoginResponse(String accessToken, String refreshToken, String username, Long userId, String role) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.username = username;
		this.userId = userId;
		this.role = role;
	}
	public LoginResponse() {
		super();
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	@Override
	public String toString() {
		return "LoginResponse [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", username=" + username
				+ ", userId=" + userId + ", role=" + role + "]";
	}
	

	


	
	
	
	
}
