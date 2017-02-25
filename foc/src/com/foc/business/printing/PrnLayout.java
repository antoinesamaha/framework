package com.foc.business.printing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.foc.Globals;
import com.foc.business.config.BusinessConfig;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;

public class PrnLayout extends FocObject {

	private PrnLayoutDefinition layoutDefinition = null;
	
	public PrnLayout(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public String getFileName(){
		return getPropertyString(PrnLayoutDesc.FLD_FILE_NAME);
	}
	
	public String getFileNameWithoutExtensionIfExists(){
		String fileName = getPropertyString(PrnLayoutDesc.FLD_FILE_NAME);
		if(fileName.contains(".")){
			int index = fileName.lastIndexOf(".");
			fileName = fileName.substring(0, (index-1));
		}
		return fileName;
	}

	public void setFileName(String fileName){
		setPropertyString(PrnLayoutDesc.FLD_FILE_NAME, fileName);
	}

	public PrnContext getContext(){
		return (PrnContext) getPropertyObject(PrnLayoutDesc.FLD_CONTEXT);
	}
	
	public void setContext(PrnContext context){
		setPropertyObject(PrnLayoutDesc.FLD_CONTEXT, context);
	}

	public PrnLayoutDefinition getLayoutDefinition() {
		return layoutDefinition;
	}

	public void setLayoutDefinition(PrnLayoutDefinition layoutDefinition) {
		this.layoutDefinition = layoutDefinition;
	}

	public void attachToEmail(FocNotificationEmail email, PrintingAction printingAction, boolean withLogo){
		if(printingAction != null && printingAction.getLauncher() != null){
			printingAction.getLauncher().setWithLogo(withLogo);
	
			byte[] bytes = printingAction.getLauncher().web_FillReport(this, getFileName());
			if(bytes != null){
				InputStream stream = new ByteArrayInputStream(bytes);
				email.addAttachment(getFileNameWithoutExtensionIfExists()+".pdf", stream, "application/pdf");
			}
		}
			
	//		if(template.getPrintFileName() != null && !template.getPrintFileName().isEmpty()){
	//			FocList focList = PrnLayoutDefinitionDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
	//			if(focList != null){
	//				PrnLayoutDefinition prnLayoutDefinition = (PrnLayoutDefinition) focList.searchByPropertyStringValue(PrnLayoutDefinitionDesc.FLD_PRN_FILE_NAME, template.getPrintFileName());
	//				if(prnLayoutDefinition != null){
	//					InputStream jasperFileStream = prnLayoutDefinition.getJasperReport();
	//					email.addAttachment(template.getPrintFileName()+".pdf", jasperFileStream, "application/pdf");
	//				}
	//			}
	//		}

	}
}
