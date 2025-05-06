// Biến toàn cục - sử dụng mảng rỗng thay vì danh sách cố định
let glassesData = []; // Sẽ chỉ chứa kính được chọn
let currentGlass = null; // Lưu thông tin kính hiện tại

// Các biến toàn cục khác
let video, canvas, ctx;
let selectedGlassesId = null;
let glassesImages = {};
let processedGlassesImages = {};
let isDragging = false;
let offsetX = 0, offsetY = 0;
let glassesPosition = { x: 0, y: 0, scale: 1.0, rotation: 0 };
let isWebcamActive = false;
let lastSavedImage = null;
let isTrackingEnabled = true;
let faceMesh = null;
let camera = null;
let faceDetected = false;
let lastFaceData = null;
let debugMode = false;
let lastFaceRect = null;

// MediaPipe Face Mesh landmarks cụ thể
const FACE_LANDMARKS = {
	LEFT_EYE: [33, 7, 163, 144, 145, 153, 154, 155, 133, 173, 157, 158, 159, 160, 161, 246],
	RIGHT_EYE: [362, 382, 381, 380, 374, 373, 390, 249, 263, 466, 388, 387, 386, 385, 384, 398],
	NOSE_TIP: 1,
	LEFT_EYE_CENTER: 473,
	RIGHT_EYE_CENTER: 468,
	LEFT_EYEBROW: 282,
	RIGHT_EYEBROW: 52,
	LEFT_EAR: 234, // Điểm tai trái
	RIGHT_EAR: 454, // Điểm tai phải
	FACE_OVAL: [10, 338, 297, 332, 284, 251, 389, 356, 454, 323, 361, 288, 397, 365, 379, 378, 400, 377, 152, 148, 176, 149, 150, 136, 172, 58, 132, 93, 234, 127, 162, 21, 54, 103, 67, 109]
};

const GLASSES_CONFIG = {
	WIDTH_FACTOR: 0.6
};

// Hàm lấy tham số từ URL
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// Hàm tạo kính mặc định khi không có dữ liệu
function setDefaultGlass(id = "default-glass") {
    currentGlass = {
        id: id,
        name: "Kính được chọn",
        imageUrl: "/images/kinhdenNoBG.png",
        description: "Kính đang được thử",
        price: 0
    };
    
    glassesData = [currentGlass];
    selectedGlassesId = id;
}

// Hiển thị thông tin kính đã chọn trong UI
function displaySelectedGlassInfo() {
    // Tìm hoặc tạo container để hiển thị thông tin kính
    let infoContainer = document.getElementById('selected-glass-info');
    
    if (!infoContainer) {
        // Nếu không tìm thấy, tạo mới container
        const webcamContainer = document.querySelector('.webcam-container');
        if (!webcamContainer) return;
        
        infoContainer = document.createElement('div');
        infoContainer.id = 'selected-glass-info';
        infoContainer.className = 'selected-glass-info card mt-3';
        webcamContainer.parentNode.insertBefore(infoContainer, webcamContainer.nextSibling);
    }
    
    if (!currentGlass) return;
    
    // Cập nhật nội dung
    infoContainer.innerHTML = `
        <div class="card-body">
            <div class="d-flex align-items-center">
                <img src="${currentGlass.imageUrl}" alt="${currentGlass.name}" class="img-thumbnail" style="width: 80px; margin-right: 15px;">
                <div>
                    <h5 class="card-title mb-1">${currentGlass.name}</h5>
                    <p class="card-text small mb-1">${currentGlass.description || ''}</p>
                    <p class="card-text text-danger fw-bold">${currentGlass.price.toLocaleString('vi-VN')}₫</p>
                </div>
            </div>
        </div>
    `;
}

// Ẩn danh sách kính không cần thiết
function hideGlassesList() {
    // Tìm và ẩn danh sách kính nếu tồn tại
    const glassesList = document.querySelector('.glasses-list');
    if (glassesList) {
        glassesList.style.display = 'none';
    }
    
    // Ẩn phần bộ sưu tập kính không cần thiết
    const glassesCollection = document.getElementById('glasses-section');
    if (glassesCollection) {
        glassesCollection.style.display = 'none';
    }
    
    // Ẩn phần giới thiệu và hướng dẫn không cần thiết
    const howItWorks = document.getElementById('how-it-works');
    if (howItWorks) {
        howItWorks.style.display = 'none';
    }
    
    // Ẩn các phần không liên quan khác
    const nonEssentialSections = document.querySelectorAll('.section1, .banner1, .banner2, .banner0');
    nonEssentialSections.forEach(section => {
        if (section) section.style.display = 'none';
    });
    
    // Tạo nút quay lại trang danh sách sản phẩm
    addBackButton();
}

// Thêm nút quay lại trang danh sách sản phẩm
function addBackButton() {
    // Tìm container để thêm nút
    const container = document.querySelector('.webcam-container');
    if (!container) return;
    
    // Tạo nút quay lại
    const backButton = document.createElement('div');
    backButton.className = 'back-button mt-3 mb-3';
    backButton.innerHTML = `
        <button class="btn btn-outline-secondary" id="back-to-products">
            <i class="fas fa-arrow-left"></i> Quay lại danh sách sản phẩm
        </button>
    `;
    
    // Thêm vào trước webcam container
    container.parentNode.insertBefore(backButton, container);
    
    // Thêm sự kiện cho nút
    document.getElementById('back-to-products').addEventListener('click', () => {
        // Sử dụng history API để quay lại trang trước
        window.history.back();
    });
    
    // Thêm style cho nút
    const style = document.createElement('style');
    style.textContent = `
        .back-button {
            text-align: left;
        }
        
        .back-button button {
            transition: all 0.2s ease;
        }
        
        .back-button button:hover {
            transform: translateX(-3px);
        }
    `;
    document.head.appendChild(style);
}

