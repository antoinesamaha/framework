package com.foc.business.currency;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.FocObject;
import com.foc.event.FValidationListener;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DateLineGuiDetailsPanel extends FPanel {
	
	private DateLine dateLine = null; 
	
	public DateLineGuiDetailsPanel(FocObject focObj, int viewID){
		dateLine = (DateLine) focObj; 
			
		add(focObj, DateLineDesc.FLD_DATE, 0, 0);

		FValidationPanel vPanel = showValidationPanel(true);
		vPanel.addSubject(focObj);
		vPanel.setValidationListener(new FValidationListener() {
			
			@Override
			public boolean proceedValidation(FValidationPanel panel) {
				DateLineList dateLineList = (DateLineList) getDateLine().getFatherSubject();
				boolean proceed = true;
				
				DateLine dateLine = (DateLine) dateLineList.findByDate_Exactly(getDateLine().getDate());
				if(dateLine != null){
					Globals.showNotification("Date exists", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
					proceed = false;
				}
				
				return proceed;
			}
			
			@Override
			public boolean proceedCancelation(FValidationPanel panel) {
				return true;
			}
			
			@Override
			public void postValidation(FValidationPanel panel) {
				if(getDateLine() != null && getDateLine().getFatherSubject() != null){
					((FocList)(getDateLine().getFatherSubject())).sort();
				}
			}
			
			@Override
			public void postCancelation(FValidationPanel panel) {
			}
		});
	}
	
	public void dispose(){
		super.dispose();
	}

	public DateLine getDateLine() {
		return dateLine;
	}
	
	
}
