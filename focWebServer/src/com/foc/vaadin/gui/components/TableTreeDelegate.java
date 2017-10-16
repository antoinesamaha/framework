
package com.foc.vaadin.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.access.AccessSubject;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.ITableTreeDelegate;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.formula.CompositeKeyPropertyFormulaEnvironment;
import com.foc.list.FocList;
import com.foc.property.FDate;
import com.foc.property.FDouble;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.property.FReference;
import com.foc.property.FString;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.tree.TreeScanner;
import com.foc.util.ASCII;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVGUIFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.components.tableAndTree.FTableToolTipGenerator;
import com.foc.vaadin.gui.components.tableAndTree.FVColGen_Select;
import com.foc.vaadin.gui.components.tableAndTree.ITableListener;
import com.foc.vaadin.gui.components.tableGrid.FVTableGrid;
import com.foc.vaadin.gui.components.treeGrid.FVTreeGrid;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.tableExports.FVTableToCSVExport;
import com.foc.vaadin.gui.tableExports.FVTableToEXCELExport;
import com.foc.vaadin.gui.tableExports.FVTableTreeCSVExport;
import com.foc.vaadin.gui.tableExports.FVTableTreeEXCELExport;
import com.foc.vaadin.gui.tableExports.FVTreeToCSVExport;
import com.foc.vaadin.gui.tableExports.FVTreeToEXCELExport;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.business.BusinessEssentialsWebModule;
import com.foc.web.modules.business.FocFormula_Form;
import com.foc.web.server.session.FocWebSession;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ContainerOrderedWrapper;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.TreeTable;

public class TableTreeDelegate implements ITableTreeDelegate {

	private ArrayList<FVTableColumn> visiblePropertiesArray = null;
	private Attributes attributes = null;
	private boolean allowEditingCheckBox = false;
	private FVCheckBox editableCheckBox = null;
	private ITableTree treeOrTable = null;
	private ArrayList<FVTablePopupMenu> popupMenuArrayList = null;
	private FVTablePopupMenu[] popupMenuFullArray = null;
	private boolean            popupMenuEnabled   = true;
	private XMLViewKey xmlViewKey_New = null;
	private XMLViewKey xmlViewKey_Open = null;
  private int viewContainer_Open = 0;
	private int viewContainer_New = ITableTree.VIEW_CONTAINER_NOT_SET;
	private int editingMode = -1;
	private FVTableWrapperLayout wrapperLayout = null;
	private ValidationListener_ForOpenedObjectToGetReloadedAfterEdit validationListener_ToReloadOpenedObjects = null;
	private ItemClickListener openClickListener = null;
	private ItemClickListener doubleClickListener = null;//Only needed when explicitly in XML specified and the open is disabled
			
	private boolean duplicateEnabled   = true;
	private boolean addEnabled         = true;
	private boolean openEnabled        = true;
	private boolean deleteEnabled      = true;
	private boolean excelExportEnabled = true;
	private boolean statusStyleEnabled = true;
	
	private String lastClickedFocDataByPath = null;
	
	private FocFormula_Form formulaForm   = null;
//	private FocusListener   focusListener = null;

	private int                       selectionMode  = SELECTION_MODE_NONE;
	private ArrayList<Object>         selectedRowsId = null;
	private ArrayList<ITableListener> tableListenerArrayList = null;
	
	private String  tableName              = null;
	
	private boolean refreshRowCacheEnabled = true;
	private boolean refreshRowCacheNeeded  = false;
	
	private FTableToolTipGenerator tableToolTipGenerator = null;
	
	private static final String COL_ID_SELECT   = FVTableColumn.COL_ID_SELECT;
//	private static final String COL_LINE_NUMBER = "_LINE_NUMBER";
//	private static final String COL_CHILD_COUNT = "_COUNT";
	
	public static final int MODE_NOT_EDITABLE          = 0;
	public static final int MODE_EDITABLE              = 1;
	public static final int MODE_EDITABLE_ON_SELECTION = 2;
	
	public static final int SELECTION_MODE_NONE     = 0;
	public static final int SELECTION_MODE_MULTIPLE = 1;
	public static final int SELECTION_MODE_SINGLE   = 2;
	
	public static final int ACTION_NONE   = 0;
	public static final int ACTION_ADD    = 1;
	public static final int ACTION_OPEN   = 2;
	public static final int ACTION_DELETE = 3;

	public TableTreeDelegate(ITableTree treeOrTable, Attributes attributes) {
		this.treeOrTable = treeOrTable;
		this.attributes = attributes;
//		setAttributes(attributes);
	}

	public void dispose() {
		if(openClickListener != null && getTreeOrTable() instanceof Table){
			((Table) getTreeOrTable()).removeItemClickListener(openClickListener);
			openClickListener = null;
		}

		if(doubleClickListener != null && getTreeOrTable() instanceof Table){
			((Table) getTreeOrTable()).removeItemClickListener(doubleClickListener);
			doubleClickListener = null;
		}
		
		if(validationListener_ToReloadOpenedObjects != null){
			validationListener_ToReloadOpenedObjects.dispose();
			validationListener_ToReloadOpenedObjects = null;
		}
		
		if(visiblePropertiesArray != null){
			for(int i=0; i<visiblePropertiesArray.size(); i++){
				FVTableColumn col = visiblePropertiesArray.get(i);
				if(col != null){
					col.dispose();
				}
			}
			visiblePropertiesArray.clear();
			visiblePropertiesArray = null;
		}
		
		if(attributes != null){
			try{
				FocXMLAttributes xmlAttrib = (FocXMLAttributes) attributes;
				xmlAttrib.dispose();
			}catch(Exception e){
				Globals.logExceptionWithoutPopup(e);
			}
			attributes = null;
		}
		
		if(editableCheckBox != null){
			editableCheckBox.dispose();
			editableCheckBox = null;
		}

		treeOrTable = null;
		
		if(popupMenuArrayList != null){
			for(int i=0; i<popupMenuArrayList.size(); i++){
				FVTablePopupMenu menu = popupMenuArrayList.get(i);
				menu.dispose();
			}
			popupMenuArrayList.clear();
			popupMenuArrayList = null;
		}

		if(popupMenuFullArray != null){
			for(int i=0; i<popupMenuFullArray.length; i++){
				FVTablePopupMenu menu = popupMenuFullArray[i];
				menu.dispose();
			}
			popupMenuFullArray = null;
		}

		if(xmlViewKey_New != null){
			xmlViewKey_New.dispose();
			xmlViewKey_New = null;
		}
		
		if(xmlViewKey_Open != null){
			xmlViewKey_Open.dispose();
			xmlViewKey_Open = null;
		}		

		wrapperLayout = null;
		
		formulaForm   = null;

		if(selectedRowsId != null){
			selectedRowsId.clear();
			selectedRowsId = null;
		}

		if(tableListenerArrayList != null){
			for(int i=0; i<tableListenerArrayList.size(); i++){
				ITableListener listener = tableListenerArrayList.get(i);
				listener.dispose();
			}
			tableListenerArrayList.clear();
			tableListenerArrayList = null;
		}
		
		if(tableToolTipGenerator != null){
			tableToolTipGenerator.dispose();
			tableToolTipGenerator = null;
		}
	}
	
	public boolean hasAlert(){
		return true;
	}

  private boolean hasZoomButton(){
  	boolean has = false;
  	if(getTreeOrTable() instanceof FVTreeTable){
  		FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();  		
//  		if(treeTable.getFocDataWrapper().getFocData() instanceof FObjectTree){
    		has = true;  			
//  		}
  	}
  	return has;
  }
	
	@SuppressWarnings("serial")
	public void init() {
		setAttributes(attributes);
		
		FVTableCellStyleGenerator tableCellStyleGenerator = new FVTableCellStyleGenerator(this);
		if(getTable() != null){
			getTable().setCellStyleGenerator(tableCellStyleGenerator);
			if(isCollapsingButtonShow()){
				getTable().setColumnCollapsingAllowed(true);
			}
		}
//		((Table) getTreeOrTable()).setColumnReorderingAllowed(true);
		// xmlAttributes();
		allowRightClick(true);
		
		if(treeOrTable != null && treeOrTable instanceof FVTreeTable){
			FVTreeTable treeTable = (FVTreeTable) treeOrTable;
			treeTable.addCollapseListener(new CollapseListener() {
				
				@Override
				public void nodeCollapse(CollapseEvent event) {
					int objId = (Integer) event.getItemId();
					FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();
					if(treeTable != null && treeTable.getFocTreeWrapper() != null && treeTable.getFocTreeWrapper().getFTree() != null){
						FNode node = treeTable.getFocTreeWrapper().getFTree().findNode(objId);
						
						if(node != null){
							node.setCollapsed(true);
						}
					}
				}
			});
			
			treeTable.addExpandListener(new ExpandListener() {
				
				@Override
				public void nodeExpand(ExpandEvent event) {
					int objId = 0;
					if(event.getItemId() instanceof FReference){
						objId = ((FReference) event.getItemId()).getInteger();
					}else if(event.getItemId() instanceof Integer){
						objId = (Integer) event.getItemId();
					}
					FVTreeTable treeTable = (FVTreeTable) treeOrTable;					
					if(treeTable != null && treeTable.getFocTreeWrapper() != null && treeTable.getFocTreeWrapper().getFTree() != null){
						FNode node = treeTable.getFocTreeWrapper().getFTree().findNode(objId);
						
						if(node != null){
							node.setCollapsed(false);
						}
					}
				}
			});

			if(hasZoomButton()){
				addPopupMenu(new FVTablePopupMenu("Zoom In", true) {
					@Override
					public void actionPerformed(FocObject focObject) {
						if(focObject != null){
							FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();
	
							if(treeTable != null && treeTable.getFocTreeWrapper() != null){
								if(treeTable.getFocTreeWrapper().getZoomObject() == null || !treeTable.getFocTreeWrapper().getZoomObject().equalsRef(focObject)){
									treeTable.getFocTreeWrapper().setZoomObject(focObject);
									refresh_CallContainerItemSetChangeEvent();			
								}
							}
						}
					}
				});
				addPopupMenu(new FVTablePopupMenu("Zoom Out", true) {
					@Override
					public void actionPerformed(FocObject focObject) {
						if(focObject != null){
							FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();
							if(treeTable != null){
								if(treeTable.getFocTreeWrapper().getZoomObject() != null){
									treeTable.getFocTreeWrapper().setZoomObject(null);
									refresh_CallContainerItemSetChangeEvent();			
								}
							}
						}
					}
				});

			}

			//Works with FNode, could also work with TreeTable's setCollapsed
			addPopupMenu(new FVTablePopupMenu("Deep expand", true) {
				
				@Override
				public void actionPerformed(FocObject focObject) {
					if(focObject != null){
						FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();

						if(treeTable != null){
							FTree fTree = treeTable.getFTree();

							if(fTree != null){
								FNode foundNode = fTree.findNodeFromFocObject(focObject);

								if(foundNode != null){
									foundNode.setCollapsedWithPropagation(false);
								}
							}
						}
					}
					refresh_CallContainerItemSetChangeEvent();
				}
			});
			
			addPopupMenu(new FVTablePopupMenu("Expand up to this level", true) {
				
				@Override
				public void actionPerformed(FocObject focObject) {
					if(focObject != null){
						FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();
						
						if(treeTable != null){
							FTree fTree = treeTable.getFTree();
							
							if(fTree != null){
								FNode foundNode = fTree.findNodeFromFocObject(focObject);
								
								if(foundNode != null){
									final int nodeDepth = foundNode.getNodeDepth();
									
									fTree.scanVisible(new TreeScanner<FNode>() {

										@Override
										public boolean beforChildren(FNode node) {
											if(node.getNodeDepth() < nodeDepth){
												node.setCollapsed(false);
											}
											return true;
										}

										@Override
										public void afterChildren(FNode node) {
										}
									});
									
									refresh_CallContainerItemSetChangeEvent();
								}
							}
						}
					}
				}
			});
			
			addPopupMenu(new FVTablePopupMenu("Collapse up to this level") {
				
				@Override
				public void actionPerformed(FocObject focObject) {
					if(focObject != null){
						FVTreeTable treeTable = (FVTreeTable) getTreeOrTable();
						
						if(treeTable != null){
							FTree fTree = treeTable.getFTree();
							
							if(fTree != null){
								FNode foundNode = fTree.findNodeFromFocObject(focObject);
								
								if(foundNode != null){
									final int nodeDepth = foundNode.getNodeDepth();
									
									fTree.scanVisible(new TreeScanner<FNode>() {

										@Override
										public boolean beforChildren(FNode node) {
											if(node.getNodeDepth() >= nodeDepth){
												node.setCollapsed(true);
											}
											return true;
										}

										@Override
										public void afterChildren(FNode node) {
										}
									});
									
									refresh_CallContainerItemSetChangeEvent();
								}
							}
						}
					}
				}
			});
		}
	}
	