// Thiết lập giao diện đơn giản hóa (chỉ hiển thị kính đã chọn)
function setupSimplifiedUI() {
    // Hiển thị thông tin kính đã chọn
    displaySelectedGlassInfo();
    
    // Thêm event listener cho nút chụp ảnh
    const captureButton = document.getElementById('capture-btn');
    if (captureButton) {
        captureButton.addEventListener('click', takeScreenshot);
    }
    
    // Thêm sự kiện kéo thả để điều chỉnh kính
    setupDraggableGlasses();
    
    // Thêm điều khiển cho kính
    addGlassesControls();
    
    // Thêm nút tải ảnh và chức năng xử lý
    addImageUploadFeature();
    
    // Thêm nút chuyển đổi theo dõi khuôn mặt
    addTrackingToggle();
    
    // Hiển thị hướng dẫn
    showInstructions();
    
    // Ẩn danh sách kính nếu có (vì chỉ hiển thị kính được chọn)
    hideGlassesList();
}

// Khởi tạo ứng dụng khi DOM đã tải xong
document.addEventListener('DOMContentLoaded', async () => {
	console.log("Ứng dụng thử kính đang khởi tạo...");

	// Thiết lập webcam và canvas
	video = document.getElementById('webcam');
	canvas = document.getElementById('canvas');

	if (!canvas) {
		console.error("Không tìm thấy canvas");
		return;
	}

	ctx = canvas.getContext('2d');

	// Thiết lập kích thước mặc định cho canvas
	canvas.width = 640;
	canvas.height = 480;

	try {
		// Lấy ID kính từ URL
		const glassesIdFromUrl = getUrlParameter('glassesId');
		
		if (glassesIdFromUrl) {
			console.log("Đã nhận tham số glassesId từ URL:", glassesIdFromUrl);
			selectedGlassesId = glassesIdFromUrl;
			
			// Tìm dữ liệu kính từ localStorage
			const storedGlassData = localStorage.getItem('glassData');
			if (storedGlassData) {
				try {
					// Phân tích dữ liệu kính từ JSON
					currentGlass = JSON.parse(storedGlassData);
					console.log("Đã tải dữ liệu kính từ localStorage:", currentGlass);
					
					// Thêm kính vào mảng glassesData
					glassesData = [currentGlass];
					
					// Hiển thị thông tin kính đã chọn trong UI
					displaySelectedGlassInfo();
					
					// Xóa dữ liệu từ localStorage sau khi đã xử lý
					localStorage.removeItem('glassData');
				} catch (e) {
					console.error("Lỗi khi phân tích dữ liệu kính từ localStorage:", e);
					// Nếu có lỗi khi phân tích dữ liệu, tạo kính mặc định
					setDefaultGlass(glassesIdFromUrl);
				}
			} else {
				// Nếu không có dữ liệu từ localStorage nhưng có ID từ URL
				setDefaultGlass(glassesIdFromUrl);
			}
		} else {
			// Nếu không có tham số glassesId, hiển thị thông báo
			showMessage("Không có kính nào được chọn. Vui lòng chọn kính từ trang sản phẩm.", "warning", 5000);
			// Tạo kính mặc định
			setDefaultGlass();
		}
		
		// Tải hình ảnh kính
		await preloadGlassesImages();
		console.log("Đã tải hình ảnh kính thành công");
	} catch (error) {
		console.error("Tải hình ảnh kính thất bại:", error);
		showMessage("Không thể tải hình ảnh kính. Vui lòng thử lại sau.", "danger", 0);
	}

	// Thiết lập giao diện đơn giản hóa (chỉ cho kính đã chọn)
	setupSimplifiedUI();

	// Khởi tạo webcam và MediaPipe Face Mesh
	try {
		await setupWebcamAndFaceMesh();
		console.log("Đã khởi tạo webcam và Face Mesh thành công");
	} catch (error) {
		console.error("Khởi tạo webcam hoặc Face Mesh thất bại:", error);
		showMessage("Không thể truy cập webcam hoặc khởi tạo Face Mesh. Vui lòng thử lại.", "warning", 0);

		// Chuyển sang chế độ thủ công nếu không khởi tạo được Face Mesh
		isTrackingEnabled = false;
		setupManualMode();
	}

	// Đặt lại vị trí kính phù hợp
	resetGlassesPosition();

	// Ẩn thông báo đang tải
	const loadingElement = document.getElementById('loading');
	if (loadingElement) {
		loadingElement.style.display = 'none';
	}

	// Bắt đầu vòng lặp hiển thị
	requestAnimationFrame(renderLoop);
});

// Thiết lập webcam và khởi tạo Face Mesh
async function setupWebcamAndFaceMesh() {
	return new Promise(async (resolve, reject) => {
		try {
			// Kiểm tra MediaPipe đã được tải chưa
			if (!window.FaceMesh) {
				showMessage("Đang tải thư viện MediaPipe Face Mesh...", "info", 0);
				await loadMediaPipeScripts();
			}

			// Khởi tạo Face Mesh
			faceMesh = new FaceMesh({
				locateFile: (file) => {
					return `https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh@0.4/${file}`;
				}
			});

			faceMesh.setOptions({
				maxNumFaces: 1,
				refineLandmarks: true,
				minDetectionConfidence: 0.5,
				minTrackingConfidence: 0.5
			});

			faceMesh.onResults(onFaceMeshResults);

			// Khởi tạo camera với MediaPipe
			camera = new Camera(video, {
				onFrame: async () => {
					if (isWebcamActive && isTrackingEnabled) {
						await faceMesh.send({ image: video });
					}
				},
				width: 640,
				height: 480
			});

			// Bắt đầu camera
			await camera.start();
			isWebcamActive = true;

			// Hiển thị hướng dẫn
			showMessage("Face Mesh đã sẵn sàng, đang theo dõi khuôn mặt...", "success", 3000);

			resolve();
		} catch (error) {
			console.error("Lỗi khi thiết lập Face Mesh:", error);
			reject(error);
		}
	});
}

// Tải các thư viện MediaPipe nếu chưa được tải
async function loadMediaPipeScripts() {
	const scripts = [
		"https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh@0.4/face_mesh.min.js",
		"https://cdn.jsdelivr.net/npm/@mediapipe/camera_utils/camera_utils.js"
	];

	const loadPromises = scripts.map(src => {
		return new Promise((resolve, reject) => {
			const script = document.createElement('script');
			script.src = src;
			script.async = true;
			script.onload = resolve;
			script.onerror = reject;
			document.head.appendChild(script);
		});
	});

	await Promise.all(loadPromises);

	// Đợi thêm một chút để đảm bảo các thư viện đã được khởi tạo
	return new Promise(resolve => setTimeout(resolve, 500));
}

