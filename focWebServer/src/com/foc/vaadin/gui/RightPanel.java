package com.foc.vaadin.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.dataModelTree.DataModelNodeList;
import com.foc.desc.dataModelTree.DataModelNodeTree;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.gui.components.FVTree;
import com.foc.vaadin.gui.layouts.FVDeleteLayout;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.windows.CreateFieldWindow;
import com.foc.vaadin.xmleditor.XMLEditor;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.FocRightPanel_Tree;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class RightPanel extends AbsoluteLayout implements IRightPanel {
  
  private FocDesc               focDesc              = null;
  private TabSheet              tabSheet             = null;
  
  private AbsoluteLayout        firstTab             = null;
  private Panel                 treeContainerPanel   = null;
//  private FVTree			          fieldTree            = null;
  
  private FocRightPanel_Tree    rightPanelTree       = null;
  
  private AbsoluteLayout        paletteTab           = null;
  private FVTree                paletteTree          = null;
  private HierarchicalContainer paletteTreeContainer = null;
  
  private Button createField;
  private Button save;
  private Button xmlBtn = new Button("Show XML");
  
  private ICentralPanel centralPanel = null;
  
  public RightPanel(ICentralPanel centralPanel, FocDesc focDesc) {
    super();
    
    setHeight("800px");
    setWidth("280px");
    
    this.centralPanel = centralPanel;
    this.focDesc = focDesc;
    init();

    setLayouts_DragDrop(true);
    
    //setSizeFull();
  }
  
  public void dispose(){
  	setLayouts_DragDrop(false);
    focDesc = null;
    
    createField = null;
    save = null;
//    if(fieldTree != null){
//      fieldTree.dispose();
//      fieldTree = null;
//    }
    
    if(rightPanelTree != null){
    	rightPanelTree.dispose();
    	rightPanelTree = null;
    }
    
    centralPanel = null;
  }
  
  public UI getWindow_ApplicationWindow(){
  	UI window = getUI();
  	if(window.getParent() != null){
  		window = (UI) window.getParent();
  	}
  	return window;
  }
  
  
  public boolean canModifyViews(){
  	/*
  	FocWebVaadinWindow window = null;
  	window.getFocWebSession().getFocUser()
  	return XMLViewDictionary.allowViewModification(FocUser user, centralPanel.getXMLView());
  	*/
  	return true;
  }
  
  public FocDesc getFocDesc(){
    return focDesc;
  }
  
  public ArrayList<FVLayout> getLayoutArray(){
  	return centralPanel != null ? centralPanel.getLayouts() : null;
  }
  
  public void setLayouts_DragDrop(boolean dnd){
    if (getLayoutArray() != null) {
      Random rand = new Random();
      for (FVLayout layout : getLayoutArray()){
        layout.setDragDrop(dnd);
        if(dnd){
//        	layout.addStyleName(FVLayout.RANDOM_BACKGROUND_PREFIX+(rand.nextInt(6)+1));
          layout.addStyleName(FVLayout.BORDER_BACKGROUND_STYLE);
        }else{
        	layout.addStyleName(FVLayout.DEFAULT_STYLE);
        }
      }
    }
  }
  
  public void init() {
    tabSheet = new TabSheet();
    tabSheet.setHeight("800px");
    tabSheet.setWidth("100%");
    addComponent(tabSheet);
    
//    tabSheet.addListener(new TabSheet.SelectedTabChangeListener() {
//      
//      @Override
//      public void selectedTabChange(SelectedTabChangeEvent event) {
//        Component tab = tabSheet.getSelectedTab();
//        
//        if (getLayoutArray() != null) {
//          if (tab.equals(paletteTab)) {
//            for (FVLayout layout : getLayoutArray()){
//              layout.setDragDrop(true);
//            }
//          }
//        }
//      }
//    });
    
    firstTab = new AbsoluteLayout();
    firstTab.setHeight("800px");
    firstTab.setWidth("100%");
    firstTab.setSizeFull();
    firstTab.setCaption("Edit Panel");
    
    paletteTab = new AbsoluteLayout();
    paletteTab.setHeight("800px");
    paletteTab.setWidth("100%");
    paletteTab.setSizeFull();
    paletteTab.setCaption("Palette");
    paletteTreeContainer = new HierarchicalContainer();
    paletteTreeContainer.addContainerProperty("name", String.class, null);
    
    Iterator<String> iter = FVGUIFactory.getInstance().keySet().iterator();
    while(iter != null && iter.hasNext()){
    	String tag = iter.next();
      paletteTreeContainer.addItem(tag);
      paletteTreeContainer.setChildrenAllowed(tag, false);
    }
    /*
    for (int i = 0; i < paletteLabels.length; i++) {
      paletteTreeContainer.addItem(paletteLabels[i]);
      paletteTreeContainer.setChildrenAllowed(paletteLabels[i], false);
    }
    */
    
    paletteTree = new FVTree();
    paletteTree.setContainerDataSource(paletteTreeContainer);
    paletteTab.addComponent(paletteTree);
    
    treeContainerPanel = new Panel();
    treeContainerPanel.setStyleName(Reindeer.PANEL_LIGHT);
    treeContainerPanel.setHeight("400px");
    
    save = new Button();
    save.setIcon(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_SAVE));
    save.setStyleName(BaseTheme.BUTTON_LINK);
    
    createField = new Button();
    createField.setIcon(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_ADD));
    createField.setStyleName(BaseTheme.BUTTON_LINK);
    
