package iuh.fit.se.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import iuh.fit.se.services.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
	@Autowired
	private Cloudinary cloudinary;

	@Override
	public String uploadFile(MultipartFile file) {
		try {
			// Kiểm tra file hợp lệ
			if (file == null || file.isEmpty()) {
				throw new IllegalArgumentException("File không hợp lệ hoặc rỗng");
			}

			// Lấy tên file gốc và loại bỏ phần mở rộng
			String originalFilename = file.getOriginalFilename();
			if (originalFilename == null) {
				originalFilename = "unknown";
			}

			// Loại bỏ phần mở rộng file (ví dụ: .avif, .jpg, .png)
			String filenameWithoutExtension = originalFilename;
			int lastDot = originalFilename.lastIndexOf('.');
			if (lastDot != -1) {
				filenameWithoutExtension = originalFilename.substring(0, lastDot);
			}

			// Tạo tên file dựa trên thời gian hiện tại và tên file đã xử lý
			String filename = System.currentTimeMillis() + "_" + filenameWithoutExtension;

			// Cấu hình để upload ảnh lên Cloudinary với folder và tên file tùy chỉnh
			Map<String, Object> options = new HashMap<>();
			options.put("folder", "images"); // Chỉ định thư mục "images"
			options.put("public_id", filename); // Sử dụng tên file tùy chỉnh

			// Upload ảnh lên Cloudinary
			Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);

			// Trả về URL của ảnh đã upload
			return result.get("secure_url").toString();
		} catch (IOException e) {
			throw new RuntimeException("Lỗi khi upload file lên Cloudinary: " + e.getMessage());
		}
	}

	@Override
	public boolean deleteImage(String imageUrl) {
		try {
			// Lấy public_id từ imageUrl
			String publicId = extractPublicId(imageUrl);
			if (publicId != null) {
				// Xóa hình ảnh trên Cloudinary sử dụng publicId
				cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String extractPublicId(String imageUrl) {
		try {
			// Kiểm tra URL hợp lệ
			if (imageUrl == null || imageUrl.isEmpty()) {
				return null;
			}

			// Tách phần sau "/upload/"
			String[] parts = imageUrl.split("/upload/");
			if (parts.length <= 1) {
				return null;
			}

			String publicPart = parts[1];

			// Loại bỏ version (vd: v1745601535/)
			int firstSlash = publicPart.indexOf('/');
			if (firstSlash != -1) {
				publicPart = publicPart.substring(firstSlash + 1);
			}

			// Loại bỏ phần mở rộng file (.jpg, .png, .avif,...)
			int lastDot = publicPart.lastIndexOf('.');
			if (lastDot != -1) {
				return publicPart.substring(0, lastDot); // Lấy public_id
			}

			return publicPart;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