// Chuyển sang chế độ thủ công
function setupManualMode() {
	// Khởi tạo webcam mà không sử dụng Face Mesh
	setupWebcam().catch(error => {
		console.error("Lỗi khi khởi tạo webcam:", error);
		showMessage("Không thể truy cập webcam. Vui lòng đảm bảo bạn đã cấp quyền truy cập camera.", "warning", 0);
	});

	// Thông báo cho người dùng
	showMessage("Đã chuyển sang chế độ điều chỉnh thủ công. Bạn có thể kéo thả kính để điều chỉnh vị trí.", "info", 5000);
}

// Khởi tạo webcam tiêu chuẩn (không dùng Face Mesh)
async function setupWebcam() {
	try {
		const stream = await navigator.mediaDevices.getUserMedia({
			video: {
				width: { ideal: 1280 },
				height: { ideal: 720 },
				facingMode: "user"
			},
			audio: false
		});

		video.srcObject = stream;

		return new Promise(resolve => {
			video.onloadedmetadata = () => {
				// Thiết lập kích thước canvas phù hợp với video
				if (canvas) {
					canvas.width = video.videoWidth;
					canvas.height = video.videoHeight;
				}
				video.play();
				isWebcamActive = true;
				resolve();
			};
		});
	} catch (error) {
		console.error('Lỗi khi truy cập webcam:', error);
		throw error;
	}
}

// Xử lý kết quả từ Face Mesh
function onFaceMeshResults(results) {
	if (results.multiFaceLandmarks && results.multiFaceLandmarks.length > 0) {
		const landmarks = results.multiFaceLandmarks[0];
		lastFaceData = landmarks;
		faceDetected = true;

		if (isTrackingEnabled) {
			updateGlassesPosition(landmarks);
		}
	} else {
		faceDetected = false;
	}
}

// Cập nhật vị trí kính dựa trên dữ liệu khuôn mặt từ Face Mesh
function updateGlassesPosition(landmarks) {
	if (!landmarks) return;

	// Lấy tọa độ mắt trái, mắt phải và mũi
	const leftEye = landmarks[FACE_LANDMARKS.LEFT_EYE_CENTER];
	const rightEye = landmarks[FACE_LANDMARKS.RIGHT_EYE_CENTER];
	const noseTip = landmarks[FACE_LANDMARKS.NOSE_TIP];

	// Tính góc xoay của khuôn mặt
	const rotation = Math.atan2(
		rightEye.y - leftEye.y,
		rightEye.x - leftEye.x
	);

	// Tính khoảng cách giữa hai mắt (pixel) để scale kính
	const eyeDistance = Math.hypot(
		(rightEye.x - leftEye.x) * canvas.width,
		(rightEye.y - leftEye.y) * canvas.height
	);

	// Tọa độ trung điểm giữa hai mắt (pixel)
	const eyeCenterX = ((leftEye.x + rightEye.x) / 2) * canvas.width;
	const eyeCenterY = ((leftEye.y + rightEye.y) / 2) * canvas.height;

	// Tọa độ đầu mũi (pixel)
	const noseXpx = noseTip.x * canvas.width;
	const noseYpx = noseTip.y * canvas.height;

	// Tính vị trí kính:
	//  - X: lấy theo mũi để canh chính giữa sống mũi
	//  - Y: dựa vào trung tâm mắt, hạ thêm khoản từ eyeDistance, rồi bù offset mũi–mắt
	const xPos = noseXpx - canvas.width / 2;
	const yPos = (eyeCenterY - canvas.height / 2)
		- eyeDistance        // hạ kính thấp hơn chút so với mắt
		+ (noseYpx - eyeCenterY); // bù thêm offset từ mũi xuống

	glassesPosition = {
		x: xPos,
		y: yPos,
		scale: eyeDistance / 100 * 1.6,
		rotation: rotation
	};
}

// Thiết lập giao diện người dùng
function setupUI() {
	// Thiết lập giao diện chọn kính
	setupGlassesSelection();

	// Thêm event listener cho nút chụp ảnh
	const captureButton = document.getElementById('capture-btn');
	if (captureButton) {
		captureButton.addEventListener('click', takeScreenshot);
	}

	// Thêm event listeners cho các nút "Thử ngay"
	document.querySelectorAll('.try-glasses').forEach(button => {
		button.addEventListener('click', function() {
			const glassesId = this.getAttribute('data-glasses-id');
			selectGlassesById(glassesId);

			const tryOnSection = document.getElementById('try-on-section');
			if (tryOnSection) {
				tryOnSection.scrollIntoView({ behavior: 'smooth' });
			}
		});
	});

	// Thêm sự kiện kéo thả để điều chỉnh kính
	setupDraggableGlasses();

	// Thêm điều khiển cho kính
	addGlassesControls();

	// Thêm nút tải ảnh và chức năng xử lý
	addImageUploadFeature();

	// Thêm nút chuyển đổi theo dõi khuôn mặt
	addTrackingToggle();

	// Hiển thị hướng dẫn
	showInstructions();
}

// Thêm nút chuyển đổi theo dõi khuôn mặt
function addTrackingToggle() {
	const webcamContainer = document.querySelector('.webcam-container');
	if (!webcamContainer) return;

	const toggleDiv = document.createElement('div');
	toggleDiv.className = 'tracking-toggle mb-2';
	toggleDiv.innerHTML = `
        <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="trackingToggle" ${isTrackingEnabled ? 'checked' : ''}>
            <label class="form-check-label" for="trackingToggle">Tự động theo dõi khuôn mặt</label>
        </div>
    `;

	webcamContainer.parentNode.insertBefore(toggleDiv, webcamContainer);

	document.getElementById('trackingToggle').addEventListener('change', function() {
		isTrackingEnabled = this.checked;
		showMessage(isTrackingEnabled ?
			"Đã bật chế độ tự động theo dõi khuôn mặt" :
			"Đã tắt chế độ tự động, bạn có thể điều chỉnh kính thủ công",
			"info", 2000);
	});
}

