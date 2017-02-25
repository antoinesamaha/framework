package com.foc.vaadin.gui.layouts.validationLayout;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVGearWrapper;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVCommentLayout extends FVGearWrapper<FVTextField>{

	private FocXMLLayout     xmlLayout            = null;
	private FocObject        focObject            = null;
	private FVLabel          statusLabel          = null;
	private PopupLinkButton  seeAllCommentsButton = null;
	
  public FVCommentLayout(FocXMLLayout xmlLayout, FVLayout containerLayout, FocObject focObject){
  	this.xmlLayout = xmlLayout;  	
		setSpacing(true);
		setFocObject(containerLayout, focObject);
//		addStyleName("border");
		addStyleName("noPrint");
//		setCaption("Status");
	}
	
	public void dispose(){
		super.dispose();
		focObject = null;
		xmlLayout = null;
		if(statusLabel != null){
			statusLabel.dispose();
			statusLabel = null;
		}
		if(seeAllCommentsButton != null){
			seeAllCommentsButton.dispose();
			seeAllCommentsButton = null;
		}
	}
	
  private void setFocObject(FVLayout containerLayout, FocObject focObject){
    this.focObject = focObject;
    if(focObject != null && xmlLayout!= null){
    	FVTextField txtFld = (FVTextField) xmlLayout.newGuiField(containerLayout, "TRANSACTION.WF_COMMENT", "TRANSACTION.WF_COMMENT", new FocXMLAttributes());
    	txtFld.setCaption("My Comment");
//			FVTextField txtFld = new FVTextField("Comment");
			txtFld.addStyleName("noPrint");			
//			txtFld.setValue("-- Under development --");
			txtFld.setWidth("100%");
			setComponent(txtFld);
    	
//			FProperty property = (FProperty) focObject.iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
//			if(property != null){
//				statusLabel = new FVLabel(property.getString());
//				statusLabel.addStyleName("noPrint");
//				setComponent(statusLabel);
//			}
    }
  }
  
  private FocObject getFocObject(){
    return focObject;
  }
  
  public PopupLinkButton getViewCommentsButton() {
    return getViewCommentsButton(false);
  }

  private PopupLinkButton getViewCommentsButton(boolean create){
  	if(seeAllCommentsButton == null && create){
  		seeAllCommentsButton = new PopupLinkButton("View All Comments", new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	      	if(xmlLayout != null && getFocObject() != null && getFocObject() instanceof IWorkflow){
	      		IWorkflow iworkflow = (IWorkflow) getFocObject();
	      		FocList focList = iworkflow.iWorkflow_getWorkflow().getLogList();
		        XMLViewKey xmlViewKey = new XMLViewKey(WFLogDesc.WF_LOG_VIEW_KEY, XMLViewKey.TYPE_TABLE,  WorkflowWebModule.CTXT_WF_COMMENT_LOG, XMLViewKey.VIEW_DEFAULT);
		        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(xmlLayout.getMainWindow(), xmlViewKey, focList);
		        xmlLayout.getMainWindow().changeCentralPanelContent(centralPanel, true);
	      	}
	      	/*
	      	if(getFocObject() != null && getFocObject().getThisFocDesc() instanceof IWorkflowDesc){
	      		
	      		IWorkflowDesc iworkflowDesc= (IWorkflowDesc) getFocObject().getThisFocDesc();
	      		IWorkflow iworkflow = (IWorkflow) getFocObject();
		      	
		      	XMLViewKey xmlKey = new XMLViewKey("IWorkflow", XMLViewKey.TYPE_FORM, WorkflowWebModule.CTXT_CANCEL_TRANSACTION, XMLViewKey.VIEW_DEFAULT);
						WFLog_PopupWindow_Form centralPanel = (WFLog_PopupWindow_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(getWindow(), xmlKey, (IFocData) iworkflow);
						centralPanel.setStatusLayout(FVCommentLayout.this);
						centralPanel.parseXMLAndBuildGui();
						
						FocCentralPanel centralWindow = new FocCentralPanel();
						centralWindow.fill();
						centralWindow.changeCentralPanelContent(centralPanel, false);
						
						Window window = centralWindow.newWrapperWindow();
						window.setWidth("400px");
						window.setHeight("300px");
						window.setPositionX(300);
						window.setPositionY(100);
						FocWebApplication.getInstanceForThread().addWindow(window);
	      	}
	      	*/
	      }
	    });
  	}
    return seeAllCommentsButton;
  }

	@Override
	public void fillMenu(VerticalLayout root) {
		root.addComponent(getViewCommentsButton(true));		
	}
}