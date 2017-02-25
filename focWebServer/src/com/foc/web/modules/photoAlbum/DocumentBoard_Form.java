package com.foc.web.modules.photoAlbum;

import java.util.ArrayList;
import java.util.Iterator;

import com.foc.access.FocDataMap;
import com.foc.business.photoAlbum.DocumentType;
import com.foc.business.photoAlbum.DocumentTypeDesc;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class DocumentBoard_Form extends FocXMLLayout {
	
	private FocObject objectOfAttachments = null;
	
	private ArrayList<TypeButtonListener> listenerArray = null;
	
	@Override
	public void dispose(){
		super.dispose();
		if(listenerArray != null){
			for(int i=0; i<listenerArray.size(); i++){
				TypeButtonListener listener = listenerArray.get(i);
				if(listener != null){
					listener.dispose();
				}
			}
		}
		objectOfAttachments = null;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		FocList typeList = DocumentTypeDesc.getInstance().getFocList();
		typeList.loadIfNotLoadedFromDB();
		
		Iterator<FocXMLGuiComponent> iter = getComponentMapIterator();
		while(iter != null && iter.hasNext()){
			FocXMLGuiComponent comp = iter.next();
			if(comp != null && comp instanceof FVButton){
				String name = comp.getDelegate().getNameFromAttributes();
				if(name != null){
					DocumentType type = (DocumentType) typeList.searchByPropertyStringValue(FField.FLD_NAME, name);
					if(type != null){
						FVButton button = (FVButton) comp;
						
						TypeButtonListener listener = new TypeButtonListener(type, button);
						if(listenerArray == null) listenerArray = new ArrayList<DocumentBoard_Form.TypeButtonListener>();
						listenerArray.add(listener);
					}
				}		
			}
		}
		
	}
	
	public class TypeButtonListener implements ClickListener {
		
		private DocumentType type = null;
		private FVButton button = null;
		
		public TypeButtonListener(DocumentType type, FVButton button){
			this.type = type;
			this.button = button;
			if(button != null){
				button.addClickListener(this);
			}
		}
		
		public void dispose(){
			type = null;
			if(button != null){
				button.removeClickListener(this);
				button = null;
			}
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			PhotoAlbumListWithFilter focList = new PhotoAlbumListWithFilter(objectOfAttachments);
			if(focList != null){
				focList.loadIfNotLoadedFromDB();

				FocDataMap map = new FocDataMap(focList);
				map.put(PhotoAlbum_Table.KEY_FILTER_TYPE, type);
				
				INavigationWindow window = getMainWindow();
        XMLViewKey xmlViewKey = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, PhotoAlbumWebModule.CTXT_ALL_DOCUMENTS, XMLViewKey.VIEW_DEFAULT);
      	ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(window, xmlViewKey, map);
      	centralPanel.setFocDataOwner(true);
      	window.changeCentralPanelContent(centralPanel, true);
			}
		}
		
	}

	public FocObject getObjectOfAttachments() {
		return objectOfAttachments;
	}

	public void setObjectOfAttachments(FocObject objectOfAttachments) {
		this.objectOfAttachments = objectOfAttachments;
	}
}