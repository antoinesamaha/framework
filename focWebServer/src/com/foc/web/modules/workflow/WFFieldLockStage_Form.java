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
package com.foc.web.modules.workflow;

import com.foc.business.workflow.WFFieldLockStage;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFFieldLockStage_Form extends FocXMLLayout{

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		
		WFFieldLockStage fieldLockStage = getFieldLockStage();
		if(fieldLockStage != null){
			fieldLockStage.fillChoicesForStagesProperty();
		}
		
	}
	
	public WFFieldLockStage getFieldLockStage(){ 
		return (WFFieldLockStage) getFocData();
	}

}