//    fieldTree = new FVTree();

    INavigationWindow mainWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
    DataModelNodeTree dataModelNodeTree = new DataModelNodeTree(new DataModelNodeList(getFocDesc(), 3));
    
    XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.RIGHT_PANEL_STORAGE, XMLViewKey.TYPE_TREE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT); 
    setRightPanelTree((FocRightPanel_Tree) XMLViewDictionary.getInstance().newCentralPanel(mainWindow, xmlViewKey, dataModelNodeTree)); 
    
    refreshTree();
    
    tabSheet.addTab(firstTab, "Edit");
    tabSheet.addTab(paletteTab, "Palette");
    
    populate();
  }

  public void refreshTree(){
  	
  	if(getRightPanelTree() != null){
  		getRightPanelTree().markAsDirtyRecursive();
  		getRightPanelTree().refresh();
  	}
//	  DataModelNodeTree tree = new DataModelNodeTree(new DataModelNodeList(getFocDesc(), 3));
	  
//	  FField nameFld = DataModelNodeDesc.getInstance().getFieldByID(FField.FLD_NAME);
	  
//	  fieldTree.setContainerDataSource(tree);
//	  fieldTree.setItemCaptionPropertyId(nameFld.getName());
  }
  
  public String readFromXMLFileStream(){
    String xmlContent = (centralPanel != null && centralPanel.getXMLView() != null) ? centralPanel.getXMLView().getXMLString() : "";
    return xmlContent;
  }
  
  public static void popupXmlEditor(XMLView view, String xmlString){
  	String title = "View: "+view.getXmlViewKey().getUserView(); 
  	String fileName = view.getFullFileName();
  	if(fileName != null && !fileName.isEmpty()){
  		title += " file: "+fileName;
  	}
  	Globals.logString("XML before new XMLEditor="+xmlString);
  	XMLEditor xmlEditor = new XMLEditor(view, title, xmlString);
  	FocWebApplication.getInstanceForThread().addWindow(xmlEditor);
  }
  
  private void populate() {
    xmlBtn.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        FVLayout rootLayout = (getLayoutArray().size() > 0) ? getLayoutArray().get(0) : null; 
        if(rootLayout != null){
          Globals.logString("XML BUTTON "+rootLayout.getXMLType());
        }
//        XMLBuilder builder = new XMLBuilder(rootLayout);
//        getWindow_ApplicationWindow().addWindow(new XMLEditor(centralPanel, centralPanel.getXMLView().getXmlFileName(), readFromXMLFileStream()));
        popupXmlEditor(centralPanel.getXMLView(), readFromXMLFileStream());
      }
    });
    
    save.addClickListener(new Button.ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        for (FVLayout layout : getLayoutArray()) {
          layout.setDragDrop(false);
          layout.setStyleName(FVLayout.DEFAULT_STYLE);
        }
        
//        XMLBuilder builder = new XMLBuilder(getLayoutArray().get(0));
//        XMLView view = centralPanel.getXMLView();
//        view.saveXML(builder.getXMLString());
        XMLView view = centralPanel.getXMLView();
        view.saveXML(readFromXMLFileStream());
        
    		INavigationWindow window = centralPanel.getMainWindow();
    		if(window != null){
    			window.removeUtilityPanel(RightPanel.this);
    		}
      }
    });
    
    createField.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
          CreateFieldWindow createFldWindow = new CreateFieldWindow(getFocDesc());
          getWindow_ApplicationWindow().addWindow(createFldWindow);
        
      }
    });
    
//    treeContainerPanel.setContent(fieldTree);
    if(getRightPanelTree() != null){
    	treeContainerPanel.setContent(getRightPanelTree());
    }
    firstTab.addComponent(treeContainerPanel);
    firstTab.addComponent(createField, "top: 410px;left:20px;");
    firstTab.addComponent(save, "top: 410px;left:75px;");
    firstTab.addComponent(xmlBtn, "top: 450px;left:75px;");
    firstTab.addComponent(new FVDeleteLayout(), "top: 500px;");
    
  }
  
  @Override
	public void refresh() {
		refreshTree();
	}
	
	public void setEditingPermission(int permission) {
		/*
	  if (permission == IRightPanel.PERMISSION_NOTHING) {
	    editLayout.removeAllComponents();
	  } else if (permission == IRightPanel.PERMISSION_SELECT) {
	    editLayout.removeComponent(construct);
	  } else if (permission == IRightPanel.PERMISSION_READ_WRITE) {
	    
	  }
	  */
	}

	public FocRightPanel_Tree getRightPanelTree() {
		return rightPanelTree;
	}

	public void setRightPanelTree(FocRightPanel_Tree rightPanelTree) {
		this.rightPanelTree = rightPanelTree;
	}
}
