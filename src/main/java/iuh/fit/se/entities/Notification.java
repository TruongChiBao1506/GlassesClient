package iuh.fit.se.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
	private Long id;
	private String message;
	private Order order;
	private LocalDateTime createdAt;
	private boolean isRead = false;
	
	public Notification(Long id, String message, Order order, LocalDateTime createdAt, boolean isRead) {
		super();
		this.id = id;
		this.message = message;
		this.order = order;
		this.createdAt = createdAt;
		this.isRead = isRead;
	}

	public Notification() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", message=" + message + ", order=" + order + ", createdAt=" + createdAt
				+ ", isRead=" + isRead + "]";
	}
	
}
