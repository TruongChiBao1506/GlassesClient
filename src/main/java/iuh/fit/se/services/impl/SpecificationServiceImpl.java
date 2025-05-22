package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.entities.Specifications;
import iuh.fit.se.services.SpecificationsService;
import iuh.fit.se.utils.ApiResponse;

@Service
public class SpecificationServiceImpl implements SpecificationsService {
	private RestClient restClient;
	private ObjectMapper objectMapper;
	@Value("${api.url}")
	private String ENDPOINT;

	public SpecificationServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		super();
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse findById(Long id) {
		return restClient.get().uri(ENDPOINT + "/Specification/Specifications" + id).accept(MediaType.APPLICATION_JSON)
				.exchange((request, response) -> {
					ApiResponse<Specifications> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Specifications.class));
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
	public ApiResponse<List<Specifications>> findAll() {
		return restClient.get().uri(ENDPOINT + "/Specification/Specifications") // Endpoint lấy danh sách tất cả sản
																				// phẩm kính
				.accept(MediaType.APPLICATION_JSON).exchange((request, response) -> {
					ApiResponse<List<Specifications>> apiResponse = null;
					try (InputStream is = response.getBody()) {
						// Đọc dữ liệu từ phản hồi và chuyển thành ApiResponse
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(),
								new TypeReference<List<Specifications>>() {
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
}
