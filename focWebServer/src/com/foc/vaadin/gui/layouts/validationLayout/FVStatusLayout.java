package com.foc.vaadin.gui.layouts.validationLayout;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.Globals;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.status.StatusHolderDesc;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.map.WFTransactionConfig;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVGearWrapper;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.windows.optionWindow.IOption;
import com.foc.vaadin.gui.windows.optionWindow.OptionDialogWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WFTransactionWrapper_Form;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.modules.workflow.Workflow_Cancel_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVStatusLayout extends FVGearWrapper<FVLabel>{

	private FocXMLLayout xmlLayout        = null;
	private FocObject    focObject        = null;
	private FVLabel      statusLabel      = null;
	private PopupLinkButton resetStatusButton= null;
	private PopupLinkButton approveButton = null;
	private PopupLinkButton cancelButton  = null;
	private PopupLinkButton closeButton   = null;
	private VerticalLayout           root = null;
	private OptionDialogWindow approveOptionDialogWindow = null;
	
	
  public FVStatusLayout(FocXMLLayout xmlLayout, FocObject focObject){
		setSpacing(true);
		setFocObject(focObject);
		this.xmlLayout = xmlLayout;
		addStyleName("border");
		addStyleName("noPrint");
		setCaption("Status");
	}
	
	public void dispose(){
		super.dispose();
		if(statusLabel != null){
			statusLabel.dispose();
			statusLabel = null;
		}
		if(resetStatusButton != null){
			resetStatusButton.dispose();
			resetStatusButton = null;
		}
		if(approveButton != null){
			approveButton.dispose();
			approveButton = null;
		}
		if(cancelButton != null){
			cancelButton.dispose();
			cancelButton = null;
		}
		if(closeButton != null){
			closeButton.dispose();
			closeButton = null;
		}
		if(approveOptionDialogWindow != null){
			approveOptionDialogWindow.dispose();
			approveOptionDialogWindow = null;
		}
		focObject = null;
		xmlLayout = null;
		root = null;
	}
	
	@Override
	public void fillMenu(VerticalLayout root) {
	  setRootVerticalLayout(root);
	  FProperty property = (FProperty) getFocObject().iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
	  int status = property.getInteger();

	  WFTransactionConfig transConfig = getFocObject().workflow_getTransactionConfig();
	  boolean hasMapForSignatures = transConfig != null ? transConfig.isApproveByMapSignature() : false;
	  
	  if(status == StatusHolderDesc.STATUS_PROPOSAL){
	  	if(getFocObject().workflow_IsAllowApprove() /*&& !hasMapForSignatures*/){
	  		root.addComponent(getApproveButton(true));
	  	}
	  	if(getFocObject().workflow_IsAllowCancel()){
	  		root.addComponent(getCancelButton(true));
	  	}
	  }else if(status == StatusHolderDesc.STATUS_APPROVED){
	  	if(getFocObject().workflow_IsAllowCancel()){
	  		root.addComponent(getCancelButton(true));
	  	}
	  	if(getFocObject().workflow_IsAllowClose()){
	  		root.addComponent(getCloseButton(true));
	  	}
	  }
	  
	  if(FocWebApplication.getFocUser().getGroup().allowStatusManualModif()){
	    if(status != StatusHolderDesc.STATUS_PROPOSAL) root.addComponent(getResetStatusButton(true));
	  }
	}
		
  public IStatusHolder getIStatusHolder(){
    return (IStatusHolder)focObject;
  }
  
  public StatusHolder getStatusHolder(){
    return getIStatusHolder().getStatusHolder();
  }
  
  private void setFocObject(FocObject focObject){
    this.focObject = focObject;
    if(focObject != null){
			FProperty property = (FProperty) focObject.iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
			if(property != null){
				statusLabel = new FVLabel(property.getString());
				statusLabel.addStyleName("noPrint");
				setComponent(statusLabel);
			}
    }
  }
  
  private FocObject getFocObject(){
    return focObject;
  }
  
  public void refreshStatusLabel(){
  	VerticalLayout root = getRootVerticalLayout();
    if(statusLabel != null && root != null){
      removeAllComponents();
      root.removeAllComponents();
      setFocObject(getFocObject());
      fillMenu(root);
    }
  }

  public PopupLinkButton getApproveButton() {
    return getApproveButton(false);
  }
  
  public PopupLinkButton getApproveButton(boolean create) {
  	if(approveButton == null && create){
		  approveButton = new PopupLinkButton("Approve", new ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		      approveOptionDialogWindow = new OptionDialogWindow("Approve this transaction", null);
		      approveOptionDialogWindow.setWidth("300px");
		      approveOptionDialogWindow.setHeight("200px");
		      
		      approveOptionDialogWindow.addOption("Yes, approve", new IOption() {
		        @Override
		        public void optionSelected(Object contextObject) {
		          getStatusHolder().setStatusToValidated();
		          getFocObject().validate(true);
		          xmlLayout.copyMemoryToGui();
		          xmlLayout.getValidationLayout().commit();
		          refreshStatusLabel();
		        }
		      });
		      approveOptionDialogWindow.addOption("Cancel", new IOption() {
		        @Override
		        public void optionSelected(Object contextObject) {
		          
		        }
		      });
		      
		      getUI().addWindow(approveOptionDialogWindow);
		    }
		  });
  	}
  	return approveButton;
  }

  public PopupLinkButton getResetStatusButton(boolean create) {
  	if(resetStatusButton == null && create){
  		resetStatusButton = new PopupLinkButton("Reset to proposal", new ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		      approveOptionDialogWindow = new OptionDialogWindow("Reset this transaction to proposal", null);
		      approveOptionDialogWindow.setWidth("300px");
		      approveOptionDialogWindow.setHeight("200px");
		      
		      approveOptionDialogWindow.addOption("Yes, reset", new IOption() {
		        @Override
		        public void optionSelected(Object contextObject) {
		          getStatusHolder().resetStatusToProposal();
		          getFocObject().validate(true);
		          xmlLayout.getValidationLayout().commit();
		          refreshStatusLabel();
		        }
		      });
		      approveOptionDialogWindow.addOption("Cancel", new IOption() {
		        @Override
		        public void optionSelected(Object contextObject) {
		          
		        }
		      });
		      getUI().addWindow(approveOptionDialogWindow);
		    }
		  });
  	}
  	return resetStatusButton;
  }

  public OptionDialogWindow getApproveOptionDialogWindow() {
    return approveOptionDialogWindow;
  }
  
  private PopupLinkButton getCloseButton(boolean create){
  	if(closeButton == null && create){
	    closeButton = new PopupLinkButton("Close", new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	        OptionDialogWindow optionWindow = new OptionDialogWindow("Close this transaction", null);
	        optionWindow.setWidth("200px");
	        optionWindow.setHeight("200px");
	        
	        optionWindow.addOption("Yes close", new IOption() {
	          @Override
	          public void optionSelected(Object contextObject) {
	            getStatusHolder().setStatus(StatusHolderDesc.STATUS_CLOSED);
	            getStatusHolder().setClosureDate(Globals.getDBManager().getCurrentTimeStamp_AsTime());
	            xmlLayout.getValidationLayout().commit();
	            refreshStatusLabel();
	          }
	        });
	        optionWindow.addOption("No", new IOption() {
	          @Override
	          public void optionSelected(Object contextObject) {
	            
	          }
	        });
	        getUI().addWindow(optionWindow);
	      }
	    });
  	}
    return closeButton;
  }
  
  public PopupLinkButton getCancelButton() {
    return getCancelButton(false);
  }

  private PopupLinkButton getCancelButton(boolean create){
  	if(cancelButton == null && create){
	    cancelButton = new PopupLinkButton("Cancel", new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	      	popupCancel(getWindow(), getFocObject(), FVStatusLayout.this, null);
	      }
	    });
  	}
    return cancelButton;
  }

  public static void popupCancel(FocCentralPanel mainWindow, FocObject focObject, FVStatusLayout statusLayout, WFTransactionWrapper_Form transactionWrapperForm){
		if(focObject != null && focObject.getThisFocDesc() instanceof IWorkflowDesc){
			
			IWorkflowDesc iworkflowDesc= (IWorkflowDesc) focObject.getThisFocDesc();
			IWorkflow iworkflow = (IWorkflow) focObject;
	  	
	  	XMLViewKey xmlKey = new XMLViewKey("IWorkflow", XMLViewKey.TYPE_FORM, WorkflowWebModule.CTXT_CANCEL_TRANSACTION, XMLViewKey.VIEW_DEFAULT);
			Workflow_Cancel_Form centralPanel = (Workflow_Cancel_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(mainWindow, xmlKey, (IFocData) iworkflow);
//			centralPanel.setStatusLayout(statusLayout);
//			centralPanel.setFocXMLLayout(xmlL);
			centralPanel.parseXMLAndBuildGui();
			
			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);
			
			Window window = centralWindow.newWrapperWindow();
//			window.setWidth("500px");
//			window.setHeight("300px");
			window.setPositionX(200);
			window.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(window);
		}
  }

  public void cancelTransaction(){
//  	if(getStatusHolder() != null){
//  		getStatusHolder().setStatusToCanceled(comment);
//  	}
//    if(getFocObject() != null){
//    	getFocObject().validate(true);
//    }
    if(xmlLayout != null && xmlLayout.getValidationLayout() != null){
    	xmlLayout.getValidationLayout().commit();
    }
    refreshStatusLabel();
  }
  
  private void setRootVerticalLayout(VerticalLayout root){
    this.root = root;
  }
  
  public VerticalLayout getRootVerticalLayout(){
    return root;
  }
}