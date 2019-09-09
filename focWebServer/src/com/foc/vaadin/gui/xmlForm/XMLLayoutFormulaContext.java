/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
