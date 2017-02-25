package com.fab.model.table.underlyingCustomisation;

import com.fab.model.table.TableDefinition;
import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.list.FocList;

public class UndCustTable extends TableDefinition{
	public UndCustTable(FocConstructor constr){
		super(constr);
		newFocProperties();
		setPropertyBoolean(UndCustTableDesc.FLD_DB_RESIDENT, true );
		setPropertyBoolean(UndCustTableDesc.FLD_KEY_UNIQUE , false);
		setPropertyBoolean(UndCustTableDesc.FLD_WITH_REF   , false);
	}
	
	@Override
	public boolean isAlreadyExisting() {
		return true;
	}
	
	private static UndCustTable undCustomFields = null;
	public static UndCustTable getInstance(){
		if(undCustomFields == null){
			if(!Globals.getApp().isEmptyDatabaseJustCreated()){//20151216-Without this condition we get error messages when the SaaS Empty database is entered the first time.
				FocList list = UndCustTableDesc.getList(FocList.LOAD_IF_NEEDED);
				if(list != null){
					undCustomFields = (UndCustTable) list.getAnyItem();
					if(undCustomFields == null){
						undCustomFields = (UndCustTable) list.newEmptyItem();
						undCustomFields.setName("UNDERLYING_USER_FIELDS");
						list.add(undCustomFields);
						if(list.size() == 1){
							list.validate(true);
						}
					}
				}
			}
		}
		return undCustomFields;
	}
}
