package iuh.fit.se.entities;

public class ModifyProductId {
	private Long modifiedBy;
	
	private Long glass;
	
	public ModifyProductId(Long modifiedBy, Long glass) {
		super();
		this.modifiedBy = modifiedBy;
		this.glass = glass;
	}

	public ModifyProductId() {
		super();
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getGlass() {
		return glass;
	}

	public void setGlass(Long glass) {
		this.glass = glass;
	}

	@Override
	public String toString() {
		return "ModifyProductId [modifiedBy=" + modifiedBy + ", glass=" + glass + "]";
	}
	
}
