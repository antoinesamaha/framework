package com.foc.web.modules.workflow;

import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.desc.FocObject;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;

@SuppressWarnings("serial")
public class WFTransactionWrapper_Comment_Standard_Form extends FocXMLLayout {
	
	private boolean sign = false;//isSigning otherwise would be rejecting all previous signatures
	
	public WFTransactionWrapper getTransactionWrapper() {
		return (WFTransactionWrapper) getFocData();
	}

//	public Workflow getWorkflow() {
//		return getWFTransactionWrapper() != null ? getWFTransactionWrapper().iWorkflow_getWorkflow() : null;
//	}
//	
//	public StatusHolder getStatusHolder() {
//		return getWFTransactionWrapper() != null ? getWFTransactionWrapper().getStatusHolder() : null;
//	}

	public String getWrittenComment() {
		String writtenComment = "";
		FVTextArea textArea = (FVTextArea) getComponentByName("MY_COMMENT");
		if(textArea != null) {
			writtenComment = textArea.getValue();
		}
		return writtenComment;
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		super.showValidationLayout(showBackButton, position);
		FVValidationLayout vLay = getValidationLayout();
		if(vLay != null) {
			vLay.setExitWithoutPrompt(true);
			vLay.addValidationListener(new IValidationListener() {
				
				@Override
				public void validationDiscard(FVValidationLayout validationLayout) {
				}
				
				@Override
				public boolean validationCommit(FVValidationLayout validationLayout) {
					return false;
				}
				
				@Override
				public boolean validationCheckData(FVValidationLayout validationLayout) {
					boolean error = false;
					return error;
				}
				
				@Override
				public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
					if(getMainWindow() != null && commited) {
						WFTransactionWrapper wrapper = getTransactionWrapper();
						if(wrapper != null) {
							//If Cancel Mode we popup the comment dialog 
							if(!isSign()){
								wrapper.undoAllSignatures(getWrittenComment());
							}else if(isSign()){
								FocObject focObject = wrapper.getFocObject();
								if(focObject != null) {
									focObject.workflow_SignIfAllowed(getWrittenComment());
								}
							}
							if(getMainWindow() != null && getMainWindow() instanceof FocWebVaadinWindow) {
								((FocWebVaadinWindow)getMainWindow()).refreshPendingSignatureCount();
							}
						}
						
						//If from dash board there is no console and we don't need to goBack() 	
						getMainWindow().refreshCentralPanelAndRightPanel();
					}
				}
			});
		}
	}

	public boolean isSign() {
		return sign;
	}
	
	public void setIsSigning(boolean sign) {
		this.sign = sign;
	}

}