// Thêm điều khiển cho việc điều chỉnh kính
function addGlassesControls() {
	const webcamContainer = document.querySelector('.webcam-container');
	if (!webcamContainer) return;

	const controlsDiv = document.createElement('div');
	controlsDiv.className = 'glasses-controls mt-3';
	controlsDiv.innerHTML = `
        <div class="card">
            <div class="card-header bg-primary text-white py-1">
                <h6 class="mb-0">Điều chỉnh kính</h6>
            </div>
            <div class="card-body py-2">
                <div class="d-flex justify-content-center align-items-center flex-wrap mb-2">
                    <button id="move-up" class="btn btn-sm btn-outline-secondary m-1">↑</button>
                    <button id="move-down" class="btn btn-sm btn-outline-secondary m-1">↓</button>
                    <button id="move-left" class="btn btn-sm btn-outline-secondary m-1">←</button>
                    <button id="move-right" class="btn btn-sm btn-outline-secondary m-1">→</button>
                </div>
                
                <div class="d-flex justify-content-center mb-2">
                    <div class="btn-group">
                        <button id="size-smaller" class="btn btn-sm btn-info">Nhỏ hơn</button>
                        <button id="size-larger" class="btn btn-sm btn-info">Lớn hơn</button>
                    </div>
                </div>
                
                <div class="d-flex justify-content-center">
                    <button id="reset-position" class="btn btn-sm btn-warning">Đặt lại vị trí</button>
                </div>
                
                <p class="small text-center mt-2">Hoặc kéo thả kính để điều chỉnh</p>
            </div>
        </div>
    `;

	// Thêm vào sau webcam container
	webcamContainer.parentNode.insertBefore(controlsDiv, webcamContainer.nextSibling);

	// Thêm sự kiện cho các nút
	document.getElementById('move-up').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesPosition(0, -10);
	});

	document.getElementById('move-down').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesPosition(0, 10);
	});

	document.getElementById('move-left').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesPosition(-10, 0);
	});

	document.getElementById('move-right').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesPosition(10, 0);
	});

	document.getElementById('size-smaller').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesSize(-0.1);
	});

	document.getElementById('size-larger').addEventListener('click', () => {
		isTrackingEnabled = false;
		document.getElementById('trackingToggle').checked = false;
		adjustGlassesSize(0.1);
	});

	document.getElementById('reset-position').addEventListener('click', resetGlassesPosition);
}

// Điều chỉnh vị trí kính
function adjustGlassesPosition(dx, dy) {
	glassesPosition.x += dx;
	glassesPosition.y += dy;
}

// Điều chỉnh kích thước kính
function adjustGlassesSize(dScale) {
	glassesPosition.scale = Math.max(0.3, Math.min(2.0, glassesPosition.scale + dScale));
}

// Đặt lại vị trí kính
function resetGlassesPosition() {
	// Vị trí mặc định khi không có dữ liệu khuôn mặt
	glassesPosition = {
		x: 0,
		y: -canvas.height / 8,
		scale: 1.0,
		rotation: 0
	};

	// Bật lại chế độ theo dõi khuôn mặt
	isTrackingEnabled = true;
	if (document.getElementById('trackingToggle')) {
		document.getElementById('trackingToggle').checked = true;
	}

	showMessage("Đã đặt lại vị trí kính", "info", 2000);
}

// Thiết lập giao diện chọn kính
function setupGlassesSelection() {
	const glassesItems = document.querySelectorAll('.glasses-item');

	glassesItems.forEach(item => {
		item.addEventListener('click', () => {
			// Xóa lớp active từ tất cả các mục
			glassesItems.forEach(el => el.classList.remove('active'));

			// Thêm lớp active cho mục đã chọn
			item.classList.add('active');

			// Thiết lập kính đã chọn
			selectedGlassesId = item.getAttribute('data-glasses');
			console.log("Đã chọn kính:", selectedGlassesId);

			// Thông báo cho người dùng
			showMessage(`Đã chọn ${getGlassesNameById(selectedGlassesId)}`, "info", 2000);
		});
	});
}

// Lấy tên kính theo ID
function getGlassesNameById(id) {
	const glasses = glassesData.find(item => item.id === id);
	return glasses ? glasses.name : "Kính không xác định";
}

// Chọn kính theo ID
function selectGlassesById(id) {
	const glassesItem = document.querySelector(`.glasses-item[data-glasses="${id}"]`);
	if (glassesItem) {
		// Xóa lớp active từ tất cả các mục
		document.querySelectorAll('.glasses-item').forEach(el => el.classList.remove('active'));

		// Thêm lớp active cho mục đã chọn
		glassesItem.classList.add('active');

		// Thiết lập kính đã chọn
		selectedGlassesId = id;
		console.log("Đã chọn kính từ nút thử ngay:", selectedGlassesId);

		// Hiển thị thông báo hướng dẫn
		if (isTrackingEnabled) {
			showMessage("Kính sẽ tự động theo dõi khuôn mặt của bạn!", "info", 3000);
		} else {
			showMessage("Bạn có thể kéo thả để di chuyển kính hoặc dùng các nút điều khiển", "info", 3000);
		}
	}
}

// Hiển thị hướng dẫn sử dụng
function showInstructions() {
	const instructionsDiv = document.getElementById('instructions');
	if (instructionsDiv) {
		instructionsDiv.innerHTML = `
            <div class="alert alert-info p-2 m-0">
                <p class="mb-1 small">
                    <b>Hướng dẫn:</b> Kính sẽ tự động theo dõi khuôn mặt của bạn. Di chuyển để thấy kết quả!
                </p>
            </div>
        `;
	}
}

// Thiết lập sự kiện kéo thả kính
function setupDraggableGlasses() {
	if (!canvas) return;

	// Sự kiện chuột
	canvas.addEventListener('mousedown', handleDragStart);
	canvas.addEventListener('mousemove', handleDragMove);
	window.addEventListener('mouseup', handleDragEnd);

	// Hỗ trợ cả sự kiện cảm ứng
	canvas.addEventListener('touchstart', handleTouchStart, { passive: false });
	canvas.addEventListener('touchmove', handleTouchMove, { passive: false });
	canvas.addEventListener('touchend', handleDragEnd);
}

// Bắt đầu kéo thả
function handleDragStart(e) {
	e.preventDefault();
	if (!selectedGlassesId) return;

	// Tắt chế độ tự động khi người dùng thao tác thủ công
	if (isTrackingEnabled) {
		isTrackingEnabled = false;
		if (document.getElementById('trackingToggle')) {
			document.getElementById('trackingToggle').checked = false;
		}
		showMessage("Đã chuyển sang chế độ điều chỉnh thủ công", "info", 2000);
	}

	const rect = canvas.getBoundingClientRect();
	const x = e.clientX - rect.left;
	const y = e.clientY - rect.top;

	checkDraggingStart(x, y);
}

