package com.foc.web.modules.photoAlbum;

import java.io.File;
import java.io.InputStream;

import com.foc.access.FocDataMap;
import com.foc.business.photoAlbum.DocumentType;
import com.foc.business.photoAlbum.DocumentTypeDesc;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbum.PhotoAlbumFileResource;
import com.foc.business.photoAlbum.PhotoAlbumAccessDesc;
import com.foc.business.photoAlbum.PhotoAlbumAppGroup;
import com.foc.business.photoAlbum.PhotoAlbumConfig;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FCloudStorageProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.BlobResource;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTablePopupMenu;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class PhotoAlbum_Table extends FocXMLLayout {

	public static final String KEY_FILTER_TYPE = "FILTER_TYPE";
	
	private FVComboBox   documentTypeFilterComboBox = null;
	private Button       unrelatedAttachmentsButton = null;
	private DocumentType filteredType               = null;
	
	@Override
	public void dispose() {
		super.dispose();
		unrelatedAttachmentsButton = null;
		if(documentTypeFilterComboBox != null){
			documentTypeFilterComboBox.dispose();
			documentTypeFilterComboBox = null;
		}
	}
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		FocListWrapper wrapper = null;
		
		if(focData != null){
			Object obj = focData.iFocData_getDataByPath(KEY_FILTER_TYPE);
			if(obj instanceof DocumentType){
				filteredType = (DocumentType) obj;
			}
		}

		if(focData instanceof FocDataMap){
			FocDataMap map = (FocDataMap) focData;
			focData = map.getMainFocData();
		}

		if(focData instanceof FocList){
			FocList photoAlbumList = (FocList) focData; 
			wrapper = new FocListWrapper(photoAlbumList);
		}else if(focData instanceof FocListWrapper){
			wrapper = (FocListWrapper) focData;
		}
		
		focData = wrapper;
		addFilterByDocumentTypeAndAccessRights(wrapper);
    
		super.init(window, xmlView, focData);
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
    if(showUploadButton()){
    	addUploadButton();
    }
    
    if(showDetachedGallery()){
    	//If he cannot upload he cannot assotiate unrelated documents
    	addUnrelatedAttachmentsButton();
    }
		
    addPopupMenu_DownAttachment();
    addDocumentTypeFilterComboBox();
    
    PhotoAlbumConfig config = PhotoAlbumConfig.getInstance();
    if(config != null && config.isSingleGroup()){
			FVTableWrapperLayout treeWrapper = getTableWrapperLayout();
			if(treeWrapper != null && treeWrapper.getTableTreeDelegate() != null){
				treeWrapper.getTableTreeDelegate().removeColumn("ALLOWED_GROUPS");
			}
    }
  }
	
	private void addDocumentTypeFilterComboBox(){
		FVTableWrapperLayout treeWrapper = getTableWrapperLayout();
		if(treeWrapper != null){
			treeWrapper.addHeaderComponent_ToLeft(getDocumentTypeComboBox());
		}
		if(filteredType != null){
			getDocumentTypeComboBox().select(filteredType.getCaption());
		}
	}
	
	private FVComboBox getDocumentTypeComboBox(){
		if(documentTypeFilterComboBox == null){
			documentTypeFilterComboBox = new FVComboBox();
			documentTypeFilterComboBox.setImmediate(true);
			documentTypeFilterComboBox.setWidth("300px");
			documentTypeFilterComboBox.setInputPrompt(" -Document Type- ");
			FocListWrapper focListWrapper = getTableWrapperLayout() != null && getTableWrapperLayout().getFocDataWrapper() instanceof FocListWrapper ? (FocListWrapper)getTableWrapperLayout().getFocDataWrapper() : null;
			focListWrapper.setTableTreeDelegate(getTableWrapperLayout().getTableTreeDelegate());
			FocList documentTypesList = DocumentTypeDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
			if(documentTypesList != null){
//				documetnTypeFilterComboBox.addItem(DocumentTypeDesc.SELECT_ALL);
				for(int i=0; i<documentTypesList.size(); i++){
					DocumentType documentType = (DocumentType) documentTypesList.getFocObject(i);
					documentTypeFilterComboBox.addItem(documentType.getCaption());
				}
			}
			documentTypeFilterComboBox.addValueChangeListener(new Property.ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					FocListWrapper focListWrapper = getTableWrapperLayout() != null && getTableWrapperLayout().getFocDataWrapper() instanceof FocListWrapper ? (FocListWrapper)getTableWrapperLayout().getFocDataWrapper() : null;
					if(focListWrapper != null){
						filteredType = null;
						Object selectedValue = getDocumentTypeComboBox().getValue();
						if(!Utils.isStringEmpty((String)selectedValue)){
							FocList typeList = DocumentTypeDesc.getInstance().getFocList();
							if(typeList != null){
								typeList.loadIfNotLoadedFromDB();
								filteredType = (DocumentType) typeList.searchByPropertyStringValue(DocumentTypeDesc.FLD_CAPTION, (String)selectedValue);
							}
						}
								
						focListWrapper.resetVisibleListElements();
//						focListWrapper.refreshGuiForContainerChanges();
						getTableWrapperLayout().getTableTreeDelegate().refresh_CallContainerItemSetChangeEvent();
					}
				}
			});
		}
		return documentTypeFilterComboBox;
	}
	
	private void addFilterByDocumentTypeAndAccessRights(FocListWrapper focListWrapper){
		if(focListWrapper != null){
			focListWrapper.addContainerFilter(new Filter() {
				
				@Override
				public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
					int include = PhotoAlbumAccessDesc.READ_ONLY;//false;
					PhotoAlbum photoAlbum = item instanceof PhotoAlbum ? (PhotoAlbum) item : null;
					
					if(filteredType != null && !FocObject.equal(filteredType, photoAlbum.getDocumentType())){
						//In this case a type is selected and it does not match
						include = PhotoAlbumAccessDesc.READ_ONLY;//false;
					}else{
//						include = photoAlbum.hasAccess();
						include = photoAlbum.hasAccess();
					}
					
					return include == PhotoAlbumAccessDesc.READ_WRITE ? true : false;
				}
				
				@Override
				public boolean appliesToProperty(Object propertyId) {
					return false;
				}
			});
		}
	}
	
	public PhotoAlbumListWithFilter getPhotoAlbumList(){
		FocList list = null;
		if(getFocData() instanceof FocDataWrapper){
			list = (FocList) getFocData();
		}else{
			list = getFocList(); 
		}
		return list instanceof PhotoAlbumListWithFilter ? (PhotoAlbumListWithFilter) list : null; 
	}
	
	protected boolean showUploadButton(){
		PhotoAlbumAppGroup group = PhotoAlbumAppGroup.getCurrentAppGroup();
		return group != null && group.isAllowUpload();
	}

	protected boolean showDetachedGallery(){
		PhotoAlbumAppGroup group = PhotoAlbumAppGroup.getCurrentAppGroup();
		return group != null && group.isAllowAccessToDetachedGallery();
	}

	private FVTableWrapperLayout getTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("PHOTO_ALBUM");
	}
  
	protected FVUpload_Image newUploadButton(){
	  FVUpload_Image uploadImage = new FVUpload_Image();
	  FVImageReceiver imageReceiver = new FVImageReceiver() {
	    public void imageReceived(SucceededEvent event, InputStream inputStream) {
	    	addPhotoToAlbum(event.getFilename(), inputStream);
	    }
	  };
	  uploadImage.setImageReceiver(imageReceiver);
	  return uploadImage;
	}
	
  private void addUploadButton(){
  	FVTableWrapperLayout tableWrapper = getTableWrapperLayout();
  	if(tableWrapper != null){
  		tableWrapper.setSpacing(true);
	    FVUpload_Image uploadImage = newUploadButton();
	    tableWrapper.addHeaderComponent_ToLeft(uploadImage);
  	}
  }
  
  private void addUnrelatedAttachmentsButton(){
		FVTableWrapperLayout tableWrapper = getTableWrapperLayout();
  	if(tableWrapper != null){
  		unrelatedAttachmentsButton = new Button("Add From Detached Galery", new Button.ClickListener() {
  			@Override
  			public void buttonClick(ClickEvent event) {
  				PhotoAlbumListWithFilter photoAlbumFilter = new PhotoAlbumListWithFilter();
  				photoAlbumFilter.applyFilterOnUnrelatedObjects();
  				photoAlbumFilter.reloadFromDB();
  				XMLViewKey xmlViewKey = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, PhotoAlbumWebModule.CTXT_UNRELATED_ATTACHMENTS, XMLViewKey.VIEW_DEFAULT);
  				PhotoAlbum_UnrelatedAttachments_Table centralPanel = (PhotoAlbum_UnrelatedAttachments_Table) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(getMainWindow(), xmlViewKey, photoAlbumFilter);
  				centralPanel.setAttachmentsList((PhotoAlbumListWithFilter) getFocList());
  				centralPanel.parseXMLAndBuildGui();
  				getMainWindow().changeCentralPanelContent(centralPanel, true);
  			}
  		});
  		
  		tableWrapper.addHeaderComponent_ToLeft(unrelatedAttachmentsButton);
  	}  		
  }
  
  @Override
  public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
  	ColumnGenerator columnGenerator = null;

  	if(tableColumn.getName().equals("DOWNLOAD")){
  		columnGenerator = new ColumnGenerator() {
  			public Object generateCell(Table source, Object itemId, Object columnId) {
  				long objId = (Long) itemId;
  				FVButton button = new FVButton(tableColumn.getCaption());
  				Resource iconResource = FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_DOWNLOAD);
  				button.setStyleName(Runo.BUTTON_LINK);
  				button.setWidth("-1px");
  				button.setHeight("-1px");
  				button.setIcon(iconResource);
  				PhotoAlbumFileResource resource = getPhotoAlbumFileResource(objId);
					FileDownloader downloader = new FileDownloader(resource);
					downloader.extend(button);
  				return button;
  			}
  		};
  	}
  	return columnGenerator;
  }
  
  private PhotoAlbumFileResource getPhotoAlbumFileResource(long objId){
  	PhotoAlbum album = (PhotoAlbum) getFocList().searchByReference(objId);
  	PhotoAlbumFileResource resource = album.new PhotoAlbumFileResource(new File(""), album);
  	return resource;
  }

  public String getFormContextAfterUpload() {
  	return XMLViewKey.CONTEXT_DEFAULT;
  }
  
  public String getFormViewAfterUpload() {
  	return XMLViewKey.VIEW_DEFAULT;
  }
  
  public void addPhotoToAlbum(String fileName, InputStream inputStream){
    if(inputStream != null){
    	FocWebApplication application = FocWebApplication.getInstanceForThread();
      
      if(application != null){
      	FocListWrapper wrapper = (FocListWrapper) getFocData();
      	PhotoAlbumListWithFilter photoAlbumList = (PhotoAlbumListWithFilter) wrapper.getFocList();
      	
//        PhotoAlbumListWithFilter photoAlbumList = getPhotoAlbumList();
        PhotoAlbum photoAlbum = photoAlbumList.addPhotoAlbum(fileName, inputStream);
        if(photoAlbum != null){
        	photoAlbum.setDocumentType(filteredType);
	        refresh();
	        XMLViewKey key = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, getFormContextAfterUpload(), getFormViewAfterUpload());
	        getMainWindow().changeCentralPanelContent(XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, photoAlbum), true);
        }
      }
    }
  }
  
	public void addPopupMenu_DownAttachment(){
		FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getComponentByName("PHOTO_ALBUM");
		if(tableWrapper != null){
			tableWrapper.getTableTreeDelegate().addPopupMenu(new FVTablePopupMenu("Download Attached File"){
				@Override
				public void actionPerformed(FocObject focObject) {
					PhotoAlbum photoAlbum = (PhotoAlbum) focObject;
					if(photoAlbum != null){
						FCloudStorageProperty cloudStorageProperty = (FCloudStorageProperty) photoAlbum.getFocProperty(PhotoAlbumDesc.FLD_IMAGE);
						BlobResource blobResource = BlobResource.newBlobResource(cloudStorageProperty);
						blobResource.openDownloadWindow();
					}
				}	
			});
		}
	}

	public DocumentType getFilteredType() {
		return filteredType;
	}

	public void setFilteredType(DocumentType filteredType) {
		this.filteredType = filteredType;
	}
}