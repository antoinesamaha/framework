package com.foc.vaadin.gui.components.tableGrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.treeGrid.FVColumnEditorRendererConverter;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class FVTableGrid extends Grid implements FocXMLGuiComponent, ITableTree {
  
  private FocListWrapper             focListWrapper    = null;
  private TableTreeDelegate          tableTreeDelegate = null;
  private FocXMLGuiComponentDelegate delegate          = null;
  private ItemClickListener          clickListener     = null;
  
  public boolean childrenAllowedCheckerEnabled = true;
  
  public FVTableGrid(String displayPropertyPath, Attributes attributes) {
  	super();
  	getTableTreeDelegate(attributes);
  	delegate = new FocXMLGuiComponentDelegate(this);
    init();
  }

  @Override
  public void dispose(){
    if(focListWrapper != null){
      focListWrapper.dispose();
      focListWrapper = null;
    }
    
    if(delegate != null){
      delegate.dispose();
      delegate = null;
    }
    
    if(tableTreeDelegate != null){
      tableTreeDelegate.dispose();
      tableTreeDelegate = null;
    }
    
    clickListener = null;
  }
  
	protected Object convertValueToItemId(String value){
  	Integer intObject = null;
  	try{
  		intObject = Integer.valueOf(value);
  	}catch(Exception e){
  	}
		return intObject != null ? intObject : value;
	}
	
  private void init(){
    getTableTreeDelegate().init();
  }

  @Override
  public void setAttributes(Attributes attributes) {
    getTableTreeDelegate().setAttributes(attributes);
  }
  
  @Override
  public Attributes getAttributes() {
    return getTableTreeDelegate().getAttributes();
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_TABLE_GRID;
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  }

  @Override
  public FProperty getFocData() {
    return null;
  }

  public FVTableWrapperLayout getWrapperLayout(){
  	return getTableTreeDelegate() != null ? getTableTreeDelegate().getWrapperLayout() : null;
  }
  
  public TableTreeDelegate getTableTreeDelegate() {
  	return getTableTreeDelegate(null);
  }

  public TableTreeDelegate getTableTreeDelegate(Attributes attributes) {
    if(tableTreeDelegate == null){
      tableTreeDelegate = new TableTreeDelegate(this, attributes);
    }
    return tableTreeDelegate;
  }

  @Override
  public FocDesc getFocDesc() {
    return getFocList() != null ? getFocList().getFocDesc() : null;
  }

	@Override
	public void open(FocObject focObject) {
	  getTableTreeDelegate().open(focObject);
	}

  @Override
  public void applyFocListAsContainer() {
  	//ANTOINE123
//  	FVTableGridIndexedContainer gridIndexedContainer = new FVTableGridIndexedContainer(this, focListWrapper);
//  	setContainerDataSource(gridIndexedContainer);
  	setContainerDataSource(focListWrapper);
  	
  	setEditable(getDelegate().isEditable());
  	if(getTableTreeDelegate() != null && getTableTreeDelegate().getVisiblePropertiesArrayList() != null){
  		FocDesc focDesc = getFocDesc();
			for(int i = 0; i < getTableTreeDelegate().getVisiblePropertiesArrayList().size(); i++){
				FVTableColumn tableColumn = getTableTreeDelegate().getVisiblePropertiesArrayList().get(i);
				if(tableColumn != null && tableColumn.isVisible(getTableTreeDelegate().getFocXMLLayout())){
//					Column column = getColumn(getDataPath(tableColumn));
					Column column = getColumn(tableColumn.getDataPath());
					if(column != null){
						column.setHeaderCaption(tableColumn.getCaption());
						column.setEditable(tableColumn.isColumnEditable());
						int width = tableColumn.getWidth();
						if(width >= 0) {
							column.setWidth(width);
						}
						
						if(focDesc != null){
							FField field = tableColumn.getField();
							if(field != null){
								FVColumnEditorRendererConverter converter = new FVColumnEditorRendererConverter(tableColumn, column);								
								converter.createComponents();
							}
						}
					}
				}
			}
  	}
  	
  	if(getAttributes() != null){
  		String frozenColCount = getAttributes().getValue(FXML.ATT_FROZEN_COLUMNS);
  		int frozencols = Utils.parseInteger(frozenColCount, -1);
  		if(frozencols >= 0){
  			setFrozenColumnCount(frozencols);
  		}
  	}
  }
  
  @Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }
  
  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }

  @Override
  public void setFocData(IFocData focData) {
    if(focData != null){
    	if(focData instanceof FocList){ 
    		FocList foclist = ((FocList)focData);
    		this.focListWrapper = new FocListWrapper(foclist);
    	}else if(focData instanceof FocListWrapper){
        this.focListWrapper = (FocListWrapper) focData;
    	}
//      this.focListWrapper.setTableTree(this);
    	getTableTreeDelegate().adjustFocDataWrappers_FilterAndSroting();
      if(getWrapperLayout() != null) getWrapperLayout().setFilterBoxListenerIfNecessary();
    }else{
      this.focListWrapper = null;
    }
  }
  
  @Override
  public void addItemClickListener(ItemClickListener listener) {
    super.addItemClickListener(listener);
    clickListener = listener;
  }
  
  public ItemClickListener getItemClickListener(){
   return clickListener; 
  }

  @Override
  public String getValueString() {
    return null;
  }

  @Override
  public void setValueString(String value) {
  }

  @Override
  public void delete(int ref) {
  	getFocList().removeItem(ref);
  }

  @Override
  public FVTableColumn addColumn(FocXMLAttributes attributes) {
  	FVTableColumn column = getTableTreeDelegate() != null ? getTableTreeDelegate().addColumn(attributes) : null;
  	if(column != null && getTableTreeDelegate() != null && column.isVisible(getTableTreeDelegate().getFocXMLLayout())){
//			super.addColumn(getDataPath(column));
  		super.addColumn(column.getDataPath());
  	}
    return column; 
  }
  
