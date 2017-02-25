package com.foc.desc.field;

import java.awt.Component;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.FocObject;
import com.foc.gui.image.FImagePanel;
import com.foc.gui.table.cellControler.AbstractCellControler;
import com.foc.property.FImageProperty;
import com.foc.property.FProperty;

public class FImageField extends FBlobMediumField {
	private int width  = 0;
	private int height = 0;

  public FImageField(String name, String title, int id, int width, int height) {
    super(name, title, id, false);
    this.width  = width ;
    this.height = height;
  }
 
  public Component getGuiComponent(FProperty prop){
    return new FImagePanel((FImageProperty) prop);
  }
  
  public AbstractCellControler getTableCellEditor_ToImplement(FProperty prop){
    return null;
  }
    
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
    return new FImageProperty(masterObj, getID());
  }
  
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj) {
    return newProperty(masterObj, null);
  }

  @Override
	public int getFabType() {
	  return FieldDefinition.SQL_TYPE_ID_IMAGE;
	}
	
  public int getWidth(){
  	return width;
  }

  public int getHeight(){
  	return height;
  }
}
