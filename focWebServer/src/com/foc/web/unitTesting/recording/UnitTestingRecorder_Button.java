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
package com.foc.web.unitTesting.recording;

import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UnitTestingRecorder_Button implements ClickListener {

	private FVButton button          = null;
	private String   buttonNameInMap = "";
	
	public UnitTestingRecorder_Button(FVButton button) {
		this.button = button;
		if(button != null) {
			button.addClickListener(this);
		}
	}
	
	public void dispose() {
		if(button != null && FocUnitRecorder.getInstance(false) != null) {
			//Sometimes we dispose before being called for listener  
			getNameFromButtonIfEmpty();
			button.removeClickListener(this);
			button = null;
		}
	}

	private void getNameFromButtonIfEmpty() {
		if(button != null && Utils.isStringEmpty(buttonNameInMap)) {
			FocXMLGuiComponentDelegate delegate = button.getDelegate(); 
			if(delegate != null){
				buttonNameInMap = delegate.getNameInMap();
			}
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		if(recorder != null) {
			getNameFromButtonIfEmpty();
			if(!Utils.isStringEmpty(buttonNameInMap)){
				FocUnitRecorder.recordLine("button_Click(\""+buttonNameInMap+"\");");
			}
		}
	}
}
