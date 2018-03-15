package com.foc.business.notifier;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocTableName;
import com.foc.business.printing.PrnLayout;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@FocEntity
@SuppressWarnings("serial")
public class FNotifTrigReport extends PojoFocObject implements FocNotificationConst {
    
  public static final String DBNAME = "FNotifTrigReport";

	@FocTableName()
	public static final String FIELD_ReportContext = "ReportContext";

	@FocForeignEntity(table = "PRN_LAYOUT")
	public static final String FIELD_PrintingLayout = "PrintingLayout";
	
  public FNotifTrigReport(FocConstructor constr){
    super(constr);
  }
  
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}
	
	public String getTABLE_NAME() {
		return getPropertyString(FIELD_ReportContext);
	}

	public void setTABLE_NAME(String value) {
		setPropertyString(FIELD_ReportContext, value);
	}

  public FocDesc getTableDesc(){
    return getPropertyDesc(FIELD_ReportContext);
  }
  
  public void setTableDesc(FocDesc focDesc){
    setPropertyDesc(FIELD_ReportContext, focDesc);
  }

	public PrnLayout getPrintingLayout() {
		return (PrnLayout) getPropertyObject(FIELD_PrintingLayout);
	}

	public void setPrintingLayout(PrnLayout value) {
		setPropertyObject(FIELD_PrintingLayout, value);
	}
}
