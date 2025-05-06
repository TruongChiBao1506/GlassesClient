document.querySelectorAll('.color-option').forEach(option => {
	option.addEventListener('click', function() {
		// Xóa selected class từ tất cả các màu
		document.querySelectorAll('.color-option').forEach(opt => {
			opt.classList.remove('selected');
		});

		// Thêm selected class cho màu được chọn
		this.classList.add('selected');

		// Lấy đường dẫn hình ảnh từ data attributes
		const frontImage = this.getAttribute('data-image-front');
		const sideImage = this.getAttribute('data-image-side');

		// Cập nhật src cho cả 2 hình
		document.getElementById('selectedImageFront').src = frontImage;
		document.getElementById('selectedImageSide').src = sideImage;
	});
});

// Thiết lập màu đầu tiên là selected khi tải trang
document.querySelector('.color-option').classList.add('selected');


const addToCartButton = document.getElementById("addToCart");
const productDetails = document.getElementById("pro_detail");

//addToCartButton.addEventListener("click", () => {
	
//	const product = {
//		id: productDetails.getAttribute("data-id"),
//		name: productDetails.getAttribute("data-name"),
//		price: productDetails.getAttribute("data-price"),
//		image: productDetails.getAttribute("data-image"),
//		colorCode: productDetails.getAttribute("data-colorCode"),
//		colorName: productDetails.getAttribute("data-colorName"),
//	};
//	console.log(product);
//	addToCart(product);
//	let sl = 0;
//	    var cart = JSON.parse(localStorage.getItem('cart'));
//	    if (cart != null) {
//	        sl = cart.length;
//	    }
//	document.querySelector(".soluong_cart").innerHTML = sl;
//	showMessage();
//});


function addToCart(product) {
	let cart = JSON.parse(localStorage.getItem("cart")) || [];

	const existingProductIndex = cart.findIndex((item) => item.id === product.id);

	if (existingProductIndex !== -1) {
		cart[existingProductIndex].quantity += 1;
	} else {
		cart.push({ ...product, quantity: 1 });
	}

	localStorage.setItem("cart", JSON.stringify(cart));
}

function showMessage() {
	Swal.fire(
		'Thêm vào giỏ hàng thành công',
		'Nhấn OK để tắt thông báo',
		'success'
	)
}

// Review section functionality
document.addEventListener('DOMContentLoaded', function() {
    // Interactive star rating for review form
    const stars = document.querySelectorAll('.star-rating .star');
    const ratingSelect = document.getElementById('rating');
    const ratingText = document.querySelector('.rating-text');
    
    if (stars.length > 0 && ratingSelect) {
        // Initialize rating text descriptions
        const ratingDescriptions = {
            '': 'Chọn đánh giá',
            '1': 'Tệ',
            '2': 'Kém',
            '3': 'Trung bình',
            '4': 'Tốt',
            '5': 'Tuyệt vời'
        };
        
        // Handle star hover effects
        stars.forEach(star => {
            // Hover effect
            star.addEventListener('mouseover', function() {
                const rating = this.getAttribute('data-rating');
                highlightStars(rating);
                ratingText.textContent = ratingDescriptions[rating];
            });
            
            // Click to select rating
            star.addEventListener('click', function() {
                const rating = this.getAttribute('data-rating');
                ratingSelect.value = rating;
                highlightStars(rating, true);
                ratingText.textContent = ratingDescriptions[rating];
            });
        });
        
        // Reset stars when mouse leaves the container
        document.querySelector('.star-rating').addEventListener('mouseleave', function() {
            const selectedRating = ratingSelect.value;
            highlightStars(selectedRating, true);
            ratingText.textContent = ratingDescriptions[selectedRating || ''];
        });
        
        // Function to highlight stars up to a certain rating
        function highlightStars(rating, permanent = false) {
            stars.forEach(star => {
                const starRating = star.getAttribute('data-rating');
                const starIcon = star.querySelector('i');
                
                if (starRating <= rating) {
                    starIcon.className = 'bi bi-star-fill';
                    if (permanent) star.classList.add('selected');
                } else {
                    starIcon.className = 'bi bi-star';
                    if (permanent) star.classList.remove('selected');
                }
            });
        }
    }
    
    // "Show more" reviews functionality
    const showMoreBtn = document.getElementById('show-more-reviews');
    if (showMoreBtn) {
        showMoreBtn.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            const currentPage = parseInt(this.getAttribute('data-page')) + 1;
            
            fetch(`/products/glasses/${productId}/reviews?page=${currentPage}`)
                .then(response => response.json())
                .then(data => {
                    // Add new reviews to the list
                    const reviewsList = document.querySelector('.reviews-list');
                    
                    data.reviews.forEach(review => {
                        const reviewHtml = createReviewElement(review);
                        reviewsList.insertAdjacentHTML('beforeend', reviewHtml);
                    });
                    
                    // Update page number for next load
                    this.setAttribute('data-page', currentPage);
                    
                    // Hide button if no more reviews
                    if (!data.hasMore) {
                        this.style.display = 'none';
                    }
                })
                .catch(error => {
                    console.error('Error fetching more reviews:', error);
                });
        });
    }
    
    // Helper function to create a review element
    function createReviewElement(review) {
        const starIcons = [];
        for (let i = 1; i <= 5; i++) {
            starIcons.push(`<i class="${i <= review.rating ? 'bi bi-star-fill' : 'bi bi-star'}" style="font-size: 0.9rem;"></i>`);
        }
        
        return `
            <div class="review-container">
                <div class="card my-3 p-4 shadow-sm">
                    <div class="d-flex align-items-center mb-2">
                        <div class="mr-3">
                            <div class="user-avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" 
                                style="width: 50px; height: 50px; font-size: 20px;">
                                <span>${review.username.substring(0, 1).toUpperCase()}</span>
                            </div>
                        </div>
                        
                        <div>
                            <strong class="text-primary d-block" style="font-size: 1.1rem;">${review.username}</strong>
                            <div class="d-flex align-items-center mt-1">
                                <div class="stars" style="color: #ffc107;">
                                    ${starIcons.join('')}
                                </div>
                                <span class="ml-2 text-muted">${review.createdAt}</span>
                                <i class="bi bi-patch-check-fill text-success ml-2" title="Verified Purchase"></i>
                            </div>
                        </div>
                    </div>
                    
                    <div class="mt-3" style="font-size: 1rem; line-height: 1.6;">${review.content}</div>
                </div>
            </div>
        `;
    }
});