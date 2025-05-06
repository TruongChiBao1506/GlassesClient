package iuh.fit.se.services;

import java.util.List;

import iuh.fit.se.entities.FrameSize;
import iuh.fit.se.utils.ApiResponse;

public interface FrameSizeService {
	public ApiResponse  findById(Long id);
	public ApiResponse<List<FrameSize>> findAll();
}	
