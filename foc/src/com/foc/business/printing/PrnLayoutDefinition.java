package com.foc.business.printing;

import java.io.InputStream;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class PrnLayoutDefinition extends FocObject {
	
	public PrnLayoutDefinition(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public String getContextDBName(){
		return getPropertyString(PrnLayoutDefinitionDesc.FLD_PRN_CONTEXT);
	}

	public void setContextDBName(String context){
		setPropertyString(PrnLayoutDefinitionDesc.FLD_PRN_CONTEXT, context);
	}
	
	public String getFileName(){
		return getPropertyString(PrnLayoutDefinitionDesc.FLD_PRN_FILE_NAME);
	}
	
	public void setFileName(String fileName){
		setPropertyString(PrnLayoutDefinitionDesc.FLD_PRN_FILE_NAME, fileName);
	}

  public void setJasperReport(InputStream inputStream){
  	Globals.getApp().getDataSource().focObject_addBlobFromInputStream(this, PrnLayoutDefinitionDesc.FLD_PRN_JASPER_FILE, inputStream);
  }

  public InputStream getJasperReport(){
  	return Globals.getApp().getDataSource().focObject_LoadInputStream(this, PrnLayoutDefinitionDesc.FLD_PRN_JASPER_FILE);
  }
}
