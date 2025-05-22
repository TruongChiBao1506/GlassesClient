function loadData(year, month, page = 0, size = 10) {
	const token = document.getElementById('token').value;
	const refreshToken = document.getElementById('refreshToken').value;
	
	// Display loading indicator
	Swal.fire({
		title: 'Đang tải dữ liệu',
		text: 'Vui lòng đợi trong giây lát...',
		icon: 'info',
		allowOutsideClick: false,
		didOpen: () => {
			Swal.showLoading();
		}
	});
	
	// Gọi API doanh thu
	const revenueApi = month
		? `http://52.77.218.250:8080/api/orders/revenue/daily?year=${year}&month=${month}`
		: `http://52.77.218.250:8080/api/orders/revenue/monthly?year=${year}`;

	// Gọi API trạng thái
	const statusApi = month
		? `http://52.77.218.250:8080/api/orders/status-percentage/monthly?year=${year}&month=${month}`
		: `http://52.77.218.250:8080/api/orders/status-percentage/yearly?year=${year}`;

	const apiUrl = month
		? `http://52.77.218.250:8080/api/orders/list?year=${year}&month=${month}&page=${page}&size=${size}`
		: `http://52.77.218.250:8080/api/orders/list?year=${year}&page=${page}&size=${size}`;

	// Trả về Promise để có thể theo dõi khi việc tải dữ liệu hoàn thành
	return new Promise((resolve, reject) => {
		// Đặt cookie 1 lần trước khi gọi API
		document.cookie = `refreshToken=${refreshToken}; path=/`;
		// Gọi API
		Promise.all([
			fetch(revenueApi, {
				method: "GET",
				headers: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			}),
			fetch(statusApi, {
				method: "GET",
				headers: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			}),
			fetch(apiUrl, {
				method: "GET",
				headers: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			}),
		])
			.then(responses => {
				// Kiểm tra xem tất cả các response có ok hay không
				for (const response of responses) {
					if (!response.ok) {
						throw new Error(`Lỗi API: ${response.status}`);
					}
				}
				return Promise.all(responses.map(response => response.json()));
			})
			.then(([revenueData, statusData, ordersData]) => {
				// Close loading indicator
				Swal.close();
				
				console.log("Orders Data:", ordersData);
				const isDaily = !!month;
				const categories = isDaily
					? revenueData.map(item => `Ngày ${item.day}`)
					: revenueData.map(item => `Tháng ${item.month}`);
				const revenues = revenueData.map(item => item.totalRevenue);

				// Xóa biểu đồ cũ trước khi render biểu đồ mới
				const chartContainer = document.querySelector("#area-chart");
				chartContainer.innerHTML = "";
				renderChart(categories, revenues, isDaily ? 'Doanh thu theo ngày' : 'Doanh thu theo tháng');

				const statusCategories = Object.keys(statusData);
				const statusValues = Object.values(statusData);

				// Xóa biểu đồ cũ trước khi render biểu đồ mới
				const pieChartContainer = document.querySelector("#pie-chart");
				pieChartContainer.innerHTML = "";
				renderPieChart(statusCategories, statusValues, isDaily ? 'Trạng thái đơn hàng theo ngày' : 'Trạng thái đơn hàng theo tháng');

				renderOrderList(ordersData);
				renderPagination(ordersData, year, month, page, size);
				
				// Show success toast
				Swal.fire({
					toast: true,
					position: 'top-end',
					icon: 'success',
					title: 'Đã cập nhật dữ liệu',
					showConfirmButton: false,
					timer: 1500
				});
				
				// Giải quyết promise khi tất cả dữ liệu đã được tải và hiển thị
				resolve(ordersData);
			})
			.catch(error => {
				// Close loading indicator and show error
				Swal.fire({
					title: 'Lỗi!',
					text: `Không thể tải dữ liệu: ${error.message}`,
					icon: 'error',
					confirmButtonText: 'Đóng'
				});
				
				console.error("Lỗi khi tải dữ liệu:", error);
				reject(error);
			});	});
}
const statusColors = {
	PENDING: '#FFA500',
	FAILED: '#dc3545',
	PAID: '#28a745',
	COMPLETED: '#006400',
	SHIPPED: '#00FF00',
	CANCELED: '#FF0000',
	PROCESSING: '#0000FF',
};
function renderPieChart(categories, data, title) {
	const colors = categories.map(status => statusColors[status]);
	const pieChartOptions = {
		chart: {
			type: "pie", // Định dạng biểu đồ pie chart
			height: 350,
		},
		series: data, // Dữ liệu số lượng từng trạng thái
		labels: categories, // Danh sách trạng thái
		colors: colors, // Màu sắc tương ứng với từng trạng thái
		legend: {
			position: "bottom",
			labels: {
				colors: "#ffffff", // Đổi màu chữ legend (vd: trắng)
				useSeriesColors: false, // Không sử dụng màu mặc định của series
			}, // Hiển thị chú thích ở dưới
		},
		dataLabels: {
			enabled: true, // Hiển thị phần trăm trên biểu đồ
			formatter: function(val, opts) {
				return val.toFixed(2) + "%";
			},
		},
		tooltip: {
			y: {
				formatter: function(val) {
					return val + " orders"; // Tooltip hiển thị số lượng
				},
			},
		},
		title: {
			text: title,
			align: 'center',
			style: {
				color: '#fff' // Màu trắng cho tiêu đề
			}
		}
	};
	const pieChartContainer = document.querySelector("#pie-chart");
	const chart = new ApexCharts(pieChartContainer, pieChartOptions);
	chart.render();

}