	private boolean isCollapsingButtonShow(){
		boolean collapsingAllowed = true;
		String str = getAttributes() != null ? getAttributes().getValue(FXML.ATT_COLLAPSING_ALLOWED) : null;
		if(str != null && (str.toLowerCase().equals("false") || str.equals("0"))){
			collapsingAllowed = false;
		}
		return collapsingAllowed;
	}

	private boolean isLeafLevelEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_LEAF_LEVEL);
			enabled = enableAction != null && !enableAction.isEmpty();
		}
		return enabled;
	}
	
	private boolean isRedirectEnabledEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_REDIRECT_ENABLED);
			enabled = enableAction != null && !enableAction.toLowerCase().equals("false");
		}
		return enabled;
	}

	private boolean isDoubleClickEnabled(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_DOUBLE_CLICK_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}

	private boolean isOpenEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_OPEN_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}
	
	private boolean isExcelExportEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_EXCEL_EXPORT_ENABLED);
//			enabled = enableAction != null && enableAction.toLowerCase().equals("true");
			enabled = enableAction == null || (enableAction != null && enableAction.toLowerCase().equals("true"));
		}
		return enabled;
	}

	public boolean isLeafNodeEnabled(){
		return isEditable() && isLeafLevelEnabled_Attribute();
	}
	
	public boolean isRedirectEnabled(){
		boolean allowNamingModif = Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getGroup() != null ? Globals.getApp().getUser_ForThisSession().getGroup().allowNamingModif() : false;
		return isEditable() && isRedirectEnabledEnabled_Attribute() && allowNamingModif;
	}

	public void setOpenEnabled(boolean enabled){
		openEnabled = enabled;
	}
	
	public boolean isOpenEnabled(){
		return isEditable() && isOpenEnabled_Attribute() && openEnabled;
	}

	public void setExportToExcelEnabled(boolean enabled){
		excelExportEnabled = enabled;
	}
	
	public boolean isExcelExportEnabled(){
		return isEditable() && isExcelExportEnabled_Attribute() && excelExportEnabled;
	}

	private boolean isAddEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_ADD_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}

	public boolean isTransactionFilterEnabled(){
		boolean enabled = true;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_TRANSACTION_FILTER_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}

	public boolean isTransactionColorEnabled(){
		boolean enabled = true;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_TRANSACTION_COLORING_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}

	public boolean getTransactionDefaultColor(){
		boolean enabled = true;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_TRANSACTION_DEFAULT_COLORING);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}

	private boolean isDuplicateEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_DUPLICATE_ENABLED);
			enabled = enableAction != null && enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}
	
	public boolean toggleStatusStyleEnabled(){
		setStatusStyleEnabled(!isStatusStyleEnabled());
		return isStatusStyleEnabled();
	}

	public boolean isStatusStyleEnabled(){
		return statusStyleEnabled;
	}
	
	public void setStatusStyleEnabled(boolean statusStyleEnabled){
		this.statusStyleEnabled = statusStyleEnabled;
	}
	
//	private boolean isStatusStyleEnabled_Attribute(){
//		boolean enabled = true;
//		if(getAttributes() != null){
//			String enableAction = getAttributes().getValue(FXML.ATT_STATUS_STYLE_ENABLED);
//			enabled = enableAction != null && enableAction.toLowerCase().equals("false");
//		}
//		return enabled;
//	}

	public void setAddEnabled(boolean enabled){
		addEnabled = enabled;
		
	}
	
	public void setDuplicateEnabled(boolean enabled){
		duplicateEnabled = enabled;
	}

	public boolean isAllowListModification(){
		boolean allow = isEditable();
		if(allow && getTreeOrTable() != null){
			FocList list = getTreeOrTable().getFocList();
			if(list != null && list.getFocDesc() != null){
				AccessSubject accessSubject = list.getFatherSubject();
				if(accessSubject != null && accessSubject instanceof FocObject){
					FocDesc focDesc = list.getFocDesc();
					if(focDesc != null){
						allow = ((FocObject)accessSubject).isEditable_AtomicNoDotLookup(focDesc.getFieldName_ForList());
					}
				}
			}
		}
		return allow;
	}
	
	public boolean isAddEnabled(){
		return isEditable() && isAddEnabled_Attribute() && addEnabled && (getFocDesc() == null || getFocDesc().workflow_IsAllowInsert()) && isAllowListModification();
	}
	
	public boolean isDuplicateEnabled(){
		return duplicateEnabled && isDuplicateEnabled_Attribute();
	}

	private boolean isDeleteEnabled_Attribute(){
		boolean enabled = false;
		if(getAttributes() != null){
			String enableAction = getAttributes().getValue(FXML.ATT_DELETE_ENABLED);
			enabled = enableAction == null || enableAction.toLowerCase().equals("true");
		}
		return enabled;
	}
	
	public void setDeleteEnabled(boolean enabled){
		deleteEnabled = enabled;
	}
	
	public boolean isDeleteEnabled(){
		return isEditable() && isDeleteEnabled_Attribute() && deleteEnabled && (getFocDesc() == null || getFocDesc().workflow_IsAllowDelete(null)) && isAllowListModification();
	}
	
	public boolean isPopupMenuEnabled() {
		return popupMenuEnabled;
	}

	public void setPopupMenuEnabled(boolean popupMenuEnabled) {
		this.popupMenuEnabled = popupMenuEnabled;
	}
	
	public FocDesc getFocDesc() {
		return treeOrTable != null ? treeOrTable.getFocDesc() : null;
	}

	public Table getTable() {
		return treeOrTable instanceof Table ? (Table) treeOrTable : null;
	}

	public FocXMLAttributes getAttributes() {
		return (FocXMLAttributes) attributes;
	}

	public ArrayList<ITableListener> getTableListenerArrayList(boolean create) {
		if(tableListenerArrayList == null && create){
			tableListenerArrayList = new ArrayList<ITableListener>();
		}
		return tableListenerArrayList;
	}

	public void addListener(ITableListener tableListener) {
		getTableListenerArrayList(true).add(tableListener);
	}

	public void removeListener(ITableListener tableListener) {
		getTableListenerArrayList(false).remove(tableListener);
	}

	private void fireEvent_Open(FocObject focObject) {
		ArrayList<ITableListener> arrayList = getTableListenerArrayList(false);
		if(arrayList != null){
			for(int i = 0; i < arrayList.size(); i++){
				arrayList.get(i).openItem(focObject);
			}
		}
	}

	private void fireEvent_Add(FocObject fatherObject, FocObject focObject) {
		ArrayList<ITableListener> arrayList = getTableListenerArrayList(false);
		if(arrayList != null){
			for(int i = 0; i < arrayList.size(); i++){
				arrayList.get(i).addItem(fatherObject, focObject);
			}
		}
	}

	private void fireEvent_Delete(FocObject focObject) {
		ArrayList<ITableListener> arrayList = getTableListenerArrayList(false);
		if(arrayList != null){
			for(int i = 0; i < arrayList.size(); i++){
				arrayList.get(i).deleteItem(focObject);
			}
		}
	}

	public void setAttributes(Attributes attrib) {
		attributes = attrib;
		FocXMLGuiComponentStatic.applyAttributes((FocXMLGuiComponent) getTreeOrTable(), attributes);

		if(attributes != null){
			String style = attributes.getValue(FXML.ATT_STYLE);
			if(style != null && !style.isEmpty()){
				if(getTable() != null) getTable().addStyleName(style);
			}
			
			String rowSelectionMode = attributes.getValue(FXML.ATT_GRID_SELECTION_MODE);
			if(rowSelectionMode != null && rowSelectionMode.equals(FXML.VAL_GRID_SELECTION_MODE_MULTI)){
				if(getTreeOrTable() instanceof Grid){
					Grid grid = (Grid) getTreeOrTable();
					grid.setSelectionMode(SelectionMode.MULTI);
				}
			}
			
			String pageLength = attributes.getValue(FXML.ATT_TREE_TABLE_PAGE_LENGTH);

//			if(pageLength != null && !pageLength.isEmpty()){
//				if(pageLength.equals(FXML.VAL_TREE_TABLE_FIT_SIZE)){
//					if(getTreeOrTable() != null && getTreeOrTable().getFocList() != null){
//						int listSize = ((Table)getTreeOrTable()).getVisibleItemIds().size();
//						if(listSize > 0){
//							getTable().setPageLength(listSize);
//						}else if(listSize == 0){
//							getTable().setPageLength(1);
//						}
//						getTable().markAsDirty();
//					}
//				}else{
//					try{
//						getTable().setPageLength(Integer.parseInt(pageLength));
//					}catch (Exception e){
//						Globals.logException(e);
//					}
//				}
//			}
			
			if(pageLength != null && !pageLength.isEmpty()){
				if(pageLength.equals(FXML.VAL_TREE_TABLE_FIT_SIZE)){
					if(getTreeOrTable() != null && getTreeOrTable().getFocList() != null){
						fitTreeToMax();
//						FocList list = getTreeOrTable().getFocList();
//						int listSize = list.size();
////						if(listSize > 50) listSize = 50;
//						if(listSize > 0){
//							getTable().setPageLength(listSize);
//						}else if(listSize == 0){
//							getTable().setPageLength(1);
//						}
//						getTable().markAsDirty();
					}
				}else{
					try{
						if(getTable() != null){
							getTable().setPageLength(Integer.parseInt(pageLength));
						}
					}catch (Exception e){
						Globals.logException(e);
					}
				}
			}
			
			String replaceCheckbox = attributes.getValue(FXML.ATT_REPLACE_CHECKBOX);
			
			if(!Utils.isStringEmpty(replaceCheckbox) && replaceCheckbox.equalsIgnoreCase("true")){
				if(getWrapperLayout() != null){
					getWrapperLayout().enableReplace();
				}
			}

			editingMode = MODE_NOT_EDITABLE;
			String value = getAttributes().getValue(FXML.ATT_IN_LINE_EDITABLE);
			if(value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("onSelection")) && isEditable()){
				editingMode = MODE_EDITABLE;
				if(getWrapperLayout() != null && getWrapperLayout().getDelegate() != null){
					getWrapperLayout().getDelegate().setEditable(true);

//					if(getTable() != null){
//						boolean atLEastOneIsEditable = false;
//						ArrayList<FVTableColumn> arrayList = getVisiblePropertiesArrayList();
//						if(arrayList != null){
//							for(int i = 0; i < arrayList.size() && !atLEastOneIsEditable; i++){
//								FVTableColumn col = arrayList.get(i);
//								atLEastOneIsEditable = col != null && col.isEditable_ExplicitlyInAttribute();
//							}
//						}
//						if(atLEastOneIsEditable){
//							getTable().addStyleName("inLineEditable");
//						}
//					}
				}
			}
			
			/*
			if(value != null && value.equalsIgnoreCase("onSelection")){
				editingMode = MODE_EDITABLE_ON_SELECTION;
				getTreeOrTable().setEditable(true);
				getTable().setImmediate(true);
				getTable().addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
//					  getFocXMLLayout().validationCheckData();
						Integer refSelected = (Integer) getTable().getValue();
						int refSelectefInt = refSelected != null ? refSelected.intValue() : 0; 
						if(lastRefSelected == 0 || (refSelectefInt != lastRefSelected)){
							lastRefSelected = refSelectefInt;
							Globals.logString("XXXXXXXXXXX  Called the Listener in Table Delegate");
						  if(getFocXMLLayout() != null) getFocXMLLayout().copyGuiToMemory();
							getTable().markAsDirty();
							getTable().refreshRowCache();
							getFocXMLLayout().markAsDirty();
							getFocXMLLayout().refresh();
						}
					}
				});
			}
			*/
			
		}
	}
	
	public void fitTreeToMax(){
		FocList list = getTreeOrTable().getFocList();
		if(list != null && getTable() != null){
			int listSize = list.size();
			if(getTable() != null && getTable().getContainerDataSource() != null && getTable().getContainerDataSource().getItemIds() != null){
				listSize = getTable().getContainerDataSource().getItemIds().size();
			}
	//		if(listSize > 50) listSize = 50;
			if(listSize > 0){
				getTable().setPageLength(listSize);
			}else if(listSize == 0){
				getTable().setPageLength(1);
			}
			getTable().markAsDirty();
		}
	}
	
	public ArrayList<FVTableColumn> getVisiblePropertiesArrayList() {
		if(visiblePropertiesArray == null){
			visiblePropertiesArray = new ArrayList<FVTableColumn>();
		}
		return visiblePropertiesArray;
	}
	
	public FVTableColumn getVisibleColumnByIndex(int index){
		FVTableColumn result = null;
		
		if(getVisiblePropertiesArrayList() != null && getVisiblePropertiesArrayList().size() >= index){
			result = getVisiblePropertiesArrayList().get(index);
		}
		
		return result;
	}

	public void removeColumn(String name) {
		FVTableColumn column = findColumn(name);
		if(column != null && getVisiblePropertiesArrayList() != null){
			getVisiblePropertiesArrayList().remove(column);
			getTreeOrTable().applyFocListAsContainer();
//			getTreeOrTable().getFocDataWrapper().firePropertySetChangeEvent();
		}
	}
	
	public FVTableColumn findColumn(String name) {
		FVTableColumn found = null;
		ArrayList<FVTableColumn> arrayList = getVisiblePropertiesArrayList();
		for(int i = 0; i < arrayList.size() && found == null; i++){
			FVTableColumn col = arrayList.get(i);
			if(col.getName().equals(name)){
				found = col;
			}
		}
		return found;
	}

	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		return addColumn(false, attributes);
	}
	
	public FVTableColumn addColumnAtFirst(FocXMLAttributes attributes) {
		return addColumn(true, attributes);
	}
	
	public FVTableColumn addColumn(boolean atFirst, FocXMLAttributes attributes) {
		FVTableColumn tableColumn = null;
		try{
			//The VisibleWhen false was first used for pivots that require fields for computation
			//But we do not wish to realy show these columns in the tables.
			boolean columnVisible = true;
			if(attributes != null){
				String visible = attributes.getValue(FXML.ATT_VISIBLE_WHEN);
				if(visible != null && visible.trim().toLowerCase().equals("false")){
					columnVisible = false;
				}
			}
			if(columnVisible){
				tableColumn = new FVTableColumn(getTreeOrTable(), attributes);
				
				boolean first = atFirst;
				if(isRTL()){
					first = !first;
				}
				
				if(first){
					getVisiblePropertiesArrayList().add(0, tableColumn);
				}else{
					getVisiblePropertiesArrayList().add(tableColumn);				
				}
				
				tableColumn.applyColumnSettings(getTreeOrTable());
				if(getTreeOrTable() instanceof Table){
					adjustGeneratedColumnForPropertyID(tableColumn);
				}
	
				if(getTreeOrTable() instanceof Table){
					((Table) getTreeOrTable()).markAsDirty();
				}
			}
		}catch (Exception e){
			Globals.logException(e);
		}
		return tableColumn;
	}
	
	public FVTableColumn addColumn(String title, String dataPath) {
		FVTableColumn tableColumn = null;
		try{
			tableColumn = new FVTableColumn(getTreeOrTable(), dataPath, null, null, null, null, title, title, "-1");
			getVisiblePropertiesArrayList().add(tableColumn);
			tableColumn.applyColumnSettings(getTreeOrTable());
			adjustGeneratedColumnForPropertyID(tableColumn);

			((Table) getTreeOrTable()).markAsDirty();

		}catch (Exception e){
			Globals.logException(e);
		}
		return tableColumn;
	}

	public static String newComponentName(String tableName, String objRef, String colProperty) {
		StringBuffer buff = new StringBuffer(newComponentNamePrefix(tableName, objRef));
		buff.append(colProperty);
		return buff.toString();
	}

	public static String newComponentNamePrefix(String tableName, String objRef) {
		StringBuffer buff = new StringBuffer(tableName);
		buff.append("|");
		buff.append(objRef);
		buff.append("|");
		return buff.toString();
	}
	
