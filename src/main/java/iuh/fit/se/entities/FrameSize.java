package iuh.fit.se.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FrameSize {
	private Long id;
	private Double frameWidth;
    private Double lensWidth;
    private Double bridge;
    private Double templeLength;
    private Double lensHeight;
    private Double frameWeight;
    
//    private Glass glass;

	public FrameSize(Long id, Double frameWidth, Double lensWidth, Double bridge, Double templeLength,
			Double lensHeight, Double frameWeight) {
		super();
		this.id = id;
		this.frameWidth = frameWidth;
		this.lensWidth = lensWidth;
		this.bridge = bridge;
		this.templeLength = templeLength;
		this.lensHeight = lensHeight;
		this.frameWeight = frameWeight;
	}

	public FrameSize() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(Double frameWidth) {
		this.frameWidth = frameWidth;
	}

	public Double getLensWidth() {
		return lensWidth;
	}

	public void setLensWidth(Double lensWidth) {
		this.lensWidth = lensWidth;
	}

	public Double getBridge() {
		return bridge;
	}

	public void setBridge(Double bridge) {
		this.bridge = bridge;
	}

	public Double getTempleLength() {
		return templeLength;
	}

	public void setTempleLength(Double templeLength) {
		this.templeLength = templeLength;
	}

	public Double getLensHeight() {
		return lensHeight;
	}

	public void setLensHeight(Double lensHeight) {
		this.lensHeight = lensHeight;
	}

	public Double getFrameWeight() {
		return frameWeight;
	}

	public void setFrameWeight(Double frameWeight) {
		this.frameWeight = frameWeight;
	}

//	public Glass getGlass() {
//		return glass;
//	}
//
//	public void setGlass(Glass glass) {
//		this.glass = glass;
//	}

	@Override
	public String toString() {
		return "FrameSize [id=" + id + ", frameWidth=" + frameWidth + ", lensWidth=" + lensWidth + ", bridge=" + bridge
				+ ", templeLength=" + templeLength + ", lensHeight=" + lensHeight + ", frameWeight=" + frameWeight
				+ "]";
	}
    
    
}
