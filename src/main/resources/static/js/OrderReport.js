function loadData(year, month) {
	const token = document.getElementById('token').value;
	console.log("TOKEN:", token);
	// Gọi API doanh thu
	const revenueApi = month
		? `http://localhost:8080/api/orders/revenue/daily?year=${year}&month=${month}`
		: `http://localhost:8080/api/orders/revenue/monthly?year=${year}`;

	// Gọi API trạng thái
	const statusApi = month
		? `http://localhost:8080/api/orders/status-percentage/monthly?year=${year}&month=${month}`
		: `http://localhost:8080/api/orders/status-percentage/yearly?year=${year}`;

	const apiUrl = month
		? `http://localhost:8080/api/orders/list?year=${year}&month=${month}`
		: `http://localhost:8080/api/orders/list?year=${year}`;

	// Gọi API
	Promise.all([
		fetch(revenueApi, {
			method: "GET",
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'application/json'
			}
		}),
		fetch(statusApi, {
			method: "GET",
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'application/json'
			}
		}),
		fetch(apiUrl, {
			method: "GET",
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'application/json'
			}
		}),
	])
		.then(responses => Promise.all(responses.map(response => response.json())))
		.then(([revenueData, statusData, ordersData]) => {
			const isDaily = !!month;
			const categories = isDaily
				? revenueData.map(item => `Ngày ${item.day}`)
				: revenueData.map(item => `Tháng ${item.month}`);
			const revenues = revenueData.map(item => item.totalRevenue);

			renderChart(categories, revenues, isDaily ? 'Daily Revenue' : 'Monthly Revenue');

			const statusCategories = Object.keys(statusData);
			const statusValues = Object.values(statusData);

			renderPieChart(statusCategories, statusValues, isDaily ? 'Daily Order Status' : 'Monthly Order Status');

			renderOrderList(ordersData);
		})
	//fetch(url)
	//.then(response => response.json())
	//.then(data => {
	//	const isDaily = !!month;
	//	const categories = isDaily
	//		? data.map(item => `Ngày ${item.day}`)
	//		: data.map(item => `Tháng ${item.month}`);
	//	const revenues = data.map(item => item.totalRevenue);

	//	renderChart(categories, revenues, isDaily ? 'Daily Revenue' : 'Monthly Revenue');
	//})
	//.catch(error => console.error("Error fetching revenue data:", error));
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

	orders.forEach(order => {
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
	const apiUrl = month
		? `http://localhost:9998/api/orders/orders-export?year=${year}&month=${month}`
		: `http://localhost:9998/api/orders/orders-export?year=${year}`;

	// const url = `/report/order-report/export?year=${year}&month=${month || ''}`;

	fetch(apiUrl, {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json', // Không bắt buộc nhưng tốt để tương thích API
		},
	})
		.then(response => {
			if (response.ok) {
				return response.blob(); // File Excel trả về dưới dạng Blob
			} else {
				throw new Error('Failed to export orders');
			}
		})
		.then(blob => {
			// Tạo link tải file
			const downloadUrl = window.URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = downloadUrl;
			a.download = `orders_${year}_${month || 'all'}.xlsx`; // Tên file tải về
			document.body.appendChild(a);
			a.click();
			a.remove(); // Xóa phần tử sau khi click
			window.URL.revokeObjectURL(downloadUrl); // Giải phóng bộ nhớ
		})
		.catch(error => {
			console.error('Error:', error);
			alert('Có lỗi xảy ra khi xuất đơn hàng.');
		});
}
function handleExport() {
	const year = document.getElementById('year').value;
	const month = document.getElementById('month').value;

	if (!year) {
		alert("Year is required!");
		return;
	}

	exportOrders(year, month); // Gọi hàm JavaScript tải file
}

function updateChart() {
	const year = document.getElementById("year").value;
	const month = document.getElementById("month").value;
	loadData(year, month || null);
}
// Tự động tải biểu đồ mặc định khi trang load
document.addEventListener("DOMContentLoaded", () => {
	const defaultYear = new Date().getFullYear();
	const defaultMonth = null; // Ban đầu không chọn tháng
	loadData(defaultYear, defaultMonth);
});