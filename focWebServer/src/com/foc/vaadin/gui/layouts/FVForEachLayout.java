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
package com.foc.vaadin.gui.layouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;
import com.foc.list.FocList;
import com.foc.list.FocListOrderFocObject;
import com.foc.property.FAttributeLocationProperty;
import com.foc.property.validators.PropertyAndFieldPath;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVLine;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class FVForEachLayout extends FVVerticalLayout {
  
  private FocXMLLayout            xmlLayout          = null;
//	private FocList                 focList            = null;
	private FocListOrderFocObject   listOrder          = null;	
	private XMLViewKey              xmlViewKey         = null;

  private FocListWrapper          focListWrapper         = null;   
  private boolean                 focListWrapper_Owner   = false;	
	
	private FVButton                addNewLineButton   = null;
	private FVVerticalLayout        bannerContainer    = null;
	
	private ArrayList<FVBannerLayout> bannerList   = null;
	
	private boolean paginate  = false;
	private int     pageCount = 20;
	private int     pageStart = 0;
	private int     numberOfDisplayedElements = 0;
	private String 									pageNextButtonStyleName = "";
	
	public FVForEachLayout(FocXMLLayout xmlLayout, IFocData focList, XMLViewKey xmlViewKey, Attributes attributes) {
		this(xmlLayout, focList, xmlViewKey, attributes, false, "");
	}
	
  public FVForEachLayout(FocXMLLayout xmlLayout, IFocData focList, XMLViewKey xmlViewKey, Attributes attributes, boolean withPagination, String nextButtonStyleName) {
  	super(attributes);
  	setCaption(null);
  	setSpacing(false);
  	setMargin(false);
  	paginate = withPagination;
  	pageNextButtonStyleName = nextButtonStyleName;
  	
  	bannerContainer = new FVVerticalLayout(null);
  	bannerContainer.setSpacing(false);
  	bannerContainer.setMargin(false);
  	bannerContainer.setCaption(null);
  	bannerVerticalContainerCreated(bannerContainer);
  	addComponent(bannerContainer);
  	
  	this.xmlLayout  = xmlLayout;
  	this.xmlViewKey = xmlViewKey;
 	  addNewLine_Button();
  	
  	setFocData(focList);
  	applyEditable();
  }
  
  public void dispose(){
  	super.dispose();
  	if(focListWrapper != null){
  		if(focListWrapper_Owner){
  			focListWrapper.dispose();
  		}
  		focListWrapper = null;
  	}
  	xmlLayout = null;
  	
  	if(bannerList != null){
      for(int i=0; i<getBannerList(false).size(); i++){
        FVBannerLayout lay = getBannerList(false).get(i);
        if(lay != null) lay.dispose();
      }
      getBannerList(false).clear();
      bannerList = null;
  	  
  	}
  	if(xmlViewKey != null){
  		xmlViewKey.dispose();
  		xmlViewKey = null;
  	}
  	
  	if(listOrder != null){
  		listOrder.dispose();
  		listOrder = null;
  	}
  }
  
  public void bannerVerticalContainerCreated(FVVerticalLayout bannerContainer) {
  	
  }
  
  public void bannerLayoutCreated(FVBannerLayout lay) {
  	
  }  
  
  public boolean isEditable(){
    FocXMLGuiComponentDelegate delegate = getDelegate();
    return delegate != null ? delegate.isEditable() : true;
  }
  
  public void applyEditable(){
    if(addNewLineButton != null) addNewLineButton.setVisible(isEditable());
    ArrayList<FVBannerLayout> bannerList = getBannerList(false);
    if(bannerList != null){
      for(int i=0; i<bannerList.size(); i++){
        FVBannerLayout banner = bannerList.get(i);
        if(banner != null){
          banner.applyEditable();
        }
      }
    }
  }
  
  private void addNewLine_Button(){
  	if(isAddEnabled()){
	    addNewLineButton = new FVButton("Add New Line");
	    if(getAttributes() != null){
		    if(ConfigInfo.isUnitDevMode() && getAttributes().getValue(FXML.ATT_NAME) != null){
		      addNewLineButton.setDescription(getAttributes().getValue(FXML.ATT_NAME));
		    }
		    if(getAttributes().getValue(FXML.ATT_ADD_BUTTON_CAPTION) != null){
		    	addNewLineButton.setCaption(getAttributes().getValue(FXML.ATT_ADD_BUTTON_CAPTION));
		    }
	    }
	    addNewLineButton.addClickListener(new ClickListener() {
	      
	      @Override
	      public void buttonClick(ClickEvent event) {
	        FocList list = getFocList();
	        FocObject obj = list.newEmptyItem();
	        
	        addBannerForFocObject(obj);
	      }
	    });
	    addComponent(addNewLineButton);
  	}
  }
  
  @Override
  public String getType() {
    return FXML.TAG_INCLUDE_XML_FOR_EACH;
  }

//	public void copyGuiToMemory() {
//		if(arrayofItemLayout != null){
//			for(int i=0; i<arrayofItemLayout.size(); i++){
//				arrayofItemLayout.get(i).copyGuiToMemory();
//			}
//		}
//	}
//
//	@Override
//	public void copyMemoryToGui() {
//		if(arrayofItemLayout != null){
//			for(int i=0; i<arrayofItemLayout.size(); i++){
//				arrayofItemLayout.get(i).copyMemoryToGui()()();
//			}
//		}
//	}

	@Override
	public IFocData getFocData() {
		return focListWrapper;
	}
	
  @Override
  public void setFocData(IFocData focData) {
/*  	if(focData instanceof FocList){
  		this.focList = (FocList) focData;
      if(focList != null){
        //if(focData != focList){
          addCentralPanel(focList);
        //}
      }
  	}  	
  	
*/    if(focData instanceof FocListWrapper){
      this.focListWrapper = (FocListWrapper) focData;
      focListWrapper_Owner = false;
    }else if(focData instanceof FocList){
      FocList list = (FocList) focData;
//      list.loadIfNotLoadedFromDB();
      this.focListWrapper = new FocListWrapper(list);
      focListWrapper_Owner = true;        
    }

		FocXMLAttributes attr = (FocXMLAttributes) getAttributes();
		String expression = attr != null ? attr.getValue(FXML.ATT_FILTER_EXPRESSION) : null;
		if(!Utils.isStringEmpty(expression)){
			focListWrapper.addFilterByExpression(expression);
		}
    addCentralPanel(focListWrapper);
  }
  
  private void prepareListOrder(){
  	FocXMLAttributes attr = (FocXMLAttributes) getAttributes();
    String  sortingExpression = attr != null ? attr.getValue(FXML.ATT_SORTING_EXPRESSION) : null;
    
    if(sortingExpression != null){
      listOrder = new FocListOrderFocObject();
      
      if(sortingExpression.startsWith("-")){
      	sortingExpression = sortingExpression.substring(1);
      	listOrder.setReverted(true);
      }
      
      StringTokenizer stringTokenizer = new StringTokenizer(sortingExpression, ",");
      while(stringTokenizer.hasMoreTokens()){
        String sortingName = stringTokenizer.nextToken();
        
  	    PropertyAndFieldPath propertyAndFieldPath = FAttributeLocationProperty.newFieldPath_PropertyAndField(false, sortingName, getFocList().getFocDesc(), null, false);
  	    FFieldPath           fieldPath            = propertyAndFieldPath.getFieldPath();
//        FFieldPath fieldPath = FFieldPath.newFieldPath(getFocList().getFocDesc(), sortingName);
        if(fieldPath != null){
          listOrder.addField(fieldPath);
        }else{
          Globals.showNotification("Could not resolve sorting expression: ", sortingName, IFocEnvironment.TYPE_WARNING_MESSAGE);
        }
      }
    }        
  }
  
  private void addCentralPanel(FocListWrapper focList){
  	prepareListOrder();
  	
  	ArrayList<FocObject> arrayToDisplay = new ArrayList<FocObject>();
  	
    int start = 0;
    int end   = focList.size()-1; 
    
    if(paginate) {
      start = pageStart;
      end   = pageStart + pageCount -1;
      if(end > focList.size()-1) end = focList.size()-1; 
    }

    for(int i=start; i<=end; i++){
      FocObject focObj = focList.getAt(i);
      FVBannerLayout bannerLayout = findBanner(focObj);
      if(bannerLayout == null){
        arrayToDisplay.add(focObj);
        numberOfDisplayedElements++;
      }
    }
    
    if(listOrder != null){
    	try{
    		Collections.sort(arrayToDisplay, listOrder);
    	}catch(Exception e){
    		Globals.logException(e);
    	}
    }
    
    for(int i=0; i<arrayToDisplay.size(); i++){
      FocObject focObj = arrayToDisplay.get(i);
      addBannerForFocObject(focObj);
    }
    if(paginate) toogleNextPageButtonVisibility();
  }
  
  private void toogleNextPageButtonVisibility() {
  	if(paginate && bannerContainer != null ) {
  		removeNextPageButton();
  		if(focListWrapper != null && focListWrapper.getFocList() != null && focListWrapper.getFocList().size()-1 > numberOfDisplayedElements) {
  			addNextPageLayoutButton();
  		}
  	}
  }

  public void gotoNextPage() {
  	FocList focList = getFocList();
  	if(focList != null && paginate && (pageStart + pageCount) < focList.size()) {
  		pageStart += pageCount;
  	}
  }
  
  public void gotoPreviousPage() {
  	if(paginate) {
  		pageStart -= pageCount;
  		if(pageStart < 0) pageStart = 0;
  	}
  }
  
  private void addNextPageLayoutButton() {
  	if(paginate && bannerContainer != null) {
  		String buttonRefreshValue = "Show more";
  		if(ConfigInfo.isArabic()) buttonRefreshValue = "إظهار المزيد";
	    FVButton nextPageButton = new FVButton(buttonRefreshValue);
			nextPageButton.addStyleName(pageNextButtonStyleName);
			nextPageButton.setWidth("100%");
			nextPageButton.addClickListener( new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					insertNextPageToBanner();
					toogleNextPageButtonVisibility();
				}
			});
			FVBannerLayout bannerLayout = new FVBannerLayout(null);
			bannerLayout.addComponent(nextPageButton);
			bannerLayoutCreated(bannerLayout);			
			bannerContainer.addComponent(bannerLayout);
			getBannerList(true).add(bannerLayout);
  	}
  }
  
  private void removeNextPageButton() {
  	if(paginate && bannerContainer != null) {
  		for(int i=0; i < bannerContainer.getComponentCount(); i++) {
  			FVBannerLayout bannerLayout = (FVBannerLayout) bannerContainer.getComponent(i);
  			if(bannerLayout != null && bannerLayout.getComponent(0) != null && bannerLayout.getComponent(0) instanceof FVButton) {
  				bannerContainer.removeComponent(bannerLayout);
  			}  			
  		}
  	}
  }
  
  public void insertNextPageToBanner() {
  	if(paginate && focListWrapper != null && focListWrapper.getFocList() != null) {
  		int start = numberOfDisplayedElements;
  		if(numberOfDisplayedElements + pageCount < focListWrapper.getFocList().size()) {
  			numberOfDisplayedElements += pageCount;  			
  		} else {
  			numberOfDisplayedElements = focListWrapper.getFocList().size();
  		}  		
			for(int i=start; i < numberOfDisplayedElements; i++){
	      FocObject focObj = focListWrapper.getFocList().getFocObject(i);
	      addBannerForFocObject(focObj, false);
	    }
  	}
  }
  
  private FVBannerLayout findBanner(FocObject focObj){
    FVBannerLayout bannerLayout = null;
    ArrayList<FVBannerLayout> bannerList = getBannerList(false);
    if(bannerList != null){
      for(int i=0; i<bannerList.size() && bannerLayout == null; i++){
        FVBannerLayout layout = bannerList.get(i);
        FocObject currentFocObj = layout.getFocObject();
        if(currentFocObj.equalsRef(focObj)){
          bannerLayout = layout;
        }
      }
    }
    return bannerLayout;
  }
  
  private boolean isDeleteEnabled(){
  	boolean enabled = true;
  	String delEnabledStr = getAttributes().getValue(FXML.ATT_DELETE_ENABLED);
  	if(delEnabledStr != null){
  		delEnabledStr = delEnabledStr.toLowerCase().trim();
  		if(delEnabledStr.equals("false") || delEnabledStr.equals("0")){
  			enabled = false;
  		}
  	}
  	return enabled;
  }

  private boolean isOpenEnabled(){
  	boolean enabled = true;
  	String openEnabledStr = getAttributes().getValue(FXML.ATT_OPEN_ENABLED);
  	if(openEnabledStr != null){
  		openEnabledStr = openEnabledStr.toLowerCase().trim();
  		if(openEnabledStr.equals("false") || openEnabledStr.equals("0")){
  			enabled = false;
  		}
  	}
  	return enabled;
  }  

  private boolean isAddEnabled(){
  	boolean enabled = true;
  	String addEnabledStr = getAttributes().getValue(FXML.ATT_ADD_ENABLED);
  	if(addEnabledStr != null){
  		addEnabledStr = addEnabledStr.toLowerCase().trim();
  		if(addEnabledStr.equals("false") || addEnabledStr.equals("0")){
  			enabled = false;
  		}
  	}
  	return enabled;
  }  
  
  public void addBannerForFocObject(FocObject focObj){
  	addBannerForFocObject(focObj, false);
  }

	public void addBannerForFocObject(FocObject focObj, boolean addOnTop) {
		String userView = getAttributes().getValue(FXML.ATT_VIEW_KEY_VIEW);
		if (userView != null) {
			xmlViewKey.setUserView(userView);
		}
		FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel_NoAdjustmentToLastSelectedView(null, xmlViewKey, focObj);
		if (centralPanel != null) {
			FVBannerLayout bannerLayout = new FVBannerLayout(null);
			centralPanel.setParentLayout(xmlLayout);
			bannerLayout.addCentralPanel(centralPanel);
			bannerLayoutCreated(bannerLayout);
			if (isDeleteEnabled()) {
				DeleteButtonForEach deleteButtonForEach = new DeleteButtonForEach(bannerLayout, focObj);
				bannerLayout.addDeleteComponent(deleteButtonForEach);
			}
			if (isOpenEnabled()) {
				OpenButtonForEach openButtonForEach = new OpenButtonForEach(focObj);
				bannerLayout.addOpenComponent(openButtonForEach);
			}
			if (!addOnTop) {
				bannerContainer.addComponent(bannerLayout);
			} else {
				bannerContainer.addComponentAsFirst(bannerLayout);
			}
			getBannerList(true).add(bannerLayout);
			if (getAttributes() != null 
					&& getAttributes().getValue(FXML.ATT_FOR_EACH_SEPARATOR) != null 
					&& getAttributes().getValue(FXML.ATT_FOR_EACH_SEPARATOR).toUpperCase().equals("FALSE")) {
			} else {
				
				if (!addOnTop) {
					bannerContainer.addComponent(new FVLine());
				}else{
					bannerContainer.addComponentAsFirst(new FVLine());
				}
			}
		}
	}

  public FocListWrapper getFocListWrapper() {
  	return focListWrapper;
  }
  
  public FocList getFocList() {
  	return getFocListWrapper() != null ? getFocListWrapper().getFocList() : null;
  }

