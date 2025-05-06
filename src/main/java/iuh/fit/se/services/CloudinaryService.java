package iuh.fit.se.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
	public String uploadFile(MultipartFile file);
	public boolean deleteImage(String imageUrl);
}
