package com.foc.dataSource.servlet;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.FDataRequest_Abstract;
import com.foc.shared.dataStore.IDataStoreConst;
import com.foc.shared.json.B01JsonSaxCallBack_Abstract;

public class B01JsonCallBack_Server extends B01JsonSaxCallBack_Abstract {
	
	private FDataRequestList_Server dataReqList = null;
	private FDataRequest_Server     dataReq     = null;
	
	public B01JsonCallBack_Server(FDataRequestList_Server dataReqList){
		super();
		this.dataReqList = dataReqList;
	}
		
	public void dispose(){
		super.dispose();
		dataReqList = null;
	}

//	public void newKey(StringBuffer key){
//		if(key.isE)
//	}
	
	@Override
	public void newKeyValuePair(String key, String value) {
		Object lastObject = objStack_GetLast();
		if(lastObject == null){
			dataReq.setJsonKeyValue(key, value);
		}else if(lastObject instanceof FocObject){
			FocObject gwfObject = (FocObject) lastObject;
			FProperty prop = gwfObject.getFocPropertyByName(key);
			if(prop != null){
				if(prop instanceof FObject){
					FObject objProp = (FObject) prop;
					int ref = 0;
					FocObject focObjectAsValue = null;
					if(value != null){
						ref = Integer.valueOf(value);
					}
					if(objProp.getFocField() != null && objProp.getFocField().getSelectionList() != null){
						focObjectAsValue = objProp.getFocField().getSelectionList().searchByReference(ref);
					}
					objProp.setObject(focObjectAsValue);
				}else{
					prop.setString(value);
				}
			}
		}
	}

	@Override
	public void newObject(String objectKey, String key, String reference) {
		Object lastObject = objStack_GetFirst();
		if(lastObject == null){
			if(objectKey != null && objectKey.equals(IDataStoreConst.JSON_KEY_FW_OBJECT)){
				FocList   list    = dataReq.getFwList();
				FocDesc   focDesc = list.getFocDesc();
				FocObject focObj  = null;
				if(dataReq.getCommand() == FDataRequest_Abstract.COMMAND_INSERT){
					focObj = list.newEmptyItem();
					list.add(focObj);
				}else if(key.equals(focDesc.getRefFieldName())){
					int ref = Integer.valueOf(reference);
					focObj = list.searchByReference(ref);
				}
				dataReq.setFwObject(focObj);
				objStack_Push(focObj);
				newKeyValuePair(key, reference);
			}else{
				dataReq = dataReqList.newDataRequest();
				dataReq.setJsonKeyValue(key, reference);
			}
		}else if(lastObject instanceof FocList){
//			FwList list = (FwList) lastObject;
//			FwObject objectPushed = list.pushObject(Integer.valueOf(reference));
//			objStack_Push(objectPushed);
		}
	}

	@Override
	public void newList(String key) {
		if(objStack_GetFirst() == null){
			//objStack_Push(dataReqList);
		}else{
			
		}
	}
	
	public void endObject() {
		/*
		Object obj = objStack_GetLast();
		if(obj != null && obj instanceof FocObject){
			((FocObject)obj).validate(true);
			Object fatherSubject = ((FocObject)obj).getFatherSubject();
			if(fatherSubject != null && fatherSubject instanceof FocList){
				((FocList)fatherSubject).validate(true);
			}
		}
		*/
		super.endObject();
	}
}
