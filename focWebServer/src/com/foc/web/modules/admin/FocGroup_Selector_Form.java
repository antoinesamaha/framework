package com.foc.web.modules.admin;

import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

@SuppressWarnings("serial")
public class FocGroup_Selector_Form extends FocXMLLayout {
	
	private FVComboBox combo         = null;
	private FocGroup   selectedGroup = null;

	public void dispose(){
		super.dispose();
		if(combo != null){
			combo.dispose();
			combo = null;
		}
		selectedGroup = null;
	}
	
	public FocGroup getSelectedGroup() {
		return selectedGroup;
	}
	
	private FVVerticalLayout getVerticalLayout(){
		return (FVVerticalLayout) getComponentByName("_MAIN_LAYOUT");
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		combo = new FVComboBox();
		FocList focList = FocGroup.getList(FocList.LOAD_IF_NEEDED);
		for(int i=0; i<focList.size(); i++){
			FocGroup group = (FocGroup) focList.getFocObject(i);
			
			combo.addItem(group.getName());
		}
		
		combo.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				selectedGroup = null;
				if(combo != null){
					String groupName = (String) combo.getValue();
					FocList focList = FocGroup.getList(FocList.LOAD_IF_NEEDED);
					if(focList != null){
						selectedGroup = (FocGroup) focList.searchByPropertyStringValue(FocGroupDesc.FLD_NAME, groupName);
					}
				}
			}
		});
		getVerticalLayout().addComponent(combo);
	}
	
}
