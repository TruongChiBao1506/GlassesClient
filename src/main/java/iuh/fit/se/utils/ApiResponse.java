package iuh.fit.se.utils;

import java.util.Map;

public class ApiResponse<T> {

	private int status;

	private Map<String, Object> errors = null;

	private T data;

	private String message;

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(int status,Map<String, Object> errors,
			T data,String message) {
		super();
		this.status = status;
		this.errors = errors;
		this.data = data;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, Object> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, Object> errors) {
		this.errors = errors;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiResponse [status=" + status + ", errors=" + errors + ", data=" + data + ", message=" + message + "]";
	}


}
