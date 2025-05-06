package iuh.fit.se.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
	private Long userId;
	private Long productId;
	private String username;
	private String productName;
	private String content;
	private int rating;
	private LocalDateTime createdAt;
}
