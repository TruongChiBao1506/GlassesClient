package iuh.fit.se.dtos;

import org.springframework.web.multipart.MultipartFile;

import iuh.fit.se.entities.Category;
import iuh.fit.se.entities.FrameSize;
import iuh.fit.se.entities.Specifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlassesFormDTO {
	private Long id;
	private String name;
	private String brand;
	private Double price;
    private String colorName;
    private String colorCode;
    private MultipartFile  imageFrontUrl;
    private MultipartFile  imageSideUrl;
	private boolean gender;
	private int stock;
	private String description;
	
	private Specifications specifications;
	private FrameSize frameSize;
	private Category category;

}
