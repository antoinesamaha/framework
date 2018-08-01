package com.foc.web.modules.workflow;

import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.WFLog;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFLog_Banner_Standard_Table extends WFLog_Table {
	
	private Workflow workflow = null; 
			
  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
  	try {
  		workflow = ((IWorkflow)focData).iWorkflow_getWorkflow();
  		FocList list = workflow.getLogList();
  		focData = list;
  	}catch(Exception e) {
  	}
  	super.init(window, xmlView, focData);
  }
  
  @Override
  public void dispose() {
  	super.dispose();
  	workflow = null;
  }
  
  public Workflow getWorkflow() {
  	return workflow;
  }
  
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVTextArea textArea = getTextArea();
		if(textArea != null) {
//			textArea.addStyleName("fenix-me-bubble");
			textArea.addStyleName("border-blue");
			textArea.setEnabled(true);
		}
	}
	
	public FocList getChatList() {
		return (FocList) getFocList();
	}
	
	public FVTextArea getTextArea() {
		return (FVTextArea) getComponentByName("MY_COMMENT");
	}
	
	public void button_SEND_Clicked(FVButtonClickEvent evt){
		FocList  list     = getChatList();
		Workflow workflow = getWorkflow();
		
		FVTextArea textArea = getTextArea();
		if(list != null && textArea != null) {
			String message = textArea.getValue();
			if(!Utils.isStringEmpty(message)) {
				long refLogline = workflow.insertLogLine(WFLogDesc.EVENT_COMMENT, message);
				list.reloadFromDB();
				
				WFLog log = (WFLog) list.searchByReference(refLogline);
//  		  FocDataMap focDataMap = new FocDataMap(chat);
//	  		focDataMap.putString("TABLE_NAME", ChatJoinDesc.getInstance().getStorageName());
//				FocNotificationManager.getInstance().fireEvent(new FocNotificationEvent(FocNotificationConst.EVT_TABLE_ADD, focDataMap));
				
				if(log != null) {
	        FVForEachLayout forEachLayout = (FVForEachLayout) getComponentByName("_BannerTable");
	        if(forEachLayout != null){
	        	forEachLayout.addBannerForFocObject(log);
	        	textArea.setValue("");
					}
				}
			}
		}
	}

}