function renderChart(categories, revenues, title) {
	var options = {
		series: [{
			name: "Revenue",
			data: revenues, // Dữ liệu doanh thu theo từng ngày/tháng
		}],
		chart: {
			height: 350,
			type: 'area',
		},
		title: {
			text: title,
			align: 'center',
			style: {
				color: '#fff' // Màu trắng cho tiêu đề
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			curve: 'smooth'
		},
		xaxis: {
			categories: categories, // Các ngày/tháng
			labels: {
				style: {
					colors: '#fff', // Màu trắng cho nhãn trục X
					fontSize: '12px'
				}
			}
		},
		yaxis: {
			title: {
				text: 'Revenue (VNĐ)',
				style: {
					color: '#fff' // Màu trắng cho tiêu đề trục Y
				}
			},
			labels: {
				style: {
					colors: '#fff', // Màu trắng cho nhãn trục Y
					fontSize: '12px'
				},
				formatter: function(value) {
					// Làm gọn giá trị: loại bỏ phần thập phân thừa
					return value.toLocaleString('vi-VN', { maximumFractionDigits: 0 });
				}
			}
		},
		tooltip: {
			theme: 'dark',
			x: {
				format: 'dd/MM'
			},
			y: {
				formatter: function(value) {
					return value.toLocaleString('vi-VN') + "đ";
				}
			}
		},
		theme: {
			palette: 'palette5'
		}
	};
	const chartContainer = document.querySelector("#area-chart");
	//chartContainer.innerHTML = "";
	const chart = new ApexCharts(chartContainer, options);
	chart.render();
}
//function updateOrderList() {
//	const year = document.getElementById("year").value;
//	const month = document.getElementById("month").value;

//	const apiUrl = month
//		? `/api/orders/list?year=${year}&month=${month}`
//		: `/api/orders/list?year=${year}`;

//	fetch(apiUrl)
//		.then(response => response.json())
//		.then(data => {
//			renderOrderList(data);
//		})
//		.catch(error => console.error("Error fetching order list:", error));
//}

function renderOrderList(orders) {
	const tbody = document.getElementById("order-list-body");
	tbody.innerHTML = ""; // Clear existing rows
	const data = orders.data;
	data.forEach(order => {
		const row = document.createElement("tr");
		row.innerHTML = `
            <td>${order.id}</td>
            <td>${order.orderNumber}</td>
            <td>${order.user.fullname}</td>
            <td>${new Date(order.orderDate).toLocaleDateString()}</td>
			<td>${order.shippingAddress}</td>
			<td>${order.paymentMethod}</td>
			<td>${order.status}</td>
            <td>${order.totalAmount.toLocaleString('vi-VN')} đ</td>
        `;
		tbody.appendChild(row);
	});
}
function exportOrders(year, month) {
	const token = document.getElementById('token').value;
	const apiUrl = month
		? `http://52.77.218.250:8080/api/orders/orders-export?year=${year}&month=${month}`
		: `http://52.77.218.250:8080/api/orders/orders-export?year=${year}`;

	// Hiển thị thông báo đang tải với SweetAlert2
	Swal.fire({
		title: 'Đang xuất báo cáo',
		text: 'Vui lòng đợi trong giây lát...',
		icon: 'info',
		allowOutsideClick: false,
		didOpen: () => {
			Swal.showLoading();
		}
	});

	fetch(apiUrl, {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ${token}`,
			'Content-Type': 'application/json'
		},
	})
		.then(response => {
			if (response.ok) {
				if (response.headers.get('Content-Length') === '0' || response.status === 204) {
					throw new Error('Không có dữ liệu để xuất báo cáo.');
				}
				return response.blob(); // File Excel trả về dưới dạng Blob
			} else if (response.status === 404) {
				throw new Error('Không tìm thấy dữ liệu đơn hàng cho thời gian được chọn.');
			} else {
				throw new Error('Không thể xuất báo cáo. Vui lòng thử lại sau.');
			}
		})
		.then(blob => {
			// Kiểm tra kích thước blob - nếu quá nhỏ thì có thể là file trống
			if (blob.size < 100) {
				throw new Error('Không có dữ liệu để xuất báo cáo.');
			}
			
			// Tạo link tải file
			const downloadUrl = window.URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = downloadUrl;
			a.download = `BaoCao_DonHang_${year}_${month || 'tatca'}.xlsx`; // Tên file tải về
			document.body.appendChild(a);
			a.click();
			a.remove(); // Xóa phần tử sau khi click
			window.URL.revokeObjectURL(downloadUrl); // Giải phóng bộ nhớ
			
			// Hiển thị thông báo thành công
			Swal.fire({
				title: 'Thành công!',
				text: 'Xuất báo cáo thành công',
				icon: 'success',
				timer: 2000,
				timerProgressBar: true,
				showConfirmButton: false
			});
		})
		.catch(error => {
			console.error('Lỗi:', error);
			
			// Hiển thị thông báo lỗi
			Swal.fire({
				title: 'Lỗi!',
				text: error.message || 'Có lỗi xảy ra khi xuất báo cáo đơn hàng',
				icon: 'error',
				confirmButtonText: 'Đóng'
			});
		 });
}

// Hàm cài đặt tooltips cho các nút
function setupTooltips() {
    // Thêm tooltip cho nút Filter nếu có Bootstrap Tooltip
    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const filterBtn = document.querySelector('.btn-filter');
        if (filterBtn) {
            filterBtn.setAttribute('data-bs-toggle', 'tooltip');
            filterBtn.setAttribute('data-bs-placement', 'top');
            filterBtn.setAttribute('title', 'Lọc dữ liệu theo năm và tháng đã chọn');
            
            new bootstrap.Tooltip(filterBtn);
        }
        
        const exportBtn = document.querySelector('.btn-export');
        if (exportBtn) {
            exportBtn.setAttribute('data-bs-toggle', 'tooltip');
            exportBtn.setAttribute('data-bs-placement', 'top');
            exportBtn.setAttribute('title', 'Xuất báo cáo ra file Excel');
            
            new bootstrap.Tooltip(exportBtn);
        }
    }
}

// Cập nhật lại function handleExport để thêm validation chặt chẽ hơn
function handleExport() {
	const year = document.getElementById('year').value;
	const month = document.getElementById('month').value;

	if (!year) {
		Swal.fire({
			title: 'Cảnh báo!',
			text: 'Bạn cần chọn năm trước khi xuất báo cáo',
			icon: 'warning',
			confirmButtonText: 'Đóng'
		});
		return;
	}
	
	// Kiểm tra xem có dữ liệu trong bảng không
	const tableRows = document.querySelectorAll('#order-list-body tr');
	if (tableRows.length === 0) {
		Swal.fire({
			title: 'Thông báo!',
			text: 'Không có dữ liệu để xuất báo cáo trong thời gian đã chọn',
			icon: 'info',
			confirmButtonText: 'Đóng'
		});
		return;
	}

	exportOrders(year, month); // Gọi hàm JavaScript tải file
}

function updateChart() {
	const year = document.getElementById("year").value;
	const month = document.getElementById("month").value;
	const pageSize = document.getElementById("page-size")?.value || 10;
	
	if (!year) {
		Swal.fire({
			title: 'Lưu ý',
			text: 'Vui lòng chọn năm để lọc dữ liệu',
			icon: 'warning',
			confirmButtonText: 'Đóng'
		});
		return;
	}
	
	// Sử dụng hàm loadData đã cải tiến để tải dữ liệu
	loadData(year, month, 0, parseInt(pageSize))
		.then(data => {
			console.log("Dữ liệu đã được cập nhật thành công!");
		})
		.catch(error => {
			console.error("Lỗi khi cập nhật dữ liệu:", error);
		});
}
// Tự động tải biểu đồ mặc định khi trang load
document.addEventListener("DOMContentLoaded", () => {
	const defaultYear = new Date().getFullYear();
	const defaultMonth = null; // Ban đầu không chọn tháng
	
	// Hiển thị thông báo đang tải lần đầu
	Swal.fire({
		title: 'Đang tải dữ liệu',
		text: 'Vui lòng đợi trong giây lát...',
		icon: 'info',
		allowOutsideClick: false,
		didOpen: () => {
			Swal.showLoading();
		}
	});
	
	// Đảm bảo dropdown năm được đặt thành năm hiện tại
	const yearSelect = document.getElementById('year');
	if (yearSelect) {
		for (let i = 0; i < yearSelect.options.length; i++) {
			if (parseInt(yearSelect.options[i].value) === defaultYear) {
				yearSelect.selectedIndex = i;
				break;
			}
		}
	}
	
	loadData(defaultYear, defaultMonth)
		.then(() => {
			Swal.close();
		})
		.catch(error => {
			Swal.fire({
				title: 'Lỗi!',
				text: 'Không thể tải dữ liệu. Vui lòng thử lại sau.',
				icon: 'error',
				confirmButtonText: 'Đóng'
			});
		});
	
	// Thêm event listener cho phím Enter cho tìm kiếm nhanh
	document.addEventListener('keydown', function(event) {
		// Nếu người dùng đang focus vào dropdown và nhấn Enter
		if (event.key === 'Enter') {
			const activeElement = document.activeElement;
			if (activeElement && (activeElement.id === 'year' || activeElement.id === 'month')) {
				event.preventDefault();
				// Hiển thị thông báo tải
				Swal.fire({
					toast: true,
					position: 'top-end',
					title: 'Đang tải dữ liệu...',
					showConfirmButton: false,
					timer: 1000,
					timerProgressBar: true
				});
				updateChart();
			} else {
				// Nếu không focus vào dropdown, vẫn xử lý Enter như nút Filter
				updateChart();
			}
		}
	});
	
	// Cài đặt tooltip cho các nút
	setupTooltips();
});

// Hàm tự động cập nhật khi thay đổi dropdown
function autoUpdateOnChange() {
	// Thêm class loading cho các dropdown để hiển thị trạng thái đang cập nhật
	document.getElementById('year').classList.add('loading');
	document.getElementById('month').classList.add('loading');
	
	// Hiển thị thông báo auto-update
	const notificationElement = document.getElementById('auto-update-notification');
	notificationElement.classList.add('active');
	
	// Chỉ tự động cập nhật sau 300ms để tránh quá nhiều request
	if (window.autoUpdateTimeout) {
		clearTimeout(window.autoUpdateTimeout);
	}
	
	window.autoUpdateTimeout = setTimeout(() => {
		const year = document.getElementById("year").value;
		const month = document.getElementById("month").value;
		const pageSize = document.getElementById("page-size")?.value || 10;
		
		// Hiển thị thông báo nhẹ nhàng trong quá trình tải dữ liệu
		Swal.fire({
			toast: true,
			position: 'top-end',
			title: 'Đang cập nhật...',
			showConfirmButton: false,
			timer: 1000,
			timerProgressBar: true,
			didOpen: (toast) => {
				toast.addEventListener('mouseenter', Swal.stopTimer)
				toast.addEventListener('mouseleave', Swal.resumeTimer)
			}
		});
		
		loadData(year, month, 0, parseInt(pageSize))
			.then(() => {
				// Xóa class loading sau khi hoàn thành
				document.getElementById('year').classList.remove('loading');
				document.getElementById('month').classList.remove('loading');
				notificationElement.classList.remove('active');
			})
			.catch(() => {
				// Xóa class loading kể cả khi có lỗi
				document.getElementById('year').classList.remove('loading');
				document.getElementById('month').classList.remove('loading');
				notificationElement.classList.remove('active');
			});
	}, 300);
}

// Render phân trang
function renderPagination(ordersData, year, month, currentPage, currentSize) {
    const paginationContainer = document.getElementById('pagination-container');
    if (!paginationContainer) return;
    
    const totalPages = ordersData.totalPages;
    const totalItems = ordersData.totalItems;
    
    if (!totalPages || totalPages <= 0) {
        paginationContainer.style.display = 'none';
        return;
    }
    
    paginationContainer.style.display = 'block';
    
    // Tạo phần HTML cho phân trang
    let paginationHtml = `
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <!-- Previous button -->
                <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:void(0)" onclick="navigateToPage(${currentPage - 1}, ${currentSize}, '${year}', '${month || ''}')" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                
                <!-- Page numbers -->
    `;
    
    // Hiển thị phân trang thông minh (tối đa 5 trang)
    const maxPagesToShow = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
    
    // Điều chỉnh startPage nếu endPage giảm
    if (endPage - startPage + 1 < maxPagesToShow) {
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }
    
    // Luôn hiển thị trang đầu
    if (startPage > 0) {
        paginationHtml += `
            <li class="page-item">
                <a class="page-link" href="javascript:void(0)" onclick="navigateToPage(0, ${currentSize}, '${year}', '${month || ''}')">1</a>
            </li>
        `;
        
        // Thêm dấu "..." nếu không phải trang liền kề
        if (startPage > 1) {
            paginationHtml += `
                <li class="page-item disabled">
                    <a class="page-link" href="javascript:void(0)">...</a>
                </li>
            `;
        }
    }
    
    // Thêm các trang giữa
    for (let i = startPage; i <= endPage; i++) {
        paginationHtml += `
            <li class="page-item ${currentPage === i ? 'active' : ''}">
                <a class="page-link" href="javascript:void(0)" onclick="navigateToPage(${i}, ${currentSize}, '${year}', '${month || ''}')">${i + 1}</a>
            </li>
        `;
    }
    
    // Luôn hiển thị trang cuối
    if (endPage < totalPages - 1) {
        // Thêm dấu "..." nếu không phải trang liền kề
        if (endPage < totalPages - 2) {
            paginationHtml += `
                <li class="page-item disabled">
                    <a class="page-link" href="javascript:void(0)">...</a>
                </li>
            `;
        }
        
        paginationHtml += `
            <li class="page-item">
                <a class="page-link" href="javascript:void(0)" onclick="navigateToPage(${totalPages - 1}, ${currentSize}, '${year}', '${month || ''}')">${totalPages}</a>
            </li>
        `;
    }
    
    // Thêm nút Next
    paginationHtml += `
                <!-- Next button -->
                <li class="page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:void(0)" onclick="navigateToPage(${currentPage + 1}, ${currentSize}, '${year}', '${month || ''}')" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
        
        <!-- Pagination info and size selector -->
        <div class="d-flex justify-content-center align-items-center mt-2">
            <span class="pagination-info me-3">
                Trang <span>${currentPage + 1}</span> / <span>${totalPages}</span>
                <span>
                    (Hiển thị <span>${totalItems > 0 ? currentPage * currentSize + 1 : 0}</span> - 
                    <span>${Math.min((currentPage * currentSize + currentSize), totalItems)}</span> 
                    trong tổng số <span>${totalItems}</span> đơn hàng)
                </span>
            </span>
            
            <!-- Items per page selector -->
            <div class="items-per-page-selector">
                <div class="d-flex align-items-center">
                    <label for="page-size" class="me-2">Hiển thị:</label>
                    <select id="page-size" class="form-select form-select-sm" style="width: auto;" onchange="changePageSize(this.value, '${year}', '${month || ''}')">
                        <option value="5" ${currentSize == 5 ? 'selected' : ''}>5</option>
                        <option value="10" ${currentSize == 10 ? 'selected' : ''}>10</option>
                        <option value="20" ${currentSize == 20 ? 'selected' : ''}>20</option>
                        <option value="50" ${currentSize == 50 ? 'selected' : ''}>50</option>
                    </select>
                </div>
            </div>
        </div>
    `;
    
    paginationContainer.innerHTML = paginationHtml;
}

// Hàm điều hướng trang
function navigateToPage(page, size, year, month) {
    // Hiển thị loading indicator
    Swal.fire({
        toast: true,
        position: 'top-end',
        title: 'Đang tải trang ' + (page + 1),
        showConfirmButton: false,
        timer: 1000,
        timerProgressBar: true
    });
    
    // Load dữ liệu trang mới
    loadData(year, month || null, page, parseInt(size));
}

// Hàm thay đổi kích thước trang
function changePageSize(size, year, month) {
    const intSize = parseInt(size);
    
    // Hiển thị thông báo loading
    Swal.fire({
        toast: true,
        position: 'top-end',
        title: `Đang cập nhật hiển thị ${intSize} đơn hàng mỗi trang`,
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true
    });
    
    // Gọi loadData để tải lại dữ liệu với kích thước trang mới
    loadData(year, month || null, 0, intSize);
}