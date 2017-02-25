package com.foc.vaadin.gui.xmlForm;

import com.foc.access.FocDataMap;
import com.foc.desc.FocObject;
import com.foc.formula.FocSimpleFormulaContext;
import com.foc.formula.Formula;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.vaadin.ui.Component;

public class XMLLayoutFormulaContext extends FocSimpleFormulaContext {
	private FocXMLLayout       xmlLayout              = null;
	private FocXMLGuiComponent componentToHideAndShow = null;
	
	public XMLLayoutFormulaContext(FocXMLLayout xmlLayout, FocXMLGuiComponent componentToHideAndShow, String formulaExpression) {
		super(new Formula(formulaExpression), null);
		this.xmlLayout = xmlLayout;
		this.componentToHideAndShow = componentToHideAndShow;
		
		plugListeners();
	}
	
	public void dispose(){
		super.dispose();
		xmlLayout = null;
		componentToHideAndShow = null;
	}

	public void computeFormulaAndApplyVisibilityOnComponent(){
	  boolean   visible   = true;
	  FocObject focObject = null;
	  IFocData focData = xmlLayout.getFocData();
	  if(focData instanceof FocDataMap){
	  	focData = ((FocDataMap)focData).getMainFocData();
	  }
    if(focData instanceof FocObject){
	    focObject = (FocObject) focData;
	  }
	  
    Object valueObj = compute(focObject);
    
    if(valueObj instanceof Boolean){
      visible = ((Boolean)valueObj).booleanValue();
    }else if(valueObj instanceof String){
      visible = ((String)valueObj).toUpperCase().equals("TRUE");                    
    }
    ((Component)componentToHideAndShow).setVisible(visible);
	}
	
	@Override
	public void plugListeners(String expression) {
	  if(expression != null && !expression.isEmpty()){
	    IFocData propFocData = xmlLayout.getFocData().iFocData_getDataByPath(expression);
	    if(propFocData != null){
	      xmlLayout.mapDataPath2ListenerAction_PutFormulaContext(expression, this);
	    }
	  }
	}

	@Override
	public void unplugListeners(String expression) {
	  
	}
}
