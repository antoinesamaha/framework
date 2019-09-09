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
package com.foc.dataSource.servlet;

import com.foc.dataSource.store.DataStore;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.FDataRequest_Abstract;
import com.foc.shared.dataStore.IDataStoreConst;
import com.foc.shared.json.B01JsonBuilder;

public class FDataRequest_Server extends FDataRequest_Abstract {
	
	//One of these 2 is to be filled not both. The other remains null
	private FocObject fwObject = null;
	private FocList   fwList   = null;
	
	public FDataRequest_Server(){
	}
	
	public void dispose(){
		super.dispose();
		fwObject = null;
		fwList   = null;
	}

	@Override
	public void parseJson(StringBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toJson(B01JsonBuilder builder) {
		if(builder != null){
			builder.beginObject();
			builder.appendKeyValue(JSON_KEY_SERIAL_NUMBER, String.valueOf(getSerialNumber()));
			builder.appendKeyValue(JSON_KEY_COMMAND, String.valueOf(getCommand()));
			builder.appendKeyValue(JSON_KEY_DATA_KEY, getDataKey());
	
			//If list != null this means we are getting from server so we do not need to send the list
			if(getFwList() != null && getCommand() == COMMAND_QUERY){
				builder.appendKey(JSON_KEY_FW_LIST);
				getFwList().toJson(builder);
			}
			
			if(getFwObject() != null && getCommand() == COMMAND_INSERT){
				builder.appendKeyValue(JSON_KEY_FW_OBJECT_REF, fwObject.getReference().getInteger());	
			}
			
			builder.endObject();
		}
	}

	public FocObject getFwObject() {
		return fwObject;
	}

	public void setFwObject(FocObject fwObject) {
		this.fwObject = fwObject;
	}

	public FocList getFocList_FromDataStore() {
		return (FocList) DataStore.getInstance().getData(getDataKey());
	}

	public FocList getFwList() {
		if(fwList == null){
			//if(getCommand() == COMMAND_QUERY){
				fwList = getFocList_FromDataStore();
			//}
		}
		return fwList;
	}

	public void setFwList(FocList fwList) {
		this.fwList = fwList;
	}
	
	@Override
	public void setJsonKeyValue(String key, String value){
		super.setJsonKeyValue(key, value);
		if(key.equals(JSON_KEY_FW_OBJECT)){
			
		}else if(key.equals(JSON_KEY_FW_LIST)){
			
		}
	}
	
	public void execute(){
		if(getCommand() == IDataStoreConst.COMMAND_QUERY){
			FocList list = (FocList) DataStore.getInstance().getData(getDataKey());
			if(list != null){
				list.loadIfNotLoadedFromDB();
				setFwList(list);
			}
		}else if(getCommand() == IDataStoreConst.COMMAND_DELETE){
			getFwObject().setDeleted(true);
			getFwList().remove(getFwObject());
			getFwList().validate(true);
			setFwList(null);
		}else{
			getFwList().validate(true);
			setFwList(null);
		}
	}
}