//	private Listener itemClickListener = null;
	
	public Component newGuiComponent(FocObject focObject, FVTableColumn tableColumn, FProperty property) {
		Component component = null;
		if(property != null){
			FocXMLLayout xmlLayout = getFocXMLLayout();
			if(xmlLayout != null){

				String tableName = getTableName();
				String objRef = focObject.getReference().toString();
				String columnName = tableColumn.getName();

				String compName = newComponentName(tableName, objRef, columnName);
				component = xmlLayout.getComponentByName(compName);
				
				boolean createANewConmponent = component == null;
				if(!createANewConmponent){
					FocXMLGuiComponent xmlGuiComp = (FocXMLGuiComponent) component;
					String componentXMLType = xmlGuiComp.getXMLType();
					String newComponentXMLType = FVGUIFactory.getInstance().getKeyForProperty(property);
					
					if(componentXMLType != null && newComponentXMLType != null && !newComponentXMLType.equals(componentXMLType)){
						createANewConmponent = true;
					}
				}
				
				if(createANewConmponent){
					component = xmlLayout.newGuiField(compName, focObject, tableColumn.getDataPath(), property, tableColumn.getAttributes());
				}
				//We want the default width to be 100% for the component so that the resizing of the component is automatic with the column  
				if(tableColumn.getAttributes() != null && tableColumn.getAttributes().getValue(FXML.ATT_WIDTH) == null){
					component.setWidth("100%");
				}

				/*
				if(itemClickListener == null){
					itemClickListener = new Listener() {
						@Override
						public void componentEvent(Event event) {
							if(event != null && event.getComponent() != null && event.getComponent() instanceof FocXMLGuiComponent){
								FocXMLGuiComponentDelegate delegate = ((FocXMLGuiComponent)event.getComponent()).getDelegate();
								if(delegate != null){
									lastClickedFocDataByPath = ((FocXMLGuiComponent)event.getComponent()).getDelegate().getDataPath();
								}
							}
						}
					}; 
				}
				
				component.addListener(itemClickListener);
				*/
			}

			/*
			 * FocXMLGuiComponent guiField =
			 * FVFieldFactory.getInstance().newGuiComponent(property, null);
			 * AbstractComponent component = (AbstractComponent) guiField;
			 */
		}
		return component;
	}

	public boolean isRTL(){
		return getFocXMLLayout() != null ? getFocXMLLayout().isRTL() : false;
	}
	
	public FocXMLLayout getFocXMLLayout() {
		FocXMLLayout xmlLayout = null;

		Component comp = (Component) getTreeOrTable();
		while (comp != null && xmlLayout == null){
			if(comp instanceof FocXMLLayout){
				xmlLayout = (FocXMLLayout) comp;
			}
			comp = comp.getParent();
		}
		return xmlLayout;
	}

	private void adjustGeneratedColumnForPropertyID(FVTableColumn tableColumn) {
		
		String columnName = tableColumn.getName();

		tableColumn.applyColumnSettings(getTreeOrTable());
		
		if(getFocDesc() != null){
			ColumnGenerator colGenerator = getFocXMLLayout() != null ? getFocXMLLayout().table_getGeneratedColumn(getTableName(), tableColumn) : null;

			if(colGenerator == null){
				colGenerator = getColumnGenerator(tableColumn);
			}
			
			try{
				
				if(getTable().getColumnGenerator(columnName) == null){
					getTable().addGeneratedColumn(columnName, colGenerator);
					
				}
				
			}catch (Exception e){
				Globals.logException(e);
			}
		}
	}
	
	private ColumnGenerator getColumnGenerator(FVTableColumn tableColumn){
		return tableColumn.getColumnGenerator(true);
	}
	
	private Object getDisplayObject_ForProperty_NonEditable(FProperty property, FVTableColumn column){
		Object objReturned = null;
		if(property != null){
			objReturned = property.vaadin_TableDisplayObject(null, column.getCaptionProp());
			if(objReturned instanceof Boolean){
				if(((Boolean) objReturned).booleanValue()){
					objReturned = new Embedded("", new ThemeResource("../runo/icons/16/ok.png"));
				}else{
					objReturned = new Embedded("", new ThemeResource("../runo/icons/16/cancel.png"));
				}
			}else if(property instanceof FObject
					  || property instanceof FDate
					  || property instanceof FString
					  || property instanceof FDouble){
	
				FVLabel lbl = null;
				String styleAttrib = column.getAttributes().getValue(FXML.ATT_STYLE);
				if(styleAttrib != null && !styleAttrib.isEmpty()){
					lbl = new FVLabel((String)objReturned);
					lbl.parseStyleAttributeValue(styleAttrib);
					objReturned = lbl;
				}else if(property instanceof FDouble){
					lbl = new FVLabel((String)objReturned);
					lbl.addStyleName("foc-text-right");
					objReturned = lbl;
				}

			}else{
				objReturned = property;
			}
		}

		return objReturned;
	}
	
	/*
	 * public void adjustGeneratedColumns(){ if(getVisiblePropertiesArrayList() !=
	 * null){ for(int i=0; i<getVisiblePropertiesArrayList().size(); i++){
	 * adjustGeneratedColumnForPropertyID(getVisiblePropertiesArrayList().get(i));
	 * } // getTable().setVisibleColumns(getVisibleColumnIds()); } }
	 */

	/**
	 * This is applicable only for TreeTable and Table
	 */
	public void pushVisibleColumns() {
		ArrayList<String> arrayList = newVisibleColumnIds();
		if(arrayList.size() > 0){
			Table table = (Table) getTreeOrTable();
			table.setVisibleColumns(arrayList.toArray());
		}
		computeFooter();
	}

	public ArrayList<String> newVisibleColumnIds() {
		ArrayList<String> strgCol = new ArrayList<String>();
		for(int i = 0; i < getVisiblePropertiesArrayList().size(); i++){
			FVTableColumn tableColumn = getVisiblePropertiesArrayList().get(i);
			if(tableColumn.isVisible(getFocXMLLayout())){
				strgCol.add(tableColumn.getName());
			}
		}
		//This is added for the Tree Grid because the title node needs the REF and the ref is most likely not added in the Column Tags.
		if(getTreeOrTable() instanceof FVTreeGrid){
			String refFieldName = getFocDesc().getRefFieldName();
			if(!Utils.isStringEmpty(refFieldName) && !strgCol.contains(refFieldName)){
				if(getFocDesc() != null && getFocDesc().getFieldByID(FField.REF_FIELD_ID) != null){ 
					strgCol.add(refFieldName);
				}
			}
		}
		//-------------
		return strgCol;
	}
	
	public void setContainerDataSource(Container container){
		Table table = (Table) getTreeOrTable();
		if(table != null){
			ArrayList<String> arrayOfColIds = newVisibleColumnIds();
			try{
				table.setContainerDataSource(container, arrayOfColIds);
			}catch(Exception e){
				Globals.logException(e);
				arrayOfColIds = newVisibleColumnIds();
				table.setContainerDataSource(container, arrayOfColIds);
			}
		}
	}

	public void fillXMLNodeContent(XMLBuilder builder) {
		String viewContainerOpenAsAttribute = viewContainer_Int2Attribute(viewContainer_Open);
		builder.appendLine("<" + FXML.TAG_VIEW_KEY_OPEN + " " + FXML.ATT_VIEW_CONTAINER + "=\"" + viewContainerOpenAsAttribute + "\" />");
		for(int i = 0; i < getVisiblePropertiesArrayList().size(); i++){
			FVTableColumn tableCol = getVisiblePropertiesArrayList().get(i);

			String dataPath = tableCol.getDataPath();
			String captionProp = tableCol.getCaptionProp();
			String immediate = tableCol.getImmediate();
			String editable = tableCol.getEditable();

			String dataPathCaptionPropValues = "";
			String immediateValue = "";
			String editableValue = "";

			if(immediate != null && !immediate.isEmpty()){
				immediateValue = "immediate=\"" + immediate + "\"";
			}

			if(editable != null && !editable.isEmpty()){
				editableValue = "editable=\"" + editable + "\"";
			}

			if(dataPath != null && !dataPath.isEmpty() && captionProp != null && !captionProp.isEmpty()){
				dataPathCaptionPropValues = "dataPath=\"" + dataPath + "\" captionProperty=\"" + captionProp + "\"";
			}

			builder.appendLine("<" + FXML.TAG_TABLE_COLUMN + " name=\"" + tableCol.getName() + "\" caption=\"" + tableCol.getCaption() + "\" " + dataPathCaptionPropValues + " " + immediateValue + " " + editableValue + " />");
		}
	}

	public boolean isAllowEditingCheckBox() {
		return allowEditingCheckBox;
	}

	public FVCheckBox getEditableCheckBox() {
		if(isAllowEditingCheckBox() && editableCheckBox == null){
			editableCheckBox = new FVCheckBox("Check for edit.");
			editableCheckBox.setImmediate(true);
			editableCheckBox.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					boolean editable = editableCheckBox.getValue();
					editableCheckBox.setCaption(editable ? "Uncheck for normal mode" : "Check for editable mode");
					getWrapperLayout().getDelegate().setEditable(editableCheckBox.getValue());
					if(editable){
						// adjustGeneratedColumns();
						Table table = (Table) getTreeOrTable();
//						table.markAsDirtyRecursive();
					}
				}
			});
		}
		return editableCheckBox;
	}
	
	public ITableTree getTreeOrTable() {
		return treeOrTable;
	}

	public ArrayList<FVTablePopupMenu> getPopupMenuArrayList() {
		if(popupMenuArrayList == null){
			popupMenuArrayList = new ArrayList<FVTablePopupMenu>();
		}
		return popupMenuArrayList;
	}

	private FVTablePopupMenu[] getPopupMenuFullArray() {
		FVTablePopupMenu[] returnedMenu = null;
		if(popupMenuFullArray == null){
			int popupMenuArraylistSize = getPopupMenuArrayList().size();
			ArrayList<FVTablePopupMenu> fvTablePopupMenu = new ArrayList<FVTablePopupMenu>();
			
			for(int i = 0; i < popupMenuArraylistSize; i++){
				FVTablePopupMenu tablePopupMenu = getPopupMenuArrayList().get(i);
				if(isPopupMenuEnabled()){
					fvTablePopupMenu.add(tablePopupMenu);
				}else if(!isPopupMenuEnabled() && tablePopupMenu.isAllowThisMenu()){
					fvTablePopupMenu.add(tablePopupMenu);
				}
			}
			
			FVTablePopupMenu[] fvTablePopupMenuArray = new FVTablePopupMenu[fvTablePopupMenu.size()];
			Iterator<FVTablePopupMenu> tablePopupMenuItr = fvTablePopupMenu.iterator();
			int i=0;
			while(tablePopupMenuItr.hasNext()){
				fvTablePopupMenuArray[i] = tablePopupMenuItr.next();
				i++;
			}
			popupMenuFullArray = fvTablePopupMenuArray;
		}
		returnedMenu = popupMenuFullArray;
		return returnedMenu;
	}

	public void addPopupMenu(FVTablePopupMenu popupMenuInterface) {
		getPopupMenuArrayList().add(popupMenuInterface);
	}
	
	public void removePopupMenu(String caption) {
		for(int i=0;i<getPopupMenuArrayList().size();i++){
			FVTablePopupMenu menu = getPopupMenuArrayList().get(i);
			if(menu.getCaption() == caption){
				getPopupMenuArrayList().remove(i);
			}
		}
	}
	
	public void removePopupMenu(int actionId) {
		for(int i=0;i<getPopupMenuArrayList().size();i++){
			FVTablePopupMenu menu = getPopupMenuArrayList().get(i);
			if(menu.getActionId() == actionId){
				getPopupMenuArrayList().remove(i);
			}
		}
	}
	
	public void addPopupMenu_CopyAllRows(){
		addPopupMenu(new FVTablePopupMenu("Copy cell to all rows") {
			@Override
			public void actionPerformed(FocObject focObject) {

				if(lastClickedFocDataByPath == null){
					Globals.showNotification("No editable cell selected", "", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
				}else{
				//----BHussein
					String value = "";
					
					if(focObject != null){
						IFocData focDataSource = focObject.iFocData_getDataByPath(lastClickedFocDataByPath);
						if(focDataSource.iFocData_getValue() != null){
							value = focDataSource.iFocData_getValue().toString();	
						}
					}

					OptionDialog optionDialog = new OptionDialog("Confirmation", "Are you sure you want to copy cell :"+lastClickedFocDataByPath+", of value :"+value+", to all lines?"){
						@Override
						public boolean executeOption(String option) {
							if(option.equals("COPY")){
								
								FocObject focObject = getSelectedObject();
								
								if(focObject != null && lastClickedFocDataByPath != null){
									IFocData focDataSource = focObject.iFocData_getDataByPath(lastClickedFocDataByPath);
									if(focDataSource != null && focDataSource instanceof FProperty){
										FProperty prop = (FProperty) focDataSource; 
										FocList list = getTreeOrTable().getFocList();

										for(int i=0; i<list.size(); i++){
											FocObject tarObj = list.getFocObject(i);
											IFocData targetFocDataSource = tarObj.iFocData_getDataByPath(lastClickedFocDataByPath);
											if(targetFocDataSource != null && targetFocDataSource instanceof FProperty){
												FProperty targetProp = (FProperty)targetFocDataSource; 
												targetProp.copy(prop);
											}
										}
									}
								}
							}
						  return false;
						}
					};
					optionDialog.addOption("COPY", "Yes copy");
					optionDialog.addOption("CANCEL", "Cancel");
		    	optionDialog.setWidth("500px");
		    	optionDialog.setHeight("200px");
	
		    	Globals.popupDialog(optionDialog);
				}
			}
		});
	}
	
	public void addPopupMenu_OpenTreeNode() {
		
		addPopupMenu(new FVTablePopupMenu("View Leaves") {
			@Override
			public void actionPerformed(FocObject focObject) {
				if(focObject != null && focObject.getReference() != null && getTable() != null && getTable() instanceof FVTreeTable){
					int objectRef = focObject.getReference().getInteger();
					FVTreeTable fvTreeTable = (FVTreeTable) getTable();
					fvTreeTable.setChildrenAllowed(objectRef, true);
					fvTreeTable.setChildrenAllowedCheckerEnable(false);
					fvTreeTable.setCollapsed(objectRef, false);
					fvTreeTable.markAsDirtyRecursive();
					
				}
			}
		});
	}
	
	private void addPopUpMenu_Redirect(){
		/*
		getTable().addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				FocObject selectedObject = (FocObject) event.getItem();
				FocList listToChooseFrom = getFocDesc().getFocList(FocList.LOAD_IF_NEEDED);
				if(selectedObject != null && listToChooseFrom != null && getAttributes() != null && getAttributes().getValue(FXML.ATT_REDIRECT_ENABLED) != null){
					String dataPath = getAttributes().getValue(FXML.ATT_REDIRECT_ENABLED);
					FVRedirectWindow redirectWindow = new FVRedirectWindow(listToChooseFrom, selectedObject, dataPath);
					redirectWindow.popup();
				}
			}
		});
		*/
		
		addPopupMenu(new FVTablePopupMenu("Redirect Object") {
			@Override
			public void actionPerformed(FocObject focObject) {
				FocObject selectedObject = focObject;
				FocList listToChooseFrom = getFocDesc().getFocList(FocList.LOAD_IF_NEEDED);
				if(selectedObject != null && listToChooseFrom != null && getAttributes() != null && getAttributes().getValue(FXML.ATT_REDIRECT_ENABLED) != null){
					String dataPath = getAttributes().getValue(FXML.ATT_REDIRECT_CAPTION_PROPERTY);
					FVRedirectWindow redirectWindow = new FVRedirectWindow(listToChooseFrom, selectedObject, dataPath);
					redirectWindow.popup();
				}
			}
		});
	}

	public void addPopupMenu_Add() {
		if(getWrapperLayout() != null){
			FVButton button = new FVButton("Add");
			button.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if(addEnabled){
						FocObject focObject = getSelectedObject();
						if(focObject != null){
							addItem(focObject);
						}
					}else{
						Globals.showNotification("Rights Error", "You do not have the right to add items to this table", IFocEnvironment.TYPE_WARNING_MESSAGE);
					}
				}
			});
			getWrapperLayout().addHeaderComponent(button);	
		}
		
		addPopupMenu(new FVTablePopupMenu(ACTION_ADD, "Add") {
			@Override
			public void actionPerformed(FocObject focObject) {
				if(addEnabled){
					addItem(focObject);
				}else{
					Globals.showNotification("Rights Error", "You do not have the right to add items to this table", IFocEnvironment.TYPE_WARNING_MESSAGE);
				}
			}
		});
	}
	
	public void addPopupMenu_Duplicate(){
		addPopupMenu(new FVTablePopupMenu("Duplicate") {
			
			@Override
			public void actionPerformed(FocObject focObject) {
				if(getWrapperLayout() != null && focObject != null){
					duplicate(focObject);
				}
			}
		});
	}
	
	public void duplicate(FocObject sourceObj){
		ITableTree iTableTree = getTreeOrTable();
		if(iTableTree != null && iTableTree.getFocDataWrapper() != null){
	  	FocList focList = iTableTree.getFocList();
			if(focList != null){
				boolean callValidation = true;
		  	if(!focList.isDirectlyEditable() && focList.isDirectImpactOnDatabase()){
		  		callValidation = false;
		  	}
		  	if(getFocXMLLayout() != null){
		    	getFocXMLLayout().copyGuiToMemory();
		    }
				FocObject newObj = focList.newEmptyItem();
		    sourceObj.duplicate(newObj, sourceObj.getMasterObject(), true, callValidation);
		    if(isOpenEnabled()){
		    	open(newObj);
		    }
		    iTableTree.getFocDataWrapper().refreshGuiForContainerChanges();
		    if(getFocXMLLayout() != null){
		    	getFocXMLLayout().copyMemoryToGui();
		    }
			}
		}
  }
	
	public void addPopupMenu_ExportAsCSV(){
		addPopupMenu(new FVTablePopupMenu("Export To CSV File") {
			
			@Override
			public void actionPerformed(FocObject focObject) {
				FVTableTreeCSVExport excelExport = null;
				if(getTreeOrTable() instanceof FVTreeTable){
//					excelExport = new FVTreeToExcelExport(TableTreeDelegate.this);
					excelExport = new_fvTreeTableExportToCSV();
				}else{
//					excelExport = new FVTableToExcelExport(TableTreeDelegate.this);
					excelExport = new_fvTableExportToCSV();
				}
				excelExport.dispose();
			}
		});
	}
	
	public void addPopupMenu_ExporAsExcel(){
		addPopupMenu(new FVTablePopupMenu("Export To EXCEL File") {
			
			@Override
			public void actionPerformed(FocObject focObject) {
				FVTableTreeEXCELExport excelExport = null;
				if(getTreeOrTable() instanceof FVTreeTable){
					excelExport = new_fvTreeExportToEXCEL();
				}else{
					excelExport = new_fvTableExportToEXCEL();
				}
				excelExport.dispose();
			}
		});
	}
	
	public FVTableTreeCSVExport new_fvTreeTableExportToCSV(){
		return new FVTreeToCSVExport(this);
	}
	
	public FVTableTreeCSVExport new_fvTableExportToCSV(){
		return new FVTableToCSVExport(this);
	}
	
	public FVTableToEXCELExport new_fvTableExportToEXCEL(){
		return new FVTableToEXCELExport(this);
	}
	
	public FVTreeToEXCELExport new_fvTreeExportToEXCEL(){
		return new FVTreeToEXCELExport(this);
	}
	
	public FocObject getSelectedObject_ForVaadinTable(){
		FocObject focObject = null;
		
		Table table = (Table) getTreeOrTable();
		Object selected = table.getValue();
		if(selected instanceof Integer){
			int ref = ((Integer)selected).intValue();
			focObject = getTreeOrTable().getFocList().searchByReference(ref);
		}
		return focObject;
	}
	
	public void setSelectedObject_ForVaadinTable(FocObject selectedObject){
		Table table = (Table) getTreeOrTable();
		if(selectedObject != null){
			if(table != null) table.select(selectedObject.getReferenceInt());
		}else{
			if(table != null) table.select(table.getNullSelectionItemId());
		}
	}
	
	public FocObject getSelectedObject(){
		FocObject focObject = getTreeOrTable().getSelectedObject();
		return focObject;
	}
	
	public void addPopupMenu_Open() {
		addPopupMenu(new FVTablePopupMenu(ACTION_OPEN, "Open") {
			@Override
			public void actionPerformed(FocObject focObject) {
				open(focObject);
			}
		});

		openClickListener = new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick()){
					if(getFocXMLLayout() != null){
						//By default FocXMLLayout will also call the defaultItemDoubleClickAction(event); 
						getFocXMLLayout().table_ItemDoubleClick(getTableName(), getTreeOrTable(), event);
					}else{
						defaultItemDoubleClickAction(event);
					}
				}
			}
		};
		
		getTreeOrTable().addItemClickListener(openClickListener);
