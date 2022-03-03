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

import org.json.JSONException;

import com.foc.Globals;
import com.foc.business.workflow.implementation.WFLog;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class WF_LOG_JSON_Standard_Form extends FocXMLLayout {
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();

		WFLog log = getWFLog();
				
		String val = log.getDocZip();//.replace("'","\"");
		String changes = log.getChanges();

//		val = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
		try{
			Globals.logString(val);
			if(Utils.isStringEmpty(val)) val = "{}";
			org.json.JSONObject jsonObj = new org.json.JSONObject(val);
			val = jsonObj.toString(4);

			FVVerticalLayout vLay = (FVVerticalLayout) getComponentByName("VLAY");
			FVTextArea textArea = new FVTextArea(null, null);
			textArea.setCaption("Document");
			textArea.setWidth("100%");
			textArea.setHeight("200px");
			textArea.setValue(val);
			vLay.addComponent(textArea);
			
			if(Utils.isStringEmpty(changes)) changes = "{}";
			jsonObj = new org.json.JSONObject(changes);
			val = jsonObj.toString(4);

			textArea = new FVTextArea(null, null);
			textArea.setCaption("Changes");
			textArea.setWidth("100%");
			textArea.setHeight("200px");
			textArea.setValue(val);
			textArea.addStyleName("focXMLEditor");
			vLay.addComponent(textArea);
			
//			FVLabel jsonTextArea = (FVLabel) getComponentByName("JSON_PRETTY");
//	    if(jsonTextArea != null && val != null) {
//	    	jsonTextArea.setValue(val);
//	    }

		}catch (JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
//		
//    JsonParser parser = new JsonParser();
//    JsonObject jo = (JsonObject) parser.parse(val);
//    String res = jo.getAsString();
//    res = res;
//    JsonElement je = jo;
////    JsonElement je = jo.get("some_array");
////
//    //Parsing back the string as Array
//    JsonArray ja = (JsonArray) parser.parse(o.get("some_array").getAsString());
//    for (JsonElement jo : ja) {
//    JsonObject j = (JsonObject) jo;
//        // Your Code, Access json elements as j.get("some_element")
//    }
//		
//		
//	  JsonElement jelement = new JsonParser().parse(val);
//	  if(jelement != null) {
//	    JsonObject  jobject = jelement.getAsJsonObject();
//	    if(jobject != null) {
//		    String result = jobject.getAsString();
//		    
//		    FVTextArea jsonTextArea = (FVTextArea) getComponentByName("JSON_PRETTY");
//		    if(jsonTextArea != null && result != null) {
//		    	jsonTextArea.setValue(result);
//		    }
//	    }
//	  }
	}
	
	private WFLog getWFLog() {
		return (WFLog) getFocObject();
	}
}
