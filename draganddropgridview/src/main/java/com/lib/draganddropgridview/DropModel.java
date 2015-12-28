package com.lib.draganddropgridview;

public class DropModel extends Object{
	
	private boolean isItemClick = false;
	private boolean isDrop = false;

	public boolean isItemClick() {
		return isItemClick;
	}
	public void setItemClick(boolean isItemClick) {
		this.isItemClick = isItemClick;
	}
	public boolean isDrop() {
		return isDrop;
	}
	public void setDrop(boolean isDrop) {
		this.isDrop = isDrop;
	}
	
}
