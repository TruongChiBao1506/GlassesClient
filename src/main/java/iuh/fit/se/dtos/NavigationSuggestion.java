package iuh.fit.se.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavigationSuggestion {
	private String url;
	private String title;
	private String description;
	private String type;
	private boolean autoRedirect;
}
