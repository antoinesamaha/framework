package com.foc.web.modules.link;

import java.util.ArrayList;

import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.link.FocLinkOutBox;
import com.foc.link.FocLinkOutBoxDetail;
import com.foc.link.FocLinkOutBoxDetailDesc;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class FocLink_Out_Log_Post_Form extends FocXMLLayout {
	
	private FocXMLLayout userSelectionLayout = null;
	
	public FocLinkOutBox getFocLink_Out_Log(){
		return (FocLinkOutBox) getFocData();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if(userSelectionLayout != null){
			userSelectionLayout.dispose();
			userSelectionLayout = null;
		}
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();

		if(userSelectionLayout == null){

			FocList userList = FocUserDesc.getList();
			if(userList != null){
				XMLViewKey key = new XMLViewKey(userList.getFocDesc().getStorageName(), XMLViewKey.TYPE_TABLE, LinkWebModule.CTXT_POST, XMLViewKey.VIEW_DEFAULT);
				userSelectionLayout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, userList);
				userSelectionLayout.setParentLayout(this);
				addComponent((Component) userSelectionLayout);
			}
		}

	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		if(getFocLink_Out_Log() != null && userSelectionLayout != null){
			FVTableWrapperLayout userSelectionTableWrapperLayout = (FVTableWrapperLayout) getComponentByName("USER_SELECTION_TABLE");

			if(userSelectionTableWrapperLayout != null && userSelectionTableWrapperLayout.getTableTreeDelegate() != null){

				ArrayList<Object> selectionArray = userSelectionTableWrapperLayout.getTableTreeDelegate().selectionColumn_getSelectedIdArrayList();

				if(selectionArray != null){

					FocList detailsList = getFocLink_Out_Log().getDetailsList();
					FocList userList = (FocList) userSelectionLayout.getFocData();

					if(detailsList != null && userList != null){
						for(int i=0; i<selectionArray.size(); i++){
							Integer userId = (Integer) selectionArray.get(i);

							if(userId != null){
								FocUser currentUser = (FocUser) userList.searchByReference(userId);

								if(currentUser != null){
									FocLinkOutBoxDetail details = (FocLinkOutBoxDetail) detailsList.newEmptyItem();

									details.setToUser(currentUser);
									details.setStatus(FocLinkOutBoxDetailDesc.STATUS_POSTED);

									detailsList.add(details);

									getFocLink_Out_Log().validate(false);
								}
							}
						}
					}
				}
			}
		}
		return super.validationCheckData(validationLayout);
	}
}
