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

import java.net.MalformedURLException;
import java.net.URL;

import com.foc.focVaadinTheme.FocVaadinTheme;

import com.foc.Globals;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public abstract class FVGearWrapper<C extends FocXMLGuiComponent> extends HorizontalLayout {

	public abstract void fillMenu(VerticalLayout root);
	
	private C component = null;
	private PopupView popup = null; 
	
	public FVGearWrapper(){
		//setSpacing(true);
	}
	
	public FVGearWrapper(C component){
		this();
		setComponent(component);
	}
	
	public void dispose(){
		dispose_Component();
		popup = null;
	}

	public void dispose_Component(){
		if(this.component != null){
			this.component.dispose();
			this.component = null;
		}
	}

	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(popup != null){
			popup.setEnabled(enabled);
		}
	}
	
	public FocCentralPanel getWindow(){
		return findAncestor(FocCentralPanel.class);
	}
	
	public void setComponent(C component) {
		setComponent(component, true);
	}
	
	public void setComponent(C component, boolean addPopup){
		dispose_Component();
		this.component = component;
		addComponentAsFirst((Component) component);
		
		if (addPopup && isEnabled()) {
		// In this sample we update the minimized view value with the content of
	    // the TextField inside the popup.
	    popup = new PopupView(new PopupTextField());
	    popup.addStyleName("gear");
//	    popup.addStyleName("v-popupview-gear");
//	    popup.setStyleName("v-popupview-gear");
	    popup.setDescription("More...");
	    popup.setHideOnMouseOut(true);
	    addComponent(popup);
	    setComponentAlignment(popup, Alignment.BOTTOM_LEFT);
		}
	}
	
	@Override
	public void setWidth(String width) {
		if(component != null){
			((Component)component).setWidth(width);
		}
	}
	
	@Override
	public void setHeight(String height) {
		if(component != null){
			((Component)component).setHeight(height);
		}
	}
	
	public C getComponent() {
		return component;
	}
	
  //Create a dynamically updating content for the popup
	public class PopupTextField implements PopupView.Content {
		private VerticalLayout root = new VerticalLayout();
		
	  public PopupTextField() {
	    root.setSizeUndefined();
      root.setSpacing(true);
      root.setMargin(false);
      fillMenu(root);
	  }
		
	  public Component getPopupComponent() {
        return root;
    }

		@Override
		public String getMinimizedValueAsHTML() {
			String html = ""; 
			
			UI app = FocWebApplication.getInstanceForThread();
			if(app != null){
				URL url = null;
				try{
					url = app.getPage().getLocation().toURL();
				}catch (MalformedURLException e){
					Globals.logException(e);
				}
				String path = url != null ? url.getProtocol()+"://"+url.getAuthority()+url.getPath() : null;
								
				if(path != null){
					
//					int slashAfterHostPort = path.indexOf('/', 9);
//					int slashAfterPath     = slashAfterHostPort > 0 ? path.indexOf('/', slashAfterHostPort+1) : -1;
//					if(slashAfterPath > 0){
//						String newUrl = url.toString().substring(0, slashAfterPath);
						html = "<img src=\""+path+"VAADIN/themes/"+FocVaadinTheme.THEME_NAME+"/icons/16x16/preferences-icon.png\" width=\"16\" height=\"16\"></img>";
//					}
				}
			}
				
			return html;
		}
	}

	public class PopupLinkButton extends FVButton {
		
		public PopupLinkButton(String string, ClickListener clickListener) {
			super(string, clickListener);
	    setStyleName(BaseTheme.BUTTON_LINK);
		}
		
	}
}
