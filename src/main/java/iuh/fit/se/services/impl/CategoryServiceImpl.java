package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.entities.Category;
import iuh.fit.se.services.CategoryService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private SessionUtil sessionUtil;

	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";

	public CategoryServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<Category> findById(Long id) {
		return restClient.get().uri(ENDPOINT + "/products/categories/" + id)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
					ApiResponse<Category> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), Category.class));
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
	public ApiResponse<List<Category>> findAll() {
		return restClient.get().uri(ENDPOINT + "/products/categories")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.exchange((request, response) -> {
			ApiResponse<List<Category>> apiResponse = null;
			try (InputStream is = response.getBody()){
				apiResponse = objectMapper.readValue(is, ApiResponse.class);
				apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), List.class));
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