// Xử lý sự kiện cảm ứng bắt đầu
function handleTouchStart(e) {
	e.preventDefault();
	if (!selectedGlassesId || !e.touches[0]) return;

	// Tắt chế độ tự động khi người dùng thao tác thủ công
	if (isTrackingEnabled) {
		isTrackingEnabled = false;
		if (document.getElementById('trackingToggle')) {
			document.getElementById('trackingToggle').checked = false;
		}
	}

	const rect = canvas.getBoundingClientRect();
	const x = e.touches[0].clientX - rect.left;
	const y = e.touches[0].clientY - rect.top;

	checkDraggingStart(x, y);
}

// Kiểm tra nếu người dùng bắt đầu kéo kính
function checkDraggingStart(x, y) {
	// Lấy kích thước và vị trí hiện tại của kính
	const glassesRect = getGlassesRect();

	// Kiểm tra xem click có nằm trong khung kính không
	if (x >= glassesRect.x && x <= glassesRect.x + glassesRect.width &&
		y >= glassesRect.y && y <= glassesRect.y + glassesRect.height) {
		isDragging = true;
		offsetX = x - glassesRect.x;
		offsetY = y - glassesRect.y;
	}
}

// Di chuyển khi kéo
function handleDragMove(e) {
	if (!isDragging) return;

	const rect = canvas.getBoundingClientRect();
	const x = e.clientX - rect.left;
	const y = e.clientY - rect.top;

	updateDragPosition(x, y);
}

// Xử lý sự kiện cảm ứng di chuyển
function handleTouchMove(e) {
	e.preventDefault();
	if (!isDragging || !e.touches[0]) return;

	const rect = canvas.getBoundingClientRect();
	const x = e.touches[0].clientX - rect.left;
	const y = e.touches[0].clientY - rect.top;

	updateDragPosition(x, y);
}

// Cập nhật vị trí khi kéo thả
function updateDragPosition(x, y) {
	const centerX = canvas.width / 2;
	const centerY = canvas.height / 2;

	// Tính toán vị trí mới
	glassesPosition.x = x - offsetX - centerX + glassesPosition.scale * canvas.width * 0.15;
	glassesPosition.y = y - offsetY - centerY + glassesPosition.scale * canvas.height * 0.04;
}

// Kết thúc kéo thả
function handleDragEnd() {
	isDragging = false;
}

// Thêm chức năng tải ảnh từ thiết bị
function addImageUploadFeature() {
	const webcamContainer = document.querySelector('.webcam-container');
	if (!webcamContainer) return;

	// Tạo nút tải ảnh lên
	const uploadDiv = document.createElement('div');
	uploadDiv.className = 'image-upload mt-2';
	uploadDiv.innerHTML = `
        <label for="image-upload" class="btn btn-sm btn-secondary">Tải ảnh lên</label>
        <input type="file" id="image-upload" accept="image/*" style="display: none;">
    `;

	webcamContainer.parentNode.insertBefore(uploadDiv, webcamContainer.nextSibling);

	// Thêm xử lý sự kiện khi tải ảnh lên
	document.getElementById('image-upload').addEventListener('change', handleImageUpload);
}

// Xử lý tải ảnh lên
function handleImageUpload(event) {
	const file = event.target.files[0];
	if (!file) return;

	const reader = new FileReader();

	reader.onload = function(e) {
		const img = new Image();
		img.onload = function() {
			// Dừng webcam nếu đang hoạt động
			if (isWebcamActive && video.srcObject) {
				const tracks = video.srcObject.getTracks();
				tracks.forEach(track => track.stop());
				isWebcamActive = false;

				// Dừng nhận diện khuôn mặt
				if (camera) {
					camera.stop();
				}
			}

			// Điều chỉnh canvas để phù hợp với hình ảnh
			canvas.width = img.width;
			canvas.height = img.height;

			// Vẽ hình ảnh lên canvas
			ctx.clearRect(0, 0, canvas.width, canvas.height);
			ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

			// Lưu hình ảnh để sử dụng khi vẽ
			lastSavedImage = img;

			// Đặt lại vị trí kính và tắt chế độ tự động
			isTrackingEnabled = false;
			if (document.getElementById('trackingToggle')) {
				document.getElementById('trackingToggle').checked = false;
			}

			// Đặt vị trí mặc định cho kính trên ảnh
			resetGlassesPosition();

			showMessage("Đã tải ảnh lên thành công. Hãy điều chỉnh kính cho phù hợp.", "success", 3000);
		};
		img.src = e.target.result;
	};

	reader.readAsDataURL(file);
}

// Lấy hình chữ nhật của kính
function getGlassesRect() {
	if (!canvas || !selectedGlassesId || !glassesImages[selectedGlassesId]) {
		return { x: 0, y: 0, width: 0, height: 0 };
	}

	// Vị trí mặc định ở giữa canvas
	const centerX = canvas.width / 2;
	const centerY = canvas.height / 2;

	// Kích thước kính
	const glassesWidth = canvas.width * 0.3 * glassesPosition.scale;
	const glassesImg = glassesImages[selectedGlassesId];
	const aspectRatio = glassesImg ? (glassesImg.height / glassesImg.width) : 0.33;
	const glassesHeight = glassesWidth * aspectRatio;

	// Tính toán vị trí
	const x = centerX - glassesWidth / 2 + glassesPosition.x;
	const y = centerY - glassesHeight / 2 + glassesPosition.y;

	return {
		x: x,
		y: y,
		width: glassesWidth,
		height: glassesHeight
	};
}