//  public void setFocList(FocList focList) {
//    this.focList = focList;
//  }
  
  public ArrayList<FVBannerLayout> getBannerList(boolean create){
    if(bannerList == null && create) bannerList = new ArrayList<FVForEachLayout.FVBannerLayout>();
    return bannerList;
  }
  
  public FVButton getAddLineButton(){
    return addNewLineButton;
  }
  
  public class FVBannerLayout extends FVHorizontalLayout{

  	private FVVerticalLayout    buttonLayout = null; 
    private DeleteButtonForEach deleteButton = null;
    private OpenButtonForEach   openButton   = null;
    private FocXMLLayout        centralPanel = null;

    public FVBannerLayout(Attributes attributes) {
      super(attributes);
    }

    public FVVerticalLayout getButtonLayout(){
    	if(buttonLayout == null){
    		buttonLayout = new FVVerticalLayout(null);
    		addComponent(buttonLayout);
    	}
    	return buttonLayout;
    }
    
    public DeleteButtonForEach getDeleteButton(){
      return deleteButton;
    }

    public void setDeleteButton(DeleteButtonForEach button){
      this.deleteButton = button;
    }

    public FocXMLLayout getCentral(){
      return centralPanel;
    }
    
    public void setCentral(FocXMLLayout central){
      this.centralPanel = central;
    }
    
    public FocObject getFocObject(){
      return (FocObject)getCentral().getFocData();
    }

    public void addDeleteComponent(DeleteButtonForEach button){
    	getButtonLayout().addComponent(button);
      setDeleteButton(button);
    }

    public void addOpenComponent(OpenButtonForEach button){
    	getButtonLayout().addComponent(button);
      setOpenButton(button);
    }

    public void addCentralPanel(FocXMLLayout central){
      this.addComponent(central);
      setCentral(central);
    }
    
    public void applyEditable(){
      boolean editable = FVForEachLayout.this.isEditable();
      if(deleteButton != null){
      	deleteButton.setVisible(editable);
      }
      if(openButton != null){
      	openButton.setVisible(editable);
      }
    }

		public OpenButtonForEach getOpenButton() {
			return openButton;
		}

		public void setOpenButton(OpenButtonForEach openButton) {
			this.openButton = openButton;
		}
  }
  
  public class DeleteButtonForEach extends Button {
    
    private Component toBeRemoved = null;
    private FocObject focObject   = null;
    
    public DeleteButtonForEach(Component toBeRemoved, FocObject focObject) {
      setCaption("Delete");
      if(getAttributes() != null && getAttributes().getValue(FXML.ATT_DELETE_BUTTON_CAPTION) != null){
      	setCaption(getAttributes().getValue(FXML.ATT_DELETE_BUTTON_CAPTION));
      }
      setToBeRemoved(toBeRemoved);
      setFocObject(focObject);
      
      addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	OptionDialog optionDialog = new OptionDialog("Delete Confirmation","Are you sure you want to delete this item?") {
						@Override
						public boolean executeOption(String optionName) {
							if(optionName.equals("YES")){
			        	if(bannerContainer != null && getToBeRemoved() != null && getFocList() != null && getFocObject() != null){
			            bannerContainer.removeComponent(getToBeRemoved());
			            int ref = getFocObject().getReference().getInteger();
			            getFocList().removeItem(ref);
			            copyMemoryToGui();
			          }
							}
							return false;
						}
					};
					optionDialog.addOption("YES", "Yes Delete");
					optionDialog.addOption("NO", "Cancel Deletion");
					Globals.popupDialog(optionDialog);
        }
      });
    }

    public Component getToBeRemoved() {
      return toBeRemoved;
    }

    public void setToBeRemoved(Component toBeRemoved) {
      this.toBeRemoved = toBeRemoved;
    }

    public FocObject getFocObject() {
      return focObject;
    }

    public void setFocObject(FocObject focObject) {
      this.focObject = focObject;
    }
    
  }

  public class OpenButtonForEach extends Button {
    
    private FocObject focObject   = null;
    
    public OpenButtonForEach(FocObject focObject) {
      setCaption("Open");
      if(getAttributes() != null && getAttributes().getValue(FXML.ATT_OPEN_BUTTON_CAPTION) != null){
      	setCaption(getAttributes().getValue(FXML.ATT_OPEN_BUTTON_CAPTION));
      }
      setFocObject(focObject);
      
      addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	Globals.getIFocNotification().popup(getFocObject(), true);
        }
      });
    }

    public FocObject getFocObject() {
      return focObject;
    }

    public void setFocObject(FocObject focObject) {
      this.focObject = focObject;
    }
    
  }
  
  @Override
  public void setEnabled(boolean enabled){
    //super.setEnabled(enabled);
  }

	public boolean isPaginate() {
		return paginate;
	}

	public void setPaginate(boolean paginate) {
		this.paginate = paginate;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
}
