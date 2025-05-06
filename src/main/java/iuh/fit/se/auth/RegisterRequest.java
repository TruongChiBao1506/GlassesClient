package iuh.fit.se.auth;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import iuh.fit.se.entities.Role;

public class RegisterRequest {
	private String username;
	private String fullname;
	private String password;
	private String email;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	private String address;
	private String phone;
	private boolean gender;
	private Role role; 
	
	
	public RegisterRequest(String username, String fullname, String password, String email, Date dob, String address,
			String phone, boolean gender, Role role) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.password = password;
		this.email = email;
		this.dob = dob;
		this.address = address;
		this.phone = phone;
		this.gender = gender;
		this.role = role;
	}
	public RegisterRequest() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	@Override
	public String toString() {
		return "RegisterRequest [username=" + username + ", fullname=" + fullname + ", password=" + password
				+ ", email=" + email + ", dob=" + dob + ", address=" + address + ", phone=" + phone + ", gender="
				+ gender + ", role=" + role + "]";
	}
	
	
	
	
}
