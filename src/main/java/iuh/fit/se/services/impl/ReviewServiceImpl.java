package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dtos.ReviewRequest;
import iuh.fit.se.dtos.ReviewResponse;
import iuh.fit.se.entities.Review;
import iuh.fit.se.services.ReviewService;
import iuh.fit.se.utils.ApiResponse;
import iuh.fit.se.utils.SessionUtil;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private SessionUtil sessionUtil;

	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://localhost:8080/api";

	public ReviewServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest) {
		String token = sessionUtil.getToken();
		return restClient.post().uri(ENDPOINT + "/reviews/create")
				.header("Authorization", "Bearer " + sessionUtil.getToken())
				 .header("Cookie", "refreshToken=" + sessionUtil.getRefreshToken())
				.body(reviewRequest).exchange((request, response) -> {
					ApiResponse<ReviewResponse> apiResponse = null;
					try (InputStream is = response.getBody()) {
						apiResponse = objectMapper.readValue(is, ApiResponse.class);
						apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), ReviewResponse.class));
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
	public ApiResponse<List<Review>> getReviewsByProductId(Long productId, int page, int size) {
		return restClient.get().uri(ENDPOINT + "/reviews/getReviews?productId=" + productId + "&page=" + page + "&size=" + size)
				.exchange((request, response) -> {
					ApiResponse<List<Review>> apiResponse = null;				
					try (InputStream is = response.getBody()) {
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
