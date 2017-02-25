package com.foc.plugin;

import com.foc.desc.FocDesc;

public abstract class AbstractFocDescPlugIn implements IFocDescPlugIn {

	private FocDesc focDesc = null; 
	
	public FocDesc getFocDesc(){
		return focDesc;
	}

	public void setFocDesc(FocDesc focDesc){
		this.focDesc = focDesc;
	}

	@Override
	public void dispose() {
		focDesc = null;
	}

	@Override
	public void afterConstruction(FocDesc focDesc){
	}
}
