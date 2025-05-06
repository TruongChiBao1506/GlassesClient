package iuh.fit.se.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
	private String id;
	private String userId;
	private List<Message> messages = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String language;
}
