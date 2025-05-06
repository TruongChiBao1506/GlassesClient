/**
 * File: notification-websocket.js
 * Xử lý kết nối WebSocket để nhận thông báo thời gian thực
 */

document.addEventListener('DOMContentLoaded', () => {
    const notificationDot = document.querySelector('.notification-dot');
    const token = document.getElementById('token')?.value;
    
    if (!token) {
        console.warn("Token not found, WebSocket connection might not work");
    }
    
    // Kết nối đến WebSocket
    const socket = new SockJS('http://localhost:8084/ws'); 
    const stompClient = Stomp.over(socket);
    
    // Tùy chọn kết nối, có thể thêm token auth nếu cần
    const connectionOptions = {
        // Authorization header có thể được thêm nếu cần xác thực WebSocket
        // 'Authorization': `Bearer ${token}`
    };
    
    stompClient.connect(connectionOptions, function () {
        console.log("WebSocket connected successfully!");
        
        // Lắng nghe thông báo thời gian thực từ server
        stompClient.subscribe('/topic/orders', function (message) {
            try {
                const notification = JSON.parse(message.body);
                
                // Cập nhật số lượng thông báo trên giao diện nếu có
                const notificationsCount = document.getElementById('notificationsCount');
                if (notificationsCount) {
                    notificationsCount.innerText = parseInt(notificationsCount.innerText) + 1;
                }
                
                // Hiển thị thông báo mới và đặt trạng thái dot
                displayNotification(notification);
                if (notificationDot) notificationDot.classList.add('active');
                
                // Hiển thị thông báo toast khi có thông báo mới
                if (window.Swal) {
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'info',
                        title: 'Thông báo mới!',
                        text: notification.message,
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true
                    });
                }
            } catch (error) {
                console.error("Error parsing notification:", error);
            }
        });
    }, function(error) {
        // Callback khi có lỗi kết nối
        console.error("Error connecting to WebSocket:", error);
    });
});

/**
 * Hiển thị thông báo mới trong danh sách thông báo
 */
function displayNotification(notification) {
    // Lấy danh sách thông báo (phần tử <ul>)
    const notificationList = document.querySelector('#notification-popup ul');
    
    // Kiểm tra nếu phần tử không tồn tại
    if (!notificationList) {
        console.error('Notification list element not found!');
        return;
    }
    
    // Tạo phần tử <li> cho thông báo mới
    const notificationItem = document.createElement('li');
    
    // Lấy thời gian hiện tại để hiển thị
    const now = new Date();
    const formattedTime = now.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
    const formattedDate = now.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
    
    // Xác định loại thông báo và biểu tượng tương ứng
    let notificationType = 'info';
    let notificationIcon = 'info';
    
    // Phân loại thông báo dựa trên nội dung
    if (notification.message.toLowerCase().includes('đơn hàng') || notification.message.toLowerCase().includes('order')) {
        notificationType = 'order';
        notificationIcon = 'shopping_bag';
    } else if (notification.message.toLowerCase().includes('người dùng') || notification.message.toLowerCase().includes('user')) {
        notificationType = 'user';
        notificationIcon = 'person';
    } else if (notification.message.toLowerCase().includes('sản phẩm') || notification.message.toLowerCase().includes('product')) {
        notificationType = 'product';
        notificationIcon = 'inventory_2';
    }
    
    // Tạo HTML cho thông báo mới với thiết kế cải tiến
    notificationItem.innerHTML = `
        <div class="notification-item notification-${notificationType}" id="notification-${notification.id}">
            <div class="notification-icon">
                <span class="material-icons-outlined">${notificationIcon}</span>
            </div>
            <div class="notification-content">
                <div class="notification-header">
                    <span class="notification-title">Thông báo mới</span>
                    <span class="notification-time" title="${formattedDate}">${formattedTime}</span>
                </div>
                <p class="notification-message">${notification.message}</p>
                <div class="notification-actions">
                    <button class="mark-as-read-btn" onclick="markAsRead(${notification.id})">
                        <span class="material-icons-outlined" style="font-size: 14px; margin-right: 4px;">check_circle</span>
                        Đánh dấu đã đọc
                    </button>
                </div>
            </div>
        </div>
    `;
    
    // Xóa thông báo "không có thông báo" nếu có
    const emptyMessage = document.querySelector('#notification-popup > div:not(ul):not(h3)');
    if (emptyMessage) {
        emptyMessage.remove();
    }
    
    // Thêm thông báo mới vào đầu danh sách
    notificationList.prepend(notificationItem);
    
    // Thêm hiệu ứng cho thông báo mới
    setTimeout(() => {
        const newNotification = notificationItem.querySelector('.notification-item');
        if (newNotification) {
            newNotification.classList.add('show');
        }
    }, 10);
}

/**
 * Đánh dấu thông báo đã đọc
 */
async function markAsRead(notificationId) {
    const token = document.getElementById('token').value;
    const url = 'http://localhost:8080/api/notifications/' + notificationId + '/mark-as-read';
    console.log(token);
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
            const element = document.getElementById(`notification-${notificationId}`);
            if (element) element.remove();
        } else {
            console.error("Failed to mark notification as read");
            alert("Không thể đánh dấu đã đọc thông báo");
        }
    } catch (error) {
        console.error("Error:", error)
        alert("Có lỗi xảy ra");
    }
}