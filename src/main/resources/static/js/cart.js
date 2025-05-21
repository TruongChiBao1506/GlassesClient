// Hàm định dạng tiền tệ VND
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { maximumFractionDigits: 0 }).format(amount) + 'đ';
}

// Hàm chuyển chuỗi tiền tệ thành số
function parsePrice(priceString) {
    // Loại bỏ tất cả ký tự không phải số
    return parseInt(priceString.replace(/[^\d]/g, ''));
}

// Hàm tăng số lượng sản phẩm
function tang(button, index) {
    // Lấy input số lượng - Fixed selector path for new HTML structure
    const quantityInput = button.closest('.quantity-wrapper').querySelector('.ammount-input');
    let quantity = parseInt(quantityInput.value);
    
    // Tăng số lượng
    quantity++;
    quantityInput.value = quantity;
    
    // Lấy giá sản phẩm - Fixed selector path for new HTML structure
    const priceElement = button.closest('.details').querySelector('.unit-price .price');
    const unitPrice = parsePrice(priceElement.textContent);
    
    // Cập nhật thành tiền - Fixed selector path for new HTML structure
    const totalElement = button.closest('.details').querySelector('.subtotal .total');
    const newTotal = unitPrice * quantity;
    totalElement.textContent = formatCurrency(newTotal);
    
    // Cập nhật tổng tiền
    tinhTongTien();

    // Lưu thay đổi vào server (nếu cần)
    saveTotalToServer();
    
    // Log để debug
    console.log('Tang so luong, new quantity:', quantity);
}

// Hàm giảm số lượng sản phẩm
function giam(button, index) {
    // Lấy input số lượng - Fixed selector path for new HTML structure
    const quantityInput = button.closest('.quantity-wrapper').querySelector('.ammount-input');
    let quantity = parseInt(quantityInput.value);
    
    // Giảm số lượng nếu > 1
    if (quantity > 1) {
        quantity--;
        quantityInput.value = quantity;
        
        // Lấy giá sản phẩm - Fixed selector path for new HTML structure
        const priceElement = button.closest('.details').querySelector('.unit-price .price');
        const unitPrice = parsePrice(priceElement.textContent);
        
        // Cập nhật thành tiền - Fixed selector path for new HTML structure
        const totalElement = button.closest('.details').querySelector('.subtotal .total');
        const newTotal = unitPrice * quantity;
        totalElement.textContent = formatCurrency(newTotal);
        
        // Cập nhật tổng tiền
        tinhTongTien();

        // Lưu thay đổi vào server (nếu cần)
        saveTotalToServer();
        
        // Log để debug
        console.log('Giam so luong, new quantity:', quantity);
    }
}

// Hàm tính tổng tiền
function tinhTongTien() {
    // Updated selector to match new HTML structure
    const totalElements = document.querySelectorAll('.cart__product .subtotal .total');
    let sum = 0;
    
    // Tính tổng từ các phần tử .total
    totalElements.forEach(element => {
        sum += parsePrice(element.textContent);
    });
    
    // Cập nhật hiển thị tổng tiền
    const orderTotal = document.querySelector('.order__total');
    if(orderTotal) {
        orderTotal.textContent = formatCurrency(sum);
    }
    
    // Cập nhật giá tạm tính
    const orderSubtotal = document.querySelector('.order__subtotal');
    if(orderSubtotal) {
        orderSubtotal.textContent = formatCurrency(sum);
    }
    
    // Cập nhật số lượng sản phẩm
    countItems();
    
    console.log('Tổng tiền updated:', sum);
}

// Hàm đếm số sản phẩm trong giỏ hàng
function countItems() {
    const cartItems = document.querySelectorAll('.cart__product');
    const itemCount = cartItems.length; // Số lượng loại sản phẩm
    
    // Cập nhật số lượng sản phẩm hiển thị
    const countElement = document.querySelector('.ammount');
    if(countElement) {
        countElement.textContent = itemCount;
    }
    
    // Cập nhật số lượng loại sản phẩm trên icon giỏ hàng
    const cartBadge = document.querySelector('.soluong_cart');
    if(cartBadge) {
        cartBadge.textContent = itemCount > 0 ? itemCount : '';
    }
    
    console.log('Số loại sản phẩm trong giỏ hàng:', itemCount);
}

