/**
 * Cầu nối giữa trang danh sách sản phẩm và ứng dụng thử kính
 * Xử lý việc truyền dữ liệu kính giữa các trang và cải thiện trải nghiệm người dùng
 */
(function() {
    // Debug: Kiểm tra dữ liệu kính từ localStorage
    const storedGlassData = localStorage.getItem('glassData');
    if (storedGlassData) {
        console.log("app-bridge: Dữ liệu kính từ localStorage:", storedGlassData);
        
        try {
            const glassData = JSON.parse(storedGlassData);
            
            // Đảm bảo hình ảnh được tải trước khi sử dụng
            if (glassData && glassData.imageUrl) {
                const img = new Image();
                img.crossOrigin = "Anonymous";
                img.src = glassData.imageUrl;
                
                img.onload = function() {
                    console.log("app-bridge: Đã tải hình ảnh kính thành công:", glassData.imageUrl);
                };
                
                img.onerror = function() {
                    console.error("app-bridge: Không thể tải hình ảnh kính:", glassData.imageUrl);
                    
                    // Thay đổi URL hình ảnh nếu tải thất bại
                    if (glassData.imageUrl.startsWith('/')) {
                        // Thử thêm context path nếu cần
                        const contextPath = window.location.pathname.split('/')[1];
                        if (contextPath) {
                            glassData.imageUrl = '/' + contextPath + glassData.imageUrl;
                            console.log("app-bridge: Thử lại với URL:", glassData.imageUrl);
                            localStorage.setItem('glassData', JSON.stringify(glassData));
                        }
                    }
                };
            }
            
        } catch (e) {
            console.error("app-bridge: Lỗi khi phân tích dữ liệu kính:", e);
        }
    }
    
    // Phục vụ cả trang thử kính và trang danh sách sản phẩm
    document.addEventListener("DOMContentLoaded", function() {
        // 1. Xử lý cho trang danh sách sản phẩm: Các nút "Thử Ngay"
        const productTryOnButtons = document.querySelectorAll('.action-btn.try-on, .try-now-container .btn');
        if (productTryOnButtons.length > 0) {
            console.log("app-bridge: Trang danh sách sản phẩm - Thiết lập sự kiện cho", productTryOnButtons.length, "nút Thử Ngay");
            
            productTryOnButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    // Ngăn hành vi mặc định của thẻ <a>
                    e.preventDefault();
                    
                    // Lấy thông tin kính từ thẻ cha
                    const productCard = this.closest('.product-card');
                    if (!productCard) {
                        console.error("app-bridge: Không tìm thấy product-card");
                        
                        // Nếu không tìm thấy card, mở link trong tab mới và thoát
                        window.open(this.getAttribute('href'), '_blank');
                        return;
                    }
                    
                    try {
                        // Lấy thông tin sản phẩm kính
                        const href = this.getAttribute('href');
                        const productId = href.split('glassesId=')[1]; // Lấy ID từ URL
                        const productName = productCard.querySelector('.product-name a').innerText;
                        const productPrice = parseFloat(productCard.querySelector('.current-price').innerText.replace(/[^\d]/g, ''));
                        const productImgUrl = productCard.querySelector('.image-container .front-image').getAttribute('src');
                        const productBrand = productCard.querySelector('.product-brand').innerText;
                        
                        // Tạo đối tượng dữ liệu kính
                        const glassData = {
                            id: productId,
                            name: productName,
                            imageUrl: productImgUrl,
                            description: `${productBrand} - Mẫu kính thời trang`,
                            price: productPrice
                        };
                        
                        // Lưu dữ liệu vào localStorage để sử dụng trong trang thử kính
                        localStorage.setItem('glassData', JSON.stringify(glassData));
                        
                        console.log("app-bridge: Đã lưu dữ liệu kính vào localStorage:", glassData);
                        
                        // Luôn mở trong tab mới
                        window.open(href, '_blank');
                    } catch (error) {
                        console.error("app-bridge: Lỗi khi xử lý nút Thử Ngay:", error);
                        
                        // Nếu có lỗi, vẫn mở trong tab mới
                        window.open(this.getAttribute('href'), '_blank');
                    }
                });
            });
        }
        
        // 2. Xử lý cho trang thử kính
        const tryGlassesButtons = document.querySelectorAll('.try-glasses');
        if (tryGlassesButtons.length > 0) {
            console.log("app-bridge: Trang thử kính - Thiết lập sự kiện cho", tryGlassesButtons.length, "nút thử kính");
            
            tryGlassesButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const glassesId = this.getAttribute('data-glasses-id');
                    
                    if (window.selectGlassesById && typeof window.selectGlassesById === 'function') {
                        // Nếu hàm đã được khai báo trong app.js
                        window.selectGlassesById(glassesId);
                        
                        // Cuộn đến phần thử kính
                        const tryOnSection = document.getElementById('try-on-section');
                        if (tryOnSection) {
                            tryOnSection.scrollIntoView({ behavior: 'smooth' });
                        }
                    } else {
                        console.error('app-bridge: Hàm selectGlassesById không được tìm thấy. Đảm bảo app.js đã được tải.');
                    }
                });
            });
        }
        
        // 3. Kiểm tra URL parameter và gọi chọn kính tự động nếu cần
        const urlParams = new URLSearchParams(window.location.search);
        const glassesIdFromUrl = urlParams.get('glassesId');
        
        if (glassesIdFromUrl && window.currentGlass === null) {
            console.log("app-bridge: Đã tìm thấy tham số glassesId trong URL:", glassesIdFromUrl);
            
            // Đặt hẹn giờ để đảm bảo app.js đã được tải và khởi tạo
            setTimeout(() => {
                if (window.selectGlassesById) {
                    console.log("app-bridge: Tự động chọn kính với ID:", glassesIdFromUrl);
                    window.selectGlassesById(glassesIdFromUrl);
                }
            }, 1000);
        }
        
        // 4. Xử lý nút quay lại trang sản phẩm
        const backButton = document.getElementById('back-to-products');
        if (backButton) {
            backButton.addEventListener('click', function() {
                window.history.back();
            });
        }
    });
    
    // Thêm phương thức công khai để kiểm tra trạng thái
    window.checkGlassesStatus = function() {
        console.log("app-bridge: Trạng thái hiện tại:");
        console.log("ID kính hiện tại:", window.selectedGlassesId || "Chưa chọn kính");
        if (window.glassesData) {
            console.log("Danh sách kính:", window.glassesData.map(g => g.id));
        }
        return {
            selectedId: window.selectedGlassesId,
            glasses: window.glassesData || []
        };
    };
})();