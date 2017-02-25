package com.foc.business.workflow.report;

import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FImageField;

public class SignatureReportLineDesc extends FocDesc {
	public static final int FLD_NAME_FIRST      =  1;
	public static final int FLD_TITLE_FIRST     = 11;
	public static final int FLD_SIGNATURE_FIRST = 21;
	public static final int FLD_DATE_FIRST      = 31;
	
	public SignatureReportLineDesc(){
		super(SignatureReportLine.class, FocDesc.NOT_DB_RESIDENT, "WF_SIGNATURE_REPORT_LINE", false);
		addReferenceField();
		addOrderField();
		
		for(int i=0; i<3; i++){
			int index = i+1;
			FStringField cFld = new FStringField("NAME"+index, "Name "+i, FLD_NAME_FIRST + i, false, FocUserDesc.LEN_FULL_NAME);
			addField(cFld);

			cFld = new FStringField("TITLE"+index, "Title "+i, FLD_TITLE_FIRST + i, false, WFTitleDesc.LEN_DESCRIPTION);
			addField(cFld);
			
			FImageField imageField = new FImageField("SIGNATURE"+index, "Signature "+i, FLD_SIGNATURE_FIRST + i, 180, 80);
			addField(imageField);
			
			FDateField dateFld = new FDateField("DATE"+index, "Date "+i, FLD_DATE_FIRST + i, false);
			addField(dateFld);
		}
  }
			
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  private static FocDesc focDesc = null;
  
  public static FocDesc getInstance() {
  	if(focDesc == null){
  		focDesc = new SignatureReportLineDesc();
  	}
	  return focDesc;
	}
}
