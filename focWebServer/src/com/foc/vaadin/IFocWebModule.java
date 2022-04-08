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
package com.foc.vaadin;

import com.foc.menuStructure.FocMenuItem;
import com.foc.shared.IFocWebModuleShared;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;

public interface IFocWebModule extends IFocWebModuleShared {
  public void    menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem);
  
  public boolean isActivated();
//  public XMLViewKey popupView(IFocData iFocData, INavigationWindow mainWindow, String menuCode, int xmlViewType);
}
