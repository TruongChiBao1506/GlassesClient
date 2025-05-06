/**
 * Hệ thống thông báo sử dụng SweetAlert2
 * File này chứa các hàm tiện ích để hiển thị thông báo trong toàn bộ ứng dụng
 */

// Thông báo thành công
function showSuccessAlert(message, title = 'Thành công') {
    Swal.fire({
        icon: 'success',
        title: title,
        text: message,
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false,
        position: 'top-end',
        toast: true,
        customClass: {
            popup: 'colored-toast'
        }
    });
}

// Thông báo lỗi
function showErrorAlert(message, title = 'Lỗi') {
    Swal.fire({
        icon: 'error',
        title: title,
        text: message,
        confirmButtonColor: '#3085d6'
    });
}

// Thông báo cảnh báo
function showWarningAlert(message, title = 'Cảnh báo') {
    Swal.fire({
        icon: 'warning',
        title: title,
        text: message,
        confirmButtonColor: '#3085d6'
    });
}

// Thông báo xác nhận với hai lựa chọn
function showConfirmAlert(message, confirmCallback, title = 'Xác nhận', confirmText = 'Đồng ý', cancelText = 'Hủy') {
    Swal.fire({
        title: title,
        text: message,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: confirmText,
        cancelButtonText: cancelText
    }).then((result) => {
        if (result.isConfirmed && confirmCallback) {
            confirmCallback();
        }
    });
}

// Thông báo cập nhật thông tin cá nhân thành công
function showProfileUpdateSuccess(message = 'Thông tin cá nhân đã được cập nhật thành công!') {
    // Chỉ hiển thị một thông báo duy nhất với nội dung tổng hợp
    Swal.fire({
        icon: 'success',
        title: 'Cập nhật thành công!',
        text: message,
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false,
        position: 'top-end',
        toast: true,
        customClass: {
            popup: 'colored-toast'
        }
    });
}

// Hiển thị thông báo từ URL parameters (sử dụng trong redirect sau khi xử lý form)
function displayNotificationsFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const successMessage = urlParams.get('success');
    const errorMessage = urlParams.get('error');
    const warningMessage = urlParams.get('warning');

    if (successMessage) {
        showSuccessAlert(decodeURIComponent(successMessage));
    }
    if (errorMessage) {
        showErrorAlert(decodeURIComponent(errorMessage));
    }
    if (warningMessage) {
        showWarningAlert(decodeURIComponent(warningMessage));
    }
}