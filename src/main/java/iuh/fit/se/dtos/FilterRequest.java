package iuh.fit.se.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterRequest {
	private String minPrice;
	private String maxPrice;
	private String brands;
	private String shapes;
	private String materials;
	private String colors;
	public FilterRequest(String minPrice, String maxPrice, String brands, String shapes,
			String materials, String colors) {
		super();
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.brands = brands;
		this.shapes = shapes;
		this.materials = materials;
		this.colors = colors;
	}
	public FilterRequest() {
		super();
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	public String getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
	public String getShapes() {
		return shapes;
	}
	public void setShapes(String shapes) {
		this.shapes = shapes;
	}
	public String getMaterials() {
		return materials;
	}
	public void setMaterials(String materials) {
		this.materials = materials;
	}
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
	@Override
	public String toString() {
		return "FilterRequest [minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", brands=" + brands + ", shapes="
				+ shapes + ", materials=" + materials + ", colors=" + colors + "]";
	}
	
	
}
