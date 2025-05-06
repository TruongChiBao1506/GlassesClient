package iuh.fit.se.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import iuh.fit.se.dtos.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
	private Long id;
	private String orderNumber;
	private UserProfileDTO user;
	private Date orderDate;
	private Double totalAmount;
	private String status;
	private String paymentMethod;
	private String shippingAddress;
	private List<OrderItem> orderItems;
	

	
	
	
}
