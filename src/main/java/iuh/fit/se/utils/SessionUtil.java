package iuh.fit.se.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtil {
	@Autowired
	private HttpSession session;

	public String getToken() {
		return (String) session.getAttribute("token"); // giả sử "token" là key bạn lưu trong session
	}

	public String getRefreshToken() {
		return (String) session.getAttribute("refreshToken"); // giả sử "refreshToken" là key bạn lưu trong session
	}
	
	public String getUsername() {
		return (String) session.getAttribute("username"); // giả sử "username" là key bạn lưu trong session
    }
}
