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
package com.foc.web.modules.admin;

import com.foc.admin.FocAppGroup;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class FocAppGroup_Form extends FocXMLLayout {

	private FocAppGroup getAppGroup(){
		return (FocAppGroup) getFocData();
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean bool = super.validationCheckData(validationLayout);
		if(getAppGroup() != null && getAppGroup().getFocGroup() != null){
			getAppGroup().getFocGroup().validate(true);
		}
		return bool;
	}
}
