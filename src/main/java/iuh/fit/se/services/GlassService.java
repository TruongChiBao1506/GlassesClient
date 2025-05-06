package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.dtos.FilterRequest;
import iuh.fit.se.dtos.GlassDTO;
import iuh.fit.se.entities.Glass;
import iuh.fit.se.utils.ApiResponse;

public interface GlassService {
	
	public ApiResponse<List<Glass>> findAll();
	
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
	
	public ApiResponse<List<GlassDTO>> findByCategoryEyeGlassMenFilter(FilterRequest filter);
	
	public ApiResponse<List<GlassDTO>> findByCategoryEyeGlassWomenFilter(FilterRequest filter);
	
	public ApiResponse<List<GlassDTO>> findByCategorySunGlassMenFilter(FilterRequest filter);
	
	public ApiResponse<List<GlassDTO>> findByCategorySunGlassWomenFilter(FilterRequest filter);
	
	public ApiResponse<List<GlassDTO>> search(String keyword);
	
	// Thêm mới kính
    public ApiResponse<Glass> createGlass(Glass glass);

    // Cập nhật kính
    public ApiResponse<Glass> updateGlass(Long id, Glass glass);

    // Xóa kính theo ID
    public ApiResponse<String> deleteGlass(Long id);

    // Tìm kính theo từ khóa (tên)
    public ApiResponse<List<Glass>> searchByName(String keyword);
}
