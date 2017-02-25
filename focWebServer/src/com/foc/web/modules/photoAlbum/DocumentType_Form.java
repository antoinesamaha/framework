package com.foc.web.modules.photoAlbum;

import java.util.List;

import com.vaadin.data.Container.Filter;
import com.foc.access.FocDataMap;
import com.foc.admin.DocRightsGroup;
import com.foc.admin.DocRightsGroupDesc;
import com.foc.business.photoAlbum.DocTypeAccess;
import com.foc.business.photoAlbum.DocumentType;
import com.foc.business.photoAlbum.PhotoAlbumAccessDesc;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.tableGrid.FVTableGrid;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.data.Item;

@SuppressWarnings("serial")
public class DocumentType_Form extends FocXMLLayout{

	public static final String DATA_MAP_KEY_SOURCE_LIST = "SOURCE_LIST";
	
	private DocumentType getDocumentType(){
		return getFocDataMap() != null ? (DocumentType) getFocDataMap().getMainFocData() : null;
	}
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		
		FocList docRightsGroupsList = DocRightsGroupDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		FocListWrapper notSelectedGroupsList = new FocListWrapper(docRightsGroupsList);
		notSelectedGroupsList.addContainerFilter(new Filter() {
			
			public boolean passesFilter(Object itemId, Item docRightsGroup) throws UnsupportedOperationException {
				boolean include = false;
				if(docRightsGroup != null){
					DocRightsGroup group = (DocRightsGroup) docRightsGroup;
					include = checkIfGroupHasViewingRights(group);
				}
				return include;
			}
			
			private boolean checkIfGroupHasViewingRights(DocRightsGroup docRightsGroup) {
				boolean include = true;
				FocList docTypeAccessList = getDocumentType().getDocumentTypeAccessList();
				for(int i=0;i<docTypeAccessList.size();i++){
					DocTypeAccess docTypeAccess = (DocTypeAccess) docTypeAccessList.getFocObject(i);
					if(docTypeAccess.getDocRightsGroup().equalsRef(docRightsGroup)){
						include = false;
					}
				}
				return include;
			}

			public boolean appliesToProperty(Object propertyId) {
				return false;
			}
  		
  	});
		
		FocDataMap focDataMap = new FocDataMap(focData);
  	focDataMap.put(DATA_MAP_KEY_SOURCE_LIST, notSelectedGroupsList);
  	
  	super.init(window, xmlView, focDataMap);
	}
	
	public void button_ADD_BUTTON_Clicked(FVButtonClickEvent evt){
		addSelectedGroup();
	}
	
	public void button_REMOVE_BUTTON_Clicked(FVButtonClickEvent evt){
		removeSelectedGroups();
	}
	
	private void removeSelectedGroups(){
		if(getDocumentType() != null && getSelectedGroupsGrid() != null){
			FocList docTypeAccessList  = getDocumentType().getDocumentTypeAccessList();
			List<FocObject> selectedFocObjects = getSelectedGroupsGrid().getSelectedFocObjects();
			for(int i=0; i<selectedFocObjects.size(); i++){
				DocTypeAccess docTypeAccess = (DocTypeAccess) selectedFocObjects.get(i);
				docTypeAccessList.remove(docTypeAccess);
				refreshListWrappers();
			}
			docTypeAccessList.validate(true);
		}
	}
	
	private void addSelectedGroup(){
		if(getDocumentType() != null && getNotSelectedGroupsGrid() != null){
			FocList docTypeAccessList  = getDocumentType().getDocumentTypeAccessList();
			List<FocObject> selectedFocObjects = getNotSelectedGroupsGrid().getSelectedFocObjects();
			for(int i=0; i<selectedFocObjects.size(); i++){
				DocRightsGroup group = (DocRightsGroup) selectedFocObjects.get(i);
				if(group != null && getDocumentType().getDocumentTypeAccessList() != null){
					DocTypeAccess docTypeAccess = (DocTypeAccess) docTypeAccessList.newEmptyItem();
					docTypeAccess.setDocRightsGroup(group);
					docTypeAccess.setAccessRightType(PhotoAlbumAccessDesc.READ_WRITE);
					docTypeAccess.validate(true);
					refreshListWrappers();
				}
			}
			docTypeAccessList.validate(true);
		}
	}
	
	//Not Selected Group List
	private FVTableGrid getNotSelectedGroupsGrid(){
		return (FVTableGrid) (getNotSelectedGroupsTableTreeDelegate() != null ? getNotSelectedGroupsTableTreeDelegate().getTreeOrTable() : null);
	}
	
	private TableTreeDelegate getNotSelectedGroupsTableTreeDelegate(){
		return getNotSelectedGroupsTableWrapperLayout() != null ? getNotSelectedGroupsTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	private FVTableWrapperLayout getNotSelectedGroupsTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("NOT_SELECTED_GROUPS_LIST_TABLE");
	}
	//Not Selected Group List
	
	//Selected Group List
	private FVTableGrid getSelectedGroupsGrid(){
		return (FVTableGrid) (getSelectedGroupsTableTreeDelegate() != null ? getSelectedGroupsTableTreeDelegate().getTreeOrTable() : null);
	}
	
	private TableTreeDelegate getSelectedGroupsTableTreeDelegate(){
		return getSelectedGroupsTableWrapperLayout() != null ? getSelectedGroupsTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	private FVTableWrapperLayout getSelectedGroupsTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("DOC_TYPE_ACCESS_TABLE");
	}
	//Selected Group List
	
	private FocListWrapper getNotSelectedGroupsList(){
		return getFocDataMap() != null ? (FocListWrapper) getFocDataMap().get(DATA_MAP_KEY_SOURCE_LIST) : null;
	}
	
	private FocDataMap getFocDataMap(){
		return (FocDataMap) getFocData();
	}
	
	private void refreshListWrappers(){
		if(getSelectedGroupsGrid() != null) getSelectedGroupsGrid().deselectAllFoc();
		if(getNotSelectedGroupsGrid() != null) getNotSelectedGroupsGrid().deselectAll();
		if(getNotSelectedGroupsList() != null) getNotSelectedGroupsList().refreshGuiForContainerChanges();
	}
}
