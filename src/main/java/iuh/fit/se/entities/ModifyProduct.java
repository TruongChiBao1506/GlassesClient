package iuh.fit.se.entities;

import java.util.Date;

public class ModifyProduct {
	private ModifyProductId modifyProductId;
	private User ModifiedBy;
	private Glass glass;
	private Date createdDate;
	private Date updatedDate;
	
	public ModifyProduct(ModifyProductId modifyProductId, User modifiedBy, Glass glass, Date createdDate,
			Date updatedDate) {
		super();
		this.modifyProductId = modifyProductId;
		ModifiedBy = modifiedBy;
		this.glass = glass;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	public ModifyProduct() {
		super();
	}
	
	public ModifyProductId getModifyProductId() {
		return modifyProductId;
	}
	public void setModifyProductId(ModifyProductId modifyProductId) {
		this.modifyProductId = modifyProductId;
	}
	public User getModifiedBy() {
		return ModifiedBy;
	}
	public void setModifiedBy(User modifiedBy) {
		ModifiedBy = modifiedBy;
	}
	public Glass getGlass() {
		return glass;
	}
	public void setGlass(Glass glass) {
		this.glass = glass;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	@Override
	public String toString() {
		return "ModifyProduct [modifyProductId=" + modifyProductId + ", ModifiedBy=" + ModifiedBy + ", glass=" + glass
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}

	
	
}
