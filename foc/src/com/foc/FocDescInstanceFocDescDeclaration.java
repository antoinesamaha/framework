package com.foc;

import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public class FocDescInstanceFocDescDeclaration implements IFocDescDeclaration{
	
	private FocDesc   focDescription = null;
	private FocModule module         = null; 
	
	public FocDescInstanceFocDescDeclaration(FocDesc focDescription){
		this.focDescription = focDescription ;
	}
	
	public FocDesc getFocDescription() {
		return focDescription;
	}

	public int getPriority() {
		return 0;
	}

	public void setFocModule(FocModule module) {
		this.module = module;
	}
	
	@Override
	public FocModule getFocModule() {
		return module;
	}
	
	@Override
	public String getName() {
		return (getFocDescription() != null) ? getFocDescription().getStorageName() : null;
	}
}