//		((Grid) getTreeOrTable()).addItemClickListener(openClickListener);
//		((Table) getTreeOrTable()).addItemClickListener(openClickListener);
	}
	
	public void defaultItemDoubleClickAction(ItemClickEvent event){
		Integer refIntObj = (Integer) event.getItemId();
		//TODO check later
		//					int ref = refIntObj != null ? refIntObj.intValue() : -1;
		if(refIntObj != null && isOpenEnabled()){
			FocObject focObject = getTreeOrTable().getFocList().searchByReference(refIntObj);
			getTreeOrTable().open(focObject);
		}
	}
	
	@SuppressWarnings("serial")
	public void addPopupMenu_Delete() {
		addPopupMenu(new FVTablePopupMenu(ACTION_DELETE, "Delete") {
			@Override
			public void actionPerformed(FocObject focObject) {
				delete(focObject);
			}
		});
	}
	
	public void delete(FocObject focObject){
		if(focObject != null){
			StringBuffer message = focObject.checkDeletionWithMessage();
			if(message != null){
				Globals.showNotification("Cannot delete Item.", message.toString(), IFocEnvironment.TYPE_WARNING_MESSAGE);	
			}else{
				OptionDialog dialog = new OptionDialog("Delete Confirmation", "Are you sure you want to delete this item", focObject) {
					
					@Override
					public boolean executeOption(String optionName) {
						if(optionName != null && optionName.equals("DELETE")){
							FocObject focObject = (FocObject) getOptionFocData();
							FocXMLLayout xmlLay = getFocXMLLayout();
							if(xmlLay == null){
								delete_NoPopupConfirmation(focObject);
							}else{
								xmlLay.table_DeleteItem(getTreeOrTable(), focObject);
							}
		
						}
						return false;
					}
				};
				dialog.addOption("DELETE", "Delete");
				dialog.addOption("CANCEL", "Cancel");
				dialog.setWidth("400px");
				dialog.setHeight("180px");
				dialog.popup();
			}
		}
		/*
		OptionDialogWindow optionWindow = new OptionDialogWindow("Are you sure you want to delete this item", focObject);
		optionWindow.setWidth("300px");
		optionWindow.setHeight("200px");
	
		optionWindow.addOption("Delete", new IOption() {
			@Override
			public void optionSelected(Object contextObject) {
			}
		});
		optionWindow.addOption("Cancel", new IOption() {
			@Override
			public void optionSelected(Object contextObject) {
	
			}
		});
		FocWebApplication.getInstanceForThread().addWindow(optionWindow);
		*/
//		OptionDialogWindow optionWindow = new OptionDialogWindow("Are you sure you want to delete this item", focObject);
//		optionWindow.setWidth("300px");
//		optionWindow.setHeight("200px");
//
//		optionWindow.addOption("Delete", new IOption() {
//			@Override
//			public void optionSelected(Object contextObject) {
//				FocObject focObject = (FocObject) contextObject;
//				delete_NoPopupConfirmation(focObject);
//			}
//		});
//		optionWindow.addOption("Cancel", new IOption() {
//			@Override
//			public void optionSelected(Object contextObject) {
//
//			}
//		});
//		FocWebApplication.getInstanceForThread().addWindow(optionWindow);
	}
	
	// public boolean isInLineEditing(){
	// boolean isInLineEditable = false;
	// if(getAttributes() != null){
	// String value = getAttributes().getValue(FXML.ATT_IN_LINE_EDITABLE);
	// if(value != null && value.equals("true")){
	// isInLineEditable = true;
	// getTreeOrTable().setEditable(true);
	// }
	// }
	// return isInLineEditable;
	// }

	public int getEditingMode() {
		return editingMode;
	}
	
	public void setEditingMode(int mode) {
		editingMode = mode;
	}

	public FocObject addItem(FocObject fatherObject) {
	  //This focus allows to go around a bug in vaadin
	  //When we used to edit a text field in table, 
	  //the space and arrows were taking action on the table and not on the text field
		if(getTable() != null) getTable().focus();//Do not remove this line
	  //-------
	  
		FocObject newObj = null;
		FocXMLLayout xmlLay = getFocXMLLayout();

		if(xmlLay == null){
			newObj = addItem_Internal(fatherObject);
		}else{
			newObj = xmlLay.table_AddItem(getTableName(), getTreeOrTable(), fatherObject);
		}
		
		if(getTreeOrTable() != null){
			getTreeOrTable().afterAddItem(fatherObject, newObj);
		}
		
		return newObj;
	}

	/**
	 * This method is not to be called directly because it shortcuts any override,
	 * developers should rather call the addItem.
	 * 
	 * @param fatherObject
	 * @return
	 */
	public FocObject addItem_Internal(FocObject fatherObject) {
		getFocXMLLayout().copyGuiToMemory();
		FocObject newObj = getTreeOrTable().getFocList().newEmptyItem();
		getTreeOrTable().getFocDataWrapper().adjustPropertiesForNewItemAccordingTofilter(newObj);
		newObj.code_resetIfApplicableAndCreated();
		if(newObj.getThisFocDesc().isTreeDesc()){
			newObj.setFatherObject(fatherObject);
		}
		fireEvent_Add(fatherObject, newObj);

		ICentralPanel panel = null;
		if(getFocXMLLayout() != null){
			panel = getFocXMLLayout().table_NewCentralPanel_ForForm(getTableName(), getTreeOrTable(), newObj);
		}
		if(panel == null && getEditingMode() == MODE_NOT_EDITABLE){
			panel = XMLViewDictionary.getInstance().newCentralPanel(getParentNavigationWindow(), getXmlViewKey_New(), newObj);
		}

		if(panel != null){
			int viewContainer = getViewContainer_ForNew();
			getTreeOrTable().setSelectedObject(null);
			openFormPanel(panel, viewContainer);
		}else{
			// if(!getTreeOrTable().getFocList().isDirectlyEditable()){
			getTreeOrTable().getFocList().add(newObj);
			// }
		}
		return newObj;
		/*
		 * getMainWindow().changeCentralPanelContent(panel, true);
		 * getTreeOrTable().getFocList().add(newObj); ((AbstractSelect)
		 * getTreeOrTable()).addItem(newObj);
		 */
	}

	public ICentralPanel open(FocObject focObject) {
		ICentralPanel centralPanel = null;
		if(focObject == null){
			Globals.showNotification("OPEN comand requires a row selection", "", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
		}else{
			if(getTreeOrTable() != null) getTreeOrTable().setSelectedObject(focObject);
			focObject.backup();
			fireEvent_Open(focObject);
			if(getFocXMLLayout() == null){
				boolean useADuplicateObject = focObject.isDbResident();
				
				FocObject originalObject = focObject;
				if(useADuplicateObject){
					focObject = originalObject.newObjectReloaded();
				}
//				ICentralPanel panel = (ICentralPanel) XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) getMainWindow(), getXmlViewKey_Open(), focObject);
				centralPanel = (ICentralPanel) XMLViewDictionary.getInstance().newCentralPanel(getParentNavigationWindow(), getXmlViewKey_Open(), focObject);
				if(useADuplicateObject){
					centralPanel.setFocDataOwner(true);
				  FVValidationLayout vLay = centralPanel.getValidationLayout();
				  validationListener_ToReloadOpenedObjects = new ValidationListener_ForOpenedObjectToGetReloadedAfterEdit(vLay, originalObject);
				  vLay.addValidationListener(validationListener_ToReloadOpenedObjects);
				}
			  
				openFormPanel(centralPanel, viewContainer_Open);
			}else{
				centralPanel = getFocXMLLayout().table_OpenItem(getTableName(), getTreeOrTable(), focObject, viewContainer_Open);
			}
		}
		return centralPanel;
	}

	public void delete_NoPopupConfirmation(FocObject fatherObject) {
		if(fatherObject == null){
			Globals.showNotification("No row selected", "Delete command applies on a selected row", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
		}else if(fatherObject.getReference() != null){
			int ref = fatherObject.getReference().getInteger();
			try{
				getTreeOrTable().delete(ref);
//			  getTreeOrTable().getFocList().removeItem(ref);
			}catch (Exception e){
				Globals.logException(e);
			}
			refresh_CallContainerItemSetChangeEvent();
		}
	}

	public void openFormPanel(ICentralPanel panel, int viewContainer) {
		if(panel != null){
			if(panel instanceof FocXMLLayout){
				((FocXMLLayout)panel).setTableTreeThatOpenedThisForm(getTreeOrTable());
			}
			if(viewContainer == ITableTree.VIEW_CONTAINER_POPUP){
				FocXMLLayout.popupInDialog(panel);
			}else if(viewContainer == ITableTree.VIEW_CONTAINER_SAME_WINDOW){
				getParentNavigationWindow().changeCentralPanelContent(panel, true);
//				getMainWindow().changeCentralPanelContent(panel, true);
			}else if(viewContainer == ITableTree.VIEW_CONTAINER_INNER_LAYOUT){
				getWrapperLayout().innerLayout_Replace(panel);
			}else if(viewContainer == ITableTree.VIEW_CONTAINER_NEW_BROWSER_TAB){
				FocWebApplication.getFocWebSession_Static().setInitialContectForm(panel);
			}
		}
	}

	public FocWebVaadinWindow getMainWindow() {
		return (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
	}

	public INavigationWindow getParentNavigationWindow(){
		INavigationWindow parentNavigationWindow = null;
		if(getWrapperLayout() != null){
			parentNavigationWindow = getWrapperLayout().findAncestor(FocCentralPanel.class);
		}
		if(parentNavigationWindow == null) parentNavigationWindow = getMainWindow();
		return parentNavigationWindow;
	}

	public XMLViewKey getXmlViewKey_New() {
		return xmlViewKey_New != null ? xmlViewKey_New : getXmlViewKey_Open();
	}

	public XMLViewKey getXmlViewKey_Open() {
		if(xmlViewKey_Open == null){
			FocDesc focDesc = getFocDesc();
			String storageName = focDesc != null ? focDesc.getStorageName() : null;
			
			INavigationWindow navigation = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
			if(navigation != null){
				XMLViewKey currentKey = ((FocCentralPanel) navigation).getCentralPanel().getXMLView().getXmlViewKey();
				xmlViewKey_Open = new XMLViewKey(storageName, XMLViewKey.TYPE_FORM, currentKey.getContext(), currentKey.getUserView());
			}
		}
		return xmlViewKey_Open;
	}

	public void setXmlViewKey_Open(XMLViewKey xmlViewKey_Open) {
		this.xmlViewKey_Open = xmlViewKey_Open;
	}

	private int viewContainer_ForAttribute(String attribValue) {
		int view = ITableTree.VIEW_CONTAINER_NONE;
		if(attribValue.equals(FXML.VAL_VIEW_CONTAINER__SAME_WINDOW)){
			view = ITableTree.VIEW_CONTAINER_SAME_WINDOW;
		}else if(attribValue.equals(FXML.VAL_VIEW_CONTAINER__POPUP_WINDOW)){
			view = ITableTree.VIEW_CONTAINER_POPUP;
		}else if(attribValue.equals(FXML.VAL_VIEW_CONTAINER__NEW_TAB)){
			view = ITableTree.VIEW_CONTAINER_NEW_BROWSER_TAB;
		}else if(attribValue.equals(FXML.VAL_VIEW_CONTAINER__INNER_LAYOUT)){
			view = ITableTree.VIEW_CONTAINER_INNER_LAYOUT;
			if(wrapperLayout != null){
				wrapperLayout.innerLayout_Create();
			}
		}
		return view;
	}

	private String viewContainer_Int2Attribute(int value) {
		String attrib = null;
		if(value == ITableTree.VIEW_CONTAINER_NONE){
			attrib = FXML.VAL_VIEW_CONTAINER__NONE;
		}else if(value == ITableTree.VIEW_CONTAINER_POPUP){
			attrib = FXML.VAL_VIEW_CONTAINER__POPUP_WINDOW;
		}else if(value == ITableTree.VIEW_CONTAINER_SAME_WINDOW){
			attrib = FXML.VAL_VIEW_CONTAINER__SAME_WINDOW;
		}else if(value == ITableTree.VIEW_CONTAINER_NEW_BROWSER_TAB){
			attrib = FXML.VAL_VIEW_CONTAINER__NEW_TAB;			
		}else if(value == ITableTree.VIEW_CONTAINER_INNER_LAYOUT){
			attrib = FXML.VAL_VIEW_CONTAINER__INNER_LAYOUT;
		}
		return attrib;
	}

//	public static final int VIEW_CONTAINER_NOT_SET      = -1;
//	public static final int VIEW_CONTAINER_SAME_WINDOW  =  0;
//	public static final int VIEW_CONTAINER_NONE         =  1;
//	public static final int VIEW_CONTAINER_POPUP        =  2;
//	public static final int VIEW_CONTAINER_INNER_LAYOUT =  3;

	public void setViewContainer_ForOpen(String attribValue) {
		viewContainer_Open = viewContainer_ForAttribute(attribValue);
	}

	public int getViewContainer_ForOpen() {
		return viewContainer_Open;
	}

	public void setViewContainer_ForNew(String attribValue) {
		viewContainer_New = viewContainer_ForAttribute(attribValue);
	}
	
	public int getViewContainer_ForNew(){
		int viewContainer = viewContainer_New;
		if(viewContainer == ITableTree.VIEW_CONTAINER_NOT_SET){
			viewContainer = viewContainer_Open;
		}
		return viewContainer;
	}

	public void setXmlViewKey_New(XMLViewKey xmlViewKey_New) {
		this.xmlViewKey_New = xmlViewKey_New;
	}

	private void allowRightClick(boolean allowRightClick) {
		if(allowRightClick){
			ITableTree tableOrTree = (ITableTree) getTreeOrTable();
			if(tableOrTree instanceof Table){
				((Table)tableOrTree).setImmediate(true);
				((Table)tableOrTree).addActionHandler(new Action.Handler() {
					@Override
					public void handleAction(Action action, Object sender, Object target) {
						rightClick_HandleAction(action, sender, target);
					}
	
					@Override
					public Action[] getActions(Object target, Object sender) {
						return rightClick_GetActions(target, sender);
					}
				});
			}
		}
	}

	public Action[] rightClick_GetActions(Object target, Object sender) {
		return getPopupMenuFullArray();
	}
	
	/*public void rightClick_HandleAction(Action action, Object sender, Object target) {
		if(target != null && getTreeOrTable() != null && getTreeOrTable().getFocList() != null && action instanceof FVTablePopupMenu){
			FocObject focObject = getTreeOrTable().getFocList().searchByReference((Integer) target);
			((FVTablePopupMenu) action).actionPerformed(focObject);
		}
	}*/

	public void rightClick_HandleAction(Action action, Object sender, Object target) {
		if(action instanceof FVTablePopupMenu){
			FVTablePopupMenu fvTablePopupMenu = (FVTablePopupMenu) action;
			if(target != null && getTreeOrTable() != null && getTreeOrTable().getFocList() != null && action instanceof FVTablePopupMenu){
				FocObject focObject = getTreeOrTable().getFocList().searchByReference((Integer) target);
				((FVTablePopupMenu) action).actionPerformed(focObject);
			}else if(target == null && fvTablePopupMenu.getActionId() == ACTION_ADD){
				fvTablePopupMenu.actionPerformed(getSelectedObject());
			}
		}
	}

  public void refresh_ContainerItemSetChange(){
  	Table table = getTable();
  	if(table != null){
			if(table.getContainerDataSource() instanceof ContainerOrderedWrapper){
				ContainerOrderedWrapper container = (ContainerOrderedWrapper) table.getContainerDataSource();
				container.updateOrderWrapper();
			}
			addContainerItemSetChange(table);
  	}
  }

	public void refresh_CallContainerItemSetChangeEvent() {
		if(getTreeOrTable() != null && getTreeOrTable() instanceof Table){
			Table treeOrTable = (Table) getTreeOrTable();
//			treeOrTable.containerItemSetChange(null);vaadin7.3.7
			addContainerItemSetChange(treeOrTable);
			getTreeOrTable().refreshRowCache_Foc();
		}else if(getTreeOrTable() instanceof Grid){
			if(getTreeOrTable() != null && getTreeOrTable().getFocDataWrapper() != null){
				getTreeOrTable().getFocDataWrapper().refreshGuiForContainerChanges();
			}
		}
		
		
	}
	
	private void addContainerItemSetChange(Table table){
		if(table != null){
			table.containerItemSetChange(new ItemSetChangeEvent() {
				
				@Override
				public Container getContainer() {
					return getTable() != null ? getTable().getContainerDataSource() : null;
				}
			});
		}
	}

	public FocObject selectByFocProperty(String fieldName, String fieldValue) {
		return selectByFocProperty(fieldName, fieldValue, 0);
	}
	
	public FocObject selectByFocProperty(String fieldName, String fieldValue, int ancestorRef) {//ancestorRef=0 for no ancestor
		FocObject obj = null;
		if(getTreeOrTable() != null){
			FocDataWrapper dataWrapper = getTreeOrTable() == null ? null : getTreeOrTable().getFocDataWrapper();

			if(dataWrapper != null){
				FocList list = getTreeOrTable().getFocList();

				Collection itemIds = dataWrapper.getItemIds();
				Iterator   iter    = itemIds.iterator();

				if(iter != null){
					while(iter.hasNext() && obj == null){
						FocObject currObj = list.searchByReference((Integer) iter.next());
						
						if(currObj != null){
							Object dataByPathObject = currObj.iFocData_getDataByPath(fieldName);
							if(dataByPathObject instanceof FProperty){
								FProperty prop = (FProperty) dataByPathObject;
								if(prop != null && prop.getString().trim().equals(fieldValue)){
									
									if(getTable() instanceof TreeTable && ancestorRef != 0){
										TreeTable treeTable = (TreeTable) getTable();										
										if (currObj != null) {
					            Object objectID = currObj.getReference().getInteger();
					            while (objectID != null && obj == null) {
					            	if(ancestorRef == (Integer) objectID ){
					            		obj = currObj;
					            	}
					              objectID = treeTable.getParent(objectID);   
					            }
										}
								  }else{
								  	obj = currObj;	
								  }
								}
							}
						}
					}
				}
			}

			// obj = list != null ? list.searchByPropertyStringValue(fieldName,
			// fieldValue) : null;
			if(obj != null && obj.getReference() != null){
				if(getTreeOrTable() instanceof Table){
					((Table) getTreeOrTable()).select(obj.getReference().getInteger());
				}else if(getTreeOrTable() instanceof FVTableGrid){
					FVTableGrid tableGrid = (FVTableGrid) getTreeOrTable();
	    		tableGrid.select(obj.getReference().getInteger());
				}
			}
		}
		return obj;
	}

	public String getTableName() {
		if(tableName == null){
			tableName = getAttributes() != null ? getAttributes().getValue(FXML.ATT_NAME) : "";
			if(tableName == null){
				tableName = ASCII.generateRandomString(10);
			}
		}
		return tableName;
	}

	public boolean isSelectedCheckBoxForItem(FocObject focObject) {
		boolean selected = false;
		if(focObject != null && focObject.getReference() != null){
			selected = isSelectedCheckBoxForItem(focObject.getReference().getInteger(), focObject);
		}
		return selected;
	}

	public boolean isSelectedCheckBoxForItem(int ref, FocObject focObject) {
		boolean selected = false;

		if(getFocXMLLayout() != null){
			String compName = TableTreeDelegate.newComponentName(getTableName(), String.valueOf(ref), COL_ID_SELECT);
			FVCheckBox box = (FVCheckBox) getFocXMLLayout().getComponentByName(compName);
			if(box != null)
				selected = (Boolean) box.getValue();
		}

		return selected;
	}

	public void setSelectedCheckBoxForItem(FocObject focObject, boolean selected) {
		if(focObject != null && focObject.getReference() != null){
			setSelectedCheckBoxForItem(focObject.getReference().getInteger(), selected);
		}
	}

	public void setSelectedCheckBoxForItem(int ref, boolean selected) {
		if(getFocXMLLayout() != null){
			String compName = TableTreeDelegate.newComponentName(getTableName(), String.valueOf(ref), COL_ID_SELECT);
			FVCheckBox box = (FVCheckBox) getFocXMLLayout().getComponentByName(compName);
			if(box != null)
				box.setValue(selected);
		}
	}

	@SuppressWarnings("serial")
	public class FocValueChangeListener implements ValueChangeListener {

		private Object itemId = null;

		public FocValueChangeListener(Object itemId) {
			this.itemId = itemId;

		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean isChecked = ((Boolean) event.getProperty().getValue()).booleanValue();

			if(isChecked){
				selectionColumn_getSelectedIdArrayList().add(getItemId());
			}else if(!isChecked){
				int idIndex = selectionColumn_getSelectedIdArrayList().indexOf(getItemId());
				if(idIndex >= 0){
					selectionColumn_getSelectedIdArrayList().remove(idIndex);
				}
			}
		}

		public Object getItemId() {
			return itemId;
		}
	}
	
	public void computeFooter(){
		boolean footerVisible = false;
		ArrayList<FVTableColumn> colArray = getVisiblePropertiesArrayList();
		for(int i=0; i<colArray.size(); i++){
			FVTableColumn col = colArray.get(i);
			String footerFormula = col != null ? col.getFooterFormula() : null;
			if(!Utils.isStringEmpty(footerFormula)){
				footerVisible = true;
				getTreeOrTable().computeFooter(col);
			}
		}
		if(getTreeOrTable() != null){
			((Table)getTreeOrTable()).setFooterVisible(footerVisible);
		}
	}

  public FVTableWrapperLayout getWrapperLayout() {
    return wrapperLayout;
  }

  public void setWrapperLayout(FVTableWrapperLayout wrapperLayout) {
    this.wrapperLayout = wrapperLayout;
  }

  public String formatPropertyValue(Object rowId, Object colId, Property property) {
  	String str = null;
  	
  	if (property.getType() == java.util.Date.class || property.getType() == java.sql.Date.class) {
  		try{
  			str = ((FProperty)property).getString();
  		}catch(Exception e){
  			Globals.logException(e);
  		}
  	}else if (property.getType() == Double.class) {
      /*
      DecimalFormat df = new DecimalFormat();
      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      dfs.setGroupingSeparator(',');
      df.setDecimalFormatSymbols(dfs);
      
      return df.format((Double)property.getValue());
      */
    }
    return str;
  }

	public FocFormula_Form getFormulaForm() {
		return formulaForm;
	}

	public void newFormulaForm(ITableTree breakdownTable, CompositeKeyPropertyFormulaEnvironment formulaEnvironment){
	  XMLViewKey formulaKey = new XMLViewKey(BusinessEssentialsWebModule.STORAGE_FORMULA, XMLViewKey.TYPE_FORM); 
	  FocFormula_Form formulaLayout = (FocFormula_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(getParentNavigationWindow(), formulaKey, null);
	  setFormulaForm(formulaLayout);
	  if(getTreeOrTable() instanceof FVTreeGrid){
	  	FVTreeGrid treeGrid = (FVTreeGrid) getTreeOrTable();
	  	treeGrid.addItemClickListenerForFormula();
	  }
	  formulaLayout.parseXMLAndBuildGui();
	  formulaLayout.initialize(breakdownTable, formulaEnvironment);
	  
	  FVTableWrapperLayout bkdnTreeWrapper = getWrapperLayout();
	  formulaLayout.setParentLayout(getFocXMLLayout());
	  bkdnTreeWrapper.addHeaderComponent_ToLeft(formulaLayout);
	}
	
	@SuppressWarnings("serial")
	private void setFormulaForm(FocFormula_Form formulaForm) {
		this.formulaForm = formulaForm;
		if(formulaForm != null && getTable() != null){
			getTable().addItemClickListener(new ItemClickListener() {
				
				@Override
				public void itemClick(ItemClickEvent event) {
					lastClickedFocDataByPath = null;
					if(getFormulaForm() != null){
						try{
							FProperty property              = getSelectCellProperty(event);
							Object    itemId                = event.getItemId();
							Object    lastClickedPropertyID = event.getPropertyId();
							getFormulaForm().triggerFormulaChanges(property, itemId, lastClickedPropertyID);
							
							/*Object itemId = event.getItemId();
							Object lastClickedPropertyID = event.getPropertyId();
							
							FVTableColumn col = findColumn((String) lastClickedPropertyID);
							if(col != null){
								FocList list = getTreeOrTable().getFocList();
								
								if(list != null){
									FocObject obj = list.searchByReference((Integer) itemId);
									
									if(obj != null){
										IFocData focData = obj.iFocData_getDataByPath(col.getDataPath());
										if(focData != null && focData instanceof FProperty){
											FProperty prop = (FProperty) focData; 
											getFormulaForm().triggerFormulaChanges(prop, itemId, lastClickedPropertyID);
										}
									}
									
								}
							}*/
						}catch(Exception e){
							Globals.logException(e);
						}
					}
				}
			});
		}
	}
	
	public FProperty getSelectCellProperty(ItemClickEvent event){
		FProperty property = null;
		Object itemId = event.getItemId();
		Object lastClickedPropertyID = event.getPropertyId();
		
		FVTableColumn col = findColumn((String) lastClickedPropertyID);
		if(col != null){
			FocList list = getTreeOrTable().getFocList();
			
			if(list != null){
				FocObject obj = list.searchByReference((Integer) itemId);
				
				if(obj != null){
					IFocData focData = obj.iFocData_getDataByPath(col.getDataPath());
					if(focData != null && focData instanceof FProperty){
						property = (FProperty) focData; 
					}
				}
			}
		}
		return property;
	}

	/*
	private FocusListener getFocusListener() {
		if(focusListener == null && getFormulaForm() != null){
			focusListener = new FocusListener() {
				
				@Override
				public void focus(FocusEvent event) {
					if(event.getComponent() instanceof FocXMLGuiComponent){
						FocXMLGuiComponent component = (FocXMLGuiComponent) event.getComponent();
						adjustFormulaLayoutForComponent(component);
					}
				}
			};
		}
		return focusListener;
	}
	*/

	public String unitTesting_ApplyTheFormulaInTheTextField(){
		String error = null;
		if(getFormulaForm() == null){
			error = "Formula form is null";
		}else{
			FocFormula_Form formulaForm = getFormulaForm();
			formulaForm.applyTextFieldFormulaInTheProperty();
		}
		return error;
	}
	
	public void adjustFormulaLayoutForComponent(FocXMLGuiComponent component){
		if(component != null){
			
			if(getTable() != null && (getTable().getValue() == null || (component.getDelegate().getRowId() != null && getTable().getValue() != component.getDelegate().getRowId()))){
				getTable().select(component.getDelegate().getRowId());
			}
			
			if(getFormulaForm() != null){
				FProperty property = (FProperty) component.getFocData();
				getFormulaForm().triggerFormulaChanges(property, component.getDelegate().getRowId(), component.getDelegate().getColumnId());
			}
		}
	}

//	public void setFocusListener(FocusListener focusListener) {
//		this.focusListener = focusListener;
//	}
	
	public void adjustColumnWidthForIcons(){
		FVTableColumn tableColumn = getVisibleColumnByIndex(0);
		
		if(tableColumn != null){
			int width = tableColumn.getWidth();
			String name = tableColumn.getName();
			if(width != -1 && !Utils.isStringEmpty(name)){
				getTable().setColumnWidth(name, width+70);
			}
		}
	}
	
	public boolean isReplaceActive(){
		boolean replacing = false;
		FVTableWrapperLayout wl = getWrapperLayout();
		if(wl != null){
			replacing = wl.isReplaceActive();
		}
		return replacing;
	}

	public boolean isRefreshRowCacheEnabled() {
		return refreshRowCacheEnabled;
	}

	public boolean setRefreshRowCacheEnabled(boolean refreshRowCacheEnabled) {
		boolean previousValue = this.refreshRowCacheEnabled; 
		this.refreshRowCacheEnabled = refreshRowCacheEnabled;
		if(refreshRowCacheEnabled && isRefreshRowCacheNeeded()){
			getTable().refreshRowCache();
			setRefreshRowCacheNeeded(false);
		}
		return previousValue;
	}

	public boolean isRefreshRowCacheNeeded() {
		return refreshRowCacheNeeded;
	}

	public void setRefreshRowCacheNeeded(boolean refreshRowCacheNeeded) {
		this.refreshRowCacheNeeded = refreshRowCacheNeeded;
	}
	
	public FocXMLGuiComponent findGuiComponent(FocObject focObject, String columnName){
		FocXMLGuiComponent comp = null;
		if(focObject != null && columnName != null && getFocXMLLayout() != null){
			String objRef = focObject.getReference().toString();
			
			String compName = newComponentName(getTableName(), objRef, columnName);
			comp = (FocXMLGuiComponent) getFocXMLLayout().getComponentByName(compName);
		}
		return comp;
	}
	
	public void debug_ListOfColumns() {//ancestorRef=0 for no ancestor
		FocObject obj = null;
		if(getTreeOrTable() != null){
			FocDataWrapper dataWrapper = getTreeOrTable() == null ? null : getTreeOrTable().getFocDataWrapper();

			if(dataWrapper != null){
				ArrayList<FVTableColumn> array = getVisiblePropertiesArrayList();
				if(array != null){
					Globals.logString("Column list : ");
					for(int i=0; i<array.size(); i++){
						FVTableColumn col = array.get(i);
						Globals.logString("Column "+i+" NAME="+col.getName()+" DATAPATH="+col.getDataPath());
					}
				}
			}
		}
	}
	
	public void fillButtonsAndPopupMenus(){
		if(attributes != null){
			
			if(isOpenEnabled()){
				addPopupMenu_Open();
			}else if(isDoubleClickEnabled()){
				//If the open is enabled the double click listener is managed in the open otherwise double click
				doubleClickListener = new ItemClickListener() {
					@Override
					public void itemClick(ItemClickEvent event) {
						if(event.isDoubleClick()){
							if(getFocXMLLayout() != null){
								//By default FocXMLLayout will also call the defaultItemDoubleClickAction(event); 
								getFocXMLLayout().table_ItemDoubleClick(getTableName(), getTreeOrTable(), event);
							}
						}
					}
				};
				
				getTreeOrTable().addItemClickListener(doubleClickListener);
			}

			if(isAddEnabled()){
				addPopupMenu_Add();
			}

			if(isDeleteEnabled()){
				addPopupMenu_Delete();
			}
			
			if(isDuplicateEnabled()){
				addPopupMenu_Duplicate();
			}
			
			if(isExcelExportEnabled()){
				if(ConfigInfo.isAllowCSVExport()){
					addPopupMenu_ExportAsCSV();
				}
				if(ConfigInfo.isAllowEXCELExport()){
					addPopupMenu_ExporAsExcel();
				}
			}
			
			if(isLeafNodeEnabled()){
				addPopupMenu_OpenTreeNode();
			}
			
			if(isRedirectEnabled()){
				addPopUpMenu_Redirect();
			}
			
//			if(getEditingMode() == MODE_EDITABLE){
//				addPopupMenu_CopyAllRows();
//			}
		}
	}

	public boolean isEditable() {
		return ((FocXMLGuiComponent)getTreeOrTable()).getDelegate().isEditable();
	}
	
	public void refreshEditable() {
		if(getWrapperLayout() != null){
			getWrapperLayout().adjustTableEditingToolsVisiblity();
		}
	}
	
	public String getNodeTitleDisplayStringForObjectRef(Object itemId){
		FTree tree = ((FVTreeTable)getTreeOrTable()).getFTree();
    FNode node = tree != null ? tree.vaadin_FindNode(itemId) : null;
    return getNodeTitleDisplayStringForNode(node);
	}

	public String getNodeTitleDisplayStringForObject(FocObject focObject){
		FTree tree = ((FVTreeTable)getTreeOrTable()).getFTree();
    FNode node = tree != null ? tree.findNodeFromFocObject(focObject) : null;
    return getNodeTitleDisplayStringForNode(node);
	}

	public String getNodeTitleDisplayStringForNode(FNode node){
		String propertyString = null;
		if(node != null){
			FTree tree = ((FVTreeTable)getTreeOrTable()).getFTree();
	    FProperty property = tree != null && node != null ? tree.getTreeSpecialProperty(node) : null;
			if(property != null){
				propertyString = (String) property.vaadin_TableDisplayObject(null, null);
			}else{
				propertyString = node.getDisplayTitle();
			}
		}
		return propertyString;		
	}

	public void selectionColumn_clearSelectedIdArrayList() {
		if(selectedRowsId != null){
			selectedRowsId.clear();
			selectionColumn_copyMemoryToGui();
		}
	}
		
	public ArrayList<Object> selectionColumn_getSelectedIdArrayList() {
		if(selectedRowsId == null && findColumn(COL_ID_SELECT) != null){
			selectedRowsId = new ArrayList<Object>();
		}
		return selectedRowsId;
	}

	public void selectionColumn_setSelectionMemory(int ref, boolean selected){
		if(selected){
			ArrayList<Object> arrayList = selectionColumn_getSelectedIdArrayList();
			if(arrayList != null && !arrayList.contains(ref)){
				arrayList.add(ref);
			}
		}else{
			if(selectedRowsId != null){
				selectedRowsId.remove((Integer)ref);
			}
		}
	}

	public void selectionColumn_setSelectionGUI(int ref, boolean selected){
		String compName = TableTreeDelegate.newComponentName(getTableName(), String.valueOf(ref), COL_ID_SELECT);
		FVCheckBox box = (FVCheckBox) getFocXMLLayout().getComponentByName(compName);
		if(box != null){
			box.setValue(selected);
		}
	}
	
	public void selectionColumn_copyMemoryToGui(){
		ArrayList<Object> arrayList = selectedRowsId;
		if(arrayList != null){
			if(getTreeOrTable() != null && getTreeOrTable().getFocDataWrapper() != null){
				FocDataWrapper wrapper = getTreeOrTable().getFocDataWrapper();
				for(int i=0; i<wrapper.size(); i++){
					FocObject obj = wrapper.getAt(i);
					int ref = obj.getReference().getInteger();
					selectionColumn_setSelectionGUI(ref, arrayList.contains(ref));
				}
			}
		}
	}
	
	public void selectionColumn_selectAll(boolean select){
		FVCheckBox fvCheckBox = (FVCheckBox) getFocXMLLayout().getComponentByName(FVColGen_Select.SELECT_UNSELECT_ALL_CHECK_BOX_NAME);
		if(fvCheckBox != null){
			fvCheckBox.setValue(select);
		}
	}
	
	public boolean selectionColumn_isSelectAll(){
		boolean isSelectedAll = false;
		FVCheckBox fvCheckBox = (FVCheckBox) getFocXMLLayout().getComponentByName(FVColGen_Select.SELECT_UNSELECT_ALL_CHECK_BOX_NAME);
		if(fvCheckBox != null){
			isSelectedAll = fvCheckBox.getValue();
		}
		return isSelectedAll;
	}
	
	public void selectionColumn_selectAll_Internal(boolean select){
		if(getTreeOrTable() != null && getTreeOrTable().getFocDataWrapper() != null){
			FocDataWrapper wrapper = getTreeOrTable().getFocDataWrapper();
			for(int i=0; i<wrapper.size(); i++){
				FocObject obj = wrapper.getAt(i);
				int objRef = obj.getReference().getInteger();
				selectionColumn_setSelectionMemory(objRef, select);			
			}
		}
		selectionColumn_copyMemoryToGui();
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(int selectionMode) {
		boolean modeChanged = this.selectionMode != selectionMode;  
		this.selectionMode = selectionMode;
		if(modeChanged){
			boolean isColumnIdSelectAdded = newVisibleColumnIds() != null ? newVisibleColumnIds().contains(FVTableColumn.COL_ID_SELECT) : false;
			if(selectionMode != SELECTION_MODE_NONE){
				if(!isColumnIdSelectAdded){
					FocXMLAttributes focXMLAttributes = new FocXMLAttributes();
			  	focXMLAttributes.addAttribute(FXML.ATT_NAME, FVTableColumn.COL_ID_SELECT);
			  	focXMLAttributes.addAttribute(FXML.ATT_CAPTION, "");//Select
			  	focXMLAttributes.addAttribute(FXML.ATT_WIDTH, "40px");
			  	addColumnAtFirst(focXMLAttributes);
			  	getTreeOrTable().applyFocListAsContainer();
				}
			}else{
        				if(isColumnIdSelectAdded){
					newVisibleColumnIds().remove(FVTableColumn.COL_ID_SELECT);
				}
				getTreeOrTable().applyFocListAsContainer();
			}
		}
	}
	
	public class ValidationListener_ForOpenedObjectToGetReloadedAfterEdit implements IValidationListener {
		private FocObject          originalObject   = null;
		private FVValidationLayout validationLayout = null;
		
		public ValidationListener_ForOpenedObjectToGetReloadedAfterEdit(FVValidationLayout validationLayout, FocObject originalObject){
			this.originalObject   = originalObject;
			this.validationLayout = validationLayout;
		}
		
		public void dispose(){
			if(validationLayout != null){
				validationLayout.removeValidationListener(this);
			}
			originalObject = null;
			validationLayout = null;
		}
		
		@Override
		public void validationDiscard(FVValidationLayout validationLayout) {
			
		}

		@Override
		public boolean validationCheckData(FVValidationLayout validationLayout) {
			return true;
		}

		@Override
		public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
			if(commited && originalObject != null && originalObject.isDbResident()){
				originalObject.load();
//				if(getTable() != null){
//					getTable().refreshRowCache();
//				}
			}
		}

		@Override
		public boolean validationCommit(FVValidationLayout validationLayout) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	public void adjustFocDataWrappers_FilterAndSroting() {
		ITableTree tableTree = getTreeOrTable();
    if(tableTree != null && tableTree.getFocDataWrapper() != null){
    	tableTree.getFocDataWrapper().setTableTreeDelegate(this);
      FocXMLAttributes attr = (FocXMLAttributes) getAttributes();
      String expression = attr != null ? attr.getValue(FXML.ATT_FILTER_EXPRESSION) : null;
      if(!Utils.isStringEmpty(expression)){
      	tableTree.getFocDataWrapper().addFilterByExpression(expression);
      }
      
      /*
      final String removeZeros = attr != null ? attr.getValue(FXML.ATT_REMOVE_ZEROS) : null;
      if(removeZeros != null){
      	Filter filter = new Filter() {
					
					@Override
					public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
						boolean visible = true;      
			      FocObject focObj = (FocObject) item;
			      if(removeZeros != null && focObj != null){
			      	IFocData focData = focObj.iFocData_getDataByPath(removeZeros);
			      	if(focData != null){
				      	if(focData instanceof FDouble){
				      		FProperty prop = (FProperty) focData;
				      		if(prop.getDouble() == 0){
				            visible = false;
				      		}
				      	}else if(focData instanceof FProperty){
				      		FProperty prop = (FProperty) focData;
				      		if(prop.getValue() != null && prop.getValue().toString().equals("0")){
				            visible = false;
				      		}
				      	}
			      	}
			      }
			      return visible;
					}
					
					@Override
					public boolean appliesToProperty(Object propertyId) {
						return false;
					}
				};
				tableTree.getFocDataWrapper().addContainerFilter(filter);
				tableTree.getFocDataWrapper().putFilter(removeZeros, filter);
      }
      */
      
      String  sortingExpression = attr != null ? attr.getValue(FXML.ATT_SORTING_EXPRESSION) : null;
      tableTree.getFocDataWrapper().setSortingExpression(sortingExpression);        

      /*
      if(sortingExpression != null){
        listOrder = new FocListOrderFocObject();
        
        if(sortingExpression.startsWith("-")){
        	sortingExpression = sortingExpression.substring(1);
        	listOrder.setReverted(true);
        }
        
        StringTokenizer stringTokenizer = new StringTokenizer(sortingExpression, ",");
        while(stringTokenizer.hasMoreTokens()){
          String sortingName = stringTokenizer.nextToken();
          
          if(getFocList() != null){
      	    PropertyAndFieldPath propertyAndFieldPath = FAttributeLocationProperty.newFieldPath_PropertyAndField(false, sortingName, getFocList().getFocDesc(), null, false);
      	    FFieldPath           fieldPath            = propertyAndFieldPath.getFieldPath();
//            FFieldPath fieldPath = FFieldPath.newFieldPath(getFocList().getFocDesc(), sortingName);
            if(fieldPath != null){
              listOrder.addField(fieldPath);
            }else{
              Globals.showNotification("Could not resolve sorting expression: ", sortingName, IFocEnvironment.TYPE_WARNING_MESSAGE);
            }
          }
        }
      } 
      */       
    }
    
//    FocListener focListener = new FocListener() {
//      public void focActionPerformed(FocEvent evt) {
//	      if(evt.getID() == FocEvent.ID_BEFORE_REFERENCE_SET && getFocXMLLayout() != null){
//        	if(evt.getEventSubject() instanceof FocObject){
//        		FocObject focObj = (FocObject) evt.getEventSubject();
//        		int ref = focObj.getReference().getInteger();
//        		String tableName = TableTreeDelegate.this.getTableName();
//        		String prefix = TableTreeDelegate.newComponentNamePrefix(tableName, String.valueOf(ref));
//        		getFocXMLLayout().removeComponentByName_StartWith(prefix);	
//        	}
//	      }
////DANGER
////2014-03-03		        
////This is called when the Set of properties supported by the container has changed.		        
////	        firePropertySetChangeEvent();
//      }
//
//			@Override
//			public void dispose() {
//			}
//  	};
  	
  	
  }
	
	@Override
	public void listListenerCall_BeforeObjectReferenceSet(FocObject focObj){
		if(focObj != null && getFocXMLLayout() != null){
			int ref = focObj.getReferenceInt();
			String tableName = getTableName();
			String prefix = TableTreeDelegate.newComponentNamePrefix(tableName, String.valueOf(ref));
			getFocXMLLayout().removeComponentByName_StartWith(prefix);
		}
	}
	
	public void addCellTooltipText(Object itemId, Object propertyId, String ttt){
		if(itemId != null && propertyId != null && ttt != null){
			if(tableToolTipGenerator == null && getTable() != null){
				tableToolTipGenerator = new FTableToolTipGenerator();
				getTable().setItemDescriptionGenerator(tableToolTipGenerator);
			}
			if(tableToolTipGenerator != null){
				tableToolTipGenerator.addTooltip(itemId, propertyId, ttt);
			}
		}
	}
}