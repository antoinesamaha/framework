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
package com.foc.vaadin.gui.components.popupButton;

import com.foc.vaadin.gui.components.FVNativeButton;
import com.foc.web.gui.INavigationWindow;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class FVPopupContentButton extends FVNativeButton implements ClickListener{

	private INavigationWindow iNavigationWindow = null;
	private FVPopupWindow popupWindow = null;
	private IPopupButtonClickListener popupButtonClickListener = null;
	
	public FVPopupContentButton(FVPopupWindow popupWindow, INavigationWindow iNavigationWindow, String content){
		super(content);
		this.iNavigationWindow = iNavigationWindow;
		this.popupWindow = popupWindow;
		init();
	}
		
	private void init(){
		addClickListener(this);
	}
	
	public void dispose(){
		iNavigationWindow = null;
		popupWindow = null;
	}
	
	public INavigationWindow getWindow() {
		return iNavigationWindow;
	}

	public FVPopupWindow getPopupWindow() {
		return popupWindow;
	}
	
	public void setPopupButtonClickListener(IPopupButtonClickListener popupButtonClickListener) {
		this.popupButtonClickListener = popupButtonClickListener;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(popupButtonClickListener != null) popupButtonClickListener.buttonClicked(this);
		
		if(popupWindow != null){
			popupWindow.close();
			popupWindow.dispose();
		}
	}
}
