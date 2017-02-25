package com.foc.vaadin.gui.components;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;

@SuppressWarnings("serial")
public class FVLabelInTable extends FVLabel {

	private FVTableColumn column  = null;
	private FProperty     focData = null;
	
  public FVLabelInTable(FProperty property, FocObject focObj, FVTableColumn column) {
  	super("");
  	focData = property;
  	this.column = column;
		getDelegate().setDataPathWithRoot(focObj, column.getDataPath());
  }

  public void dispose(){
  	super.dispose();
    focData = null;
    column = null;
  }
  
  @Override
  public FProperty getFocData() {
    return (FProperty) focData;
  }
  
  @Override
  public void setFocData(IFocData focData) {
  	if(focData instanceof FProperty){
  		this.focData = (FProperty) focData;
  	}
    copyMemoryToGui();
    setAttributes(getAttributes());
  }

	@Override
	public void copyMemoryToGui() {
//		super.copyMemoryToGui();
		String objReturned = "";
		if(focData != null && column != null){
			try{
				objReturned = (String) focData.vaadin_TableDisplayObject(column.getFormat(), column.getCaptionProp());
			}catch(Exception e){
				Globals.logException(e);
			}
		}
		setValue(objReturned);		
	}
}