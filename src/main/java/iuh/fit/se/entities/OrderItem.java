package iuh.fit.se.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import iuh.fit.se.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
	private Long orderItemId;
	private ProductDTO product;
	private int quantity;
	private Double unitPrice;
	private Double totalPrice;
}
