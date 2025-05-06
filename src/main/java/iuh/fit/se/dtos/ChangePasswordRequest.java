package iuh.fit.se.dtos;

public class ChangePasswordRequest {
	private String username;
	private String password;
	private String newPassword;
	
	public ChangePasswordRequest(String username, String password, String newPassword) {
		super();
		this.username = username;
		this.password = password;
		this.newPassword = newPassword;
	}


	public ChangePasswordRequest() {
		super();
	}

	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public String toString() {
		return "ChangePasswordRequest [username=" + username + ", password=" + password + ", newPassword=" + newPassword
				+ "]";
	}



	
	
}
