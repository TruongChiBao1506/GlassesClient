package iuh.fit.se.entities;

public class OrderItemId {
	 private Long order;
	 
	 private Long product;

	public OrderItemId(Long order, Long product) {
		super();
		this.order = order;
		this.product = product;
	}

	public OrderItemId() {
		super();
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getProduct() {
		return product;
	}

	public void setProduct(Long product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "OrderItemId [order=" + order + ", product=" + product + "]";
	}
	 
	 
}
