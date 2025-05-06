package iuh.fit.se.dtos;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO {
	private String username;
	private String fullname;
	private String email;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	private String phone;
	private String address;
	private boolean gender;

	public UserProfileDTO(String username, String fullname, String email, Date dob, String phone, String address,
			boolean gender) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.dob = dob;
		this.phone = phone;
		this.address = address;
		this.gender = gender;
	}
	public UserProfileDTO() {
		super();
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "UserProfileDTO [username=" + username + ", fullname=" + fullname + ", email=" + email + ", dob=" + dob
				+ ", phone=" + phone + ", address=" + address + ", gender=" + gender + "]";
	}

	
	
}


