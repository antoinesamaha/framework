package com.foc.web.modules.photoAlbum;

import java.util.List;

import com.vaadin.data.Container.Filter;
import com.foc.access.FocDataMap;
import com.foc.admin.DocRightsGroup;
import com.foc.admin.DocRightsGroupDesc;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumAccess;
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
public class PhotoAlbumAccess_GroupAccessRightSelection_Table extends FocXMLLayout{

	public static final String DATA_MAP_KEY_SOURCE_LIST = "SOURCE_LIST";
	boolean enabled = false;
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		if(focData != null && focData instanceof FocList){
			FocList photoAlbumAccessList = (FocList) focData;
			PhotoAlbum photoAlbum = (PhotoAlbum) photoAlbumAccessList.getFatherSubject();
			if(photoAlbum != null){
				enabled = photoAlbum.isEditable();
			}
		}
		
		setEnabled(enabled);
  	//-----------------------------------Not Selected Groups List-----------------------------------
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
				FocList photoAlbumAccessList = getPhotoAlbum().getPhotoAlbumAccessList();
				for(int i=0;i<photoAlbumAccessList.size();i++){
					PhotoAlbumAccess photoAlbumAccess = (PhotoAlbumAccess) photoAlbumAccessList.getFocObject(i);
					if(photoAlbumAccess.getDocRightsGroup().equalsRef(docRightsGroup)){
						include = false;
					}
				}
				return include;
			}

			public boolean appliesToProperty(Object propertyId) {
				return false;
			}
  		
  	});
	  //--------------------------------------------------------------------------------------------
		
		FocDataMap focDataMap = new FocDataMap(focData);
  	focDataMap.put(DATA_MAP_KEY_SOURCE_LIST, notSelectedGroupsList);

		super.init(window, xmlView, focDataMap);
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}
	
	/*
	public static ICentralPanel popupPhotoAlbumAccess_GroupAccessRightSelection_Form(INavigationWindow navigationWindow, final PhotoAlbum photoAlbum, boolean keepPrevious){
		
		
  	//-----------------------------------Selected Groups List-----------------------------------
		FocListWrapper selectedSuppliersList = new FocListWrapper(photoAlbum.getPhotoAlbumAccessList());
  //--------------------------------------------------------------------------------------------		
		
		
		
		FocDataMap focDataMap = new FocDataMap();
  	focDataMap.put(DATA_MAP_KEY_SOURCE_LIST, notSelectedGroupsList);
  	focDataMap.put(DATA_MAP_KEY_SELECTED_GROUPS_LIST, selectedGroupsList);
  	focDataMap.put(DATA_MAP_KEY_PHOTO_ALBUM, photoAlbum);
  	
  	XMLViewKey xmlViewKey = new XMLViewKey(PhotoAlbumAccessDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, PhotoAlbumWebModule.CONTEXT_GROUP_ACCESS_RIGHT_SELECTION, XMLViewKey.VIEW_DEFAULT);
  	FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(navigationWindow, xmlViewKey, focDataMap);
  	navigationWindow.changeCentralPanelContent(centralPanel, keepPrevious);
  	return centralPanel;
	}
	*/
	
	private FocDataMap getFocDataMap(){
		return (FocDataMap) getFocData();
	}
	
	private PhotoAlbum getPhotoAlbum(){
		return getFocList() != null ? (PhotoAlbum) getFocList().getFatherSubject() : null;
	}
	
	private FocListWrapper getNotSelectedGroupsList(){
		return getFocDataMap() != null ? (FocListWrapper) getFocDataMap().get(DATA_MAP_KEY_SOURCE_LIST) : null;
	}
	
//	private FocListWrapper getSelectedGroupsList(){
//		return getFocDataMap() != null ? (FocListWrapper) getFocDataMap().get(DATA_MAP_KEY_SELECTED_GROUPS_LIST) : null;
//	}
	
	private FVTableWrapperLayout getNotSelectedGroupsTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("NOT_SELECTED_GROUPS_LIST_TABLE");
	}
	
	private TableTreeDelegate getNotSelectedGroupsTableTreeDelegate(){
		return getNotSelectedGroupsTableWrapperLayout() != null ? getNotSelectedGroupsTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	private FVTableGrid getNotSelectedGroupsGrid(){
		return (FVTableGrid) (getNotSelectedGroupsTableTreeDelegate() != null ? getNotSelectedGroupsTableTreeDelegate().getTreeOrTable() : null);
	}
	
	private FVTableWrapperLayout getSelectedGroupsTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("SELECTED_GROUPS_LIST_TABLE");
	}
	
	private TableTreeDelegate getSelectedGroupsTableTreeDelegate(){
		return getSelectedGroupsTableWrapperLayout() != null ? getSelectedGroupsTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	private FVTableGrid getSelectedGroupsGrid(){
		return (FVTableGrid) (getSelectedGroupsTableTreeDelegate() != null ? getSelectedGroupsTableTreeDelegate().getTreeOrTable() : null);
	}
	
	public void button_ADD_BUTTON_Clicked(FVButtonClickEvent evt){
		addSelectedSuppliers();
	}
	
	public void button_REMOVE_BUTTON_Clicked(FVButtonClickEvent evt){
		removeSelectedGroups();
	}
	
	private void addSelectedSuppliers(){
		if(getPhotoAlbum() != null && getNotSelectedGroupsGrid() != null){
			FocList photoAlbumAccessList  = getPhotoAlbum().getPhotoAlbumAccessList();
			List<FocObject> selectedFocObjects = getNotSelectedGroupsGrid().getSelectedFocObjects();
			for(int i=0; i<selectedFocObjects.size(); i++){
				DocRightsGroup group = (DocRightsGroup) selectedFocObjects.get(i);
				if(group != null && getPhotoAlbum().getPhotoAlbumAccessList() != null){
					PhotoAlbumAccess photoAlbumAccess = (PhotoAlbumAccess) photoAlbumAccessList.newEmptyItem();
					photoAlbumAccess.setDocRightsGroup(group);
					photoAlbumAccess.setAccessRightType(PhotoAlbumAccessDesc.READ_WRITE);
					photoAlbumAccess.validate(true);
					refreshListWrappers();
				}
			}
			photoAlbumAccessList.validate(true);
		}
	}
	
	private void removeSelectedGroups(){
		if(getPhotoAlbum() != null && getSelectedGroupsGrid() != null){
			FocList photoAlbumAccessList  = getPhotoAlbum().getPhotoAlbumAccessList();
			List<FocObject> selectedFocObjects = getSelectedGroupsGrid().getSelectedFocObjects();
			for(int i=0; i<selectedFocObjects.size(); i++){
				PhotoAlbumAccess photoAlbumAccess = (PhotoAlbumAccess) selectedFocObjects.get(i);
				photoAlbumAccessList.remove(photoAlbumAccess);
				refreshListWrappers();
			}
			photoAlbumAccessList.validate(true);
		}
	}
	
	private void refreshListWrappers(){
		if(getSelectedGroupsGrid() != null) getSelectedGroupsGrid().deselectAllFoc();
		if(getNotSelectedGroupsGrid() != null) getNotSelectedGroupsGrid().deselectAll();
		if(getNotSelectedGroupsList() != null) getNotSelectedGroupsList().refreshGuiForContainerChanges();
//		if(getSelectedGroupsList() != null) getSelectedGroupsList().refreshGuiForContainerChanges();				
	}
}
