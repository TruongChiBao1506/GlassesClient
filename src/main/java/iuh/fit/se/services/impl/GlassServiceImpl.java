package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	private static final String ENDPOINT = "http://localhost:8080/api";

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
	public ApiResponse<List<GlassDTO>> findByCategoryEyeGlassMenFilter(FilterRequest filter) {
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
		if(filter.getMinPrice() == null && filter.getMaxPrice() == null 
				&& filter.getBrands() == null 
				&& filter.getShapes() == null 
				&& filter.getMaterials() == null
				&& filter.getColors() == null)
			url = ENDPOINT + "/products/eyeglasses/men";
		return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
			ApiResponse<List<GlassDTO>> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse
						.setData(objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<GlassDTO>>() {
						}));
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Lỗi đọc response từ product: " + e.getMessage());
				apiResponse = new ApiResponse<>();
				apiResponse.setMessage("Không thể phân tích phản hồi từ server");
			}
			return apiResponse;
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<List<GlassDTO>> findByCategoryEyeGlassWomenFilter(FilterRequest filter) {
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
		return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
			ApiResponse<List<GlassDTO>> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse
						.setData(objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<GlassDTO>>() {
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
	public ApiResponse<List<GlassDTO>> findByCategorySunGlassMenFilter(FilterRequest filter) {
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
		return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
			ApiResponse<List<GlassDTO>> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse
						.setData(objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<GlassDTO>>() {
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
	public ApiResponse<List<GlassDTO>> findByCategorySunGlassWomenFilter(FilterRequest filter) {
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
		return restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
			ApiResponse<List<GlassDTO>> apiResponse = null;
			try (InputStream is = response.getBody()) {
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse
						.setData(objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<GlassDTO>>() {
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
		return restClient.get().uri(ENDPOINT + "/products/glasses/search?keyword=" + keyword)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
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

//		@Override
//		public ApiResponse<List<Glass>> findAll() {
//		    return restClient.get()
//		            .uri(ENDPOINT + "/products/glasses") // Endpoint lấy danh sách tất cả sản phẩm kính
//		            .accept(MediaType.APPLICATION_JSON)
//		            .exchange((request, response) -> {
//		                ApiResponse<List<Glass>> apiResponse = null;
//		                if (response.getBody().available() > 0) {
//		                    // Đọc dữ liệu từ phản hồi và chuyển thành ApiResponse
//		                    apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);
//		                    apiResponse.setData(
//		                            objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<Glass>>() {
//		                            }));
//		                }
//		                return apiResponse;
//		            });
//		}

}
