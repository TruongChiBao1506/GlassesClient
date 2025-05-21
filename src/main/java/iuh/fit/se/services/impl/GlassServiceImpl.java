package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dtos.FilterRequest;
import iuh.fit.se.dtos.GlassDTO;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.services.GlassService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class GlassServiceImpl implements GlassService {

	@Autowired
	private SessionUtil sessionUtil;
	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	public GlassServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategoryEyeGlass() {
		return restClient.get().uri(ENDPOINT + "/products/eyeglasses").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategoryEyeGlassMen() {
		return restClient.get().uri(ENDPOINT + "/products/eyeglasses/men").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategoryEyeGlassWomen() {
		return restClient.get().uri(ENDPOINT + "/products/eyeglasses/women").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategorySunGlass() {
		return restClient.get().uri(ENDPOINT + "/products/sunglasses").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategorySunGlassMen() {
		return restClient.get().uri(ENDPOINT + "/products/sunglasses/men").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findByCategorySunGlassWomen() {
		return restClient.get().uri(ENDPOINT + "/products/sunglasses/women").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Glass> findById(Long id) {
		return restClient.get().uri(ENDPOINT + "/products/glasses/" + id).accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<Glass> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						System.out.println("API Response: "+ apiResponse);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Glass.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response từ products: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<String>> getAllBrand() {
		return restClient.get().uri(ENDPOINT + "/products/brands").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<String>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<String>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<String>> getAllShape() {
		return restClient.get().uri(ENDPOINT + "/products/shapes").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<String>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<String>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<String>> getAllMaterial() {
		return restClient.get().uri(ENDPOINT + "/products/materials").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<String>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<String>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<String>> getAllColor() {
		return restClient.get().uri(ENDPOINT + "/products/colors").accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<String>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<String>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});

	}

	@SuppressWarnings("unchecked")
@Override
public ApiResponse<Map<String, Object>> findByCategoryEyeGlassMenFilter(FilterRequest filter, int page, int size) {
    String url = ENDPOINT + "/products/eyeglasses/men?";
    if (filter.getMinPrice() != null) {
        url += "minPrice=" + filter.getMinPrice() + "&";
    }
    if (filter.getMaxPrice() != null) {
        url += "maxPrice=" + filter.getMaxPrice() + "&";
    }
    if (filter.getBrands() != null) {
        url += "brand=" + filter.getBrands() + "&";
    }
    if (filter.getShapes() != null) {
        url += "shape=" + filter.getShapes() + "&";
    }
    if (filter.getMaterials() != null) {
        url += "material=" + filter.getMaterials() + "&";
    }
    if (filter.getColors() != null) {
        url += "color=" + filter.getColors() + "&";
    }
    
    // Thêm tham số phân trang
    url += "page=" + page + "&size=" + size;
    
    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
            .exchange((request, response) -> {
                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
                try (InputStream is = response.getBody()) {
                    // Phân tích phản hồi trực tiếp thành Map để xử lý cấu trúc phân trang
                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
                    
                    // Trích xuất trạng thái từ phản hồi
                    Integer status = (Integer) responseMap.get("status");
                    apiResponse.setStatus(status);
                    
                    // Trích xuất và chuyển đổi danh sách kính
                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
                            new TypeReference<List<GlassDTO>>() {});
                    
                    // Tạo map để lưu tất cả dữ liệu phân trang
                    Map<String, Object> paginationData = new HashMap<>();
                    paginationData.put("data", glasses); // danh sách kính
                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
                    
                    // Đặt toàn bộ map phân trang làm dữ liệu
                    apiResponse.setData(paginationData);
                } catch (IOException e) {
                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
                }
                return apiResponse;
            });
}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> findByCategoryEyeGlassWomenFilter(FilterRequest filter, int page, int size) {
	    String url = ENDPOINT + "/products/eyeglasses/women?";
	    if (filter.getMinPrice() != null) {
	        url += "minPrice=" + filter.getMinPrice() + "&";
	    }
	    if (filter.getMaxPrice() != null) {
	        url += "maxPrice=" + filter.getMaxPrice() + "&";
	    }
	    if (filter.getBrands() != null) {
	        url += "brand=" + filter.getBrands() + "&";
	    }
	    if (filter.getShapes() != null) {
	        url += "shape=" + filter.getShapes() + "&";
	    }
	    if (filter.getMaterials() != null) {
	        url += "material=" + filter.getMaterials() + "&";
	    }
	    if (filter.getColors() != null) {
	        url += "color=" + filter.getColors() + "&";
	    }
	    
	    // Thêm tham số phân trang
	    url += "page=" + page + "&size=" + size;
	    
	    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
	            .exchange((request, response) -> {
	                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
	                try (InputStream is = response.getBody()) {
	                    // Phân tích phản hồi trực tiếp thành Map
	                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
	                    
	                    // Trích xuất trạng thái từ phản hồi
	                    Integer status = (Integer) responseMap.get("status");
	                    apiResponse.setStatus(status);
	                    
	                    // Trích xuất và chuyển đổi danh sách kính
	                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
	                            new TypeReference<List<GlassDTO>>() {});
	                    
	                    // Tạo map để lưu tất cả dữ liệu phân trang
	                    Map<String, Object> paginationData = new HashMap<>();
	                    paginationData.put("data", glasses); // danh sách kính
	                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
	                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
	                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
	                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
	                    
	                    // Đặt toàn bộ map phân trang làm dữ liệu
	                    apiResponse.setData(paginationData);
	                } catch (IOException e) {
	                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
	                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
	                }
	                return apiResponse;
	            });
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> findByCategorySunGlassMenFilter(FilterRequest filter, int page, int size) {
	    String url = ENDPOINT + "/products/sunglasses/men?";
	    if (filter.getMinPrice() != null) {
	        url += "minPrice=" + filter.getMinPrice() + "&";
	    }
	    if (filter.getMaxPrice() != null) {
	        url += "maxPrice=" + filter.getMaxPrice() + "&";
	    }
	    if (filter.getBrands() != null) {
	        url += "brand=" + filter.getBrands() + "&";
	    }
	    if (filter.getShapes() != null) {
	        url += "shape=" + filter.getShapes() + "&";
	    }
	    if (filter.getMaterials() != null) {
	        url += "material=" + filter.getMaterials() + "&";
	    }
	    if (filter.getColors() != null) {
	        url += "color=" + filter.getColors() + "&";
	    }
	    
	    // Thêm tham số phân trang
	    url += "page=" + page + "&size=" + size;
	    
	    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
	            .exchange((request, response) -> {
	                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
	                try (InputStream is = response.getBody()) {
	                    // Phân tích phản hồi trực tiếp thành Map
	                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
	                    
	                    // Trích xuất trạng thái từ phản hồi
	                    Integer status = (Integer) responseMap.get("status");
	                    apiResponse.setStatus(status);
	                    
	                    // Trích xuất và chuyển đổi danh sách kính
	                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
	                            new TypeReference<List<GlassDTO>>() {});
	                    
	                    // Tạo map để lưu tất cả dữ liệu phân trang
	                    Map<String, Object> paginationData = new HashMap<>();
	                    paginationData.put("data", glasses); // danh sách kính
	                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
	                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
	                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
	                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
	                    
	                    // Đặt toàn bộ map phân trang làm dữ liệu
	                    apiResponse.setData(paginationData);
	                } catch (IOException e) {
	                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
	                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
	                }
	                return apiResponse;
	            });
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> findByCategorySunGlassWomenFilter(FilterRequest filter, int page, int size) {
	    String url = ENDPOINT + "/products/sunglasses/women?";
	    if (filter.getMinPrice() != null) {
	        url += "minPrice=" + filter.getMinPrice() + "&";
	    }
	    if (filter.getMaxPrice() != null) {
	        url += "maxPrice=" + filter.getMaxPrice() + "&";
	    }
	    if (filter.getBrands() != null) {
	        url += "brand=" + filter.getBrands() + "&";
	    }
	    if (filter.getShapes() != null) {
	        url += "shape=" + filter.getShapes() + "&";
	    }
	    if (filter.getMaterials() != null) {
	        url += "material=" + filter.getMaterials() + "&";
	    }
	    if (filter.getColors() != null) {
	        url += "color=" + filter.getColors() + "&";
	    }
	    
	    // Thêm tham số phân trang
	    url += "page=" + page + "&size=" + size;
	    
	    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
	            .exchange((request, response) -> {
	                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
	                try (InputStream is = response.getBody()) {
	                    // Phân tích phản hồi trực tiếp thành Map
	                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
	                    
	                    // Trích xuất trạng thái từ phản hồi
	                    Integer status = (Integer) responseMap.get("status");
	                    apiResponse.setStatus(status);
	                    
	                    // Trích xuất và chuyển đổi danh sách kính
	                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
	                            new TypeReference<List<GlassDTO>>() {});
	                    
	                    // Tạo map để lưu tất cả dữ liệu phân trang
	                    Map<String, Object> paginationData = new HashMap<>();
	                    paginationData.put("data", glasses); // danh sách kính
	                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
	                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
	                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
	                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
	                    
	                    // Đặt toàn bộ map phân trang làm dữ liệu
	                    apiResponse.setData(paginationData);
	                } catch (IOException e) {
	                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
	                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
	                }
	                return apiResponse;
	            });
	}
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<GlassDTO>> search(String keyword) {
		return restClient.get().uri(ENDPOINT + "/products/search?keyword=" + keyword)
				.accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<GlassDTO>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<GlassDTO>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> searchWithPagination(String keyword, int page, int size) {
		return restClient.get().uri(ENDPOINT + "/products/search?keyword=" + keyword + "&page=" + page + "&size=" + size)
				.accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
					try (InputStream is = response.getBody()) {
						// Phân tích phản hồi trực tiếp thành Map
						Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
						
						// Trích xuất trạng thái từ phản hồi
						Integer status = (Integer) responseMap.get("status");
						apiResponse.setStatus(status);
						
						// Trích xuất và chuyển đổi danh sách kính
						List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<GlassDTO>>() {});
						
						// Tạo map để lưu tất cả dữ liệu phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", glasses); // danh sách kính
						paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
						paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
						paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
						paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
						
						// Đặt toàn bộ map phân trang làm dữ liệu
						apiResponse.setData(paginationData);
					} catch (IOException e) {
						System.err.println("Lỗi đọc response từ product: " + e.getMessage());
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> findAll() {
		return restClient.get().uri(ENDPOINT + "/products/glasses")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(
								objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
								}));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> findAllPaginated(int page, int size) {
	    String url = ENDPOINT + "/products/glasses?page=" + page + "&size=" + size;
	    
	    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
	            .exchange((request, response) -> {
	                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
	                try (InputStream is = response.getBody()) {
	                    // Phân tích phản hồi trực tiếp thành Map
	                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
	                    
	                    // Trích xuất trạng thái từ phản hồi
	                    Integer status = (Integer) responseMap.get("status");
	                    apiResponse.setStatus(status);
	                    
	                    // Trích xuất và chuyển đổi danh sách kính
	                    List<Glass> glasses = objectMapper.convertValue(responseMap.get("data"), 
	                            new TypeReference<List<Glass>>() {});
	                    
	                    // Tạo map để lưu tất cả dữ liệu phân trang
	                    Map<String, Object> paginationData = new HashMap<>();
	                    paginationData.put("data", glasses); // danh sách kính
	                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
	                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
	                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
	                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
	                    
	                    // Đặt toàn bộ map phân trang làm dữ liệu
	                    apiResponse.setData(paginationData);
	                } catch (IOException e) {
	                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
	                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
	                }
	                return apiResponse;
	            });
	}

	// Thêm
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Glass> createGlass(Glass glass) {
		return restClient.post().uri(ENDPOINT + "/products/glasses")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.contentType(MediaType.APPLICATION_JSON).body(glass)
				.exchange((request, response) -> {
					ApiResponse<Glass> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Glass.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	// update
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Glass> updateGlass(Long id, Glass glass) {
		return restClient.put().uri(ENDPOINT + "/products/glasses/" + id)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.contentType(MediaType.APPLICATION_JSON)
				.body(glass).exchange((request, response) -> {
					ApiResponse<Glass> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Glass.class));
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	// Xóa
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> deleteGlass(Long id) {
		return restClient.delete().uri(ENDPOINT + "/products/glasses/" + id)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
			ApiResponse<String> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Lỗi đọc response: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}
	// tìm
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<Glass>> searchByName(String keyword) {
		return restClient.get().uri(ENDPOINT + "/glasses/search?keyword=" + keyword)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
					ApiResponse<List<Glass>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						// Đọc response dạng Map trước
						Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
						
						// Tạo ApiResponse mới
						apiResponse = new ApiResponse<>();
						apiResponse.setStatus((Integer) responseMap.get("status"));
						
						// Lấy danh sách glasses từ phần data
						List<Glass> glasses = objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<Glass>>() {});
						apiResponse.setData(glasses);
					} catch (IOException e) {
						// TODO: handle exception
						System.err.println("Lỗi đọc response: " + e.getMessage());
						apiResponse = new ApiResponse<>();
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	// tìm theo tên có phân trang
	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Map<String, Object>> searchByNamePaginated(String keyword, int page, int size) {
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		return restClient.get().uri(ENDPOINT + "/products/glasses/search?keyword=" + encodedKeyword + "&page=" + page + "&size=" + size)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				.header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
					ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
					try (InputStream is = response.getBody()) {
						// Phân tích phản hồi trực tiếp thành Map
						Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
						
						// Trích xuất trạng thái từ phản hồi
						Integer status = (Integer) responseMap.get("status");
						apiResponse.setStatus(status);
						
						// Trích xuất và chuyển đổi danh sách kính
						List<Glass> glasses = objectMapper.convertValue(responseMap.get("data"), 
								new TypeReference<List<Glass>>() {});
						
						// Tạo map để lưu tất cả dữ liệu phân trang
						Map<String, Object> paginationData = new HashMap<>();
						paginationData.put("data", glasses); // danh sách kính
						paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
						paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
						paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
						paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
						
						// Đặt toàn bộ map phân trang làm dữ liệu
						apiResponse.setData(paginationData);
					} catch (IOException e) {
						System.err.println("Lỗi đọc response từ product: " + e.getMessage());
						apiResponse.setMessage("Không thể phân tích phản hồi từ server");
					}
					return apiResponse;
				});
	}

	@Override
	public ApiResponse<Map<String, Object>> findByCategoryEyeGlassFilter(FilterRequest filter, int page, int size) {
		 String url = ENDPOINT + "/products/eyeglasses?";
		    if (filter.getMinPrice() != null) {
		        url += "minPrice=" + filter.getMinPrice() + "&";
		    }
		    if (filter.getMaxPrice() != null) {
		        url += "maxPrice=" + filter.getMaxPrice() + "&";
		    }
		    if (filter.getBrands() != null) {
		        url += "brand=" + filter.getBrands() + "&";
		    }
		    if (filter.getShapes() != null) {
		        url += "shape=" + filter.getShapes() + "&";
		    }
		    if (filter.getMaterials() != null) {
		        url += "material=" + filter.getMaterials() + "&";
		    }
		    if (filter.getColors() != null) {
		        url += "color=" + filter.getColors() + "&";
		    }
		    
		    // Thêm tham số phân trang
		    url += "page=" + page + "&size=" + size;
		    
		    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
		            .exchange((request, response) -> {
		                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
		                try (InputStream is = response.getBody()) {
		                    // Phân tích phản hồi trực tiếp thành Map để xử lý cấu trúc phân trang
		                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
		                    
		                    // Trích xuất trạng thái từ phản hồi
		                    Integer status = (Integer) responseMap.get("status");
		                    apiResponse.setStatus(status);
		                    
		                    // Trích xuất và chuyển đổi danh sách kính
		                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
		                            new TypeReference<List<GlassDTO>>() {});
		                    
		                    // Tạo map để lưu tất cả dữ liệu phân trang
		                    Map<String, Object> paginationData = new HashMap<>();
		                    paginationData.put("data", glasses); // danh sách kính
		                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
		                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
		                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
		                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
		                    
		                    // Đặt toàn bộ map phân trang làm dữ liệu
		                    apiResponse.setData(paginationData);
		                } catch (IOException e) {
		                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
		                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
		                }
		                return apiResponse;
		            });
	}

	@Override
	public ApiResponse<Map<String, Object>> findByCategorySunGlassFilter(FilterRequest filter, int page, int size) {
		 String url = ENDPOINT + "/products/sunglasses?";
		    if (filter.getMinPrice() != null) {
		        url += "minPrice=" + filter.getMinPrice() + "&";
		    }
		    if (filter.getMaxPrice() != null) {
		        url += "maxPrice=" + filter.getMaxPrice() + "&";
		    }
		    if (filter.getBrands() != null) {
		        url += "brand=" + filter.getBrands() + "&";
		    }
		    if (filter.getShapes() != null) {
		        url += "shape=" + filter.getShapes() + "&";
		    }
		    if (filter.getMaterials() != null) {
		        url += "material=" + filter.getMaterials() + "&";
		    }
		    if (filter.getColors() != null) {
		        url += "color=" + filter.getColors() + "&";
		    }
		    
		    // Thêm tham số phân trang
		    url += "page=" + page + "&size=" + size;
		    
		    return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
		            .exchange((request, response) -> {
		                ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
		                try (InputStream is = response.getBody()) {
		                    // Phân tích phản hồi trực tiếp thành Map
		                    Map<String, Object> responseMap = objectMapper.readValue(is, Map.class);
		                    
		                    // Trích xuất trạng thái từ phản hồi
		                    Integer status = (Integer) responseMap.get("status");
		                    apiResponse.setStatus(status);
		                    
		                    // Trích xuất và chuyển đổi danh sách kính
		                    List<GlassDTO> glasses = objectMapper.convertValue(responseMap.get("data"), 
		                            new TypeReference<List<GlassDTO>>() {});
		                    
		                    // Tạo map để lưu tất cả dữ liệu phân trang
		                    Map<String, Object> paginationData = new HashMap<>();
		                    paginationData.put("data", glasses); // danh sách kính
		                    paginationData.put("currentPage", responseMap.get("currentPage")); // trang hiện tại
		                    paginationData.put("totalItems", responseMap.get("totalItems")); // tổng số mục
		                    paginationData.put("totalPages", responseMap.get("totalPages")); // tổng số trang
		                    paginationData.put("hasMore", responseMap.get("hasMore")); // có thêm dữ liệu không
		                    
		                    // Đặt toàn bộ map phân trang làm dữ liệu
		                    apiResponse.setData(paginationData);
		                } catch (IOException e) {
		                    System.err.println("Lỗi đọc response từ product: " + e.getMessage());
		                    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
		                }
		                return apiResponse;
		            });
	}
}
