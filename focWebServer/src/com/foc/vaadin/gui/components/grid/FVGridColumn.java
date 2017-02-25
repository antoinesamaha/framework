package com.foc.vaadin.gui.components.grid;

public class FVGridColumn {
	private int width = 100;

	public FVGridColumn(int width){
		this.width = width;
	}
	
	public void dispose(){
		
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
