package iuh.fit.se.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemView {
	private Long productId;
	private String name;
	private String imageUrl;
	private String colorName;
	private String colorCode;
	private int quantity;
	private Double price;
}
