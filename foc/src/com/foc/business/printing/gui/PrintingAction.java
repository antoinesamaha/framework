package com.foc.business.printing.gui;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.business.printing.IPrnReportCreator;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayoutGuiBrowsePanel_Print;
import com.foc.business.printing.PrnReportLauncher;
import com.foc.business.printing.ReportFactory;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;

public abstract class PrintingAction implements IPrnReportCreator{

	private boolean           objectToPrint_Owner = false;
	private IFocData          objectToPrint       = null;
	private String            printingContext     = null;
	private PrnReportLauncher launcher            = null;
	
	public PrintingAction() {
	}
	
	public void dispose() {
		disposeContent();
	}
	
	public void disposeContent() {
		if(objectToPrint != null) {
			if(objectToPrint_Owner) {
				objectToPrint.dispose();
			}
			objectToPrint       = null;
			objectToPrint_Owner = false;
		}
		
		if(launcher != null) {
			disposeLauncherContent();
			launcher.dispose();
			launcher = null;
		}
	}
	
	public void popupPrintingPanel() {
		if(objectToPrint != null) {
			String     prnContextName = printingContext;
			PrnContext prnContext     = ReportFactory.getInstance().findContext(prnContextName);
			
			if(prnContext != null){
	  		PrnLayoutGuiBrowsePanel_Print browse = new PrnLayoutGuiBrowsePanel_Print(prnContext, this);
	  		Globals.getDisplayManager().popupDialog(browse, "Reports", true);
			}
		}
	}

	public PrnReportLauncher getLauncher() {
		return launcher;
	}

	public boolean isObjectToPrint_Owner() {
		return objectToPrint_Owner;
	}

	public void setObjectToPrint_Owner(boolean objectToPrintOwner) {
		objectToPrint_Owner = objectToPrintOwner;
	}

	@Override
	public void initLauncher() {
		if(launcher == null){
			launcher = new PrnReportLauncher(this); 
		}
	}

	public FocObject getObjectToPrint() {
		FocObject focObject = null;
		if(objectToPrint != null && objectToPrint instanceof FocDataMap){
			FocDataMap dataMap = (FocDataMap) objectToPrint;
			focObject = (FocObject) dataMap.getMainFocData();
		}else{
			focObject = objectToPrint instanceof FocObject ? (FocObject) objectToPrint : null;
		}
		return focObject;
	}
	
	public IFocData getFocDataToPrint(){
		return objectToPrint;
	}

	public void setObjectToPrint(IFocData objectToPrint) {
		this.objectToPrint = objectToPrint;
	}
	
	public String getPrintingContext() {
		return printingContext;
	}

	public void setPrintingContext(String printingContext) {
		this.printingContext = printingContext;
	}

}