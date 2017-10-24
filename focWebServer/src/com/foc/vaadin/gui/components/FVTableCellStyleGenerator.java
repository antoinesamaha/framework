package com.foc.vaadin.gui.components;

import java.awt.Color;

import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolderDesc;
import com.foc.desc.FocObject;
import com.foc.gui.FColorProvider;
import com.foc.list.FocList;
import com.foc.list.FocListElement;
import com.foc.property.FProperty;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVTableCellStyleGenerator implements Table.CellStyleGenerator{

	private TableTreeDelegate tableTreeDelegate = null;
	
	public FVTableCellStyleGenerator(TableTreeDelegate tableTreeDelegate) {
		this.tableTreeDelegate = tableTreeDelegate;
	}
	
	public void dispose(){
		tableTreeDelegate = null;
	}
	
	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		String style = null;
		if(tableTreeDelegate != null && getTreeOrTable() != null){
			FVTableColumn column = tableTreeDelegate.findColumn((String) propertyId);
			if(column != null){
				style = column.getStyle();

				if(column.isHasStyleForColor()){
					if(itemId != null){
						FocList focList = getTreeOrTable().getFocList();
						FocObject focObject = focList.searchByReference((Integer) itemId);
						if(focObject != null){
							FProperty prop = focObject.getFocPropertyForPath(column.getDataPath());
							if(prop != null){
								Color color = prop.getBackground();
								if(color == FColorProvider.getAlertColor()){
									style = "warning";
								}
							}
						}
					}
				}
				
				if(tableTreeDelegate.getWrapperLayout() != null && tableTreeDelegate.getWrapperLayout().isAutoRefresh()){
					FocList focList = getTreeOrTable().getFocList();
					if(focList != null){
						FocObject focObject = focList.searchByReference((Integer) itemId);
						if(focObject != null && focObject.isFreshColor()){
							style = "notCompletedYet";//We should have a dedicated style.
							//But this color is good
						}
					}
				}
				
				if(tableTreeDelegate.isStatusStyleEnabled()){
					if(style == null || style.isEmpty()){
						if(itemId != null){
							FocList focList = getTreeOrTable().getFocList();
							if(focList != null){
								FocObject focObject = focList.searchByReference((Long) itemId);
								if(focObject != null){
									if(focObject.isNotCompletedYet()){
										style = "notCompletedYet";
									}else if(focObject instanceof IStatusHolder){
										IStatusHolder iStatusHolder = (IStatusHolder) focObject;
										if(iStatusHolder.getStatusHolder() != null){
											int status = iStatusHolder.getStatusHolder().getStatus();
											if(status == StatusHolderDesc.STATUS_PROPOSAL){
												style = "proposal";
											}else if(status == StatusHolderDesc.STATUS_CANCELED){
												style = "canceled";
											}else if(status == StatusHolderDesc.STATUS_CLOSED){
												style = "closed";
											}else if(status == StatusHolderDesc.STATUS_APPROVED){
												style = "approved";																				
											}
										}
									}
								}
							}
						}
					}					
				}
				
				if(style == null || style.isEmpty()){
					style="foc-tablecellprint";
				}
			}
		}
		return style;
	}
	
	private ITableTree getTreeOrTable(){
		return tableTreeDelegate != null ? tableTreeDelegate.getTreeOrTable() : null;
	}

}