// Hàm xóa sản phẩm khỏi giỏ hàng
function xoa(button, index) {
    // Hiển thị hộp thoại xác nhận đẹp mắt với SweetAlert2
    Swal.fire({
        title: 'Xác nhận xóa sản phẩm',
        text: 'Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa sản phẩm',
        cancelButtonText: 'Hủy bỏ',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // Lấy productId từ phần tử sản phẩm
            const productElement = button.closest('.cart__product');
            const productId = getProductIdFromElement(productElement);
            
            // Gửi request để xóa sản phẩm khỏi giỏ hàng
            removeFromServer(productId, function() {
                // Hiển thị thông báo thành công nhỏ gọn hơn
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 1500,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                });
                
                Toast.fire({
                    icon: 'success',
                    title: 'Đã xóa sản phẩm'
                });
                
                // Sau khi xóa thành công, xóa phần tử khỏi DOM
                productElement.remove();
                
                // Tính lại tổng tiền và số lượng
                tinhTongTien();
                
                // Kiểm tra nếu giỏ hàng trống
                checkEmptyCart();
            });
        }
    });
}

// Hàm lấy productId từ phần tử sản phẩm
function getProductIdFromElement(element) {
    // Thử tìm productId từ thuộc tính data
    const detailsElement = element.querySelector('.details');
    if (detailsElement && detailsElement.dataset.productId) {
        return detailsElement.dataset.productId;
    }
    
    // Nếu không có data attribute, thử lấy từ URL của ảnh (phương pháp dự phòng)
    const img = element.querySelector('img');
    if (img) {
        const imgSrc = img.getAttribute('src');
        const match = imgSrc.match(/\/([^\/]+)$/);
        if (match) {
            // Lấy tên file và bỏ phần mở rộng
            const filename = match[1];
            const productId = filename.split('.')[0];
            if (!isNaN(productId)) {
                return productId;
            }
        }
    }
    
    // Trường hợp không tìm thấy, thử lấy từ index
    return index;
}

// Hàm gửi request xóa sản phẩm đến server
function removeFromServer(productId, callback) {
    // Tạo form data để gửi POST request
    const formData = new FormData();
    formData.append('productId', productId);
    
    // Gửi POST request đến server
    fetch('/cart/remove', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            console.log("Đã xóa sản phẩm khỏi giỏ hàng");
            if (callback) callback();
        } else {
            console.error("Lỗi khi xóa sản phẩm khỏi giỏ hàng");
            // Sử dụng SweetAlert2 thay vì alert
            Swal.fire({
                icon: 'error',
                title: 'Không thể xóa sản phẩm',
                text: 'Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng. Vui lòng thử lại sau.',
                confirmButtonColor: '#3085d6'
            });
        }
    })
    .catch(error => {
        console.error("Lỗi khi gửi request:", error);
        // Sử dụng SweetAlert2 thay vì alert
        Swal.fire({
            icon: 'error',
            title: 'Đã xảy ra lỗi',
            text: 'Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối và thử lại sau.',
            confirmButtonColor: '#3085d6'
        });
    });
}

// Hàm kiểm tra giỏ hàng trống
function checkEmptyCart() {
    const cartItems = document.querySelectorAll('.cart__product');
    
    if (cartItems.length === 0) {
        // Hiển thị thông báo giỏ hàng trống
        const emptyCartHTML = `
            <div class="empty-cart-message text-center p-4">
                <i class="fa fa-shopping-cart fa-3x mb-3 text-muted"></i>
                <p class="lead">Giỏ hàng của bạn đang trống</p>
                <a href="/products/eyeglasses/men" class="btn btn-primary mt-2">Tiếp tục mua sắm</a>
            </div>
        `;
        
        document.querySelector(".cartlist").innerHTML = emptyCartHTML;
        document.querySelector(".order__total").textContent = '0đ';
    }
}

