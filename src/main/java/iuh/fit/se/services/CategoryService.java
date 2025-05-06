package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.entities.Category;
import iuh.fit.se.utils.ApiResponse;

public interface CategoryService {
	public ApiResponse<Category> findById(Long id);
	
	public ApiResponse<List<Category>> findAll();
}
