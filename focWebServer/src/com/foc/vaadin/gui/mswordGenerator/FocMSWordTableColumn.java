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
package com.foc.vaadin.gui.mswordGenerator;

import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordTableColumn extends FocMSWordComponent{

	private String dataPath = null;

	public FocMSWordTableColumn(FocMSWordLayout pdfParent, FocXMLAttributes xmlAttribute) {
		super(pdfParent, xmlAttribute);
		dataPath = buildDataPath(xmlAttribute);
	}

	@Override
	public void write(IMSWordContainer container) {
	}
	
	public String getDataPath(){
		return dataPath;
	}
	
	private String buildDataPath(FocXMLAttributes xmlAttribute){
		String name            = xmlAttribute.getValue(FXML.ATT_NAME);
		String dataPath        = xmlAttribute.getValue(FXML.ATT_DATA_PATH);
		String captionProperty = xmlAttribute.getValue(FXML.ATT_CAPTION_PROPERTY);
		
		if(dataPath == null){
			dataPath = name;
		}
		if(captionProperty != null){
			dataPath = dataPath + "." + captionProperty;
		}
		return dataPath;
	}
}
