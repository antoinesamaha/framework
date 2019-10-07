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

import java.util.ArrayList;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public interface ICentralPanel {
  public void                refresh();
  public void                init(INavigationWindow window, XMLView xmlView, IFocData focData);
  public void                beforeViewChangeListenerFired();
  public void                parseXMLAndBuildGui();
  public void                re_parseXMLAndBuildGui();
  public IFocData            getFocData();
	public boolean             isFocDataOwner();
	public void                setFocDataOwner(boolean focDataOwner);
  public XMLView             getXMLView();
  public void                setXMLView(XMLView xmlView);
  public void                dispose();
  public IRightPanel         getRightPanel(boolean createIfNeeded);
  public ArrayList<FVLayout> getLayouts();
  public INavigationWindow   getMainWindow();
  public FValidationSettings getValidationSettings(boolean createIfNeeded);
  public FVValidationLayout  getValidationLayout();
  public void                showValidationLayout(boolean showBackButton);
  public void                print();
  public String              getPreferredPageWidth();
  public boolean             isFullScreen();
  public void                addMoreMenuItems(FVValidationLayout validationLayout);
  public boolean             copyGuiToMemory();
  public int                 getViewRights();
  public void                setViewRights(int viewRights);
  public void                goBack(FocCentralPanel focCentralPanel);
  public String              getLinkSerialisation();
  public void                setLinkSerialisation(String serialisation);
  
  public void                addedToNavigator();
  public void                removedFromNavigator();
  public void                optionButtonClicked();

  public ICentralPanel       getRootCentralPanel();//Is the Same when this Layout is the Root, not embedded 
  public boolean             isRootLayout();//Used to indicate that this is the first level Layout and not embedded
                                            //We need to know when we click Cancel=goBack() on an internal Layout...
  
  //Used when we goBack on a middle Layout we want to mark it as goBack Requested so we do not show it in the Navigation Window
  public boolean             isGoBackRequested();
  public void                setGoBackRequested(boolean goBackRequested);
  //-----------------------------------------------------------------------
  
  public boolean isPropertyChangeSuspended();
}

