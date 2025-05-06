package iuh.fit.se.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specifications {
	private Long id;
    private String pdRange;
    private String prescriptionRange;
    private Boolean availableAsProgressiveBifocal;
    private Boolean readers;
    private String frameSizeDescription;
    private String rim;
    private String shape;
    private String material;
    private String feature;
    
//    private Glass glass;

	public Specifications(Long id, String pdRange, String prescriptionRange, Boolean availableAsProgressiveBifocal,
			Boolean readers, String frameSizeDescription, String rim, String shape, String material, String feature) {
		super();
		this.id = id;
		this.pdRange = pdRange;
		this.prescriptionRange = prescriptionRange;
		this.availableAsProgressiveBifocal = availableAsProgressiveBifocal;
		this.readers = readers;
		this.frameSizeDescription = frameSizeDescription;
		this.rim = rim;
		this.shape = shape;
		this.material = material;
		this.feature = feature;
	}

	public Specifications() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPdRange() {
		return pdRange;
	}

	public void setPdRange(String pdRange) {
		this.pdRange = pdRange;
	}

	public String getPrescriptionRange() {
		return prescriptionRange;
	}

	public void setPrescriptionRange(String prescriptionRange) {
		this.prescriptionRange = prescriptionRange;
	}

	public Boolean getAvailableAsProgressiveBifocal() {
		return availableAsProgressiveBifocal;
	}

	public void setAvailableAsProgressiveBifocal(Boolean availableAsProgressiveBifocal) {
		this.availableAsProgressiveBifocal = availableAsProgressiveBifocal;
	}

	public Boolean getReaders() {
		return readers;
	}

	public void setReaders(Boolean readers) {
		this.readers = readers;
	}

	public String getFrameSizeDescription() {
		return frameSizeDescription;
	}

	public void setFrameSizeDescription(String frameSizeDescription) {
		this.frameSizeDescription = frameSizeDescription;
	}

	public String getRim() {
		return rim;
	}

	public void setRim(String rim) {
		this.rim = rim;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
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
		return "Specifications [id=" + id + ", pdRange=" + pdRange + ", prescriptionRange=" + prescriptionRange
				+ ", availableAsProgressiveBifocal=" + availableAsProgressiveBifocal + ", readers=" + readers
				+ ", frameSizeDescription=" + frameSizeDescription + ", rim=" + rim + ", shape=" + shape + ", material="
				+ material + ", feature=" + feature + "]";
	}
    
    
}
