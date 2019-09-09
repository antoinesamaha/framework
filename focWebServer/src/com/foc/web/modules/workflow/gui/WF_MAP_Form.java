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

import com.foc.business.workflow.map.WFMap;
import com.foc.business.workflow.map.WFSignature;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WF_MAP_Form extends FocXMLLayout{
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		reSortSignatureList();
	}
	
	public void dispose() {
		//This is important to reload in the cache the map after editing on screen 
		WFMap map = (WFMap) getFocObject();
		if(map != null){
			map.resetPreviousStage2SignatureMap();
		}
		super.dispose();
	}
	
	private void reSortSignatureList() {
		WFMap map = (WFMap) getFocObject();
		if(map != null) {
			map.getSignatureList();//This alows to sort the Signature List
		}
	}
	
	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		reSortSignatureList();
		WFSignature signature = (WFSignature) super.table_AddItem(tableName, table, fatherObject);
		if(signature != null && tableName.equals("WFSIGNATURE_TABLE")){
			FocList list = (FocList) signature.getFatherSubject();
			if(list != null && list.size() >= 2){
				WFSignature prev = (WFSignature) list.getFocObject(list.size()-2);
				if(prev != null){
					signature.setPreviousStage(prev.getTargetStage());
					copyMemoryToGui();
				}
			}
		}
		return signature; 
	}

}
