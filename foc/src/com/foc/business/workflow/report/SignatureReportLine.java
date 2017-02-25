package com.foc.business.workflow.report;

import java.awt.image.BufferedImage;
import java.sql.Date;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.property.FImageProperty;

public class SignatureReportLine extends FocObject {
	
	public SignatureReportLine(FocConstructor constr){
		super(constr);
		newFocProperties();
	}

	public void setName(int index, String name){
		setPropertyString(SignatureReportLineDesc.FLD_NAME_FIRST + index, name);
	}

	public void setDate(int index, Date date){
		setPropertyDate(SignatureReportLineDesc.FLD_DATE_FIRST + index, date);
	}

	public void setTitle(int index, String title){
		setPropertyString(SignatureReportLineDesc.FLD_TITLE_FIRST + index, title);
	}
	
	public void setSignature(int index, BufferedImage imageBuffer){
		FImageProperty iProp = (FImageProperty) getFocProperty(SignatureReportLineDesc.FLD_SIGNATURE_FIRST + index);
		iProp.setImageValue(imageBuffer);
	}
}
