package com.foc.gui.table.view;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.list.FocList;
import com.foc.property.FObject;

public class UserView extends FocObject{
	private ViewFocList viewFocList = null;
	
	public UserView(FocConstructor constr){
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
  	super.dispose();
  	viewFocList = null;
  }

  public static void disposeColumnsConfigFocList(FocList list){
    list.dispose();
    list = null;
  }
  
  @Override
  public FocList getObjectPropertySelectionList(int fieldID){
  	FocList list = super.getObjectPropertySelectionList(fieldID);
  	if(fieldID == UserViewDesc.FLD_VIEW){
  		list = viewFocList;
  	}
  	return list;
  }

  @Override
  public FPanel newDetailsPanel(int viewID){
    return null;
  }
  
  public FocUser getUser(){
    FObject user = (FObject)getFocProperty(UserViewDesc.FLD_USER);
    return (FocUser)user.getObject_CreateIfNeeded();
  }
  
  public void setUser(FocUser user){
    FObject objProp = (FObject) getFocProperty(UserViewDesc.FLD_USER);
    objProp.setObject(user);
  }
  
  public String getViewKey(){
  	return getPropertyString(UserViewDesc.FLD_VIEW_KEY);
  }
  
  public void setViewKey(String viewKey){
  	setPropertyString(UserViewDesc.FLD_VIEW_KEY, viewKey);
  }

  public String getViewContext(){
  	return getPropertyString(UserViewDesc.FLD_VIEW_CONTEXT);
  }
  
  public void setViewContext(String viewContext){
  	setPropertyString(UserViewDesc.FLD_VIEW_CONTEXT, viewContext);
  }

  public ViewConfig getViewConfig(){
  	return (ViewConfig) getPropertyObject(UserViewDesc.FLD_VIEW);
  }
  
  public void setViewConfig(ViewConfig viewConfig){
  	setPropertyObject(UserViewDesc.FLD_VIEW, viewConfig);
  }
  
  public void setViewConfigRef(long ref){
  	((FObject)getFocProperty(UserViewDesc.FLD_VIEW)).setLocalReferenceInt_WithoutNotification(ref);
  }
  
  public ViewFocList getViewFocList() {
		return viewFocList;
	}

	public void setViewFocList(ViewFocList viewFocList) {
		this.viewFocList = viewFocList;
	}
}
