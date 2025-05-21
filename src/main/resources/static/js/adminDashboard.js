// SIDEBAR TOGGLE

let sidebarOpen = false;
const sidebar = document.getElementById('sidebar');

function openSidebar() {
	if (!sidebarOpen) {
		sidebar.classList.add('sidebar-responsive');
		sidebarOpen = true;
	}
}

function closeSidebar() {
	if (sidebarOpen) {
		sidebar.classList.remove('sidebar-responsive');
		sidebarOpen = false;
	}
}

// ---------- CHARTS ----------

// AREA CHART
const areaChartOptions = {
	series: [
		{
			name: 'Purchase Orders',
			data: [31, 40, 28, 51, 42, 109, 100],
		},
		{
			name: 'Sales Orders',
			data: [11, 32, 45, 32, 34, 52, 41],
		},
	],
	chart: {
		type: 'area',
		background: 'transparent',
		height: 400,
		width: '100%',
		stacked: false,
		toolbar: {
			show: false,
		},
	},
	colors: ['#00ab57', '#d50000'],
	labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
	dataLabels: {
		enabled: false,
	},
	fill: {
		gradient: {
			opacityFrom: 0.4,
			opacityTo: 0.1,
			shadeIntensity: 1,
			stops: [0, 100],
			type: 'vertical',
		},
		type: 'gradient',
	},
	grid: {
		borderColor: '#55596e',
		yaxis: {
			lines: {
				show: true,
			},
		},
		xaxis: {
			lines: {
				show: true,
			},
		},
	},
	legend: {
		labels: {
			colors: '#f5f7ff',
		},
		show: true,
		position: 'top',
	},
	markers: {
		size: 6,
		strokeColors: '#1b2635',
		strokeWidth: 3,
	},
	stroke: {
		curve: 'smooth',
	},
	xaxis: {
		axisBorder: {
			color: '#55596e',
			show: true,
		},
		axisTicks: {
			color: '#55596e',
			show: true,
		},
		labels: {
			offsetY: 5,
			style: {
				colors: '#f5f7ff',
			},
		},
	},
	yaxis: [
		{
			title: {
				text: 'Purchase Orders',
				style: {
					color: '#f5f7ff',
				},
			},
			labels: {
				style: {
					colors: ['#f5f7ff'],
				},
			},
		},
		{
			opposite: true,
			title: {
				text: 'Sales Orders',
				style: {
					color: '#f5f7ff',
				},
			},
			labels: {
				style: {
					colors: ['#f5f7ff'],
				},
			},
		},
	],
	tooltip: {
		shared: true,
		intersect: false,
		theme: 'dark',
	},
};

const areaChart = new ApexCharts(
	document.querySelector('#area-chart'),
	areaChartOptions
);
document.addEventListener('DOMContentLoaded', function() {
	const token = document.getElementById('token').value;
	const refreshToken = document.getElementById('refreshToken').value;
	document.cookie = `refreshToken=${refreshToken}; path=/`;
	fetch('http://54.254.82.176:8080/api/orders/orders-statistic', {
		method: "GET",
		headers: {
			'Authorization': `Bearer ${token}`,
			
			'Content-Type': 'application/json'
		},
		credentials: 'include'
	})
	.then(response => response.json())
	.then(data => {
		console.log(data);
		areaChart.updateOptions({
			xaxis: { categories: data.data.month },
			series: [
				{
					name: 'Purchase Orders',
					data: data.data.purchasedOrder,
				},
				{
					name: 'Sales Orders',
					data: data.data.salesOrder,
				},
			],
		});
	})
	.catch(error => {
		console.error("Error fetching order statistics:", error);
	});
	
	areaChart.render();
	
	// Đảm bảo biểu đồ thay đổi kích thước khi trình duyệt thay đổi kích thước
	window.addEventListener('resize', function() {
		areaChart.render();
	});
});