// Vòng lặp hiển thị chính
function renderLoop() {
	try {
		if (!ctx || !canvas) {
			console.error('Canvas hoặc context không khả dụng');
			return requestAnimationFrame(renderLoop);
		}

		// Xóa khung hình trước
		ctx.clearRect(0, 0, canvas.width, canvas.height);

		// Vẽ hình ảnh nền (video hoặc ảnh đã tải)
		if (isWebcamActive && video.readyState === 4) {
			ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

			// Hiển thị trạng thái nhận diện khuôn mặt
			if (isTrackingEnabled) {
				// Hiển thị chỉ báo phát hiện khuôn mặt
				ctx.fillStyle = faceDetected ? "#28a745" : "#ffc107";
				ctx.beginPath();
				ctx.arc(canvas.width - 15, 15, 8, 0, 2 * Math.PI);
				ctx.fill();

				// Hiển thị chữ trạng thái
				ctx.font = "12px Arial";
				ctx.fillText(faceDetected ? "Đã phát hiện khuôn mặt" : "Đang tìm khuôn mặt...", canvas.width - 150, 20);

				// Vẽ landmarks khuôn mặt nếu ở chế độ debug
				if (debugMode && lastFaceData) {
					drawFaceMeshDebug(lastFaceData);
				}
			}
		} else if (lastSavedImage) {
			ctx.drawImage(lastSavedImage, 0, 0, canvas.width, canvas.height);
		}

		// Vẽ kính nếu đã chọn
		if (selectedGlassesId) {
			// Sử dụng hình ảnh đã qua xử lý (không có nền)
			const glassImg = processedGlassesImages[selectedGlassesId] || glassesImages[selectedGlassesId];
			if (glassImg) {
				drawGlasses(glassImg);
			}
		}
	} catch (error) {
		console.error("Lỗi trong vòng lặp hiển thị:", error);
	}

	// Tiếp tục vòng lặp
	requestAnimationFrame(renderLoop);
}

// Vẽ debug landmarks khuôn mặt
function drawFaceMeshDebug(landmarks) {
	ctx.fillStyle = '#00FF00';

	// Vẽ các điểm landmarks
	for (let i = 0; i < landmarks.length; i++) {
		const x = landmarks[i].x * canvas.width;
		const y = landmarks[i].y * canvas.height;

		ctx.beginPath();
		ctx.arc(x, y, 1, 0, 2 * Math.PI);
		ctx.fill();
	}

	// Đánh dấu đặc biệt các điểm quan trọng
	const importantPoints = [
		FACE_LANDMARKS.LEFT_EYE_CENTER,
		FACE_LANDMARKS.RIGHT_EYE_CENTER,
		FACE_LANDMARKS.NOSE_TIP,
		FACE_LANDMARKS.LEFT_EYEBROW,
		FACE_LANDMARKS.RIGHT_EYEBROW
	];

	ctx.fillStyle = '#FF0000';
	for (const idx of importantPoints) {
		const point = landmarks[idx];
		if (point) {
			const x = point.x * canvas.width;
			const y = point.y * canvas.height;

			ctx.beginPath();
			ctx.arc(x, y, 3, 0, 2 * Math.PI);
			ctx.fill();
		}
	}
}

// Vẽ kính
function drawGlasses(glassesImg) {
	if (!ctx || !glassesImg) return;

	// Nếu có khuôn mặt được phát hiện và đang ở chế độ theo dõi, vẽ kính theo vị trí khuôn mặt
	if (faceDetected && isTrackingEnabled && lastFaceRect) {
		// Tính toán vị trí mắt dựa trên khuôn mặt phát hiện được
		const faceWidth = lastFaceRect.width;
		const faceHeight = lastFaceRect.height;
		const faceX = lastFaceRect.x;
		const faceY = lastFaceRect.y;

		// Tính toán tọa độ mắt
		const eyeY = faceY + faceHeight * 0.45;

		// Tính toán kích thước kính phù hợp với khuôn mặt
		const glassesWidth = faceWidth * 1.05;
		const aspectRatio = glassesImg.height / glassesImg.width;
		const glassesHeight = glassesWidth * aspectRatio;

		// Tính toán vị trí đặt kính
		const x = faceX - (glassesWidth - faceWidth) / 2;
		const y = eyeY - glassesHeight * 0.3;

		// Tính góc nghiêng của khuôn mặt
		let rotationAngle = 0;

		// Tính toán góc xoay của khuôn mặt dựa vào vị trí mắt hoặc các điểm nhận dạng khác
		if (lastFaceRect.hasOwnProperty('angle')) {
			// Nếu thuật toán nhận diện mặt cung cấp góc nghiêng
			rotationAngle = lastFaceRect.angle;
		} else {
			// Tính góc nghiêng dựa trên các điểm khác trên khuôn mặt nếu có
			// Hoặc sử dụng góc xoay từ lần nhận diện trước đó
			rotationAngle = glassesPosition.rotation || 0;
		}

		// Lưu ngữ cảnh canvas hiện tại
		ctx.save();

		// Đặt điểm gốc cho phép biến đổi tại tâm của kính
		ctx.translate(x + glassesWidth / 2, y + glassesHeight / 2);

		// Xoay theo góc nghiêng của khuôn mặt
		ctx.rotate(rotationAngle);

		// Thử nghiệm với nhiều phép biến đổi khác nhau để sửa lỗi kính bị lật ngược
		ctx.scale(1, -1); // Lật theo trục Y thay vì trục X

		// Vẽ kính với tọa độ tương đối so với tâm
		ctx.drawImage(
			glassesImg,
			-glassesWidth / 2, -glassesHeight / 2,
			glassesWidth, glassesHeight
		);

		// Khôi phục ngữ cảnh canvas
		ctx.restore();

		// Lưu vị trí kính hiện tại để sử dụng cho chế độ kéo thả
		glassesPosition = {
			x: x + glassesWidth / 2 - canvas.width / 2,
			y: y + glassesHeight / 2 - canvas.height / 2,
			scale: glassesWidth / (canvas.width * 0.5),
			rotation: rotationAngle
		};

		// Vẽ điểm vị trí mắt để debug (nếu bật chế độ debug)
		if (debugMode) {
			ctx.fillStyle = "#ff0000";
			ctx.beginPath();
			ctx.arc(faceX + faceWidth / 2, eyeY, 5, 0, 2 * Math.PI);
			ctx.fill();
		}
	} else {
		// Khi không phát hiện khuôn mặt hoặc chế độ theo dõi bị tắt
		// Lấy hình chữ nhật của kính từ vị trí thủ công
		const rect = getGlassesRect();

		// Lưu ngữ cảnh canvas hiện tại
		ctx.save();

		// Đặt điểm gốc tại tâm của kính
		ctx.translate(rect.x + rect.width / 2, rect.y + rect.height / 2);

		// Áp dụng góc xoay được lưu trữ trong glassesPosition
		if (glassesPosition.rotation) {
			ctx.rotate(glassesPosition.rotation);
		}

		// Thử nghiệm với lật trục Y thay vì trục X
		ctx.scale(1, -1);

		// Vẽ kính với tọa độ tương đối so với tâm
		ctx.drawImage(
			glassesImg,
			-rect.width / 2, -rect.height / 2,
			rect.width, rect.height
		);

		// Khôi phục ngữ cảnh canvas
		ctx.restore();
	}
}

