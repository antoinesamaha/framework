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
package com.foc.web.modules.workflow.gui;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVObjectComboBox;

@SuppressWarnings("serial")
public class WF_LOG_Form extends WF_LOG_NoWorkflow_Standard_Form {

	@Override
	protected String getLogContext() {
		return XMLViewKey.CONTEXT_DEFAULT;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVObjectComboBox comp = (FVObjectComboBox) getComponentByName("WF_CURRENT_STAGE");
		if (comp != null) {
			boolean editable = false;
			FocObject focObject = (FocObject) getFocData();
			if (focObject != null) {
				FocDesc focDesc = focObject.getThisFocDesc();
				if (focDesc != null) {
					editable = focDesc.workflow_IsAllowSignatureStageModification();
				}
			}
			comp.setEnabled(editable);
		}
	}
}