//  private String getDataPath(FVTableColumn column){
//  	String strg = column.getName();
//  	if(column.getCaptionProp() != null && !column.getCaptionProp().isEmpty()){
//  		strg = strg + "." + column.getCaptionProp();
//  	}
//  	return strg;
//  }
  
	@Override
	public void computeFooter(FVTableColumn col) {
	}

  public void setFocListWrapper(FocListWrapper focListWrapper) {
    this.focListWrapper = focListWrapper;
  }

  @Override
  public FocDataWrapper getFocDataWrapper() {
    return focListWrapper;
  }
  
  public FocListWrapper getFocListWrapper(){
    return (FocListWrapper) getFocDataWrapper();
  }
  
  public void setColorByLevel(){
  }
  
	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return getFocDataWrapper() != null ? getFocDataWrapper().setRefreshGuiDisabled(disabled) : false;
	}
	
	@Override
	public void refreshEditable() {
	}
	
	@Override
	public void setEditable(boolean editable) {
		if(getDelegate() != null){
			getDelegate().setEditable(editable);
			super.setEditorEnabled(editable);
		}
	}

	@Override
	public FVCheckBox getEditableCheckBox() {
		return null;
	}

	@Override
	public void refreshRowCache_Foc() {
	}
	
	@Override
	public FocObject getSelectedObject() {
		FocObject focObject = null;

		//In case of multi-selection mode we can't get the selected row while adding a new object 
		if(getSelectionModel() instanceof SelectionModel.Single) {
			Object selected = getSelectedRow();
			if(selected instanceof Integer){
				int ref = ((Integer)selected).intValue();
				focObject = getFocList().searchByReference(ref);
			}
		}
		return focObject;
	}
	
	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public FocList getFocList() {
		return getFocListWrapper() != null ? getFocListWrapper().getFocList() : null;
	}
	
	public void selectAll(){
		if(getFocListWrapper() != null){
			for(int i=0; i<getFocListWrapper().size(); i++){
				if(getFocListWrapper().getAt(i) != null){
					select(getFocListWrapper().getAt(i).getReferenceInt());
				}
			}
		}
	}
	
	public void deselectAllFoc(){
		if(getFocListWrapper() != null){
			for(int i=0; i<getFocListWrapper().size(); i++){
				if(getFocListWrapper().getAt(i) != null){
					deselect(getFocListWrapper().getAt(i).getReferenceInt());
				}
			}
		}
	}
	
	public List<FocObject> getSelectedFocObjects(){
		Collection<Object> selectedRows = null;
		if(getSelectionModel() instanceof MultiSelectionModel){
			MultiSelectionModel multiSelectMode = (MultiSelectionModel) getSelectionModel();
			selectedRows = multiSelectMode.getSelectedRows();
		}else if(getSelectionModel() instanceof SingleSelectionModel){
			SingleSelectionModel singleSelectionModel = (SingleSelectionModel) getSelectionModel();
			selectedRows = singleSelectionModel.getSelectedRows();
		}
		Iterator<Object> selectedRowsItr = selectedRows.iterator();
		List<FocObject> selectedFocObjectList = new ArrayList<FocObject>();
		if(getFocListWrapper() != null && getFocListWrapper().getFocListFiltered() != null){
			while(selectedRowsItr.hasNext()){
				Integer selectedFocObjectRef = (Integer) selectedRowsItr.next();
				FocObject focObject = getFocListWrapper().getFocListFiltered().searchByRealReferenceOnly(selectedFocObjectRef);
				selectedFocObjectList.add(focObject);
			}
		}
		return selectedFocObjectList;
	}

	@Override
	public void setSelectedObject(FocObject selectedObject) {
		if(selectedObject != null) select(selectedObject.getReferenceInt());
	}
}
