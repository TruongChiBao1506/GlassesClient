package iuh.fit.se.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iuh.fit.se.dtos.FilterRequest;
import iuh.fit.se.dtos.GlassDTO;
import iuh.fit.se.entities.Category;
import iuh.fit.se.entities.FrameSize;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.entities.Notification;
import iuh.fit.se.entities.Review;
import iuh.fit.se.entities.Specifications;
import iuh.fit.se.services.CategoryService;
import iuh.fit.se.services.CloudinaryService;
import iuh.fit.se.services.FrameSizeService;
import iuh.fit.se.services.GlassService;
import iuh.fit.se.services.NotificationService;
import iuh.fit.se.services.ReviewService;
import iuh.fit.se.services.SpecificationsService;
import iuh.fit.se.utils.ApiResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class GlassController {

	@Autowired
	GlassService glassService;

	@Autowired
	ReviewService reviewService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	FrameSizeService frameSizeService;

	@Autowired
	SpecificationsService specificationsService;

	@Autowired
	CloudinaryService cloudinaryService;

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/eyeglasses")
	public String getEyeGlasses(Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<Glass>> response = glassService.findByCategoryEyeGlass();
		ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();
		
		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính mắt");
		}
		if (responseNotifications == null || responseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseNotifications != null && responseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", responseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}
		
		return "KinhNam";
	}

	@GetMapping("/eyeglasses/men")
	public String getEyeGlassesMen(Model model, @RequestParam(required = false) String brand,
			@RequestParam(required = false) String shape, @RequestParam(required = false) String material,
			@RequestParam(required = false) String color, @RequestParam(required = false) String priceMin,
			@RequestParam(required = false) String priceMax) {
		FilterRequest filter = new FilterRequest();
		filter.setBrands(brand);
		filter.setShapes(shape);
		filter.setMaterials(material);
		filter.setColors(color);
		if (priceMin != "")
			filter.setMinPrice(priceMin);
		if (priceMax != "")
			filter.setMaxPrice(priceMax);

		System.out.println("Filter: " + filter.toString());

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<GlassDTO>> response = glassService.findByCategoryEyeGlassMenFilter(filter);
		ApiResponse<List<String>> responseBrand = glassService.getAllBrand();
		ApiResponse<List<String>> responseShape = glassService.getAllShape();
		ApiResponse<List<String>> responseMaterial = glassService.getAllMaterial();
		ApiResponse<List<String>> responseColor = glassService.getAllColor();

		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính theo bộ lọc");
		}
		if (responseBrand == null || responseBrand.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thương hiệu");
		}
		if (responseShape == null || responseShape.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách hình dạng");
		}
		if (responseMaterial == null || responseMaterial.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách chất liệu");
		}
		if (responseColor == null || responseColor.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách màu sắc");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}

		List<GlassDTO> glasses = null;
		System.out.println("glasses Men");
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			glasses = response.getData();
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseBrand != null && responseBrand.getStatus() == 200) {
			model.addAttribute("brands", responseBrand.getData());
		} else {
			model.addAttribute("brands", List.of());
		}
		
		if (responseShape != null && responseShape.getStatus() == 200) {
			model.addAttribute("shapes", responseShape.getData());
		} else {
			model.addAttribute("shapes", List.of());
		}
		
		if (responseMaterial != null && responseMaterial.getStatus() == 200) {
			model.addAttribute("materials", responseMaterial.getData());
		} else {
			model.addAttribute("materials", List.of());
		}
		
		if (responseColor != null && responseColor.getStatus() == 200) {
			model.addAttribute("colors", responseColor.getData());
		} else {
			model.addAttribute("colors", List.of());
		}

		model.addAttribute("category", "Eyeglasses");
		model.addAttribute("gender", "Men");

		return "KinhNam";
	}

	@GetMapping("/eyeglasses/women")
	public String getEyeGlassesWomen(Model model, @RequestParam(required = false) String brand,
			@RequestParam(required = false) String shape, @RequestParam(required = false) String material,
			@RequestParam(required = false) String color, @RequestParam(required = false) String priceMin,
			@RequestParam(required = false) String priceMax) {
		FilterRequest filter = new FilterRequest();
		filter.setBrands(brand);
		filter.setShapes(shape);
		filter.setMaterials(material);
		filter.setColors(color);
		if (priceMin != "")
			filter.setMinPrice(priceMin);
		if (priceMax != "")
			filter.setMaxPrice(priceMax);

		System.out.println("Filter: " + filter.toString());

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<GlassDTO>> response = glassService.findByCategoryEyeGlassWomenFilter(filter);
		ApiResponse<List<String>> responseBrand = glassService.getAllBrand();
		ApiResponse<List<String>> responseShape = glassService.getAllShape();
		ApiResponse<List<String>> responseMaterial = glassService.getAllMaterial();
		ApiResponse<List<String>> responseColor = glassService.getAllColor();

		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính theo bộ lọc");
		}
		if (responseBrand == null || responseBrand.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thương hiệu");
		}
		if (responseShape == null || responseShape.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách hình dạng");
		}
		if (responseMaterial == null || responseMaterial.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách chất liệu");
		}
		if (responseColor == null || responseColor.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách màu sắc");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		List<GlassDTO> glasses = null;
		System.out.println("glasses Women");
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			glasses = response.getData();
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseBrand != null && responseBrand.getStatus() == 200) {
			model.addAttribute("brands", responseBrand.getData());
		} else {
			model.addAttribute("brands", List.of());
		}
		
		if (responseShape != null && responseShape.getStatus() == 200) {
			model.addAttribute("shapes", responseShape.getData());
		} else {
			model.addAttribute("shapes", List.of());
		}
		
		if (responseMaterial != null && responseMaterial.getStatus() == 200) {
			model.addAttribute("materials", responseMaterial.getData());
		} else {
			model.addAttribute("materials", List.of());
		}
		
		if (responseColor != null && responseColor.getStatus() == 200) {
			model.addAttribute("colors", responseColor.getData());
		} else {
			model.addAttribute("colors", List.of());
		}

		model.addAttribute("category", "Eyeglasses");
		model.addAttribute("gender", "Women");
		return "KinhNam";
	}

	@GetMapping("/sunglasses")
	public String getSunGlasses(Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<Glass>> response = glassService.findByCategorySunGlass();
		ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();
		
		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính mát");
		}
		if (responseNotifications == null || responseNotifications.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseNotifications != null && responseNotifications.getStatus() == 200) {
			model.addAttribute("notifications", responseNotifications.getData());
		} else {
			model.addAttribute("notifications", List.of());
		}
		
		return "KinhNam";
	}

	@GetMapping("/sunglasses/men")
	public String getSunGlassesMen(Model model, @RequestParam(required = false) String brand,
			@RequestParam(required = false) String shape, @RequestParam(required = false) String material,
			@RequestParam(required = false) String color, @RequestParam(required = false) String priceMin,
			@RequestParam(required = false) String priceMax) {
		FilterRequest filter = new FilterRequest();
		filter.setBrands(brand);
		filter.setShapes(shape);
		filter.setMaterials(material);
		filter.setColors(color);
		if (priceMin != "")
			filter.setMinPrice(priceMin);
		if (priceMax != "")
			filter.setMaxPrice(priceMax);

		System.out.println("Filter: " + filter.toString());

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<GlassDTO>> response = glassService.findByCategorySunGlassMenFilter(filter);
		ApiResponse<List<String>> responseBrand = glassService.getAllBrand();
		ApiResponse<List<String>> responseShape = glassService.getAllShape();
		ApiResponse<List<String>> responseMaterial = glassService.getAllMaterial();
		ApiResponse<List<String>> responseColor = glassService.getAllColor();

		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính theo bộ lọc");
		}
		if (responseBrand == null || responseBrand.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thương hiệu");
		}
		if (responseShape == null || responseShape.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách hình dạng");
		}
		if (responseMaterial == null || responseMaterial.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách chất liệu");
		}
		if (responseColor == null || responseColor.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách màu sắc");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}

		List<GlassDTO> glasses = null;
		System.out.println("Sunglasses Men");
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			glasses = response.getData();
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseBrand != null && responseBrand.getStatus() == 200) {
			model.addAttribute("brands", responseBrand.getData());
		} else {
			model.addAttribute("brands", List.of());
		}
		
		if (responseShape != null && responseShape.getStatus() == 200) {
			model.addAttribute("shapes", responseShape.getData());
		} else {
			model.addAttribute("shapes", List.of());
		}
		
		if (responseMaterial != null && responseMaterial.getStatus() == 200) {
			model.addAttribute("materials", responseMaterial.getData());
		} else {
			model.addAttribute("materials", List.of());
		}
		
		if (responseColor != null && responseColor.getStatus() == 200) {
			model.addAttribute("colors", responseColor.getData());
		} else {
			model.addAttribute("colors", List.of());
		}

		model.addAttribute("category", "Sunglasses");
		model.addAttribute("gender", "Men");
		return "KinhNam";
	}

	@GetMapping("/sunglasses/women")
	public String getSunGlassesWomen(Model model, @RequestParam(required = false) String brand,
			@RequestParam(required = false) String shape, @RequestParam(required = false) String material,
			@RequestParam(required = false) String color, @RequestParam(required = false) String priceMin,
			@RequestParam(required = false) String priceMax) {
		FilterRequest filter = new FilterRequest();
		filter.setBrands(brand);
		filter.setShapes(shape);
		filter.setMaterials(material);
		filter.setColors(color);
		if (priceMin != "")
			filter.setMinPrice(priceMin);
		if (priceMax != "")
			filter.setMaxPrice(priceMax);

		System.out.println("Filter: " + filter.toString());

		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<GlassDTO>> response = glassService.findByCategorySunGlassWomenFilter(filter);
		ApiResponse<List<String>> responseBrand = glassService.getAllBrand();
		ApiResponse<List<String>> responseShape = glassService.getAllShape();
		ApiResponse<List<String>> responseMaterial = glassService.getAllMaterial();
		ApiResponse<List<String>> responseColor = glassService.getAllColor();

		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách kính theo bộ lọc");
		}
		if (responseBrand == null || responseBrand.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách thương hiệu");
		}
		if (responseShape == null || responseShape.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách hình dạng");
		}
		if (responseMaterial == null || responseMaterial.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách chất liệu");
		}
		if (responseColor == null || responseColor.getStatus() != 200) {
			errorMessages.add("Không thể lấy danh sách màu sắc");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}

		List<GlassDTO> glasses = null;
		System.out.println("Sunglasses Women");
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			glasses = response.getData();
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
		
		if (responseBrand != null && responseBrand.getStatus() == 200) {
			model.addAttribute("brands", responseBrand.getData());
		} else {
			model.addAttribute("brands", List.of());
		}
		
		if (responseShape != null && responseShape.getStatus() == 200) {
			model.addAttribute("shapes", responseShape.getData());
		} else {
			model.addAttribute("shapes", List.of());
		}
		
		if (responseMaterial != null && responseMaterial.getStatus() == 200) {
			model.addAttribute("materials", responseMaterial.getData());
		} else {
			model.addAttribute("materials", List.of());
		}
		
		if (responseColor != null && responseColor.getStatus() == 200) {
			model.addAttribute("colors", responseColor.getData());
		} else {
			model.addAttribute("colors", List.of());
		}

		model.addAttribute("category", "Sunglasses");
		model.addAttribute("gender", "Women");
		return "KinhNam";
	}

	@GetMapping("/glasses/{id}")
	public String getGlassesbyId(@PathVariable Long id, Model model, HttpSession session) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<Glass> response = glassService.findById(id);
		ApiResponse<List<Review>> responseReview = reviewService.getReviewsByProductId(id, 0, 5);
		
		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể hiển thị thông tin kính");
		}
		if (responseReview == null || responseReview.getStatus() != 200) {
			errorMessages.add("Không thể hiển thị các đánh giá");
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", null);
		}
		
		model.addAttribute("username", session.getAttribute("username"));
		
		if (responseReview != null && responseReview.getData() != null) {
			List<?> rawReviews = responseReview.getData();
			List<Review> reviews = new ArrayList<>();
			
			// Xử lý chuyển đổi từ LinkedHashMap sang Review
			for (Object rawReview : rawReviews) {
				if (rawReview instanceof Review) {
					reviews.add((Review) rawReview);
				} else if (rawReview instanceof Map) {
					Map<?, ?> reviewMap = (Map<?, ?>) rawReview;
					Review review = convertMapToReview(reviewMap);
					if (review != null) {
						reviews.add(review);
					}
				}
			}
			
			// Tính toán điểm đánh giá trung bình
			double averageRating = calculateAverageRating(reviews);
			model.addAttribute("averageRating", averageRating);
			
			// Thêm danh sách đánh giá
			model.addAttribute("reviews", reviews);
			
			// Tính tổng số đánh giá
			model.addAttribute("reviewCount", reviews.size());
			
			// Tính số lượng đánh giá cho mỗi mức sao (1-5)
			Map<Integer, Integer> ratingCounts = countRatingsByLevel(reviews);
			model.addAttribute("ratingCounts", ratingCounts);
			
			model.addAttribute("hasMore", responseReview.getMessage());
		} else {
			model.addAttribute("reviews", List.of());
			model.addAttribute("averageRating", 0.0);
			model.addAttribute("reviewCount", 0);
			model.addAttribute("ratingCounts", Map.of());
			model.addAttribute("hasMore", null);
		}
		
		return "detail";
	}

	// Phương thức hỗ trợ chuyển đổi từ Map sang đối tượng Review
	private Review convertMapToReview(Map<?, ?> map) {
		try {
			Review review = new Review();
			
			if (map.containsKey("userId") && map.get("userId") != null) {
				review.setUserId(Long.valueOf(map.get("userId").toString()));
			}
			
			if (map.containsKey("productId") && map.get("productId") != null) {
				review.setProductId(Long.valueOf(map.get("productId").toString()));
			}
			
			if (map.containsKey("username") && map.get("username") != null) {
				review.setUsername(map.get("username").toString());
			}
			
			if (map.containsKey("productName") && map.get("productName") != null) {
				review.setProductName(map.get("productName").toString());
			}
			
			if (map.containsKey("content") && map.get("content") != null) {
				review.setContent(map.get("content").toString());
			}
			
			if (map.containsKey("rating") && map.get("rating") != null) {
				review.setRating(Integer.valueOf(map.get("rating").toString()));
			}
			
			if (map.containsKey("createdAt") && map.get("createdAt") != null) {
				String dateTimeStr = map.get("createdAt").toString();
				// Convert String to LocalDateTime
				LocalDateTime dateTime = null;
				try {
					// If ISO format (2023-05-06T00:43:30.199785)
					dateTime = LocalDateTime.parse(dateTimeStr);
				} catch (Exception e) {
					// Fallback to current time if parsing fails
					dateTime = LocalDateTime.now();
				}
				review.setCreatedAt(dateTime);
			} else {
				// Set current time if no createdAt value
				review.setCreatedAt(LocalDateTime.now());
			}
			
			return review;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<Integer, Integer> countRatingsByLevel(List<Review> reviews) {
		Map<Integer, Integer> ratingCounts = new HashMap<>();
		
		// Khởi tạo map với tất cả các mức sao từ 1-5
		for (int i = 1; i <= 5; i++) {
			ratingCounts.put(i, 0);
		}
		
		// Đếm số lượng đánh giá cho mỗi mức sao
		for (Review review : reviews) {
			int rating = review.getRating();
			ratingCounts.put(rating, ratingCounts.getOrDefault(rating, 0) + 1);
		}
		
		return ratingCounts;
	}

	public double calculateAverageRating(List<Review> reviews) {
		if (reviews == null || reviews.isEmpty()) {
			return 0.0; // Trả về 0 nếu danh sách rỗng
		}

		int sum = 0;
		for (Review review : reviews) {
			sum += review.getRating(); // Cộng dồn giá trị rating
		}

		return (double) sum / reviews.size(); // Tính trung bình
	}

	@GetMapping("/search")
	public String searchGlasses(@RequestParam String keyword, Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		
		ApiResponse<List<GlassDTO>> response = glassService.search(keyword);
		ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();
		
		if (response == null || response.getStatus() != 200) {
			errorMessages.add("Không thể tìm kiếm sản phẩm với từ khóa: " + keyword);
		}
		
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		
		// Thêm dữ liệu vào model nếu có
		if (response != null && response.getData() != null) {
			model.addAttribute("glasses", response.getData());
		} else {
			model.addAttribute("glasses", List.of());
		}
			
		model.addAttribute("keyword", keyword);
		return "Result-Search";
	}

	@GetMapping
	public String getProducts(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role != null && (role.equals("ADMIN") || role.equals("SUPER"))) {
			// Tạo một danh sách để lưu trữ các thông báo lỗi
			List<String> errorMessages = new ArrayList<>();
			
			ApiResponse<List<Glass>> response = glassService.findAll();
			ApiResponse<List<Notification>> responseNotifications = notificationService.findByIsReadFalse();
			
			if (response == null || response.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách sản phẩm");
			}
			if (responseNotifications == null || responseNotifications.getStatus() != 200) {
				errorMessages.add("Không thể lấy danh sách thông báo");
			}
			
			// Thêm danh sách lỗi vào model nếu có lỗi
			if (!errorMessages.isEmpty()) {
				model.addAttribute("errorMessages", errorMessages);
			}
			
			// Thêm dữ liệu vào model nếu có
			if (response != null && response.getStatus() == 200) {
				model.addAttribute("products", response.getData());
			} else {
				model.addAttribute("products", List.of());
			}
			
			if (responseNotifications != null && responseNotifications.getStatus() == 200) {
				model.addAttribute("notifications", responseNotifications.getData());
			} else {
				model.addAttribute("notifications", List.of());
			}
			
			return "admin-products";
		} else {
			return "accessDenied";
		}
	}

	private String saveFile(MultipartFile file) throws IOException {
		// Đường dẫn thư mục lưu file vào thư mục static/uploads
		String uploadDir = "src/main/resources/static/images/product/eyeGlass/";
		String originalFilename = file.getOriginalFilename();
		String fileName = System.currentTimeMillis() + "_" + originalFilename;
		String filePath = uploadDir + fileName;

		// Tạo thư mục nếu chưa tồn tại
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Lưu file vào hệ thống
		Path path = Paths.get(filePath);
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		// Trả về URL để truy cập file từ thư mục static
		return fileName; // Đây là URL bạn sẽ sử dụng trong HTML hoặc API
	}

	@PostMapping("/add")
	public ModelAndView addGlass(@ModelAttribute Glass glass,
			@RequestParam("imageFrontFile") MultipartFile imageFrontFile,
			@RequestParam("imageSideFile") MultipartFile imageSideFile, RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();

		try {
			if (!imageFrontFile.isEmpty()) {
				String frontImageUrl = cloudinaryService.uploadFile(imageFrontFile); // Dùng Cloudinary
				glass.setImageFrontUrl(frontImageUrl);
			}

			if (!imageSideFile.isEmpty()) {
				String sideImageUrl = cloudinaryService.uploadFile(imageSideFile); // Dùng Cloudinary
				glass.setImageSideUrl(sideImageUrl);
			}

			ApiResponse response;
			if (glass.getId() == null) {
				response = glassService.createGlass(glass);
			} else {
				response = glassService.updateGlass(glass.getId(), glass);
			}

			if (response != null && response.getErrors() != null) {
				mv.setViewName("ProductAdd");
				mv.addObject("errors", response.getErrors());
			} else {
				// Add success notification
				redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được thêm thành công!");
				mv.setViewName("redirect:/products");
				return mv; // Thêm return để kết thúc phương thức khi thành công
			}

		} catch (Exception e) {
			mv.setViewName("ProductAdd");
			mv.addObject("error", "Lỗi xử lý ảnh: " + e.getMessage());
		}

		// Load lại danh sách categories để tránh mất dữ liệu khi reload form
		ApiResponse<List<Category>> categoryResponse = categoryService.findAll();
		mv.addObject("categories", categoryResponse.getData());
		return mv;
	}

	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		try {
			// Lấy thông tin sản phẩm theo id
			ApiResponse<Glass> response = glassService.findById(id);
			if (response.getErrors() != null) {
				model.addAttribute("error", "Product not found");
				return "error";
			}
			Glass product = response.getData();
			System.out.println(product);
			// Lấy danh sách các category, frame sizes và specifications
			ApiResponse<List<Category>> categoryResponse = categoryService.findAll();
			List<Category> categories = categoryResponse.getData();
			ApiResponse<List<Notification>> notificationResponse = notificationService.findByIsReadFalse();
			List<Notification> notifications = notificationResponse.getData();
			// Thêm các thông tin vào model
			model.addAttribute("glass", product);
			model.addAttribute("categories", categories);
			model.addAttribute("notifications", notifications);
			return "ProductForm"; // Tên view để hiển thị form sửa sản phẩm
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi lấy thông tin sản phẩm: " + e.getMessage());
			return "admin-products"; // Tên view để hiển thị thông báo lỗi
		}
	}

	@PostMapping("/update")
	public ModelAndView updateGlass(@ModelAttribute Glass glass,
			@RequestParam(value = "imageFrontFile", required = false) MultipartFile imageFrontFile,
			@RequestParam(value = "imageSideFile", required = false) MultipartFile imageSideFile,
			RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();
		System.out.println(glass);
		try {
			if (imageFrontFile != null && !imageFrontFile.isEmpty()) {
				// Xóa ảnh cũ nếu có
				if (glass.getImageFrontUrl() != null) {
					cloudinaryService.deleteImage(glass.getImageFrontUrl());
				}
				String frontImageUrl = cloudinaryService.uploadFile(imageFrontFile);
				glass.setImageFrontUrl(frontImageUrl);
			}
			if (imageSideFile != null && !imageSideFile.isEmpty()) {
				if (glass.getImageSideUrl() != null) {
					cloudinaryService.deleteImage(glass.getImageSideUrl());
				}
				String sideImageUrl = cloudinaryService.uploadFile(imageSideFile);
				glass.setImageSideUrl(sideImageUrl);
			}

			ApiResponse response = glassService.updateGlass(glass.getId(), glass);
			if (response != null && response.getErrors() != null) {
				mv.addObject("product", glass);
				mv.setViewName("ProductForm");
				mv.addObject("errors", response.getErrors());
			} else {
//				mv.addObject("successMessage", "Cập nhật sản phẩm thành công!");
				redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được cập nhật thành công!");
				mv.setViewName("redirect:/products");
				return mv;
			}
		} catch (Exception e) {
			mv.setViewName("ProductForm");
		}
		ApiResponse<List<Category>> categoryResponse = categoryService.findAll();
		List<Category> categories = categoryResponse.getData();
		mv.addObject("categories", categories);
		return mv;
	}

	@GetMapping("/delete/{id}")
	public String deleteGlass(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			// 1. Lấy thông tin sản phẩm để biết URL ảnh
			ApiResponse<Glass> glassResponse = glassService.findById(id);
			if (glassResponse == null || glassResponse.getData() == null) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm.");
				return "redirect:/products";
			}

			Glass glass = glassResponse.getData();

			// 2. Xóa ảnh trên Cloudinary
			if (glass.getImageFrontUrl() != null) {
				cloudinaryService.deleteImage(glass.getImageFrontUrl());
			}
			if (glass.getImageSideUrl() != null) {
				cloudinaryService.deleteImage(glass.getImageSideUrl());
			}

			// 3. Gọi API xóa sản phẩm
			ApiResponse<String> response = glassService.deleteGlass(id);

			// 4. Hiển thị thông báo
			if (response != null && response.getStatus() == 200) {
				redirectAttributes.addFlashAttribute("successMessage", response.getMessage());
			} else if (response != null && response.getStatus() == 400) {
				redirectAttributes.addFlashAttribute("error", response.getMessage());
			} else {
				redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm.");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
		}

		return "redirect:/products";
	}

	// Hiển thị form thêm
	@GetMapping("/showForm")
	public String showForm(Model model) {
		// Tạo một danh sách để lưu trữ các thông báo lỗi
		List<String> errorMessages = new ArrayList<>();
		Glass glass = new Glass();
		glass.setFrameSize(new FrameSize());
		glass.setSpecifications(new Specifications());
		model.addAttribute("glass", glass);

		ApiResponse<List<Category>> categoriesResponse = categoryService.findAll();
		ApiResponse<List<Notification>> notificationResponse = notificationService.findByIsReadFalse();
		List<Category> categories = categoriesResponse.getData(); // Lấy danh sách categories từ ApiResponse
		List<Notification> notifications = notificationResponse.getData();
		if (categoriesResponse.getErrors() != null) {
			errorMessages.add("Không thể lấy danh sách danh mục");
		}
		if (notificationResponse.getErrors() != null) {
			errorMessages.add("Không thể lấy danh sách thông báo");
		}
		// Thêm danh sách lỗi vào model nếu có lỗi
		if (!errorMessages.isEmpty()) {
			model.addAttribute("errorMessages", errorMessages);
		}
		model.addAttribute("categories", categories);
		model.addAttribute("notifications", notifications);
		return "ProductAdd";
	}

	// Search
	@GetMapping("/searchGlasses")
	public ModelAndView search(@RequestParam String keyword, ModelAndView model) {
		ApiResponse response = glassService.searchByName(keyword);

		List<Glass> glasses = null;

		if (response != null && response.getErrors() == null) {
			glasses = (List<Glass>) response.getData();
		}

		model.setViewName("admin-products");
		model.addObject("products", glasses);

		return model;
	}

	@GetMapping("/try-on")
	public String tryOnLowerCase(@RequestParam(required = false) String glassesId, Model model) {
		if (glassesId != null && !glassesId.isEmpty()) {
			model.addAttribute("selectedGlassesId", glassesId);
		}
		return "TryOn";
	}
}
