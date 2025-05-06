package iuh.fit.se.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
	private Long userId;
	private Long productId;
	private String username;
	
	private String productName;
	
	private String content;
	
	private int rating;
	
	private LocalDateTime createdAt;
	
	
}