// Hàm lưu thay đổi số lượng vào server
function saveTotalToServer() {
    // Lấy tất cả các sản phẩm trong giỏ hàng
    const cartProducts = document.querySelectorAll('.cart__product');
    
    cartProducts.forEach(product => {
        // Lấy productId từ thuộc tính data
        const productId = getProductIdFromElement(product);
		
        // Lấy số lượng hiện tại
        const quantity = parseInt(product.querySelector('.ammount-input').value);
        
        // Tạo form data để gửi POST request
        const formData = new FormData();
        formData.append('productId', productId);
		
        formData.append('quantity', quantity);
        
        // Gửi POST request đến endpoint cart/add
        fetch('/cart/update', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                console.log(`Đã cập nhật số lượng sản phẩm ID: ${productId}`);
            } else {
                console.error(`Lỗi khi cập nhật số lượng sản phẩm ID: ${productId}`);
            }
        })
        .catch(error => {
            console.error("Lỗi khi gửi request:", error);
        });
    });
}

// Hàm thanh toán
async function thanhtoan() {
    var userId = document.getElementById("id_user").value;
    var address = document.getElementById("address").value;
    
    // Tạo danh sách các sản phẩm trong giỏ hàng
    let orderItems = [];
    
    // Lấy tất cả các sản phẩm trong giỏ hàng
    const cartProducts = document.querySelectorAll('.cart__product');
    
    // Nếu có sản phẩm trong giỏ
    if (cartProducts.length > 0) {
        cartProducts.forEach(product => {
            // Updated selectors to match new HTML structure
            const name = product.querySelector('.product-title').textContent;
            const quantity = parseInt(product.querySelector('.ammount-input').value);
            const priceText = product.querySelector('.unit-price .price').textContent;
            const colorBox = product.querySelector('.color-box');
            const colorName = product.querySelector('.color-name').textContent;
            
            // Lấy mã màu từ style của color-box
            const colorStyle = colorBox.getAttribute('style');
            const colorCodeMatch = colorStyle.match(/background-color:\s*([^;]+)/);
            const colorCode = colorCodeMatch ? colorCodeMatch[1] : '#000000';
            
            // Lấy ID sản phẩm từ data attribute
            const detailsElement = product.querySelector('.details');
            const productId = detailsElement.dataset.productId;
            
            // Chuyển đổi giá từ định dạng "9.800.000đ" thành số
            const price = parsePrice(priceText);
            
            orderItems.push({
                productId: parseInt(productId),
                quantity: quantity,
                price: price,
                colorName: colorName,
                colorCode: colorCode
            });
        });
        
        const paymentMethod = document.querySelector('input[name="payment"]:checked').value;
        const token = document.getElementById("token").value;
        const orderRequest = {
            userId: userId,
            shippingAddress: address,
            paymentMethod: paymentMethod,
            orderDate: new Date(),
            status: "PENDING",
            orderItems: orderItems
        };
        
        console.log(orderRequest);
        console.log(token)
        // Gửi dữ liệu lên server
        try {
            const response = await fetch("http://54.254.82.176:8080/api/orders/checkout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
					'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(orderRequest),
            });
            
            if (response.ok) {
                const data = await response.json();
                if (data.paymenUrl) {
					clearCart();
                    showMessageSuccess();
					setTimeout(() => {
					        window.location.href = data.paymenUrl;
					    }, 1500);
                } else {
                    clearCart();
                    showMessageSuccess();
                }
            } else {
                const error = await response.text();
                alert("Lỗi: " + error);
            }
        } catch (error) {
            console.error("Có lỗi xảy ra:", error);
        }
    } else {
        showMessageError();
    }
}

// Xóa toàn bộ giỏ hàng
function clearCart() {
    document.querySelector(".cartlist").innerHTML = '';
    document.querySelector(".ammount").textContent = '0';
    document.querySelector(".order__total").textContent = '0đ';
    document.querySelector(".soluong_cart").textContent = '';
    
    checkEmptyCart();
}

function showMessageError() {
    Swal.fire(
        'Vui lòng chọn sản phẩm để thanh toán',
        'Nhấn OK để tắt thông báo',
        'error'
    );
}

function showMessageSuccess() {
    Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: 'Thanh toán thành công',
        showConfirmButton: true,
        timer: 3500
    });
}

// Khởi tạo khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    // Tính tổng tiền ban đầu
    tinhTongTien();
    
    // Kiểm tra giỏ hàng trống
    checkEmptyCart();
});
