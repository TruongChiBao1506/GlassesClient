package iuh.fit.se.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iuh.fit.se.dtos.ReviewRequest;
import iuh.fit.se.dtos.ReviewResponse;
import iuh.fit.se.services.ReviewService;
import iuh.fit.se.utils.ApiResponse;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/products/glasses/{productId}/reviews")
	public String createReview(@ModelAttribute ReviewRequest reviewRequest, @PathVariable("productId") Long productId, RedirectAttributes redirectAttributes) {
		ReviewRequest review = new ReviewRequest();
		review.setProductId(productId);
		review.setUsername(reviewRequest.getUsername());
		review.setRating(reviewRequest.getRating());
		review.setContent(reviewRequest.getContent());
		System.out.println("review: " + review.toString());
		ApiResponse<ReviewResponse> response = reviewService.createReview(review);
		if (response != null && response.getStatus() == 200) {
			redirectAttributes.addAttribute("success", response.getMessage());
			return "redirect:/products/glasses/" + productId;
		}else {
			redirectAttributes.addAttribute("error", "Tạo review thất bại!");
			return "redirect:/products/glasses/" + productId;
		}	
	}
}
