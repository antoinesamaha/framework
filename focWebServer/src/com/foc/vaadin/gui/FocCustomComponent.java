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
package com.foc.vaadin.gui;

import com.foc.vaadin.FocWebVaadinWindow;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class FocCustomComponent extends CustomComponent {

	private FocWebVaadinWindow mainWindow = null;
	private AbsoluteLayout     mainLayout = null;
	private int                width      = 200;
	private int                height     = 200;
	
	public FocCustomComponent(FocWebVaadinWindow mainWindow, int width, int height){
		this.mainWindow = mainWindow;
		setDimensions(width, height);
	}
	
	public void dispose(){
		mainWindow = null;
		mainLayout = null;
	}
	
	public void init(){
		setCompositionRoot(buildMainLayout());
	}

	public void setDimensions(int width, int height){
		this.width      = width ;
		this.height     = height;
		if(mainLayout != null){
			mainLayout.setWidth(width+"px");
			mainLayout.setHeight(height+"px");
		}
		// top-level component properties
		setWidth(width+"px");
		setHeight(height+"px");
	}
	
	protected AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();

		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
//		mainLayout.setMargin(false);
		
		setDimensions(width, height);
		
		return mainLayout;
	}

	public AbsoluteLayout getMainLayout() {
		return mainLayout;
	}
	
	public FocWebVaadinWindow getMainWindow(){
		return mainWindow;
	}
	
	public Label addLabel(String value, String style, int left, int top, int width, int heigth){
		return addLabel(value, style, left, top, width, heigth, ContentMode.TEXT);
	}
	
	public Label addLabelXHTML(String value, String style, int left, int top, int width, int heigth){
		return addLabel(value, style, left, top, width, heigth, ContentMode.HTML);
	}
	
	private Label addLabel(String value, String style, int left, int top, int width, int heigth, ContentMode contentMode){
		Label lbl = null;
		if(mainLayout != null){
			lbl = new Label(value);
			lbl.setContentMode(contentMode);
			lbl.addStyleName(style);
			lbl.setWidth(width+"px");
			lbl.setHeight(heigth+"78px");
			mainLayout.addComponent(lbl, "top:"+top+".0px;left:"+left+".0px;");
		}
		return lbl;
	}
}
