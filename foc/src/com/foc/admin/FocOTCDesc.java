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
package com.foc.admin;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FDateTimeField;
import com.foc.desc.field.FLongField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FStringField;
import com.foc.list.FocLink;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

public class FocOTCDesc extends FocDesc {

	public static final String DB_TABLE_NAME = "FOTC";

	public static final int FLD_OTC = 1;
	public static final int FLD_USER = 2;
	public static final int FLD_ENTITY_NAME = 3;
	public static final int FLD_ENTITY_REF = 4;
	public static final int FLD_CREATED_AT = 5;

	public static final String FNAME_OTC = "OTC";
	public static final String FNAME_USER = "USER";
	public static final String FNAME_ENTITY_NAME = "ENTITY_NAME";
	public static final String FNAME_ENTITY_REF = "ENTITY_REF";
	public static final String FNAME_CREATED_AT = "CREATED_AT";
	
	public static final int LEN_OTC = 30;

	public FocOTCDesc() {
		super(FocOTC.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);

		addReferenceField();

		FStringField focCharFld = new FStringField(FNAME_OTC, "OTC", FLD_OTC, true, LEN_OTC);
		addField(focCharFld);

		FObjectField focObjFld = new FObjectField(FNAME_USER, "User", FLD_USER, false, FocUserDesc.getInstance(), "USER_");
		focObjFld.setSelectionList(FocUserDesc.getList(FocList.NONE));
		focObjFld.setComboBoxCellEditor(FocUserDesc.FLD_NAME);
		addField(focObjFld);

		focCharFld = new FStringField(FNAME_ENTITY_NAME, "ENTITY NAME", FLD_ENTITY_NAME, false, 100);
		addField(focCharFld);

		FLongField focLongFld = new FLongField(FNAME_ENTITY_REF, "ENTITY REF", FLD_ENTITY_REF, false, 20);
		addField(focLongFld);

		FDateTimeField dateTimeFld = new FDateTimeField(FNAME_CREATED_AT, "CREATED AT", FLD_CREATED_AT, false);
		addField(dateTimeFld);
	}

	public void afterAdaptTableModel() {
	}

	public static FocList getList() {
		return getList(FocList.LOAD_IF_NEEDED);
	}

	public static FocList newList() {
		FocLink link = new FocLinkSimple(getInstance());
		FocList list = new FocList(link);
		list.setDirectImpactOnDatabase(true);
		list.setDirectlyEditable(false);
		return list;
	}

	public FocList newFocList() {
		FocList list = super.newFocList();
		list.setDirectImpactOnDatabase(true);
		list.setDirectlyEditable(false);
		return list;
	}

	public static FocList getList(int mode) {
		return getInstance().getFocList(mode);
	}

	public static FocOTCDesc getInstance() {
		return (FocOTCDesc) getInstance(DB_TABLE_NAME, FocOTCDesc.class);
	}
}
