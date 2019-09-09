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
package com.foc.vaadin.gui.components;

import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class FVPanel extends Panel implements ICentralPanel, FocXMLGuiComponent, FVLayout {

  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private boolean goBackRequested = false;
  
  public FVPanel(){
    delegate = new FocXMLGuiComponentDelegate(this);
  }
  
  public FVPanel(Attributes attributes){
  	delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
  }
  
  @Override
  public void refresh() {
  }

  @Override
  public XMLView getXMLView() {
    return null;
  }

	@Override
	public IRightPanel getRightPanel(boolean createIfNeeded) {
		return null;
	}

  @Override
  public ArrayList<FVLayout> getLayouts() {
    return null;
  }

  @Override
  public FocWebVaadinWindow getMainWindow() {
    return (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getContent();
  }

	@Override
	public FValidationSettings getValidationSettings(boolean createIfNeeded) {
		return null;
	}

  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    
  }

  @Override
  public void showValidationLayout(boolean showBackButton) {
    
  }

  @Override
  public String getXMLType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Attributes getAttributes() {
    return null;
  }

  @Override
  public void setAttributes(Attributes attributes) {
  	FocXMLGuiComponentStatic.applyAttributes(this, attributes);    
  }

  @Override
  public void print() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public IFocData getFocData() {
    // TODO Auto-generated method stub
    return null;
  }

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}
	
	@Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }
  
  @Override
  public void dispose(){
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }    
  }

  @Override
  public void setFocData(IFocData focData) {
  }

	@Override
	public void parseXMLAndBuildGui() {
		// TODO Auto-generated method stub
		
	}

  @Override
  public String getPreferredPageWidth() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addMoreMenuItems(FVValidationLayout validationLayout) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueString(String value) {
  }

  @Override
  public int getViewRights() {
    return 0;
  }

  @Override
  public void setViewRights(int viewRights) {
  }

	@Override
	public void re_parseXMLAndBuildGui() {
	}

	@Override
	public void setXMLView(XMLView xmlView) {
	}

	@Override
	public void goBack(FocCentralPanel focCentralPanel) {
	}
	
	@Override
	public void refreshEditable() {
	}

	@Override
	public String getLinkSerialisation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLinkSerialisation(String serialisation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addedToNavigator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedFromNavigator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void optionButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFocDataOwner() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocDataOwner(boolean focDataOwner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FVValidationLayout getValidationLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGoBackRequested() {
		return goBackRequested;
	}

	@Override
	public void setGoBackRequested(boolean goBackRequested) {
		this.goBackRequested = goBackRequested;
	}

	@Override
	public void beforeViewChangeListenerFired() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICentralPanel getRootCentralPanel() {
		return this;
	}

	@Override
	public boolean isRootLayout() {
		return true;
	}

	@Override
	public void addComponent(Component comp, Attributes attributes) {
    addComponent(comp);
	}

	@Override
	public void addComponent(Component comp) {
		setContent(comp);
//		addComponent(comp);
	}

	@Override
	public void setWYSIWYGDropHandler() {
	}
	
	@Override
	public void setDragDrop(boolean state) {
	}

	@Override
	public boolean isDragDrop() {
		return false;
	}

	@Override
	public Iterator<Component> getComponentIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	@Override
	public ComponentPosition getPosition(Component comp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isXMLLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fillXMLNodeContent(XMLBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPropertyChangeSuspended() {
		return false;
	}
}
