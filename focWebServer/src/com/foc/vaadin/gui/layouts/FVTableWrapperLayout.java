package com.foc.vaadin.gui.layouts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.vaadin.data.Container.Filter;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.business.department.Department;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.IStatusHolderDesc;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.dataModelTree.DataModelNodeTree;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVPanel;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.treeGrid.FVTreeGrid;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVViewSelector_MenuBar;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.modules.workflow.TransactionFilter_Form;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import me.everpro.event.EverproItemSetChangeEvent;

@SuppressWarnings("serial")
public class FVTableWrapperLayout extends FVVerticalLayout implements FocXMLGuiComponent {

	private FVButton deleteButton            = null;
  private FVButton addButton               = null;
  private FVButton openButton              = null;
  private FVButton duplicateButton         = null;
  private FVButton statusStyleButton       = null;
  private FVButton reloadButton            = null;
  private FVButton transactionFilterButton = null;
  
  private Embedded valo_DeleteEmbedded     = null;
  private Embedded valo_AddEmbedded        = null;
  private Embedded valo_OpenEmbedded       = null;
  private Embedded valo_DuplicateEmbedded  = null;
  private Embedded valo_StatusStyleEmbedded= null;
  private Embedded valo_ReloadEmbedded     = null;
  private Embedded valo_TransactionFilterEmbedded = null;
//  private FVButton zoomButton              = null;
  
  private FVCheckBox replaceCheckBox  = null;
  private ITableTree tableOrTree      = null;
  private XMLView    xmlView_showForm = null;
  private FVPanel    showFormPanel    = null;
  private Panel      tablePanel       = null;
  private FVLabel    titleLabel       = null;

  private Filter filter = null;
  private String filterString = null;
  private FVTextField filterTextField = null;
  private boolean           filterOperation_AND = false;
  private ArrayList<String> multipleExpressions = null;
  private FVHorizontalLayout headerRootLayout   = null;
  private FVHorizontalLayout headerLeftLayout   = null;
  private FVHorizontalLayout headerRightLayout  = null; 

  private FVViewSelector_MenuBar viewSelector       = null; 
  		
  private boolean isRefreshEnabled = true;
  
	private ArrayList<Integer> excludedDepartmentArray = null;
	private ArrayList<Integer> excludedStatusArray     = null;
  
	private String codeFrom = null;
	private String codeTo   = null;
	
  public FVTableWrapperLayout() {
    super(null);
    setSpacing(false);
    setMargin(false);
    setCaption(null);
    setHeight("100%");
  }

  public void dispose() {
  	if(filter != null){
	  	if(getTableOrTree() != null){
		    FocDataWrapper dataWrapper = getTableOrTree().getFocDataWrapper();
		    if(dataWrapper != null){
		      dataWrapper.removeContainerFilter_ForDispose(filter);
		    }
	  	}
	  	filter = null;
  	}
    replaceCheckBox = null;
    if(filterTextField != null){
    	filterTextField.dispose();
    	filterTextField = null;
    }
    viewSelector    = null;
    
    if(tableOrTree != null){
    	((FocXMLGuiComponent)tableOrTree).dispose();
    	tableOrTree = null;
    }
    
    xmlView_showForm = null;
    showFormPanel = null;
    headerRootLayout = null;
    if(tablePanel != null){
    	tablePanel.setContent(null);
    	tablePanel = null;
    }
    
    if(excludedStatusArray != null){
    	excludedStatusArray.clear();
    	excludedStatusArray = null;
    }
    if(excludedDepartmentArray != null){
    	excludedDepartmentArray.clear();
    	excludedDepartmentArray = null;
    }
    super.dispose();
  }
  
  public boolean isPrintingUI(){
  	if(UI.getCurrent() instanceof FocWebApplication){
	  	FocWebApplication focWebApp = (FocWebApplication) UI.getCurrent();
	  	return focWebApp != null ? focWebApp.isPrintUI() : false;
  	}
  	return false;
  }  
  @Override
  public UI getUI() {
  	// TODO Auto-generated method stub
  	return super.getUI();
  }

  private void createHeaderLayoutsIfNeeded() {
    if (headerRootLayout == null) {
      headerRootLayout = new FVHorizontalLayout(null);
      headerRootLayout.setWidth("100%");
//      headerRootLayout.addStyleName("foc-blue");
//      headerRootLayout.addStyleName("noPrint");
//      headerRootLayout.setCaption(null);
      addComponentAsFirst(headerRootLayout);

      headerLeftLayout = new FVHorizontalLayout(null);
      headerRootLayout.addComponent(headerLeftLayout);
      headerRootLayout.setComponentAlignment(headerLeftLayout, Alignment.BOTTOM_LEFT);
      headerLeftLayout.setCaption(null);
      headerRootLayout.setExpandRatio(headerLeftLayout, 1);
//      headerLeftLayout.addStyleName("noPrint");

      headerRightLayout = new FVHorizontalLayout(null);
      headerRightLayout.setCaption(null);
      headerRootLayout.addComponent(headerRightLayout);
      headerRootLayout.setComponentAlignment(headerRightLayout, Alignment.BOTTOM_RIGHT);
      headerRightLayout.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
    }
  }
  
  public FocXMLLayout getFocXMLLayout(){
  	return getTableTreeDelegate() != null ? getTableTreeDelegate().getFocXMLLayout() : null;
  }
  
  private void addComponentToFocXMLLayoutMap(Component component){
		if(getFocXMLLayout() != null && component != null && component instanceof FocXMLGuiComponent){
			FocXMLGuiComponent focXMLGuiComponent = (FocXMLGuiComponent) component;
			FocXMLAttributes focXMLAttributes = (FocXMLAttributes) focXMLGuiComponent.getAttributes();
			if(focXMLAttributes != null){
				String componentName = focXMLAttributes.getValue(FXML.ATT_NAME);
				if(componentName != null && !componentName.isEmpty()){
					getFocXMLLayout().putComponent(componentName, component);
				}
			}
		}
  }
  
  private FVHorizontalLayout getHeaderRightLayout() {
  	createHeaderLayoutsIfNeeded();
  	return headerRightLayout;
  }
  
  private FVHorizontalLayout getHeaderLeftLayout() {
  	createHeaderLayoutsIfNeeded();
  	return headerLeftLayout;
  }
  
  private FVHorizontalLayout getHorizontalLayout() {
  	createHeaderLayoutsIfNeeded();
    return headerRootLayout;
  }
  
