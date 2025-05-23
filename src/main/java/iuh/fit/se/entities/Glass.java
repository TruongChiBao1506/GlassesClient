package iuh.fit.se.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Glass {
	private Long id;
	private String name;
	private String brand;
	private Double price;
    private String colorName;
    private String colorCode;
    private String imageFrontUrl;
    private String imageSideUrl;
	private boolean gender;
	private int stock;
	private String description;
	
	private Specifications specifications;
	private FrameSize frameSize;
	private Category category;
	
	private List<ModifyProduct> modifyProducts;
	private List<OrderItem> orderItems;
	private List<Review> reviews;
	

	public Glass(Long id, String name, String brand, Double price, String colorName, String colorCode,
			String imageFrontUrl, String imageSideUrl, boolean gender, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.colorName = colorName;
		this.colorCode = colorCode;
		this.imageFrontUrl = imageFrontUrl;
		this.imageSideUrl = imageSideUrl;
		this.gender = gender;
		this.stock = stock;
	}
	public Glass() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getImageFrontUrl() {
		return imageFrontUrl;
	}
	public void setImageFrontUrl(String imageFrontUrl) {
		this.imageFrontUrl = imageFrontUrl;
	}
	public String getImageSideUrl() {
		return imageSideUrl;
	}
	public void setImageSideUrl(String imageSideUrl) {
		this.imageSideUrl = imageSideUrl;
	}
	public Specifications getSpecifications() {
		return specifications;
	}
	public void setSpecifications(Specifications specifications) {
		this.specifications = specifications;
	}
	public FrameSize getFrameSize() {
		return frameSize;
	}
	public void setFrameSize(FrameSize frameSize) {
		this.frameSize = frameSize;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ModifyProduct> getModifyProducts() {
		return modifyProducts;
	}
	public void setModifyProducts(List<ModifyProduct> modifyProducts) {
		this.modifyProducts = modifyProducts;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	@Override
	public String toString() {
		return "Glass [id=" + id + ", name=" + name + ", brand=" + brand + ", price=" + price + ", colorName="
				+ colorName + ", colorCode=" + colorCode + ", imageFrontUrl=" + imageFrontUrl + ", imageSideUrl="
				+ imageSideUrl + ", gender=" + gender + ", stock=" + stock + ", description=" + description
				+ ", specifications=" + specifications + ", frameSize=" + frameSize + ", category=" + category + "]";
	}
	
	
	
	
}
