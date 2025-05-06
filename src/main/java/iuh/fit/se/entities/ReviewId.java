package iuh.fit.se.entities;

public class ReviewId {
	private Long userId;
	
	private Long productId;

	public ReviewId(Long userId, Long productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}

	public ReviewId() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "ReviewId [userId=" + userId + ", productId=" + productId + "]";
	}
	
	
}
