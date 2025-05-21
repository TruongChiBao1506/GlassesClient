/**
 * File: header.js
 * Xử lý chức năng của header, bao gồm các dropdown thông báo và tài khoản
 */

document.addEventListener('DOMContentLoaded', () => {
    // Khởi tạo UI elements
    const notificationBtn = document.getElementById('notification-btn');
    const notificationPopup = document.getElementById('notification-popup');
    const accountBtn = document.getElementById('account-btn');
    const accountPopup = document.getElementById('account-popup');
    const notificationDot = document.querySelector('.notification-dot');
    
    // Xử lý việc mở/đóng dropdown Thông báo
    if (notificationBtn && notificationPopup) {
        notificationBtn.addEventListener('click', (e) => {
            e.stopPropagation(); // Ngăn chặn sự kiện lan ra document
            
            // Nếu đang hiển thị account popup thì đóng nó
            if (accountPopup && accountPopup.style.display === 'block') {
                accountPopup.style.display = 'none';
            }
            
            // Hiển thị hoặc ẩn popup thông báo với hiệu ứng
            if (notificationPopup.style.display === 'none' || notificationPopup.style.display === '') {
                notificationPopup.style.display = 'block';
                if (notificationDot) notificationDot.classList.remove('active');
            } else {
                notificationPopup.style.display = 'none';
            }
        });
        
        // Cho phép click bên trong popup mà không đóng nó
        notificationPopup.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    }
    
    // Xử lý việc mở/đóng dropdown Account
    if (accountBtn && accountPopup) {
        accountBtn.addEventListener('click', (e) => {
            e.stopPropagation(); // Ngăn chặn sự kiện lan ra document
            
            // Nếu đang hiển thị notification popup thì đóng nó
            if (notificationPopup && notificationPopup.style.display === 'block') {
                notificationPopup.style.display = 'none';
            }
            
            // Hiển thị hoặc ẩn popup tài khoản với hiệu ứng
            if (accountPopup.style.display === 'none' || accountPopup.style.display === '') {
                accountPopup.style.display = 'block';
            } else {
                accountPopup.style.display = 'none';
            }
        });
        
        // Cho phép click bên trong popup mà không đóng nó
        accountPopup.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    }
    
    // Ẩn popup khi click bên ngoài
    document.addEventListener('click', () => {
        if (notificationPopup) notificationPopup.style.display = 'none';
        if (accountPopup) accountPopup.style.display = 'none';
    });
    
    // Hàm mở/đóng sidebar (mobile responsive)
    window.openSidebar = function() {
        document.getElementById("sidebar").classList.add("sidebar-responsive");
    }
    
    window.closeSidebar = function() {
        document.getElementById("sidebar").classList.remove("sidebar-responsive");
    }
});

// Hàm đánh dấu thông báo đã đọc
async function markAsRead(notificationId) {
    const token = document.getElementById('token')?.value;
    if (!token) {
        console.error("No token found!");
        return;
    }
    
    const url = 'http://54.254.82.176:8080/api/notifications/'+notificationId+'/mark-as-read';
    
    try {
        const response = await fetch(url, {
            mode: 'cors',
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json' 
            }
        });
        
        if (response.ok) {
            // Thêm hiệu ứng fade-out trước khi xóa phần tử
            const element = document.getElementById(`notification-${notificationId}`);
            if (element) {
                element.classList.add('fade-out');
                
                // Đợi hiệu ứng hoàn thành
                setTimeout(() => {
                    element.remove();
                    
                    // Kiểm tra nếu không còn thông báo nào
                    const notificationList = document.querySelector('#notification-popup ul');
                    if (notificationList && notificationList.children.length === 0) {
                        const emptyMessage = document.createElement('div');
                        emptyMessage.textContent = 'Chưa có thông báo';
                        emptyMessage.style.color = '#999';
                        emptyMessage.style.textAlign = 'center';
                        emptyMessage.style.padding = '20px 0';
                        emptyMessage.style.fontStyle = 'italic';
                        
                        const notificationPopup = document.getElementById('notification-popup');
                        if (notificationPopup) {
                            // Xóa tin nhắn "không có thông báo" cũ nếu có
                            const oldEmptyMessage = notificationPopup.querySelector('div:not(ul):not(h3)');
                            if (oldEmptyMessage) oldEmptyMessage.remove();
                            
                            // Thêm tin nhắn mới
                            notificationPopup.appendChild(emptyMessage);
                        }
                    }
                }, 300);
            }
            
            // Cập nhật số lượng thông báo trên giao diện
            const notificationsCount = document.getElementById('notificationsCount');
            if (notificationsCount) {
                const currentCount = parseInt(notificationsCount.innerText);
                if (currentCount > 0) {
                    notificationsCount.innerText = currentCount - 1;
                }
            }
        } else {
            console.error("Failed to mark notification as read");
            // Hiển thị thông báo lỗi bằng SweetAlert2
            Swal.fire({
                title: 'Lỗi!',
                text: 'Không thể đánh dấu đã đọc thông báo',
                icon: 'error',
                confirmButtonText: 'Đóng',
                confirmButtonColor: '#d33'
            });
        }
    } catch (error) {
        console.error("Error:", error);
        // Hiển thị thông báo lỗi bằng SweetAlert2
        Swal.fire({
            title: 'Lỗi!',
            text: 'Có lỗi xảy ra khi xử lý yêu cầu',
            icon: 'error',
            confirmButtonText: 'Đóng',
            confirmButtonColor: '#d33'
        });
    }
}