  public void addHeaderComponent_AsFirst(Component comp){
  	if(getHeaderRightLayout() != null){
  		getHeaderRightLayout().addComponentAsFirst(comp);
  		getHeaderRightLayout().setComponentAlignment(comp, Alignment.BOTTOM_RIGHT);
  		addComponentToFocXMLLayoutMap(comp);
  	}
  }
  
  public void addHeaderComponent_BeforeViewSelector(Component comp){
  	try{
	  	if(getHeaderRightLayout() != null){
	  		if(viewSelector != null){
	  			int viewSelectorIndex = getHeaderRightLayout().getComponentIndex(viewSelector);
	  			getHeaderRightLayout().addComponent(comp, viewSelectorIndex);
	  			getHeaderRightLayout().setComponentAlignment(comp, Alignment.BOTTOM_RIGHT);
	  		}else{
	  			addHeaderComponent(comp);
	  		}
	  	}
  	}catch(Exception ex){
  		addHeaderComponent(comp);
  	}
  }
  
  public void addHeaderComponent(Component comp){
  	if(getHeaderRightLayout() != null){
			getHeaderRightLayout().addComponent(comp);
  		getHeaderRightLayout().setComponentAlignment(comp, Alignment.BOTTOM_RIGHT);
  		addComponentToFocXMLLayoutMap(comp);
  	}
  }
  
  public void addHeaderComponent_ToLeft(Component comp){
  	if(getHeaderLeftLayout() != null){
  		getHeaderLeftLayout().addComponent(comp);
  		getHeaderLeftLayout().setComponentAlignment(comp, Alignment.BOTTOM_LEFT);
  		addComponentToFocXMLLayoutMap(comp);
  	}
  }
  
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
  }

  @Override
  public void setDescription(String description) {
    super.setDescription(description);
    if (filterTextField != null) {
      filterTextField.setDescription(description);
    }
  }

  public void setXmlView_ShowForm(XMLView showFormXmlView) {
    this.xmlView_showForm = showFormXmlView;
  }

  public XMLView getXmlView_ShowForm() {
    return xmlView_showForm;
  }
  
//  private FVButton getExportToExcelButton(){
//  	if(exportToExcelButton == null && getTableTreeDelegate().isExcelExportEnabled()){
//  		exportToExcelButton = new FVButton("");
//  		
//  		exportToExcelButton.setStyleName(BaseTheme.BUTTON_LINK);
//  		exportToExcelButton.setDescription("Export To Excel");
//  		exportToExcelButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_EXPORT_TO_EXCEL));
//  		
//  		exportToExcelButton.addClickListener(new ClickListener() {
//  			
//				@Override
//				public void buttonClick(ClickEvent event) {
//					Table table = getTableTreeDelegate() != null ? getTableTreeDelegate().getTable() : null;
//					if(table != null){
//						TableToExcelExport excelExport = new TableToExcelExport((FVTable) table);
//						excelExport.createCSVFile();
////						ExcelExport excelExport = new ExcelExport(table);
////	          excelExport.excludeCollapsedColumns();
////	          excelExport.export();
//	          
//					}
//				}
//			});
//  	}
//  	return exportToExcelButton;
//  }
  
  private void duplicateClickListenerContent(){
  	TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(tableTreeDelegate != null){
			FocObject sourceObj = tableTreeDelegate.getSelectedObject();
			if(sourceObj != null){
				tableTreeDelegate.duplicate(sourceObj);
			}else{
				Globals.showNotification("Select an Item", "to duplicate", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		}
  }
  
  private void reloadClickListener(){
  	FocList focList = getTableOrTree().getFocList();
		if(focList != null && !focList.isCollectionBehaviour()){
			focList.reloadFromDB();
			getTableOrTree().getFocDataWrapper().refreshGuiForContainerChanges();
		}
  }
  
  public Embedded valo_GetReloadEmbedded(){
  	if(valo_ReloadEmbedded == null){
  		valo_ReloadEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_REFRESH));
  		valo_ReloadEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_ReloadEmbedded.setDescription("Refresh");
  		valo_ReloadEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		
  		valo_ReloadEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					reloadClickListener();
				}
			});
  	}
  	return valo_ReloadEmbedded;
  }
  
  public FVButton getReloadButton(){
  	if(reloadButton == null){
	  	reloadButton = new FVButton("");
	  	reloadButton.setStyleName(BaseTheme.BUTTON_LINK);
	  	reloadButton.setDescription("Refresh");
	  	reloadButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
	  	reloadButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_REFRESH));
	  	reloadButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					reloadClickListener();
				}
			});
  	}
  	return reloadButton;
  }
  
  private Embedded valo_GetDuplicateEmbedded(){
  	if(valo_DuplicateEmbedded == null && getTableTreeDelegate().isDuplicateEnabled()){
  		valo_DuplicateEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_DUPLICATE_16));
  		valo_DuplicateEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_DuplicateEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		valo_DuplicateEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_DuplicateEmbedded.setDescription("Duplicate");
  		
  		valo_DuplicateEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					duplicateClickListenerContent();				
				}
			});
  	}
  	return valo_DuplicateEmbedded;
  }
  
  private FVButton getDuplicateButton(){
  	if(duplicateButton == null && getTableTreeDelegate().isDuplicateEnabled()){
  		duplicateButton = new FVButton("");
  		duplicateButton.setStyleName(BaseTheme.BUTTON_LINK);
  		duplicateButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);  		
  		duplicateButton.setDescription("Duplicate");
  		duplicateButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_DUPLICATE_16));
  		duplicateButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					duplicateClickListenerContent();
				}
			});
  	}
  	return duplicateButton;
  }
  
  private void openClickListenerContent(){
  	if(getTableTreeDelegate() != null){
			FocObject focObject = getTableTreeDelegate().getSelectedObject();
			if(focObject != null){
				getTableTreeDelegate().open(focObject);
			}else{
				Globals.showNotification("Select an Item", "to open", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		}
  }
  
  private Embedded valo_GetOpenEmbedded(){
  	if(valo_OpenEmbedded == null && getTableTreeDelegate().isAddEnabled() && !isPrintingUI()){
  		valo_OpenEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_EDIT));
  		valo_OpenEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_OpenEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		valo_OpenEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_OpenEmbedded.setDescription("Open");
  		
  		valo_OpenEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					openClickListenerContent();
				}
			});
  	}
  	return valo_OpenEmbedded;
  }
  
  private FVButton getOpenButton(){
  	if(openButton == null && getTableTreeDelegate().isOpenEnabled()){
  		openButton = new FVButton("");
  		openButton.setStyleName(BaseTheme.BUTTON_LINK);
  		openButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		openButton.setDescription("Open");
  		openButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_EDIT));
  		openButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					openClickListenerContent();
				}
			});
  		
  	}
  	return openButton;
  }
  
  private void addItemClickListenerContent(){
  	if(getTableTreeDelegate() != null){
			FocObject focObject = getTableTreeDelegate().getSelectedObject();
			getTableTreeDelegate().addItem(focObject);
		}
  }

  private Embedded valo_GetAddEmbedded(){
  	if(valo_AddEmbedded == null && getTableTreeDelegate().isAddEnabled() && !isPrintingUI()){
  		valo_AddEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_ADD));
  		valo_AddEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_AddEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		valo_AddEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		
  		valo_AddEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					addItemClickListenerContent();	
				}
			});
  	}
  	return valo_AddEmbedded;
  }
  
  private FVButton getAddButton(){
  	if(addButton == null && getTableTreeDelegate().isAddEnabled() && !isPrintingUI()){
  		addButton = new FVButton("");
  		addButton.setStyleName(BaseTheme.BUTTON_LINK);
  		addButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		addButton.setDescription("Add");
  		addButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_ADD));
  		addButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					addItemClickListenerContent();
				}
			});
  	}
  	return addButton;
  }
  
