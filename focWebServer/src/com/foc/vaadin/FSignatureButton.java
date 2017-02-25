package com.foc.vaadin;

import com.foc.Globals;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.vaadin.gui.FVIconFactory;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class FSignatureButton extends NativeButton { 
	
	private static final long DELAY = 1 * 1000;
	
	private java.sql.Date   lastUpdate         = null;
	private FocCentralPanel focCentralPanel = null;
	
	public FSignatureButton(FocCentralPanel focWebVaadinWindow) {
		this.focCentralPanel = focWebVaadinWindow;
		setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_PENDING_SIGNATURE));
		addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
        WFTransactionWrapperList wrapperFocList = new WFTransactionWrapperList();
        wrapperFocList.fill();
        getCentralPanel().changeCentralPanelContent_ToTableForFocList(wrapperFocList);
			}
		});
		resetIfNeeded(null);
	}
	
	public void dispose(){
		focCentralPanel = null;
	}

	public java.sql.Date getLastUpdate() {
		return lastUpdate;
	}

	public FocCentralPanel getCentralPanel() {
		return focCentralPanel;
	}

  private int getPendingSignature_Size(WFTransactionWrapperList transactionWrapperList){
  	int size = 0;
  	if(transactionWrapperList == null){
			transactionWrapperList = new WFTransactionWrapperList();
			transactionWrapperList.fill();
			size = transactionWrapperList.size();
			transactionWrapperList.dispose();
  	}else{
  		size = transactionWrapperList.size();
  	}
		return size;
  }	
	
	public void resetIfNeeded(WFTransactionWrapperList transactionWrapperList){
		boolean doReset = lastUpdate == null;
		
		java.sql.Date currentDate = null;
		if(!doReset){
			currentDate = Globals.getApp().getSystemDate();
			if(currentDate != null){
				long timeDiff = currentDate.getTime() - lastUpdate.getTime();
				doReset = timeDiff > DELAY;
			}
		}
		
		if(doReset){
			if(focCentralPanel != null){
				reset(transactionWrapperList);
	  	}
		}
	}
  
	public void reset(WFTransactionWrapperList transactionWrapperList){
		int listSize = getPendingSignature_Size(transactionWrapperList);
		setCaption(listSize + "");
  	setVisible(listSize > 0);
		lastUpdate = Globals.getApp().getSystemDate();
	}
}
