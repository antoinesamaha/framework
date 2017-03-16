package com.foc.desc.xml;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;

public class FocDescDeclaration_XMLBased_WFLog implements IFocDescDeclaration{
	private FocDesc transactionFocDesc = null ;
	private FocDesc focDesc            = null ;
	private boolean gettingFocDesc     = false;
	
	public FocDescDeclaration_XMLBased_WFLog(FocDesc transactionFocDesc){
		this.transactionFocDesc = transactionFocDesc;
	}
	
	public void dispose(){
		this.focDesc            = null;
		this.transactionFocDesc = null;
	}
	
	public boolean isGettingFocDesc(){
		return gettingFocDesc;
	}

	public void setGettingFocDesc(boolean getting){
		gettingFocDesc = getting;
	}
	
	public FocDesc getFocDescription() {
		if(focDesc == null && !isGettingFocDesc()){
			setGettingFocDesc(true);
			if(transactionFocDesc != null){
				focDesc = new WFLogDesc((IWorkflowDesc) transactionFocDesc);
				Globals.getApp().putIFocDescDeclaration(focDesc.getName(), this);
			}
			setGettingFocDesc(false);
		}
		return focDesc;
	}
	
	public int getPriority() {
		return IFocDescDeclaration.PRIORITY_SECOND;
	}

	@Override
	public FocModule getFocModule() {
		return transactionFocDesc != null ? transactionFocDesc.getModule() : null;
	}

	@Override
	public String getName() {
		return (getFocDescription() != null) ? getFocDescription().getStorageName() : null;
	}
}