// Hiển thị thông báo
function showMessage(message, type = 'warning', duration = 5000) {
	const webcamContainer = document.querySelector('.webcam-container');
	if (!webcamContainer) return;

	// Xóa thông báo cũ nếu có
	const oldMessages = webcamContainer.querySelectorAll('.message-overlay');
	oldMessages.forEach(msg => webcamContainer.removeChild(msg));

	const messageDiv = document.createElement('div');
	messageDiv.className = `alert alert-${type} message-overlay`;
	messageDiv.style.position = 'absolute';
	messageDiv.style.top = '10px';
	messageDiv.style.left = '10px';
	messageDiv.style.right = '10px';
	messageDiv.style.zIndex = '100';
	messageDiv.style.textAlign = 'center';
	messageDiv.style.padding = '5px';
	messageDiv.style.fontSize = '14px';
	messageDiv.innerText = message;

	webcamContainer.appendChild(messageDiv);

	// Tự động ẩn thông báo sau khoảng thời gian
	if (duration > 0) {
		setTimeout(() => {
			if (webcamContainer.contains(messageDiv)) {
				webcamContainer.removeChild(messageDiv);
			}
		}, duration);
	}

	return messageDiv;
}

// Chụp và lưu ảnh màn hình
function takeScreenshot() {
	try {
		// Kiểm tra xem canvas có khả dụng không
		if (!canvas) {
			throw new Error("Canvas không khả dụng");
		}

		// Chuyển đổi canvas thành URL dữ liệu hình ảnh
		const imageDataUrl = canvas.toDataURL('image/png');

		// Thêm vào bộ sưu tập
		addToGallery(imageDataUrl);
	} catch (error) {
		console.error('Lỗi khi chụp ảnh màn hình:', error);
		showMessage("Không thể chụp ảnh. Vui lòng thử lại sau.", "danger", 3000);
	}
}

// Thêm ảnh chụp vào bộ sưu tập
function addToGallery(imageDataUrl) {
	const gallery = document.getElementById('gallery');
	if (!gallery) {
		console.error('Không tìm thấy phần tử gallery');
		return;
	}

	// Tạo cột gallery
	const column = document.createElement('div');
	column.className = 'col-md-4 col-sm-6 gallery-item';

	// Tạo thẻ hình ảnh
	const img = document.createElement('img');
	img.src = imageDataUrl;
	img.className = 'img-fluid w-100';
	img.alt = 'Ảnh thử kính';

	// Tạo nút xóa
	const removeBtn = document.createElement('button');
	removeBtn.className = 'remove-btn';
	removeBtn.innerHTML = '&times;';
	removeBtn.addEventListener('click', () => {
		gallery.removeChild(column);
	});

	// Tạo nút tải về
	const downloadBtn = document.createElement('button');
	downloadBtn.className = 'download-btn';
	downloadBtn.innerHTML = '↓';
	downloadBtn.title = 'Tải ảnh về';
	downloadBtn.addEventListener('click', () => {
		downloadImage(imageDataUrl);
	});

	// Thêm các phần tử vào gallery item
	column.appendChild(img);
	column.appendChild(removeBtn);
	column.appendChild(downloadBtn);

	// Thêm vào gallery
	gallery.appendChild(column);

	// Hiển thị thông báo
	showMessage('Đã lưu ảnh thành công!', 'success', 2000);
}

// Tải ảnh về
function downloadImage(dataUrl) {
	const link = document.createElement('a');
	link.href = dataUrl;
	link.download = `thu-kinh-online-${new Date().getTime()}.png`;
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}

// Xử lý ảnh để loại bỏ nền ô vuông và phần chân kính thừa
function removeCheckerboardBackground(imageData) {
    const canvas = document.createElement('canvas');
    const width = imageData.width;
    const height = imageData.height;
    canvas.width = width;
    canvas.height = height;

    const ctx = canvas.getContext('2d');
    ctx.drawImage(imageData, 0, 0);

    const imgData = ctx.getImageData(0, 0, width, height);
    const data = imgData.data;

    // Phân tích hình ảnh để tìm vùng kính
    let leftBorder = width;
    let rightBorder = 0;
    let topBorder = height;
    let bottomBorder = 0;

    // Tìm biên của khu vực không trong suốt và có màu đậm (kính)
    for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
            const index = (y * width + x) * 4;
            // Nếu pixel không trong suốt và có màu đậm (có thể là phần gọng kính)
            if (data[index + 3] > 230 && (data[index] < 200 || data[index + 1] < 200 || data[index + 2] < 200)) {
                if (x < leftBorder) leftBorder = x;
                if (x > rightBorder) rightBorder = x;
                if (y < topBorder) topBorder = y;
                if (y > bottomBorder) bottomBorder = y;
            }
        }
    }

    // Tính toán kích thước của vùng kính
    const glassesWidth = rightBorder - leftBorder;
    const glassesHeight = bottomBorder - topBorder;
    const centerX = leftBorder + glassesWidth / 2;
    const centerY = topBorder + glassesHeight / 2;

    // Tìm và loại bỏ các pixel nền và phần chân kính thừa
    for (let i = 0; i < data.length; i += 4) {
        const pixelIndex = i / 4;
        const x = pixelIndex % width;
        const y = Math.floor(pixelIndex / width);

        const r = data[i];
        const g = data[i + 1];
        const b = data[i + 2];
        const a = data[i + 3];

        // 1. Loại bỏ nền trắng/xám (ô vuông)
        if (a < 250 ||
            (r > 240 && g > 240 && b > 240) ||
            (Math.abs(r - g) < 10 && Math.abs(g - b) < 10 && Math.abs(r - b) < 10 && r > 200) ||
            ((r > 225 && g > 225 && b > 225) && (Math.abs(r - g) < 15 && Math.abs(g - b) < 15 && Math.abs(r - b) < 15))) {
            data[i + 3] = 0; // Làm trong suốt
            continue;
        }

        // 2. Xóa phần chân kính (phần gọng dài ra ngoài hai bên)
        const distanceFromCenterX = Math.abs(x - centerX);
        const maxWidth = glassesWidth * 0.8; // Giữ lại 80% chiều rộng của kính

        // Nếu pixel nằm ngoài vùng kính chính và quá xa so với trung tâm
        if (distanceFromCenterX > maxWidth / 2 && (x < leftBorder || x > rightBorder)) {
            const excessDistanceX = distanceFromCenterX - maxWidth / 2;

            // Nếu vượt quá càng xa thì càng trong suốt
            if (excessDistanceX > 20) {
                data[i + 3] = 0; // Hoàn toàn trong suốt
            } else {
                // Tạo hiệu ứng mờ dần ở vùng chuyển tiếp
                const alphaFactor = 1 - (excessDistanceX / 20);
                data[i + 3] = Math.floor(a * alphaFactor);
            }
            continue;
        }

        // 3. Xóa phần càng kính hướng lên trên
        const distanceFromCenterY = y - centerY;
        const maxHeight = glassesHeight * 0.1; // Giới hạn chiều cao của kính chính

        // Nếu pixel nằm ở phần trên của kính (hướng lên) và nằm ngoài vùng kính chính
        if (distanceFromCenterY < -maxHeight / 2 && distanceFromCenterX > maxWidth * 0.3) {
            const excessDistanceY = Math.abs(distanceFromCenterY) - maxHeight / 2;

            // Nếu vượt quá càng xa thì càng trong suốt
            if (excessDistanceY > 10) {
                data[i + 3] = 0; // Hoàn toàn trong suốt
            } else {
                // Tạo hiệu ứng mờ dần ở vùng chuyển tiếp
                const alphaFactor = 1 - (excessDistanceY / 10);
                data[i + 3] = Math.floor(a * alphaFactor);
            }
        }
    }

    ctx.putImageData(imgData, 0, 0);

    const resultImage = new Image();
    resultImage.src = canvas.toDataURL('image/png');
    return resultImage;
}

