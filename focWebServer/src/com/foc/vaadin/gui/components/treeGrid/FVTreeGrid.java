package com.foc.vaadin.gui.components.treeGrid;

import me.everpro.everprotreegrid.EverproTreeGrid;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.pivot.FPivotTable;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.web.dataModel.FocPivotWrapper;
import com.foc.web.dataModel.FocTreeWrapper;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVTreeGrid extends EverproTreeGrid implements FocXMLGuiComponent, ITableTree {
  
  private FocTreeWrapper             focTreeWrapper    = null;
  private TableTreeDelegate          tableTreeDelegate = null;
  private FocXMLGuiComponentDelegate delegate          = null;
  private ItemClickListener          clickListener     = null;
  
  public boolean childrenAllowedCheckerEnabled = true;
  
  public FVTreeGrid(String displayPropertyPath, Attributes attributes) {
  	super(FField.REF_FIELD_NAME, displayPropertyPath);
  	getTableTreeDelegate(attributes);
  	delegate = new FocXMLGuiComponentDelegate(this);
    init();

//		setCellStyleGenerator(new CellStyleGenerator() {
//			@Override
//			public String getStyle(CellReference cellReference) {
//				String style = null;
//				String propertyId = (String) cellReference.getPropertyId();
//				
//				FVTableColumn column = getTableTreeDelegate().findColumn(propertyId);
//				if(column != null){
//					FField fld = column.getField();
//					if(fld != null){
//						if(fld instanceof FNumField || fld instanceof FIntField){
//							style = "rightalign";					
//						}
//					}
//				}
//				return style;
//			}
//		});
		
//		setRowStyleGenerator(new RowStyleGenerator() {
//			@Override
//			public String getStyle(RowReference rowReference) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});
  }

  @Override
  public void dispose(){
    if(focTreeWrapper != null){
      focTreeWrapper.dispose();
      focTreeWrapper = null;
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
  
  @Override
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
    adjustPropertyCaptionWidth();
//    setSelectable(true);
  }

  @Override
  public Field getFormField() {
    return null;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    getTableTreeDelegate().setAttributes(attributes);
    String treeColorByLevel = attributes.getValue(FXML.ATT_TREE_COLOR_BY_LEVEL);
    if(treeColorByLevel != null && !treeColorByLevel.isEmpty() && treeColorByLevel.toLowerCase().equals("true")){
    	setColorByLevel();
    }
  }
  
  @Override
  public Attributes getAttributes() {
    return getTableTreeDelegate().getAttributes();
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_TREE_GRID;
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  }

  public FTree getFTree() {
    FTree  temp = null;
    
    if(getFocTreeWrapper() != null){
      temp = getFocTreeWrapper().getFTree();
    }
    return temp;
  }

  public void setFTree(FTree tree) {
    if(getFocTreeWrapper() != null){
      getFocTreeWrapper().setFTree(tree);
    }
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
    return getFTree() != null && getFTree().getFocList() != null ? getFTree().getFocList().getFocDesc() : null;
  }

  @Override
  public FocList getFocList() {
    return getFTree() != null ? getFTree().getFocList() : null;
  }

	@Override
	public void open(FocObject focObject) {
	  getTableTreeDelegate().open(focObject);
	}
	
  @Override
  public void applyFocListAsContainer() {
  	setContainerDataSource(focTreeWrapper);
  	setEditable(getDelegate().isEditable());
  	
  	if(getTableTreeDelegate() != null && getTableTreeDelegate().getVisiblePropertiesArrayList() != null){
  		FocDesc focDesc = getFocDesc();
			for(int i = 0; i < getTableTreeDelegate().getVisiblePropertiesArrayList().size(); i++){
				FVTableColumn tableColumn = getTableTreeDelegate().getVisiblePropertiesArrayList().get(i);
				if(tableColumn != null && tableColumn.isVisible(getTableTreeDelegate().getFocXMLLayout())){
					Column column = getColumn(tableColumn.getDataPath());
					column.setHeaderCaption(tableColumn.getCaption());
//					column.setEditable(tableColumn.isColumnEditable());
					column.setEditable(true);
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
  	
  	if(getAttributes() != null){
  		String frozenColCount = getAttributes().getValue(FXML.ATT_FROZEN_COLUMNS);
  		int frozencols = Utils.parseInteger(frozenColCount, -1);
  		if(frozencols >= 0){
  			setFrozenColumnCount(frozencols);
  		}
  	}
  }

//  @Override
//  public void applyFocListAsContainer() {
//  	/*
//	  if(getAttributes() != null){
//	    String propertyCaption = getAttributes().getValue(FXML.ATT_CAPTION_PROPERTY);
//	    if(propertyCaption != null){
//	    	setItemCaptionPropertyId(propertyCaption);
//	    }
//	  }
//	  */
//    //getDelegate().adjustGeneratedColumns();
//
////	  getTableTreeDelegate().pushVisibleColumns();
//  	setContainerDataSource(focTreeWrapper);
//  	
//  	setEditable(getDelegate().isEditable());
//  	
//  	if(getTableTreeDelegate() != null && getTableTreeDelegate().getVisiblePropertiesArrayList() != null){
//  		FocDesc focDesc = getFocDesc();
//			for(int i = 0; i < getTableTreeDelegate().getVisiblePropertiesArrayList().size(); i++){
//				FVTableColumn tableColumn = getTableTreeDelegate().getVisiblePropertiesArrayList().get(i);
//				if(tableColumn != null && tableColumn.isVisible(getTableTreeDelegate().getFocXMLLayout())){
//					//Column column = addColumn(tableColumn.getDataPath()+tableColumn.getCaptionProp());
//					Column column = addColumn(tableColumn.getDataPath());
//					column.setHeaderCaption(tableColumn.getCaption());
//					column.setEditable(tableColumn.isColumnEditable());
//					int width = tableColumn.getWidth();
//					if(width >= 0) {
//						column.setWidth(width);
//					}
//					
//					if(focDesc != null){
//						FField field = tableColumn.getField();
//						if(field != null){
//							FVColumnEditorRendererConverter converter = new FVColumnEditorRendererConverter(tableColumn, column);								
//							converter.createComponents();
//						}
//					}
//				}
//			}
//  	}
//  	
//  	if(getAttributes() != null){
//  		String frozenColCount = getAttributes().getValue(FXML.ATT_FROZEN_COLUMNS);
//  		int frozencols = Utils.parseInteger(frozenColCount, -1);
//  		if(frozencols >= 0){
//  			setFrozenColumnCount(frozencols);
//  		}
//  		
//  		
//  	}
//
//  	/*
//	  if(getAttributes() != null && focTreeWrapper != null){
//	  	String expandTree = getAttributes().getValue(FXML.ATT_TREE_EXPANDED);
//
//	  	if(!Utils.isStringEmpty(expandTree) && expandTree.equalsIgnoreCase("true")){
//	  		boolean backup = getTableTreeDelegate().setRefreshRowCacheEnabled(false);
//	  		focTreeWrapper.getFTree().getRoot().setCollapsedWithPropagation(false);
//	  		getTableTreeDelegate().setRefreshRowCacheEnabled(backup);
//	  		refreshRowCache_Foc();
//		  }
//	  }
//	  */
//  }
  
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
    	if(focData instanceof FPivotTable){
    		FTree tree = (FTree) focData;
    		this.focTreeWrapper = new FocPivotWrapper(tree);
    	}else if(focData instanceof FTree){
        FTree tree = (FTree) focData;
        this.focTreeWrapper = new FocTreeWrapper(tree);
      }else if(focData instanceof FocTreeWrapper){
        this.focTreeWrapper = (FocTreeWrapper) focData;
      }
//      this.focTreeWrapper.setTableTree(this);
    	getTableTreeDelegate().adjustFocDataWrappers_FilterAndSroting();
      if(getWrapperLayout() != null) getWrapperLayout().setFilterBoxListenerIfNecessary();
    }else{
      this.focTreeWrapper = null;
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
    FNode node = getFTree().vaadin_FindNode(ref);
    boolean deleted = getFTree().vaadin_DeleteNode(node);
    if(deleted){
      getFTree().removeItem(ref);
    }
  }

  /*@Override
  public FVTableColumn addColumn(FocXMLAttributes attributes) {
    return getTableTreeDelegate().addColumn(attributes); 
  }*/
  
  @Override
  public FVTableColumn addColumn(FocXMLAttributes attributes) {
  	FVTableColumn column = getTableTreeDelegate() != null ? getTableTreeDelegate().addColumn(attributes) : null;
  	if(column != null && getTableTreeDelegate() != null && column.isVisible(getTableTreeDelegate().getFocXMLLayout())){
  		super.addColumn(column.getDataPath());
  	}
    return column; 
  }
  
	@Override
	public void computeFooter(FVTableColumn col) {
	}

  public void setFocTreeWrapper(FocTreeWrapper focTreeWrapper) {
    this.focTreeWrapper = focTreeWrapper;
  }

  @Override
  public FocDataWrapper getFocDataWrapper() {
    return focTreeWrapper;
  }
  
  public FocTreeWrapper getFocTreeWrapper(){
    return (FocTreeWrapper) getFocDataWrapper();
  }
  
  public void setColorByLevel(){
//		setCellStyleGenerator(new TreeLevelColorCellStyleGenerator(getFocTreeWrapper()));
  }
  
	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
//		if(fatherObject != null && fatherObject.getReference() != null){
//			setCollapsed(fatherObject.getReference().getInteger(), false);
//		}
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return getFocDataWrapper() != null ? getFocDataWrapper().setRefreshGuiDisabled(disabled) : false;
	}
	
	@Override
	public void refreshEditable() {
//		if(getTableTreeDelegate() != null){
//			getTableTreeDelegate().refreshEditable();
//		}
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
		
		Object selected = getSelectedRow();
		if(selected instanceof Integer){
			int ref = ((Integer)selected).intValue();
			focObject = getFocList().searchByReference(ref);
		}
		return focObject;
	}
	
	public void addItemClickListenerForFormula(){
		if(getTableTreeDelegate() != null && getTableTreeDelegate().getFormulaForm() != null){
			addItemClickListener(new FVTreeGridItemClickListener_FormFormulaPanel(this));
		}
	}
	
	private void adjustPropertyCaptionWidth(){
  	if(getAttributes() != null){
  		String captionWidth = getAttributes().getValue(FXML.ATT_CAPTION_WIDTH);
  		if(captionWidth != null && !captionWidth.isEmpty()){
  			if(captionWidth.contains("%")){
  				captionWidth = captionWidth.replace("%", "");
  			}else if(captionWidth.contains("px")){
  				captionWidth = captionWidth.replace("px", "");
  			}
  			getColumn(FField.REF_FIELD_NAME).setWidth(Integer.valueOf(captionWidth));
  		}
  	}
  }

	@Override
	public void setSelectedObject(FocObject selectedObject) {
		if(selectedObject != null){
			select(selectedObject.getReferenceInt());
		}
	}
}
