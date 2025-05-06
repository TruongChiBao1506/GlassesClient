package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.entities.Specifications;
import iuh.fit.se.utils.ApiResponse;

public interface SpecificationsService {
	public ApiResponse findById(Long id);
	public ApiResponse<List<Specifications>> findAll();
}
