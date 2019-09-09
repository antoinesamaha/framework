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
package com.foc.vaadin.gui.components.menuBar;

import com.foc.access.FocDataMap;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public abstract class FVMenuBarCommand implements MenuBar.Command {

  private FVMenuBar menuBar = null;
  private String    code    = null;
  
  public FVMenuBarCommand(String code, FVMenuBar menuBar){
    setCode(code);
    setMenuBar(menuBar);
  }

  public FVMenuBarCommand(){
  }

  public void dispose(){
    menuBar = null;
  }
  
  public FVValidationLayout getValidationLayout(){
    FVValidationLayout layout = null;
    if(getMenuBar() != null){
      layout = getMenuBar().getValidationLayout();
    }
    return layout;
  }
  
  public IFocData getFocData(){
    IFocData data = null;
    FVValidationLayout vLay = getValidationLayout();
    if(vLay != null){
      data = vLay.getFocData();
    }
    return data;
  }
  
  public FocObject getFocObject(){
  	FocObject focObject = null;
  	IFocData data = getFocData();
  	if(data instanceof FocObject){
  		focObject = (FocObject) data;
  	}else if(data instanceof FocDataMap){
  		FocDataMap map = (FocDataMap)data;
  		data = map.getMainFocData();
  		if(data instanceof FocObject){
  			focObject = (FocObject) data;
  		}
  	}
  	return focObject;
  }

  public void setMenuBar(FVMenuBar menuBar) {
    this.menuBar = menuBar;
  }

  public FVMenuBar getMenuBar() {
    return menuBar;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
