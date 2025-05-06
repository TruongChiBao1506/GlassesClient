package iuh.fit.se.services;


import java.util.List;

import iuh.fit.se.dtos.ReviewRequest;
import iuh.fit.se.dtos.ReviewResponse;
import iuh.fit.se.entities.Review;
import iuh.fit.se.utils.ApiResponse;

public interface ReviewService {
	
	public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest);
	
	public ApiResponse<List<Review>> getReviewsByProductId(Long productId, int page, int size);
}
