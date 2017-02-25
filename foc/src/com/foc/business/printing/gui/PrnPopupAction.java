package com.foc.business.printing.gui;

import com.foc.Globals;
import com.foc.business.printing.IPrnReportCreator;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayoutGuiBrowsePanel_Print;
import com.foc.business.printing.PrnReportLauncher;
import com.foc.business.printing.ReportFactory;

public abstract class PrnPopupAction implements IPrnReportCreator {

	private PrnReportLauncher launcher       = null;
	private String            prnContextName = null;

	public PrnPopupAction() {
    launcher = new PrnReportLauncher(this);
	}
	
	public void dispose(){
		if(launcher != null){
			disposeLauncherContent();
			launcher.dispose();
			launcher = null;
		}
	}
	
	public void actionPerformed() {
		PrnContext prnContext     = ReportFactory.getInstance().findContext(prnContextName);
		
		if(prnContext != null){
  		PrnLayoutGuiBrowsePanel_Print browse = new PrnLayoutGuiBrowsePanel_Print(prnContext, this);
  		Globals.getDisplayManager().popupDialog(browse, "Reports", true);
		}
	}

	public PrnReportLauncher getLauncher() {
		return launcher;
	}

	public String getPrnContextName() {
		return prnContextName;
	}

	public void setPrnContextName(String prnContextName) {
		this.prnContextName = prnContextName;
	}
}