// Tải trước tất cả hình ảnh kính
async function preloadGlassesImages() {
	const promises = glassesData.map(item => {
		return new Promise((resolve, reject) => {
			const img = new Image();
			img.crossOrigin = "Anonymous"; // Cho phép tải hình ảnh từ nguồn khác
			img.src = item.imageUrl;
			img.onload = () => {
				glassesImages[item.id] = img;

				// Xử lý hình ảnh để loại bỏ nền ô vuông
				const processedImg = removeCheckerboardBackground(img);
				processedImg.onload = () => {
					processedGlassesImages[item.id] = processedImg;
					resolve();
				};
			};
			img.onerror = () => {
				console.warn(`Không thể tải hình ảnh: ${item.imageUrl}`);

				// Tạo hình ảnh thay thế
				const fallbackImg = createFallbackGlassesImage(item.name);
				glassesImages[item.id] = fallbackImg;
				processedGlassesImages[item.id] = fallbackImg;
				resolve();
			};
		});
	});

	try {
		await Promise.all(promises);
	} catch (error) {
		console.error('Lỗi khi tải hình ảnh kính:', error);
		throw error;
	}
}

// Tạo hình ảnh thay thế khi không thể tải hình ảnh kính
function createFallbackGlassesImage(name) {
	const canvas = document.createElement('canvas');
	canvas.width = 300;
	canvas.height = 100;
	const ctx = canvas.getContext('2d');

	// Vẽ khung kính đơn giản
	ctx.strokeStyle = '#000';
	ctx.lineWidth = 3;

	// Vẽ tròng kính trái
	ctx.beginPath();
	ctx.ellipse(75, 50, 40, 30, 0, 0, Math.PI * 2);
	ctx.stroke();

	// Vẽ tròng kính phải
	ctx.beginPath();
	ctx.ellipse(225, 50, 40, 30, 0, 0, Math.PI * 2);
	ctx.stroke();

	// Vẽ cầu nối giữa hai tròng kính
	ctx.beginPath();
	ctx.moveTo(115, 50);
	ctx.lineTo(185, 50);
	ctx.stroke();

	// Vẽ gọng kính (chân kính)
	ctx.beginPath();
	ctx.moveTo(35, 50);
	ctx.lineTo(15, 40);
	ctx.stroke();

	ctx.beginPath();
	ctx.moveTo(265, 50);
	ctx.lineTo(285, 40);
	ctx.stroke();

	// Thêm tên kính
	ctx.fillStyle = '#000';
	ctx.font = '12px Arial';
	ctx.textAlign = 'center';
	ctx.fillText(name, 150, 90);

	// Tạo hình ảnh từ canvas
	const img = new Image();
	img.src = canvas.toDataURL('image/png');
	return img;
}

// Thêm CSS để cải thiện giao diện
(function addCustomStyles() {
	const style = document.createElement('style');
	style.textContent = `
        .gallery-item {
            position: relative;
            margin-bottom: 20px;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 3px 6px rgba(0,0,0,0.1);
        }
        
        .remove-btn {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background-color: rgba(220, 53, 69, 0.8);
            color: white;
            font-size: 20px;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .remove-btn:hover {
            background-color: rgba(220, 53, 69, 1);
        }
        
        .download-btn {
            position: absolute;
            right: 10px;
            top: 50px;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background-color: rgba(0, 123, 255, 0.8);
            color: white;
            font-size: 16px;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .download-btn:hover {
            background-color: rgba(0, 123, 255, 1);
        }
        
        .glasses-item {
            transition: all 0.2s ease;
            cursor: pointer;
        }
        
        .glasses-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        
        .glasses-item.active {
            border: 2px solid #007bff;
        }
        
        .webcam-container {
            position: relative;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 3px 10px rgba(0,0,0,0.2);
        }
        
        .glasses-controls .card-header {
            padding: 5px 10px;
        }
        
        .tracking-toggle {
            background-color: rgba(255, 255, 255, 0.9);
            padding: 8px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .form-check-input {
            cursor: pointer;
        }
        
        .message-overlay {
            border-radius: 4px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
    `;
	document.head.appendChild(style);
})();