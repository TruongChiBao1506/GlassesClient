package iuh.fit.se.services;

import java.util.List;
import java.util.Map;

import iuh.fit.se.dtos.FilterRequest;
import iuh.fit.se.dtos.GlassDTO;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.utils.ApiResponse;

public interface GlassService {
	
	public ApiResponse<List<Glass>> findAll();
	
	// Lấy tất cả sản phẩm kính có phân trang
	public ApiResponse<Map<String, Object>> findAllPaginated(int page, int size);
	
	public ApiResponse<Glass> findById(Long id);
	
	public ApiResponse<List<Glass>> findByCategoryEyeGlass();
	
	public ApiResponse<List<Glass>> findByCategoryEyeGlassMen();
	
	public ApiResponse<List<Glass>> findByCategoryEyeGlassWomen();
	
	public ApiResponse<List<Glass>> findByCategorySunGlass();
	
	public ApiResponse<List<Glass>> findByCategorySunGlassMen();
	
	public ApiResponse<List<Glass>> findByCategorySunGlassWomen();
	
	public ApiResponse<List<String>> getAllBrand();
	
	public ApiResponse<List<String>> getAllShape();
	
	public ApiResponse<List<String>> getAllMaterial();
	
	public ApiResponse<List<String>> getAllColor();
	
	public ApiResponse<Map<String, Object>> findByCategoryEyeGlassMenFilter(FilterRequest filter, int page, int size);
	
	public ApiResponse<Map<String, Object>> findByCategoryEyeGlassWomenFilter(FilterRequest filter, int page, int size);
	
	public ApiResponse<Map<String, Object>> findByCategorySunGlassMenFilter(FilterRequest filter, int page, int size);
	
	public ApiResponse<Map<String, Object>> findByCategorySunGlassWomenFilter(FilterRequest filter, int page, int size);
	
	public ApiResponse<Map<String, Object>> findByCategoryEyeGlassFilter(FilterRequest filter, int page, int size);
	
	public ApiResponse<Map<String, Object>> findByCategorySunGlassFilter(FilterRequest filter, int page, int size);
	
		public ApiResponse<List<GlassDTO>> search(String keyword);
	
	// Tìm kiếm với phân trang
	public ApiResponse<Map<String, Object>> searchWithPagination(String keyword, int page, int size);
	
	// Thêm mới kính
    public ApiResponse<Glass> createGlass(Glass glass);

    // Cập nhật kính
    public ApiResponse<Glass> updateGlass(Long id, Glass glass);

    // Xóa kính theo ID
    public ApiResponse<String> deleteGlass(Long id);    // Tìm kính theo từ khóa (tên)
    public ApiResponse<List<Glass>> searchByName(String keyword);
    
    // Tìm kính theo từ khóa (tên) có phân trang
    public ApiResponse<Map<String, Object>> searchByNamePaginated(String keyword, int page, int size);
}
