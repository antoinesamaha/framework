package com.foc.vaadin.gui.components;

import java.text.Format;
import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FNumField;
import com.foc.list.FocList;
import com.foc.pivot.FPivotTable;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.tree.TreeScanner;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.tableAndTree.TreeLevelColorCellStyleGenerator;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.web.dataModel.FocPivotWrapper;
import com.foc.web.dataModel.FocTreeWrapper;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.HierarchicalContainerOrderedWrapper;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.TreeTable;

@SuppressWarnings("serial")
public class FVTreeTable extends TreeTable implements FocXMLGuiComponent, ITableTree {
  
  private FocTreeWrapper             focTreeWrapper    = null;
  private TableTreeDelegate          tableTreeDelegate = null;
  private FocXMLGuiComponentDelegate delegate          = null;
  private ItemClickListener          clickListener     = null;
  
  public boolean childrenAllowedCheckerEnabled = true;
  
  public FVTreeTable(Attributes attributes) {
  	getTableTreeDelegate(attributes);
  	delegate = new FocXMLGuiComponentDelegate(this);
    init();
//Causes problems
//We can see the problems when we select a project with around 350 Bkdn 
//we start having cells giving blank rendering. and lines disappearing when we open close nodes quickly...
//    setCacheRate(0.2);
    
//    Item itm = getItem(null);
    /*
    setItemDescriptionGenerator(new ItemDescriptionGenerator() {
			
			@Override
			public String generateDescription(Component source, Object itemId, Object propertyId) {
		    if(propertyId == null){
	        return "Row description "+ itemId;
		    } else if(propertyId != null) {
		        return "Cell description " + itemId +","+propertyId;
		    }                                                                       
		    return null;
			}
		});
		*/
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
  public void detach() {
  	super.detach();
  }
  
  @Override
  public void setParent(HasComponents parent) {
  	super.setParent(parent);
  }
  
  @Override
  public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
  	return super.setParent(itemId, newParentId);
  }
  
  private void init(){
    getTableTreeDelegate().init();
    setSelectable(true);
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
    leafLevelAttribute_ClickListener();
  }
  
  @Override
  public Attributes getAttributes() {
    return getTableTreeDelegate().getAttributes();
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_TREE;
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
  
//  public void applyTreeDataAsContainer(){
//    getDelegate().adjustGeneratedColumns();
////    if(getAttributes() != null){
////      String propertyCaption = getAttributes().getValue(FXML.ATT_CAPTION_PROPERTY);
////      setItemCaptionPropertyId(propertyCaption);
////    }
//    setContainerDataSource(getTree());
//    setVisibleColumns(getDelegate().getVisiblePropertiesArrayList().toArray());
//  }

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
  
//  public FVTableColumn addColumn(String propertyID, String headerCaption) {
//    return getTableTreeDelegate().addColumn(propertyID, headerCaption);
//  }

  @Override
  public FVCheckBox getEditableCheckBox() {
    return getTableTreeDelegate().getEditableCheckBox();
  }
  
//  @Override
//  public FVTableColumn addColumn(Attributes attributes) {
//    FVTableColumn tc = null;
//    try{
//      String name = attributes.getValue(FXML.ATT_NAME);
//      tc = addColumn(name, attributes.getValue(FXML.ATT_CAPTION));
//      tc.setAttributes(attributes);
//    }catch(Exception e){
//      Globals.logException(e);
//    }
//    return tc;
////    FVCheckBox editableCheckBox = table.getEditableCheckBox();
////    if(editableCheckBox != null){
////      addComponent(editableCheckBox);
////    }
//  }

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
	  if(getAttributes() != null){
	    String propertyCaption = getAttributes().getValue(FXML.ATT_CAPTION_PROPERTY);
	    if(propertyCaption != null){
	    	setItemCaptionPropertyId(propertyCaption);
	    }
	    
	  }
    //getDelegate().adjustGeneratedColumns();

	  getTableTreeDelegate().pushVisibleColumns();
	  getTableTreeDelegate().setContainerDataSource(focTreeWrapper);
	  
	  if(getAttributes() != null && focTreeWrapper != null){
	  	String expandTree = getAttributes().getValue(FXML.ATT_TREE_EXPANDED);
	  	
	  	if(!Utils.isStringEmpty(expandTree) && expandTree.equalsIgnoreCase("true")){
	  		boolean backup = getTableTreeDelegate().setRefreshRowCacheEnabled(false);
	  		focTreeWrapper.getFTree().getRoot().setCollapsedWithPropagation(false);
	  		getTableTreeDelegate().setRefreshRowCacheEnabled(backup);
	  		refreshRowCache_Foc();
//	  		focTreeWrapper.getFTree().getRoot().setCollapsedWithPropagation(false);
//	  		refreshRowCache_Foc();
//	  		focTreeWrapper.getFTree().getRoot().setCollapsedWithPropagation(false);
//	  		refreshRowCache_Foc();
//	  		expandCollapseNodes(true);
//	  		if(focTreeWrapper.getItemIds() != null){
//	  			Iterator iter = focTreeWrapper.getItemIds().iterator();
//	  			
//	  			if(iter != null){
//	  				while(iter.hasNext()){
//	  					Object itemId = iter.next();
//	  					
//	  					setCollapsed(itemId, false);
//	  				}
//	  			}
//	  		}
		  }
	  }
  }
  
