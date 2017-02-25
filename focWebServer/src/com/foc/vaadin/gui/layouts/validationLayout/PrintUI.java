package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.Globals;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.JavaScript;

@SuppressWarnings("serial")
public class PrintUI extends FocWebApplication {

		@Override
  	public void init(VaadinRequest request){
  		super.init(request);
  		
  		FocCentralPanel focCentralPanel = (FocCentralPanel)getNavigationWindow();

  		focCentralPanel.setWidth(focCentralPanel.getPreferredWidth());
  		focCentralPanel.getCentralPanelWrapper().setWidth(focCentralPanel.getPreferredWidth());
  		
  		//ATTENTION
  		JavaScript.getCurrent().execute("setTimeout(function() {  window.print(); self.close();}, 2000);");
//	  		JavaScript.getCurrent().execute("setTimeout(function() {  window.print(); }, 2000);");
  	}
  	
	  public boolean isPrintUI(){
	  	return true;
	  }		
		
		@Override
		public FocCentralPanel newWindow() {
			FocCentralPanel focCentralPanel = new FocCentralPanel();
			focCentralPanel.fill();

    	XMLViewKey xmlViewKey = FocWebApplication.getFocWebSession_Static().getViewKeyToPrint();
      if(xmlViewKey != null){
      	String userPrintingViewName = null;

      	userPrintingViewName = XMLViewDictionary.getInstance().getUserPrintingView_PriorityTo_Key_UserSelection_Printing(xmlViewKey);
      	/*
      	if(xmlViewKey.isPrinterFriendly()){
      		userPrintingViewName = xmlViewKey.getUserView(); 
      	}else{
	      	//First we try to see if the user has already selected a default printing view
		      UserXMLView userXMLView = XMLViewDictionary.getInstance().userViewSelected_findViewForUserAndKey(FocWebApplication.getFocUser(), xmlViewKey);
		      
		      if(userXMLView != null){
		      	userPrintingViewName = userXMLView.getPrintingView();
		      }
		      
	      	//Second we try to see if the context has a VIEW_PRINTING from the system
		      if(userPrintingViewName == null || userPrintingViewName.isEmpty()){
		      	userPrintingViewName = XMLViewKey.VIEW_PRINTING;
		      }
      	}
      	*/
	      
	      if(userPrintingViewName != null){
	      	XMLViewKey printKey = new XMLViewKey(xmlViewKey);
	      	printKey.setUserView(userPrintingViewName);

	      	//Init the CentralPanel but do not parse XML because we want to copy some data first
	      	ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(focCentralPanel, printKey, FocWebApplication.getFocWebSession_Static().getDataToPrint(), false, false, true);
	      	if(FocWebApplication.getFocWebSession_Static().getDataDictionaly_ToPrint() != null && centralPanel instanceof FocXMLLayout){
	      		((FocXMLLayout)centralPanel).getFocDataDictionary(true).copy(FocWebApplication.getFocWebSession_Static().getDataDictionaly_ToPrint());
	      		FocWebApplication.getFocWebSession_Static().getDataDictionaly_ToPrint().dispose();
	      	}
//	      	((FocXMLLayout) centralPanel).setDataToPrintingLayout((FocXMLLayout) FocWebApplication.getFocWebSession_Static().getCentralPanelForPrinting());
	      	centralPanel.parseXMLAndBuildGui();
	      	if(centralPanel != null){
		    		((FocXMLLayout)centralPanel).setValidationLayoutVisible(false);
		    		((FocXMLLayout)centralPanel).scanComponentsAndExpandAllTrees();
		      	focCentralPanel.changeCentralPanelContent(centralPanel, false);
	//	      	((Component)centralPanel).setWidth("750px");
	//	      	focCentralPanel.setWidth("800px");
	      	}
	      }
      }
			return focCentralPanel;
		}
		
		@Override
		public String getThemeName() {
			return Globals.getIFocNotification() != null ? Globals.getIFocNotification().getThemeName() : "";
		}
  }