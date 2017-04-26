package com.foc.list.filter;

import com.foc.desc.FocDesc;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.field.FStringField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public abstract class FocDescForFilter extends FocDesc implements IFocDescForFilter {
	
	public abstract FilterDesc getFilterDesc();

	protected FilterDesc filterDesc = null; 

	public FocDescForFilter(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
		this(focObjectClass, dbResident, storageName, isKeyUnique, false);
	}
	
	public FocDescForFilter(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique, boolean addSyncFields){
		super(focObjectClass, dbResident, storageName, isKeyUnique);
		setGuiBrowsePanelClass(FocListFilterGuiBrowsePanel.class);
		setGuiDetailsPanelClass(FocListFilterGuiDetailsPanel.class);
	
		addReferenceField();
		addNameField();

    if(getFilterDesc() != null){
      getFilterDesc().fillDesc(this, 1);
    }
	}

	public FocList newFilterFocList(int mode){
		FocListOrder order = null;
		
		FocFieldEnum enumer = new FocFieldEnum(this, FocFieldEnum.CAT_KEY, FocFieldEnum.LEVEL_PLAIN);
		while(enumer != null && enumer.hasNext()){
			enumer.next();				
			if(order == null) order = new FocListOrder();
			order.addField(enumer.getFieldPath());
		}
		
		FocList list = getList(null, mode, order);

		list.setDirectImpactOnDatabase(true);
		list.setDirectlyEditable(false);
		return list;
	}
}