//  public void setAddButtonVisibility(boolean visible){
//  	if(getAddButton() != null && valo_GetAddEmbedded() != null){
//  		getAddButton().setVisible(visible);
//  		valo_GetAddEmbedded().setVisible(visible);
//  	}
//  }
  
  private void deleteItemClickListenerContent(){
  	if(getTableTreeDelegate() != null){
			FocObject focObject = getTableTreeDelegate().getSelectedObject();
			if(focObject != null){
				getTableTreeDelegate().delete(focObject);
			}else{
				Globals.showNotification("Select an Item", "to delete", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		}
  }
  
  private Embedded valo_GetDeleteEmbedded(){
  	if(valo_DeleteEmbedded == null && getTableTreeDelegate().isDeleteEnabled()){
  		valo_DeleteEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_DELETE));
  		valo_DeleteEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		valo_DeleteEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_DeleteEmbedded.setDescription("Delete");
  		valo_DeleteEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					deleteItemClickListenerContent();
				}
			});
  	}
  	return valo_DeleteEmbedded;
  }
  
  private FVButton getDeleteButton(){
  	if(deleteButton == null && getTableTreeDelegate().isDeleteEnabled()){
  		deleteButton = new FVButton("");
  		deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
  		deleteButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		deleteButton.setDescription("Delete");
  		deleteButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_DELETE));
  		deleteButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					deleteItemClickListenerContent();
				}
			});
  	}
  	return deleteButton;
  }
  
  private void statusStyleClickListener(){
  	getTableTreeDelegate().toggleStatusStyleEnabled();
		getTableTreeDelegate().getTable().refreshRowCache();
  }
  
  private Embedded valo_GetStatusStyleEmbedded(){
  	if(getTableTreeDelegate() != null){
  		getTableTreeDelegate().setStatusStyleEnabled(getTableTreeDelegate().isTransactionColorEnabled());
  	}
  	if(valo_StatusStyleEmbedded == null 
  			&& getTableTreeDelegate() != null
  			&& getTableTreeDelegate().getFocDesc() != null
  			&& getTableTreeDelegate().getFocDesc().statusHodler_IsStatusHolder() 
  			&& getTableTreeDelegate().isTransactionColorEnabled()){
  		valo_StatusStyleEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_PALETTE));
  		valo_StatusStyleEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_StatusStyleEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		valo_StatusStyleEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_StatusStyleEmbedded.setDescription("Toggle Status Color");
  		
  		valo_StatusStyleEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					statusStyleClickListener();
				}
			});
  	}
  	return valo_StatusStyleEmbedded;
  }
  
  private FVButton getStatusStyleButton(){
  	if(statusStyleButton == null && getTableTreeDelegate() != null && getTableTreeDelegate().getFocDesc() instanceof IStatusHolderDesc && getTableTreeDelegate().isTransactionColorEnabled()){
  		statusStyleButton = new FVButton("");
  		statusStyleButton.setStyleName(BaseTheme.BUTTON_LINK);
  		statusStyleButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
  		statusStyleButton.setDescription("Toggle Status Color");
  		statusStyleButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_PALETTE));
  		statusStyleButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					statusStyleClickListener();
				}
			});
  	}
  	return statusStyleButton;
  }
  
  /*
  private boolean hasZoomButton(){
  	boolean has = false;
  	if(getTableOrTree() instanceof FVTreeTable){
  		FVTreeTable treeTable = (FVTreeTable) getTableOrTree();
  		if(treeTable.getFTree() instanceof FObjectTree){
    		has = true;  			
  		}
  	}
  	return has;
  }
  
  private FVButton getZoomButton(){
  	if(zoomButton == null && hasZoomButton()){
  		zoomButton = new FVButton("");
  		zoomButton.setStyleName(BaseTheme.BUTTON_LINK);
  		zoomButton.setDescription("Transaction filter");
  		zoomButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_FILTER));
  		zoomButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					
					XMLViewKey xmlViewKey = new XMLViewKey(WorkflowWebModule.STORAGE_NAME_TRANSACTION_FILTER, XMLViewKey.TYPE_FORM);
					TransactionFilter_Form transactionFilterForm = (TransactionFilter_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(FVTableWrapperLayout.this.getWindow(), xmlViewKey, null);
					
				}
  		}
  	}
  	return zoomButton;
  }
  */
  
  private void transactionFilterClickListener(){
  	XMLViewKey xmlViewKey = new XMLViewKey(WorkflowWebModule.STORAGE_NAME_TRANSACTION_FILTER, XMLViewKey.TYPE_FORM);
		TransactionFilter_Form transactionFilterForm = (TransactionFilter_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(FVTableWrapperLayout.this.getWindow(), xmlViewKey, null);
		transactionFilterForm.setTableWrapperLayout(FVTableWrapperLayout.this);
		transactionFilterForm.parseXMLAndBuildGui();
		FVTableWrapperLayout.this.getWindow().changeCentralPanelContent(transactionFilterForm, true);
		
		transactionFilterForm.getValidationLayout().addValidationListener(new IValidationListener() {
			
			public void validationDiscard(FVValidationLayout validationLayout) {
			}
			
			public boolean validationCommit(FVValidationLayout validationLayout) {
				getTableOrTree().getFocDataWrapper().refreshGuiForContainerChanges();
				return false;
			}
			
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
			}
			});
  }
  
  private void createTransactionFilter(){
	  try{
	  	getTableOrTree().getFocDataWrapper().addContainerFilter(new Filter(){
				@Override
				public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
					 boolean visible = true;
					 
					 ArrayList<Integer> excludedStatusList     = excludedStatusArray_get(false);
					 ArrayList<Integer> excludedDepartmentList = excludedDepartmentArray_get(false);
					 
					 FocObject focObject = (FocObject) item;

					 if(focObject != null){
						 if(!Utils.isStringEmpty(getCodeFrom())){
							 visible = focObject.code_getCode().compareTo(getCodeFrom()) >= 0;
						 }
						 if(visible && !Utils.isStringEmpty(getCodeTo())){
							 visible = focObject.code_getCode().compareTo(getCodeTo()) <= 0;
						 }
					 }
					 
					 if(visible){
						 if(excludedStatusList != null){
							 if(focObject instanceof IStatusHolder){
								 FocDesc focDesc        = focObject.getThisFocDesc();
								 IStatusHolderDesc iStatusHolderDesc = (IStatusHolderDesc) focDesc;
								 FField fField = focDesc.getFieldByID(iStatusHolderDesc.getFLD_STATUS());
								 String statusFieldName = fField.getName();
								 FProperty fProperty = focObject.getFocPropertyByName(statusFieldName);
								 int statusValue = -1;
								 if(fProperty != null){
									 statusValue = fProperty.getInteger();
								 }
								 for(int i=0; i<excludedStatusList.size() && visible; i++){
									 int     excludedItemId = excludedStatusList.get(i);
									 visible = statusValue != excludedItemId;
								 }
							 }
						 }
						 if(excludedDepartmentList != null){
							 int departmentRef = -1;
							 Department department = focObject.getDepartment();
							 if(department != null && department.getReference() != null){
								 departmentRef = department.getReference().getInteger();
								 
							 }
							 for(int i=0; i<excludedDepartmentList.size() && visible; i++){
								 int excludedItemId = excludedDepartmentList.get(i);
								 visible = departmentRef != excludedItemId;
							 }
						 }
					 }
					 return visible;
				}

				@Override
				public boolean appliesToProperty(Object propertyId) {
					return false;
				}
	  	});
	  	
	  }catch(Exception e){
	  	Globals.logException(e);
	  }
  }
  
  private Embedded valo_GetTransactionFilterEmbedded(){
  	if(valo_TransactionFilterEmbedded == null && hasTransactionFilter()){
  		valo_TransactionFilterEmbedded = new Embedded("", FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_FILTER));
  		valo_TransactionFilterEmbedded.setStyleName(BaseTheme.BUTTON_LINK);
  		valo_TransactionFilterEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
  		valo_TransactionFilterEmbedded.setDescription("Transaction filter");
  		
  		valo_TransactionFilterEmbedded.addClickListener(new MouseEvents.ClickListener() {

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					transactionFilterClickListener();
				}
			});
  		createTransactionFilter();
  	}
  	return valo_TransactionFilterEmbedded;
  }
  
  private FVButton getTransactionFilterButton(){
  	if(transactionFilterButton == null && hasTransactionFilter()){
  		transactionFilterButton = new FVButton("");
  		transactionFilterButton.setStyleName(BaseTheme.BUTTON_LINK);
  		transactionFilterButton.setDescription("Transaction filter");
  		transactionFilterButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_FILTER));
  		transactionFilterButton.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					transactionFilterClickListener();
				}
			});
  		createTransactionFilter();  		
  	}
  	return transactionFilterButton;
  }
  
  public void adjustTableEditingToolsVisiblity(){
  	if(Globals.isValo()){
  		Embedded addEmbedded = valo_GetAddEmbedded();
  		if(addEmbedded != null){
  			addEmbedded.setVisible(getTableTreeDelegate().isAddEnabled());
    	}
  		Embedded openEmbedded = valo_GetOpenEmbedded();
  		if(openEmbedded != null){
  			openEmbedded.setVisible(getTableTreeDelegate().isOpenEnabled());
    	}
  		Embedded deleteEmbedded = valo_GetDeleteEmbedded();
  		if(deleteEmbedded != null){
  			deleteEmbedded.setVisible(getTableTreeDelegate().isDeleteEnabled());
    	}
  		Embedded duplicateEmbedded = valo_GetDuplicateEmbedded();
  		if(duplicateEmbedded != null){
  			duplicateEmbedded.setVisible(getTableTreeDelegate().isDuplicateEnabled());
    	}
  	}else{
  		if(getAddButton() != null){
    		getAddButton().setVisible(getTableTreeDelegate().isAddEnabled());
    	}
    	if(getOpenButton() != null){
    		getOpenButton().setVisible(getTableTreeDelegate().isOpenEnabled());
    	}
    	if(getDeleteButton() != null){
    		getDeleteButton().setVisible(getTableTreeDelegate().isDeleteEnabled());
    	}
    	if(getDuplicateButton() != null){
    		getDuplicateButton().setVisible(getTableTreeDelegate().isDuplicateEnabled());
    	}  		
  	}
//		if(getTableStatusStyleButton() != null){
////			getTableStatusStyleButton().setVisible(getTableTreeDelegate().isTableStatusStyleEnabled());
//			getTableStatusStyleButton().setVisible(true);
//		}
//		if(getTransactionFilterButton() != null){
////		getTableStatusStyleButton().setVisible(getTableTreeDelegate().isTableStatusStyleEnabled());
//		getTableStatusStyleButton().setVisible(true);
//	}		
  }
  
  public void setTitle(String title){
  	if(titleLabel == null){
			titleLabel = new FVLabel(title);
			titleLabel.setHeight("-1px");
			titleLabel.addStyleName("foc-f16");
			titleLabel.addStyleName("foc-bold");
			addHeaderComponent_ToLeft(titleLabel);
  	}else{
  		titleLabel.setValueString(title);
  	}
  }

  public void setTableOrTree(FocXMLLayout xmlLayout, ITableTree tableOrTree) {
    this.tableOrTree = tableOrTree;
    getTableTreeDelegate().setWrapperLayout(this);

    if(tableOrTree != null && tableOrTree.getTableTreeDelegate() != null){
			String title = getAttributes().getValue(FXML.ATT_TITLE);
			if(title != null && !title.isEmpty()){
				setTitle(title);
			}
			
			if(Globals.isValo()){
				Embedded open = valo_GetOpenEmbedded();
				if(open != null){
					addHeaderComponent(open);
				}
				Embedded add = valo_GetAddEmbedded();
				if(add != null){
					addHeaderComponent(add);
				}
				Embedded delete = valo_GetDeleteEmbedded();
				if(delete != null){
	  			addHeaderComponent(delete);
	  		}
				Embedded duplicate = valo_GetDuplicateEmbedded();
				if(duplicate != null){
					addHeaderComponent(duplicate);
				}
				Embedded statusStyle = valo_GetStatusStyleEmbedded();
				if(statusStyle != null){
					addHeaderComponent(statusStyle);
				}
				Embedded transactionFilter = valo_GetTransactionFilterEmbedded();
				if(transactionFilter != null){
					addHeaderComponent(transactionFilter);
				}
			}else{
				FVButton openButton = getOpenButton();
	  		if(openButton != null){
	  			addHeaderComponent(openButton);
	  		}
	    	
	  		FVButton addButton = getAddButton();
				if(addButton != null){
					addHeaderComponent(addButton);
				}
	    	
	  		FVButton deleteButton = getDeleteButton();
				if(deleteButton != null){
					addHeaderComponent(deleteButton);
				}
				
				FVButton duplicateButton = getDuplicateButton();
				if(duplicateButton != null){
					addHeaderComponent(duplicateButton);
				}
				
				FVButton statusStyleButton = getStatusStyleButton();
				if(statusStyleButton != null){
					addHeaderComponent(statusStyleButton);
				}
				FVButton transactionFilterButton = getTransactionFilterButton();
				if(transactionFilterButton != null){
					addHeaderComponent(transactionFilterButton);
				}
			}
    	
//			FVButton exportButton = getExportToExcelButton();
//			if(exportButton != null){
//				addHeaderComponent(exportButton);
//			}
			
    	if(!isPrintingUI() && getAttributes() != null && getAttributes().getValue(FXML.ATT_REFRESH_ENABLED) != null){
    		if(getAttributes().getValue(FXML.ATT_REFRESH_ENABLED).equalsIgnoreCase("false")){
    			isRefreshEnabled = false;
    		}
    	}

    	if(isRefreshEnabled && !isPrintingUI() && getXMLType() != null && !getXMLType().equals(FXML.TAG_PIVOT)){
    		if(Globals.isValo()){
    			Embedded reloadEmbedded = valo_GetReloadEmbedded();
  		  	if(reloadEmbedded != null){
  		  		addHeaderComponent(reloadEmbedded);
  		  	}
    		}else{
    			FVButton reloadButton = getReloadButton();
  		  	if(reloadButton != null){
  		  		addHeaderComponent(reloadButton);
  		  	}
    		}
    	}

  		String showViewSelector = getTableTreeDelegate().getAttributes().getValue(FXML.ATT_SHOW_VIEW_SELECTOR);
  		if(showViewSelector != null && showViewSelector.toLowerCase().equals("true")){
//  			FocXMLLayout focXMLLayout = getTableTreeDelegate().getFocXMLLayout();
  			if(xmlLayout != null){
  				viewSelector = new FVViewSelector_MenuBar(xmlLayout, null);
  				if(viewSelector != null){
  					addHeaderComponent(viewSelector);
  				}
  			}
  		}
    }
    
    FVCheckBox editCheckBox = getTableTreeDelegate().getEditableCheckBox();
    if (editCheckBox != null) {
      getHorizontalLayout().addComponent(editCheckBox);
    }
    
    setFilterBoxListenerIfNecessary();
    
    tablePanel = new Panel();
    tablePanel.addStyleName(Reindeer.PANEL_LIGHT);
    // tablePanel.addActionHandler(new KbdHandler());
////    VerticalLayout vLay = new VerticalLayout();
////    vLay.setMargin(false);
////    vLay.setSpacing(false);
//    tablePanel.setContent(vLay);
    tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
    tablePanel.setContent((Component) tableOrTree);
    
    if(tableOrTree instanceof Table){
    	((Table)tableOrTree).setHeight("100%");
    }
    
    tablePanel.setHeight("100%");
    addComponent(tablePanel);
    setExpandRatio(tablePanel, 1);
	
    // addComponent((Component) tableOrTree);
    
    if (tableOrTree instanceof Table) {
      ((Table) tableOrTree).setDropHandler(new DropHandler() {

        @Override
        public AcceptCriterion getAcceptCriterion() {
          return AcceptAll.get();
        }

        @Override
        public void drop(DragAndDropEvent event) {
          try {
            if (event.getTransferable() instanceof DataBoundTransferable) {
              DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();
              String name = t.getItemId() + "";

              FocXMLAttributes attributes = new FocXMLAttributes();

              if (t.getSourceContainer() instanceof DataModelNodeTree) {
                DataModelNodeTree tree = (DataModelNodeTree) t.getSourceContainer();
                name = tree.getItem(t.getItemId()).toString();
                attributes.addAttribute("", "name", "name", "CDATA", name);
                attributes.addAttribute("", "caption", "caption", "CDATA", name.toLowerCase());

                getTableTreeDelegate().addColumn(attributes);
              }
            }
          } catch (Exception e) {
            Globals.logException(e);
          }
        }
      });
    }
  }
  
  public void setFilterBoxListenerIfNecessary(){
  	if (withFilterBox() && filter == null && getTableOrTree() != null && getTableOrTree().getFocDataWrapper() != null) {//!(tableOrTree instanceof FVPivotTable) &&
	    filter = new Filter() {
	      @Override
	      public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
	        boolean passes = true;
	        
	        if (getFilterText() != null && !getFilterText().isEmpty()) {
	          FocObject focObject = (FocObject) item;
	          passes = isIncludeObject(focObject);
	          
	          if(!passes && getTableOrTree() != null && (getTableOrTree() instanceof FVTreeTable) || (getTableOrTree() instanceof FVTreeGrid)){
	          	FTree tree = null;
	          	if(getTableOrTree() instanceof FVTreeTable){
	          		FVTreeTable treeTable = (FVTreeTable) getTableOrTree();
		          	tree = treeTable != null ? treeTable.getFTree() : null;	          		
	          	}else{
	          		FVTreeGrid treeGrid = (FVTreeGrid) getTableOrTree();
		          	tree = treeGrid != null ? treeGrid.getFTree() : null;
	          	}
	          	
	          	FNode node = tree != null ? tree.findNode_UsingMapOnly(focObject) : null;
	          	if(node != null){
		          	node = node.getFatherNode();
		          	focObject = node != null ? (FocObject) node.getObject() : null;
		          	while(!passes && focObject != null){
	                passes = isIncludeObject(focObject);
			          	node = node.getFatherNode();
			          	focObject = node != null ? (FocObject) node.getObject() : null;
		          	}
	          	}
	          }
	          
//	          if(!passes && getTableOrTree() != null && getTableOrTree() instanceof FVTreeTable){
//	          	FVTreeTable treeTable = (FVTreeTable) getTableOrTree();
//	          	FTree tree = treeTable.getFTree();
//	          	
//	          	FNode node = tree.findNode_UsingMapOnly(focObject);
//	          	if(node != null){
//		          	node = node.getFatherNode();
//		          	focObject = node != null ? (FocObject) node.getObject() : null;
//		          	while(!passes && focObject != null){
//	                passes = isIncludeObject(focObject);
//			          	node = node.getFatherNode();
//			          	focObject = node != null ? (FocObject) node.getObject() : null;
//		          	}
//	          	}
//	          }
	        }
	        return passes;
	      }
	
	      @Override
	      public boolean appliesToProperty(Object propertyId) {
	        return false;
	      }
	    };
     
      FocDataWrapper dataWrapper = getTableOrTree().getFocDataWrapper();
      
/*      if(getTableOrTree() instanceof FVPivotTable){
      	FVPivotTable pivotTable = (FVPivotTable) getTableOrTree();
      	pivotTable.setFocData(pivotTable.getFTree());
      	dataWrapper = pivotTable.getFocDataWrapper();
//      	FocPivotWrapper focPivotWrapper = new FocPivotWrapper(pivotTable.getFTree());
//      	dataWrapper = focPivotWrapper;
      }
*/      
      dataWrapper.addContainerFilter(filter);

      filterTextField = new FVTextField();

      filterTextField.setInputPrompt(getFocXMLLayout() != null && getFocXMLLayout().isArabic() ? "بحث سريع" : "Search");
      filterTextField.setStyleName("filtertextField");
      filterTextField.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
      filterTextField.addTextChangeListener(new TextChangeListener() {
        public void textChange(TextChangeEvent event) {
          FocDataWrapper dataWrapper = (FocDataWrapper) getTableOrTree().getFocDataWrapper();
          String newFilterString = event.getText();
          
          if(			newFilterString != null 
          		&& (		newFilterString.length() > 1 
          				|| 	(filterString != null && newFilterString.length() < filterString.length())
          				)
          	){
          	filterString = newFilterString;
	          multipleExpressions = null;
	          dataWrapper.resetVisibleListElements();
	          
	          if(getTableTreeDelegate() != null){
	          	getTableTreeDelegate().refresh_CallContainerItemSetChangeEvent();
	          }
//	          (getTableOrTree()).applyFocListAsContainer();
	          if (getTableOrTree() instanceof FVTreeTable) {
	            if(!Utils.isStringEmpty(event.getText())){
	              FVTreeTable treeTable = (FVTreeTable) getTableOrTree();
	              
	              int countToExpand = 20;
	              
	          		boolean backup = treeTable.setRefreshGuiDisabled(true);
	          		Collection itemIDs = dataWrapper.getItemIds();
	              Iterator iter = itemIDs.iterator();
	              if(iter != null){
	                while (iter.hasNext() && countToExpand > 0) {
	                  treeTable.setCollapsed(iter.next(), false);
	                  countToExpand--;
	                }
	              }
	              treeTable.setRefreshGuiDisabled(backup);
	            }
	          }
          }
        }
      });
      getHeaderRightLayout().addComponent(filterTextField);
      getHeaderRightLayout().setComponentAlignment(filterTextField, Alignment.BOTTOM_RIGHT);
    }
  }
  
  public void enableReplace(){
  	if(replaceCheckBox == null){
  		replaceCheckBox = new FVCheckBox("Rep.");
//  		replaceCheckBox.addStyleName("border");
  		replaceCheckBox.setDescription("Check to 'Replace' a line content upon Drag and Drop instead of inserting a new child line.");
  		getHeaderRightLayout().addComponent(replaceCheckBox);
  		getHeaderRightLayout().setComponentAlignment(replaceCheckBox, Alignment.BOTTOM_RIGHT);
  	}
  }
  
  public boolean isReplaceActive(){
  	boolean replace = false;
  	if(replaceCheckBox != null){
  		replace = replaceCheckBox.getValue();
  	}
  	return replace;
  }
  
  public String setReplaceActive(boolean active){
  	String error = null;
  	if(replaceCheckBox != null){
  		replaceCheckBox.setValue(active);
  	}else{
  		error = "'Replace' checkbox is null";
  	}
  	return error;
  }
  
  public TableTreeDelegate getTableTreeDelegate() {
    return tableOrTree != null ? tableOrTree.getTableTreeDelegate() : null;
  }

  public ITableTree getTableOrTree() {
    return tableOrTree;
  }

  @Override
  public String getXMLType() {
    return ((FocXMLGuiComponent) tableOrTree).getXMLType();
  }

  @Override
  public Attributes getAttributes() {
    return tableOrTree != null ? ((FocXMLGuiComponent) tableOrTree).getAttributes() : null;
  }

  @Override
  public void setAttributes(Attributes attributes) {
    if (tableOrTree != null) {
      ((FocXMLGuiComponent) tableOrTree).setAttributes(attributes);
    }
  }

  @Override
  public boolean isXMLLeaf() {
    return true;
  }

  @Override
  public void fillXMLNodeContent(XMLBuilder builder) {
    tableOrTree.getTableTreeDelegate().fillXMLNodeContent(builder);
  }

  public void createShowFormPanelIfRequired() {
    if (getXmlView_ShowForm() != null && getTableOrTree() instanceof Table) {
      Table table = (Table) getTableOrTree();
      if (table != null) {
        table.setImmediate(true);
        showFormPanel = new FVPanel();
        addComponent(showFormPanel);

        table.addItemClickListener(new ItemClickListener() {
          @Override
          public void itemClick(ItemClickEvent event) {
            FVPanel panel = getShowFormPanel();
            if (panel != null) {
              panel.setContent(null);
              XMLViewKey xmlViewKey = getXmlView_ShowForm().getXmlViewKey();

              Integer itemId = (Integer) event.getItemId();
              int ref = itemId.intValue();
              FocObject focObject = getTableOrTree().getFocList().searchByReference(ref);

              ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) getWindow(), xmlViewKey, focObject);
              panel.setContent((Component) centralPanel);
            }
          }
        });
      }
    }
  }

  public FVPanel getShowFormPanel() {
    return showFormPanel;
  }

  public String getFilterText() {
    return filterString;
  }

  private String getColumnValueForFilterComparison(FVTableColumn tableCol, FocObject focObj){
  	String colValue = null;
    if(tableCol != null && focObj != null){
      String propID = tableCol.getDataPath();
      FProperty prop = focObj.getFocPropertyForPath(propID);
      if(prop == null && propID != null && !propID.isEmpty() && propID.equals(FXML.COL_TREE_NODE_TITLE) && getTableOrTree() instanceof FVTreeTable){
      	if(focObj != null){
      		colValue = getTableTreeDelegate().getNodeTitleDisplayStringForObject(focObj);
      	}
      }else{
	      Object currObj = prop != null ? prop.vaadin_TableDisplayObject(null, tableCol.getCaptionProp()) : null;
	      if (currObj != null && currObj instanceof String) {
	        if (currObj instanceof String) {
	        	colValue = (String) currObj;
	        }
	      }
      }
    }  
    return colValue;
  }
  
  public boolean isIncludeObject(FocObject focObj) {
  	ArrayList<String> expressionsArray = getMultipleExpressions();
    boolean include = expressionsArray == null || expressionsArray.isEmpty();
    
    if(!include){
      ArrayList<FVTableColumn> arrayOfTableColumns = tableOrTree != null && tableOrTree.getTableTreeDelegate() != null ? tableOrTree.getTableTreeDelegate().getVisiblePropertiesArrayList() : null;

	    boolean[] match = null;
	    if(filterOperation_AND){
	    	match = new boolean[expressionsArray.size()];	
	    }
	
	    for(int f = 0; arrayOfTableColumns != null && f < arrayOfTableColumns.size() && !include; f++) {//&& !include
	      FVTableColumn tableCol = arrayOfTableColumns.get(f);
	      String colStringValue = getColumnValueForFilterComparison(tableCol, focObj);
	      if(colStringValue != null){
	      	
	      	for(int e=0; e<expressionsArray.size(); e++){
	      		String exp = expressionsArray.get(e);
	      		int includeAtomic = isStringLikeExpression_Atomic(colStringValue, exp);
	      		if(includeAtomic == 1){
	      			if(filterOperation_AND){
	      				match[e] = true;
	      				//Check if we should stop.
	      				//We stop if all the match are true.
	      				include = true;
	      				for(int e2=0; e2<match.length; e2++){
	      					if(!match[e2]){
	      						include = false;
	      						break;
	      					}
	      				}
	      			}else{
	      				include = true;
	      			}
	      		}
	      	}
	      }
	    }
    }
    return include;
  }

  private int isStringLikeExpression_Atomic(String str, String oneExpression) {
    int include = -1;
    if (str != null) {
      str = str.replace(" ", "");
      oneExpression = oneExpression.replace(" ", "");
      if (str.toUpperCase().contains(oneExpression.toUpperCase())) {
        include = 1;
      }else{
      	include = 0;
      }
    }
    return include;
  }

  private boolean isStringLikeExpression_old(String str) {
    boolean include = getFilterText().isEmpty();
    if (!include && str != null && str.length() > 0) {
      ArrayList<String> expressionsArray_Or = getMultipleExpressions();
      
      if(filterOperation_AND) include = true;
      boolean stop = filterOperation_AND ? !include : include;
      for (int i = 0; i < expressionsArray_Or.size() && !stop; i++) {
        String oneExpression = expressionsArray_Or.get(i);
        str = str.replace(" ", "");
        oneExpression = oneExpression.replace(" ", "");
        if (str.toUpperCase().contains(oneExpression.toUpperCase())) {
          include = true;
        }else{
        	include = false;
        }
        stop = filterOperation_AND ? !include : include;
      }

      /*
       * String findExpression = findObject.getFindExpression();
       * if(findObject.isStartsWith() &&
       * str.toUpperCase().startsWith(findExpression.toUpperCase())){ include =
       * true; }else if(findObject.isContains() &&
       * str.toUpperCase().contains(findExpression.toUpperCase())){ include =
       * true; }
       */
    }
    return include;
  }

  public ArrayList<String> getMultipleExpressions() {
    if (multipleExpressions == null) {
    	filterOperation_AND = false;
      multipleExpressions = new ArrayList<String>();
      String findExpression = getFilterText().trim();
      if(findExpression.toLowerCase().startsWith("and(") && findExpression.endsWith(")")){
      	findExpression = findExpression.substring(4, findExpression.length()-1);
      	filterOperation_AND = true;
      }
      StringTokenizer tok = new StringTokenizer(findExpression, ",+", false);
      while (tok != null && tok.hasMoreTokens()) {
        String token = tok.nextToken();
        multipleExpressions.add(token);
      }
      if(multipleExpressions.size() < 2) filterOperation_AND = false;//For one item we will apply the algorithm of OR because less consuming.
    }
    return multipleExpressions;
  }

  private boolean withFilterBox() {
    boolean withFilter = true;
    String fitlerValue = getAttributes().getValue(FXML.ATT_WITH_FILTER);
    if (fitlerValue != null && !fitlerValue.isEmpty() && fitlerValue.equals("false")) {
      withFilter = false;
    }
    if(withFilter && isPrintingUI()) withFilter = false;
    return withFilter;
  }
  
  public void refresh(){
//		if(getTableTreeDelegate() != null){
//			getTableTreeDelegate().selectionColumn_copyMemoryToGui();
//		}  		
  	
  	setAttributes(getAttributes());
  	if(getTableOrTree() instanceof Table){
	  	Table table = (Table) getTableOrTree();
	  	if(table != null){
	  		
	   	  //------
	  		//Without these lines we do not get a refresh as should be when we drag and drop an underlying in the WBS
	  		//We do not see the line
	  		if(getTableTreeDelegate() != null){
	  			getTableTreeDelegate().refresh_ContainerItemSetChange();
	  			if(getTableOrTree().getFocDataWrapper() != null){
//	        	EverproItemSetChangeEvent event = new EverproItemSetChangeEvent((Container)FocDataWrapper.this, everproEventID, (Object)ref);
	        	getTableOrTree().getFocDataWrapper().resetVisibleListElements();

//	  				getTableOrTree().getFocDataWrapper().firePropertySetChangeEvent();
	  			}
	  		}
	  		/*
	  		if(table instanceof FVTreeTable){
	  			((FVTreeTable)table).refresh_ContainerItemSetChange();
	  		}else{
	  			((FVTable)table).refresh_ContainerItemSetChange();
	  		}
	  		*/
	  		//------
	  		  		
	  		//Usually the refreshRowCache doesn't take effect unless the table is marked as dirty
	  		// As of Vaadin 7, markAsDirtyRecursive() calls refreshRowCache()
	  		//  		table.refreshRowCache();
	  		table.markAsDirtyRecursive();
	  	}
  	}
  }

  // Keyboard navigation
  class KbdHandler implements Handler {
    Action tab_next = new ShortcutAction("Shift", ShortcutAction.KeyCode.TAB, null);
    Action tab_prev = new ShortcutAction("Shift+Tab", ShortcutAction.KeyCode.TAB, new int[] { ShortcutAction.ModifierKey.SHIFT });
    Action cur_down = new ShortcutAction("Down", ShortcutAction.KeyCode.ARROW_DOWN, null);
    Action cur_up = new ShortcutAction("Up", ShortcutAction.KeyCode.ARROW_UP, null);
    Action enter = new ShortcutAction("Enter", ShortcutAction.KeyCode.ENTER, null);
    Action add = new ShortcutAction("Add Below", ShortcutAction.KeyCode.A, null);
    Action delete = new ShortcutAction("Delete", ShortcutAction.KeyCode.DELETE, null);
    Action spaceBar = new ShortcutAction("Space", ShortcutAction.KeyCode.SPACEBAR, null);
    Action cur_left = new ShortcutAction("Left", ShortcutAction.KeyCode.ARROW_LEFT, null);
    Action cur_right = new ShortcutAction("Right", ShortcutAction.KeyCode.ARROW_RIGHT, null);

    public Action[] getActions(Object target, Object sender) {
      return new Action[] { tab_next, tab_prev, cur_down, cur_up, enter, add, delete, spaceBar, cur_left, cur_right };
    }

    public void handleAction(Action action, Object sender, Object target) {
      /*
       * if (target instanceof TextField) { TextField tf = (TextField) target;
       * ItemPropertyId ipId = (ItemPropertyId) tf.getData();
       * 
       * // On enter, close the edit mode if (action == enter) { // Make the
       * entire item read-only HashMap<Object,Field> itemMap =
       * fields.get(ipId.getItemId()); for (Field f: itemMap.values())
       * f.setReadOnly(true); table.select(ipId.getItemId()); table.focus();
       * 
       * // Updates the generated column table.refreshRowCache(); return; }
       * 
       * Object propertyId = ipId.getPropertyId();
       * 
       * // Find the index of the property Object cols[] =
       * table.getVisibleColumns(); int pidIndex = 0; for (int i=0;
       * i<cols.length; i++) if (cols[i].equals(propertyId)) pidIndex = i;
       * 
       * Object newItemId = null; Object newPropertyId = null;
       * 
       * // Move according to keypress if (action == cur_down) newItemId =
       * beans.nextItemId(ipId.getItemId()); else if (action == cur_up)
       * newItemId = beans.prevItemId(ipId.getItemId()); else if (action ==
       * tab_next) newPropertyId = cols[Math.min(pidIndex+1, cols.length-1)];
       * else if (action == tab_prev) newPropertyId = cols[Math.max(pidIndex-1,
       * 0)];
       * 
       * // If tried to go past first or last, just stay there if (newItemId ==
       * null) newItemId = ipId.getItemId(); if (newPropertyId == null)
       * newPropertyId = ipId.getPropertyId();
       * 
       * // On enter, just stay where you were. If we did // not catch the enter
       * action, the focus would be // moved to wrong place.
       * 
       * Field newField = fields.get(newItemId).get(newPropertyId); if (newField
       * != null) newField.focus(); } else if (target instanceof Table) { Table
       * table = (Table) target; Object selected = table.getValue();
       * 
       * if (selected == null) return;
       * 
       * if (action == enter) { // Make the entire item editable
       * HashMap<Object,Field> itemMap = fields.get(selected); for (Field f:
       * itemMap.values()) f.setReadOnly(false);
       * 
       * // Focus the first column
       * itemMap.get(table.getVisibleColumns()[0]).focus(); } else if (action ==
       * add) { // TODO } else if (action == delete) { Item item =
       * table.getItem(selected); if (item != null && item instanceof
       * BeanItem<?>) { // Change selection Object newselected =
       * table.nextItemId(selected); if (newselected == null) newselected =
       * table.prevItemId(selected); table.select(newselected); table.focus();
       * 
       * // Remove the item from the container beans.removeItem(selected);
       * 
       * // Remove from the map // TODO } } }
       */
    }
  }

	public FVViewSelector_MenuBar getViewSelector() {
		return viewSelector;
	}
	
	public ArrayList<Integer> excludedStatusArray_get(boolean create){
		if(excludedStatusArray == null && create){
			excludedStatusArray = new ArrayList<Integer>();
		}
		return excludedStatusArray;
	}

	public ArrayList<Integer> excludedDepartmentArray_get(boolean create){
		if(excludedDepartmentArray == null && create){
			excludedDepartmentArray = new ArrayList<Integer>();
		}
		return excludedDepartmentArray;
	}
	
	public boolean hasTransactionFilter_hasStatus(){
		boolean isStatusHolder = false;
		
		if(getTableOrTree() != null && getTableOrTree().getFocList() != null){
			FocDesc focDesc = getTableOrTree().getFocList().getFocDesc();
			if(focDesc != null){
				isStatusHolder = focDesc.statusHodler_IsStatusHolder();
			}
		}
		
		return isStatusHolder;
	}
	
	public boolean hasTransactionFilter_hasDepartment(){
		boolean isFieldDepartmentFound = false;
		
		if(getTableOrTree() != null && getTableOrTree().getFocList() != null){
			FocDesc focDesc = getTableOrTree().getFocList().getFocDesc();
			if(focDesc != null){
						isFieldDepartmentFound = focDesc.getFieldByID(FField.FLD_DEPARTMENT) != null;
			}
		}
		
		return isFieldDepartmentFound;
	}
	
	public boolean hasTransactionFilter(){
		return (hasTransactionFilter_hasDepartment() || hasTransactionFilter_hasStatus()) && getTableTreeDelegate() != null && getTableTreeDelegate().isTransactionFilterEnabled();
	}
	
	public FocDataWrapper getFocDataWrapper(){
		return getTableOrTree() != null ? getTableOrTree().getFocDataWrapper() : null;
	}

	public String getCodeFrom() {
		return codeFrom != null ? codeFrom : "";
	}

	public void setCodeFrom(String codeFrom) {
		this.codeFrom = codeFrom;
	}

	public String getCodeTo() {
		return codeTo != null ? codeTo : "";
	}

	public void setCodeTo(String codeTo) {
		this.codeTo = codeTo;
	}
}