  @Override
  public void setContainerDataSource(Container newDataSource, Collection columnIds) {
      //Barmaja we cannot access cStrategy
  	  //cStrategy = null;

      // FIXME: This disables partial updates until TreeTable is fixed so it
      // does not change component hierarchy during paint
      //Barmaja we cannot access cStrategy
      //containerSupportsPartialUpdates = (newDataSource instanceof ItemSetChangeNotifier) && false;
  	if(newDataSource != null){//Barmaja

      if (!(newDataSource instanceof Hierarchical)) {
          newDataSource = new ContainerHierarchicalWrapper(newDataSource);
      }

      if (!(newDataSource instanceof Ordered)) {
          newDataSource = new HierarchicalContainerOrderedWrapper(
                  (Hierarchical) newDataSource);
      }

      super.setContainerDataSource(newDataSource, columnIds);
      
  	}//Barmaja
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
    if(getFocList() != null){
    	setPageLength(getFocList().size());
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

  @Override
  public FVTableColumn addColumn(FocXMLAttributes attributes) {
    return getTableTreeDelegate().addColumn(attributes); 
  }
  
	@Override
	public void computeFooter(FVTableColumn col) {
    String footerFormula = col.getFooterFormula();
    if(footerFormula != null){
      footerFormula = footerFormula.toUpperCase();
      if(footerFormula.equals("SUM")){
        double value = 0;
        FTree tree = getFTree();
        
        Collection collects = tree.rootItemIds();
        Iterator   iter     = collects.iterator();
        while(iter != null && iter.hasNext()){
        	Integer intObj = (Integer) iter.next();
        	if(intObj != null){
        		int ref = intObj.intValue();
        		FocObject focObject = getFocList().searchByReference(ref);
        		
            IFocData focData = focObject.iFocData_getDataByPath(col.getDataPath());
            if(focData instanceof FProperty){
              FProperty property = (FProperty)focData;
              value += property != null ? property.getDouble() : 0;
            }
        	}
        }
        
        Format format = FNumField.newNumberFormat(20, 2);
        FVTreeTable.this.setColumnFooter(col.getName() , format.format(value));

        /*
      } else if(footerFormula.startsWith("PARENT.")){
        FocObject focObject = (FocObject) getFocList().getFatherSubject();
        String   path = footerFormula.substring(7);
        FProperty property = focObject.getFocPropertyForPath(path);
        FVTreeTable.this.setColumnFooter(col.getName(), property.getString());
        */
      }
    }
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
		setCellStyleGenerator(new TreeLevelColorCellStyleGenerator(getFocTreeWrapper()));
  }
  
  public void expandCollapseNodes_AccordingToMemoryFlag(){
  	if(getFTree() != null){
  		getFTree().scanVisible(new TreeScanner<FNode>() {

  			private void expandCollapseForNode(FNode node){
					if(node != null){
						if(node.getObject() != null && (node.getObject() instanceof FocObject)){
							FocObject obj = (FocObject) node.getObject();
							int objId = obj.getReference().getInteger();
							
							setCollapsed(objId, node.isCollapsed()); 
						}
					}
  			}
  			
				@Override
				public boolean beforChildren(FNode node) {
					if(!node.isCollapsed()) expandCollapseForNode(node);
					return true;
				}

				@Override
				public void afterChildren(FNode node) {
					if(node.isCollapsed()) expandCollapseForNode(node);
				}
			});
  	}
  }
  
  public void expandCollapseNodes(final boolean expand){
  	if(getFTree() != null){
  		getFTree().scanVisible(new TreeScanner<FNode>() {

				@Override
				public boolean beforChildren(FNode node) {
					return true;
				}

				@Override
				public void afterChildren(FNode node) {
					if(node != null){
						if(node.getObject() != null && (node.getObject() instanceof FocObject)){
							FocObject obj = (FocObject) node.getObject();
							int objId = obj.getReference().getInteger();
							
							setCollapsed(objId, !expand);
						}
					}
				}
			});
  	}
  }
  
  @Override
  protected String formatPropertyValue(Object rowId, Object colId, Property property) {
  	String ret = getTableTreeDelegate().formatPropertyValue(rowId, colId, property);
    return ret != null ? ret : super.formatPropertyValue(rowId, colId, property);
  }
  
  @Override
  public void refreshRowCache_Foc(){
  	boolean backup = getTableTreeDelegate().setRefreshRowCacheEnabled(false);
  	expandCollapseNodes_AccordingToMemoryFlag();
  	getTableTreeDelegate().setRefreshRowCacheEnabled(backup);
  	super.refreshRowCache();
  }
  
  @Override
  public void refreshRowCache() {
  	if(getTableTreeDelegate().isRefreshRowCacheEnabled()){
  		super.refreshRowCache();
  	}else{
  		getTableTreeDelegate().setRefreshRowCacheNeeded(true);
  	}
  }
  /*
  public void refresh_ContainerItemSetChange(){
		if(getContainerDataSource() instanceof ContainerOrderedWrapper){
			ContainerOrderedWrapper container = (ContainerOrderedWrapper) getContainerDataSource();
			container.updateOrderWrapper();
		}
		containerItemSetChange(null);
  }*/
  
  @Override
  public void setItemIcon(Object itemId, Resource icon) {
  	super.setItemIcon(itemId, icon);
  	
  	if(icon != null){
  		getTableTreeDelegate().adjustColumnWidthForIcons();
  	}
  }

	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
		if(fatherObject != null && fatherObject.getReference() != null){
			setCollapsed(fatherObject.getReference().getInteger(), false);
		}
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return getFocDataWrapper() != null ? getFocDataWrapper().setRefreshGuiDisabled(disabled) : false;
	}
	
	@Override
	public void refreshEditable() {
		if(getTableTreeDelegate() != null){
			getTableTreeDelegate().refreshEditable();
		}
	}
	
	@Override
	public void setEditable(boolean editable) {
		if(getDelegate() != null){
			getDelegate().setEditable(editable);
			super.setEditable(editable);
		}
	}
	
	private void isChildrenAllowed_Attribute(){
		if(getFTree() != null){
			getFTree().scan(new TreeScanner<FNode>() {
	
				private boolean isLeafLevel(FNode node){
					boolean isLeafLevel = false;
					if(node != null){
						int nodeDepth = node.getNodeDepth();
						if(getAttributes() != null && getAttributes().getValue(FXML.ATT_LEAF_LEVEL) != null){
							String leafDepth_Strg = getAttributes().getValue(FXML.ATT_LEAF_LEVEL);
							int leafDepth = Integer.valueOf(leafDepth_Strg);
							if(leafDepth == nodeDepth){
								isLeafLevel = true;
							}
						}
					}
					return isLeafLevel;
				}				
				
				@Override
				public boolean beforChildren(FNode node) {
					return true;
				}
	
				@Override
				public void afterChildren(FNode node) {
					if(node != null && node.getObject() != null && isLeafLevel(node) && isChildrenAllowedCheckerEnabled()){
						FocObject focObject = (FocObject) node.getObject();
						setChildrenAllowed(focObject.getReference().getInteger(), false);
					}
				}
			});
		}
	}
	
	public void leafLevelAttribute_ClickListener(){
		addExpandListener(new ExpandListener() {
			
			@Override
			public void nodeExpand(ExpandEvent event) {
				isChildrenAllowed_Attribute();
			}
		});
	}
	
	public boolean isChildrenAllowedCheckerEnabled() {
		return childrenAllowedCheckerEnabled;
	}

	public void setChildrenAllowedCheckerEnable(boolean childrenAllowedCheckerEnabled) {
		this.childrenAllowedCheckerEnabled = childrenAllowedCheckerEnabled;
	}

	@Override
	public FocObject getSelectedObject() {
		return getTableTreeDelegate().getSelectedObject_ForVaadinTable();
	}
	
	@Override
	public void setSelectedObject(FocObject selectedObject) {
		if(getTableTreeDelegate() != null){
			getTableTreeDelegate().setSelectedObject_ForVaadinTable(selectedObject);
		}
	}
}
