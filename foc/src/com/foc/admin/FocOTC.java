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

/*
 * Created on 28-Nov-2025
 */
package com.foc.admin;

import java.sql.Date;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.util.ASCII;

/**
 * @author 01Barmaja
 */
@SuppressWarnings("serial")
public class FocOTC extends FocObject {

	public FocOTC() {
		this(new FocConstructor(FocOTCDesc.getInstance()));
	}
	
	public FocOTC(FocConstructor constr) {
		super(constr);

		newFocProperties();
	}
	
	public String getOTC() {
		return getPropertyString(FocOTCDesc.FLD_OTC);
	}
	
	public void setOTC(String otc) {
		setPropertyString(FocOTCDesc.FLD_OTC, otc);
	}
	
	public FocUser getUser() {
		return (FocUser) getPropertyObject(FocOTCDesc.FLD_USER);
	}
	
	public void setUser(FocUser user) {
		setPropertyObject(FocOTCDesc.FLD_USER, user);
	}	
	
	public String getEntityName() {
		return getPropertyString(FocOTCDesc.FLD_ENTITY_NAME);
	}
	
	public void setEntityName(String entityName) {
		setPropertyString(FocOTCDesc.FLD_ENTITY_NAME, entityName);
	}
	
	public long getEntityRef() {
		return getPropertyLong(FocOTCDesc.FLD_ENTITY_REF);
	}
	
	public void setEntityRef(long entityRef) {
		setPropertyLong(FocOTCDesc.FLD_ENTITY_REF, entityRef);
	}
	
	public Date getCreatedAt() {
		return getPropertyDate(FocOTCDesc.FLD_CREATED_AT);
	}
	
	public void setCreatedAt(Date createdAt) {
		setPropertyDate(FocOTCDesc.FLD_CREATED_AT, createdAt);
	}
	
	public boolean consume() {
		return (getCreatedAt() != null && getCreatedAt().getTime() + 10 * 60 * 1000 > Globals.getApp().getSystemDate().getTime()) ? true : false; 
	}
	
	public static FocOTC newOTC(String tableName, long tableRef) {
		FocOTC focOTC = null;
		if (Globals.getApp().getUser_ForThisSession() != null) {
			focOTC = new FocOTC();
			focOTC.setCreated(true);
			
			String code = ASCII.generateRandomString(30);
			focOTC.setOTC(code);
			focOTC.setCreatedAt(Globals.getApp().getSystemDate());
			focOTC.setEntityName(tableName);
			focOTC.setEntityRef(tableRef);
			focOTC.setUser(Globals.getApp().getUser_ForThisSession());
			focOTC.validate(false);
		}
		
		return focOTC;
	}
	
	public static FocOTC findOTC(String otc) {
		FocOTC foundFocOTC = null;
		
		FocList list = new FocList(new FocLinkSimple(FocOTCDesc.getInstance()));
		list.getFilter().putAdditionalWhere("OTC", "\""+FocOTCDesc.FNAME_OTC+"\" = '"+otc+"'");
		list.loadIfNotLoadedFromDB();
		
		FocOTC focOTC = list.size() == 1 ? (FocOTC) list.getFocObject(0) : null ;
		if (focOTC != null && focOTC.consume()) {
			foundFocOTC = new FocOTC();
			foundFocOTC.copyPropertiesFrom(focOTC, null);
		}

//		focOTC.setDeleted(true);
//		focOTC.validate(false);
//		list.validate(false);
		list.dispose();
		
		return foundFocOTC;
	}
 
}
