package iuh.fit.se.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.entities.FrameSize;
import iuh.fit.se.services.FrameSizeService;
import iuh.fit.se.utils.ApiResponse;

@Service
public class FrameSizeServiceImpl implements FrameSizeService{
	private RestClient restClient;
	private ObjectMapper objectMapper;
	private static final String ENDPOINT = "http://54.254.82.176:8080/api";
	public FrameSizeServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
		super();
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	@Override
	public ApiResponse findById(Long id) {
		 return restClient.get().uri(ENDPOINT + "/FrameSize/FrameSizes" + id).accept(MediaType.APPLICATION_JSON)
					.exchange((request, response) -> {
						ApiResponse<FrameSize> apiResponse = null;
						try (InputStream is = response.getBody()){
							apiResponse = objectMapper.readValue(is, ApiResponse.class);
							apiResponse.setData(objectMapper.convertValue(apiResponse.getData(), FrameSize.class));
						} catch (IOException e) {
							// TODO: handle exception
							System.err.println("Lỗi đọc response: " + e.getMessage());
						    apiResponse = new ApiResponse<>();
						    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
						}
						return apiResponse;
					});
	}
	@Override
	public ApiResponse<List<FrameSize>> findAll() {
		 return restClient.get()
		            .uri(ENDPOINT + "/FrameSize/FrameSizes") // Endpoint lấy danh sách tất cả sản phẩm kính
		            .accept(MediaType.APPLICATION_JSON)
		            .exchange((request, response) -> {
		                ApiResponse<List<FrameSize>> apiResponse = null;
		                try (InputStream is = response.getBody()){
		                	  // Đọc dữ liệu từ phản hồi và chuyển thành ApiResponse
		                    apiResponse = objectMapper.readValue(is, ApiResponse.class);
		                    apiResponse.setData(
		                            objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<FrameSize>>() {
		                            }));
						} catch (IOException e) {
							// TODO: handle exception
							System.err.println("Lỗi đọc response: " + e.getMessage());
						    apiResponse = new ApiResponse<>();
						    apiResponse.setMessage("Không thể phân tích phản hồi từ server");
						}
		                return apiResponse;
		            });
	}
}
