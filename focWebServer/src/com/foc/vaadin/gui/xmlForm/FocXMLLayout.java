package com.foc.vaadin.gui.xmlForm;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.access.AccessSubject;
import com.foc.access.FocDataMap;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.workflow.implementation.ILoggable;
import com.foc.business.workflow.implementation.JSONLog;
import com.foc.business.workflow.implementation.LoggableChangeCumulator;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.FocDataResolver_StringConstant;
import com.foc.dataSource.store.DataStore;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FocListFilter;
import com.foc.pivot.FPivotBreakdown;
import com.foc.pivot.FPivotTable;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.property.FTime;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.tree.FTree;
import com.foc.util.ASCII;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FVGUIFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.RightPanel;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.IPivotGrid;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.chart.FVChart;
import com.foc.vaadin.gui.components.chart.FVPivotLayout;
import com.foc.vaadin.gui.components.pivot.FVPivotTable;
import com.foc.vaadin.gui.components.report.FVReport;
import com.foc.vaadin.gui.components.report.FVReportGroup;
import com.foc.vaadin.gui.components.tableAndTree.FVColumnGenerator;
import com.foc.vaadin.gui.layouts.FVChartWrapperLayout;
import com.foc.vaadin.gui.layouts.FVEncapsulatorLayout;
import com.foc.vaadin.gui.layouts.FVHTMLLayout;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVHelpLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVOptionMobileLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FocXMLLayout extends VerticalLayout implements ICentralPanel, IValidationListener, FocXMLGuiComponent, IXMLAttributeResolver {

	public static final String DEFAULT_DIALOG_WIDTH  = "1300px"; 
	public static final String DEFAULT_DIALOG_HEIGHT = "700px";

	private XMLView xmlView = null;
	private int viewRights = GroupXMLViewDesc.ALLOW_CREATION;
	private INavigationWindow mainWindow = null;
	private IFocData focData = null;
	private boolean focDataOwner = false;
	private FocDataDictionary focDataDictionary = null;
	private FValidationSettings validationSettings = null;
	private FVValidationLayout validationLayout = null;
	private Map<String, FocXMLGuiComponent> compMap = null;
	private Stack<Component> stack = null;
	private RightPanel rightPanel = null;
	private boolean reactToGuiChangeDisable = false;
	private boolean enableRightsApplicationToGuiFields = true;
	private FocXMLLayout parentLayout     = null;
	private boolean      commitWithParent = false;
	
	private ArrayList<FocXMLLayout> childLayoutArray = null;
	private boolean editable = true;
	private FocXMLGuiComponentListener valueChangeListener = null;
	private Map<String, DataPathListenerAction> propertyVisibleWhenMap = null;
	private boolean    showValidationLayout = true;
	private String     linkSerialisation    = null;
	private Properties linkSerialisationProperties = null;
	private ITableTree tableTreeThatOpenedThisForm = null;
	private boolean    goBackRequested = false;
	private Stack<com.foc.vaadin.gui.xmlForm.FocXMLLayout.FocXMLHandler.FocElement> focElementStack = null;
	private Attributes attributesOfIncludeNode = null;
	// Is this really important!
	ArrayList<FVLayout> layouts = new ArrayList<FVLayout>();
	
	private boolean immediateComponentAllowed = true;
	private String  screenHelp = null;

	private boolean propertyChangeSuspended = false;
	
	// This is the only constructor that will be called automatically
	// After that the init method will be called
	public FocXMLLayout() {
		setSpacing(false);
		setMargin(false);
		setCaption(null);
//		setHeight("99%");
	}

	public void dispose() {
		if(valueChangeListener != null){
			valueChangeListener.dispose();
			valueChangeListener = null;
		}
		
		dispose_ChildLayout();
    
		xmlView = null;
		mainWindow = null;
		parentLayout = null;
		
		dispose_ValidationSettings();
		
		dispose_ComponentsMap();
		
		if(isFocDataOwner() && focData != null){
			focData.dispose();
		}
		focData = null;
		if(focDataDictionary != null){
			focDataDictionary.dispose();
			focDataDictionary = null;
		}
		if(validationLayout != null){
			validationLayout.dispose();
			validationLayout = null;
		}
		if(stack != null){
			stack.clear();
			stack = null;
		}
		if(focElementStack != null){
			focElementStack.clear();
			focElementStack = null;
		}
		if(rightPanel != null){
			rightPanel.dispose();
			rightPanel = null;
		}
		
		if(propertyVisibleWhenMap != null){
			Iterator<DataPathListenerAction> iter = propertyVisibleWhenMap.values().iterator();
			if(iter != null){
				while(iter.hasNext()){
					DataPathListenerAction data = iter.next();
					if(data != null){
						data.dispose();
					}
				}
				propertyVisibleWhenMap.clear();
				propertyVisibleWhenMap = null;
			}
		}

		if(layouts != null){
			layouts.clear();
			layouts = null;
		}
		
		tableTreeThatOpenedThisForm = null;
		if(linkSerialisationProperties != null){
			linkSerialisationProperties.clear();
			linkSerialisationProperties = null;
		}
		
		attributesOfIncludeNode = null;
	}

	public void dispose_ComponentsMap(){
		if(getComponentMap() != null){
			ArrayList<FocXMLGuiComponent> toDeleteArray = new ArrayList<FocXMLGuiComponent>(); 
			Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
			if(iter != null){
				while(iter.hasNext()){
					FocXMLGuiComponent current = iter.next();
					toDeleteArray.add(current);
				}
			}

			for(FocXMLGuiComponent comp : toDeleteArray){
				comp.dispose();
			}
			
			compMap.clear();
			compMap = null;
		}
	}
	
	public void dispose_ChildLayout(){
	  childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
			public boolean before(FocXMLLayout layout) {
				layout.dispose();
				return true;
			}
			
			public boolean after(FocXMLLayout layout) {
				return false;
			}
		});
	  
	  if(childLayoutArray != null){
	    childLayoutArray.clear();
	    childLayoutArray = null;
	  }
	}
	
	public void dispose_ValidationSettings() {
		if(validationSettings != null){
			validationSettings.dispose();
			validationSettings = null;
		}
	}
	
	public boolean isRTL(){
		return ConfigInfo.isGuiRTL();
	}
	
	public boolean isArabic(){
		return ConfigInfo.isArabic();
	}

	public void setScreenHelp(String screenHelp){
		this.screenHelp = screenHelp;
	}
	
	public String getScreenHelpText(){
		return screenHelp;
	}

	public static FocObject getFocObject(IFocData focData) {
		FocObject focObj = null;
		if(focData instanceof FocObject){
			focObj = (FocObject) focData;
		}else if(focData instanceof FocDataMap){
			focData = ((FocDataMap)focData).getMainFocData();
			if(focData instanceof FocObject){
				focObj = (FocObject) focData;
			}
		}
		return focObj;
	}
	
	public FocObject getFocObject(){
		return getFocObject(getFocData());
	}

	public FocList getFocList(){
		FocList focList = null;
		IFocData focData = getFocData();
		if(focData instanceof FocDataMap){
			focData = ((FocDataMap) focData).getMainFocData();
		}
		
		if(focData instanceof FocListWrapper){
			focList = ((FocListWrapper)focData).getFocList();
		}else if(focData instanceof FocList){
			focList = ((FocList)focData);
		}
		
		return focList;
	}
	
	public FTree getFocTree(){
		FTree fTree = null;
		IFocData focData = getFocData();
		if(focData instanceof FTree){
			fTree = (FTree) focData;
		}else if(focData instanceof FocDataMap){
			focData = ((FocDataMap)focData).getMainFocData();
			if(focData instanceof FTree){
				fTree = (FTree) focData;
			}
		}
		return fTree;
	}
	
	public FocXMLGuiComponentListener getValueChangeListener() {
		if(valueChangeListener == null){
			valueChangeListener = new FocXMLGuiComponentListener();
		}
		return valueChangeListener;
	}

	/*
	 * public ValueChangeListener getFormulaChangeListener(){
	 * if(formulaChangeListener == null){ formulaChangeListener = new
	 * Property.ValueChangeListener() {
	 * 
	 * @Override public void valueChange(ValueChangeEvent event) {
	 * if(isVisibleWhenApplicable()){
	 * 
	 * getValueChangeListener().valueChange(event);
	 * 
	 * Iterator<Component> iter = getComponentMap().values().iterator();
	 * while(iter != null && iter.hasNext()){ Component comp = iter.next();
	 * FocXMLGuiComponent xmlComp = (FocXMLGuiComponent) comp; String expression =
	 * xmlComp.getAttributes().getValue(FXML.ATT_VISIBLE_WHEN); if(expression !=
	 * null && !expression.isEmpty()){ FocSimpleFormulaContext context = null;
	 * if(expression != null && !expression.isEmpty()){ Formula formula = new
	 * Formula(expression); context = new FocSimpleFormulaContext(formula); }
	 * if(context != null){ boolean visible = true; Object valueObj =
	 * context.compute((FocObject)getFocData()); if(valueObj instanceof Boolean){
	 * visible = ((Boolean)valueObj).booleanValue(); }else if(valueObj instanceof
	 * String){ visible = ((String)valueObj).toUpperCase().equals("TRUE"); }
	 * comp.setVisible(visible); } } } } } }; } return formulaChangeListener; }
	 */

	@Override
	public void addComponent(Component c) {
		boolean firstComponent = getComponentCount() == 0;
		super.addComponent(c);
		if(firstComponent) setExpandRatio(c, 1);
	}

	public void setDataToPrintingLayout(FocXMLLayout printingLayout) {
		if(printingLayout != null){
			FocDataDictionary srcDictionary = printingLayout.getFocDataDictionary(false);
			if(srcDictionary != null){
				FocDataDictionary tarDictionary = getFocDataDictionary(true);
				tarDictionary.copy(srcDictionary);
			}
		}
	}

	protected void beforeLayoutConstruction() {
	}
	
	protected void afterLayoutConstruction() {
	}

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		this.mainWindow = window;
		this.xmlView = xmlView;
		setFocData(focData);
		FocObject newObj = getFocObject();
		if(newObj != null){

			if(/*!newObj.workflow_IsAllowModification() ||*/ newObj.focObject_IsLocked()){
				setEditable(false);
			}

			newObj.code_resetIfApplicableAndCreated();
			if(newObj.isCreated() && newObj instanceof IStatusHolder){
				StatusHolder statusHolder = ((IStatusHolder) newObj).getStatusHolder();
				if(statusHolder != null){
					statusHolder.fillCreationInfo();
				}
			}
		}
	}

	@Override
	public void parseXMLAndBuildGui() {
		beforeLayoutConstruction();
		parseXML();
		mapDataPath2ListenerAction_ApplyVisibilityFormulas();
		innerLayout_AfterConstruction();
		afterLayoutConstruction();
	}

	@Override
	public XMLView getXMLView() {
		return xmlView;
	}

	public void setXMLView(XMLView xmlView) {
		this.xmlView = xmlView;
	}

	public boolean isConstructionMode() {
		return getRightPanel(false) != null;
	}

	@Override
	public IRightPanel getRightPanel(boolean createIfNeeded) {
		if(rightPanel == null && createIfNeeded){
			FocDesc focDesc = null;
			if(focData instanceof FocObject){
				focDesc = ((FocObject) focData).getThisFocDesc();
			}else if(focData instanceof FocList){
				focDesc = ((FocList) focData).getFocDesc();
			}else if(focData instanceof FTree){
				FocList list = ((FTree) focData).getFocList();
				focDesc = list != null ? list.getFocDesc() : null;
			}

			rightPanel = new RightPanel(this, focDesc);
			rightPanel.setEditingPermission(xmlView.getViewPermission(FocWebApplication.getFocUser()));
		}
		return rightPanel;
	}

	@Override
	public ArrayList<FVLayout> getLayouts() {
		return layouts;
	}

	@Override
	public INavigationWindow getMainWindow() {
		return mainWindow;
	}

	public INavigationWindow getParentNavigationWindow(){
		INavigationWindow parentNavigationWindow = findAncestor(FocCentralPanel.class);
		if(parentNavigationWindow == null) parentNavigationWindow = getMainWindow();
		return parentNavigationWindow;
	}

	@Override
	public FValidationSettings getValidationSettings(boolean createIfNeeded) {
		if(validationSettings == null && createIfNeeded){
			validationSettings = new FValidationSettings();
		}
		return validationSettings;
	}

	@Override
	public IFocData getFocData() {
		return focData;
	}

	private void parseXML() {
		try{
			XMLView xmlView = getXMLView();
			InputStream xmlFileName = (xmlView != null) ? xmlView.getXMLStream_ForView() : null;
			if(xmlFileName != null){
				if(xmlView != null && xmlView.getFullFileName() != null){
					Globals.logString("Parsing FocXMLLayout Filename : "+xmlView.getFullFileName());
				}
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(xmlFileName, new FocXMLHandler());
			}else{
				Globals.logString("FocXMLLayout parseXML(): Xml View file inputstream = null");
			}
		}catch (Exception e){
			goBack(null);
			Globals.logExceptionWithoutPopup(e);
		}
		refresh();
		/*
		 * Globals.logString(layouts.size()+""); if (layouts.size() != 0)
		 * ((Component) layouts.get(0)).setStyleName("bg-white");
		 */
	}

	public Stack<Component> getStack() {
		if(stack == null){
			stack = new Stack<Component>();
		}
		return stack;
	}
	
	public Stack<com.foc.vaadin.gui.xmlForm.FocXMLLayout.FocXMLHandler.FocElement> getFocElementStack() {
		if(focElementStack == null){
			focElementStack = new Stack<com.foc.vaadin.gui.xmlForm.FocXMLLayout.FocXMLHandler.FocElement>();
		}
		return focElementStack;
	}
	
	public FVLayout getCurrentLayout() {
		FVLayout layout = null;
		if(!getStack().empty()){
			layout = (FVLayout) getStack().peek();
		}
		return layout;
	}

	public Iterator<FocXMLGuiComponent> getComponentMapIterator(){
		Iterator<FocXMLGuiComponent> iter = getComponentMap() != null && getComponentMap().values() != null ? getComponentMap().values().iterator() : null;
		return iter;
	}
	
	public void removeComponentByName_StartWith(String name) {
		ArrayList<String> arrayOfCompToRemove = null;
		Iterator<String> iter = getComponentMap().keySet().iterator();
		if(iter != null){
			while(iter.hasNext()){
				String componentKey = iter.next();
				if(componentKey != null && componentKey.startsWith(name)){
					if(arrayOfCompToRemove == null) arrayOfCompToRemove = new ArrayList<String>();
					arrayOfCompToRemove.add(componentKey);
				}
			}
		}
		
		if(arrayOfCompToRemove != null){
			for(int i=0; i<arrayOfCompToRemove.size(); i++){
				getComponentMap().remove(arrayOfCompToRemove.get(i));
			}
		}
	}
	
	public FocXMLGuiComponent removeComponentByName(String name) {
		return getComponentMap().remove(name);
	}
	
	public Component getComponentByName(String name) {
		Component comp = (Component) getComponentMap().get(name);		
		if(comp == null){
			for(int i = 0; i < childXMLLayoutArray_Size() && comp == null; i++){
				comp = childLayoutArray.get(i).getComponentByName(name);
			}
		}
		/*
		if(comp == null){
			Globals.logString("Could not find component : "+name);
			Globals.logString("Instead we have : ");
			debug_PrintoutAllComponents();
		}
		*/
		return comp;
	}
	
	public void debug_PrintoutAllComponents(){
		Map map = getComponentMap();
		if(map != null){
			Iterator iter = map.keySet().iterator();
			while(iter != null && iter.hasNext()){
				String key = (String) iter.next();
				Globals.logString(key);
			}
			for(int i = 0; i < childXMLLayoutArray_Size(); i++){
				FocXMLLayout lay = childXMLLayoutArray_Get(i);
				if(lay != null) lay.debug_PrintoutAllComponents();
			}
		}
	}
	
	public String putComponent(String name, Component component) {
		if(name == null){
			name = ASCII.generateRandomString(10);
		}else{
			FocXMLGuiComponent oldGuiComponent = (FocXMLGuiComponent) getComponentByName(name);
			if(oldGuiComponent != null){
				oldGuiComponent.dispose();
			}
		}		
		
		getComponentMap().put(name, (FocXMLGuiComponent) component);
		if(component != null){
			((FocXMLGuiComponent) component).getDelegate().setNameInMap(name);
		}
		if(component != null && ConfigInfo.isUnitDevMode() && component instanceof AbstractComponent){
			if(component instanceof FVWrapperLayout){
				AbstractComponent field = (AbstractComponent) ((FVWrapperLayout) component).getFormField();
				if(field != null)
					field.setDescription("Name : " + name);
			}else{
				((AbstractComponent) component).setDescription("Name : " + name);
			}
		}
		return name;
	}
	
	public void removeComponent(String name) {
		getComponentMap().remove(name);
	}
	
	private Map<String, FocXMLGuiComponent> getComponentMap() {
		if(compMap == null){
			compMap = new HashMap<String, FocXMLGuiComponent>();
		}
		return compMap;
	}

	private FVLayout addComponentToStack(Component component, String name, FocXMLAttributes attributes) {
		Component parent = null;
		if(component != null){
			if(!getStack().empty()){
				parent = (Component) getStack().peek();
			}else{
				addComponent(component);// In this case this component is the root
				if(attributes != null && attributes.getValue(FXML.ATT_ALIGNMENT) != null && !attributes.getValue(FXML.ATT_ALIGNMENT).isEmpty()){
					String value = attributes.getValue(FXML.ATT_ALIGNMENT);
					FocXMLGuiComponentStatic.applyAlignment(this, component, value);
				}else{
					setComponentAlignment(component, Alignment.TOP_CENTER);
				}
			}
		}

		getStack().push(component);
		return (FVLayout) parent;
	}
	
	protected void component_Added(FocXMLGuiComponent component){
		
	}

	private void addComponentToGuiAndMap_AndStack(Component component, String name, FocXMLAttributes attributes) {
		FocXMLAttributes impl = new FocXMLAttributes(this, attributes);
		FVLayout parentLayout = addComponentToStack(component, name, impl);

		if(component != null){
			addComponentToGuiAndMap(component, name, impl, (FVLayout) parentLayout);
		}
	}

	protected void addComponentToGuiAndMap(Component component, String name, AttributesImpl attributes, FVLayout parent) {
		if(component != null){
			FocXMLGuiComponent xmlGuiComp = (FocXMLGuiComponent) component;
			xmlGuiComp.setAttributes(attributes);
			// FVGuiComponent.applyCommonAttributes(component, attributes);

			name = putComponent(name, component);
			
			if(component instanceof FVLayout){
				if(layouts.size() == 0){
					String heightAttrib = attributes.getValue(FXML.ATT_HEIGHT);
					if(heightAttrib != null && !heightAttrib.isEmpty()){
						setHeight(heightAttrib);
					}
				}
				layouts.add((FVLayout) component);
			}

			if(parent != null && component != null){
				parent.addComponent(component, attributes);
				xmlGuiComp.getDelegate().setParentComponent(parent);
				if(attributes != null){
					String expandRatio = attributes.getValue(FXML.ATT_EXPAND_RATIO);
					if(expandRatio != null){
						try{
							float expandRatioFloat = Float.valueOf(expandRatio);
							((AbstractOrderedLayout) parent).setExpandRatio(component, expandRatioFloat);
						}catch (Exception e){
							Globals.logException(e);
						}
					}else if(attributes.getIndex(FXML.ATT_IDX) != -1){
						attributes.removeAttribute(attributes.getIndex(FXML.ATT_IDX));
					}
				}
			}

			if(name != null && !name.isEmpty()){
				createVisibleWhenFormulaContextIfNeeded(name, (FocXMLGuiComponent) component);
			}

			addListenerToField_AndSetRequired((FocXMLGuiComponent) component);
			component_Added((FocXMLGuiComponent) component);
		}
	}

	public Component newFilterConditionGuiField(DefaultHandler handler, FocListFilter listFilter, String dataPath, FocXMLAttributes attributes){
		Component comp = null;
		if(listFilter != null && listFilter.getThisFilterDesc() != null && !Utils.isStringEmpty(dataPath)){
			FilterCondition filterCond = listFilter.getThisFilterDesc().findConditionByFieldPrefix(dataPath);
			if(filterCond != null){
				FocXMLFilterConditionBuilder.addConditionComponents(handler, this, listFilter, filterCond, attributes);
			}
		}
		return comp;
	}
	
	public Component newGuiField(FVLayout layout, String name, String dataPath, FocXMLAttributes attributes) {
		return newGuiField(layout, null, name, null, dataPath, null, attributes);
	}

	// This call is done from Table
	public Component newGuiField(String name, IFocData rootDataPath, String dataPath, FProperty property, FocXMLAttributes attributes) {
		if(attributes.getValue(FXML.ATT_GEAR_ENABLED) == null){
			attributes.addAttribute(FXML.ATT_GEAR_ENABLED, "false");
		}
		if(!Globals.isValo() /*|| Globals.getApp().isUnitTest()*/){
			if(attributes.getValue(FXML.ATT_USE_POPUP_VIEW) == null){
				attributes.addAttribute(FXML.ATT_USE_POPUP_VIEW, "true");
			}
		}
		return newGuiField(null, null, name, rootDataPath, dataPath, property, attributes);
	}

	/**
	 * 
	 * @param layout
	 *          The container layout. Not applicable when called from a Table or
	 *          Tree
	 * @param xmlTagKey
	 *          Allows to get the GuiComponentCreator from the factory. When null,
	 *          will be re composed from the IFocData which should be a FProperty
	 * @param name
	 * @param rootFocData
	 *          the starting point to use with dataPath to get to the IFocData
	 * @param dataPath
	 *          the dataPath to get to the IFocData starting from the rootFocData
	 * @param focData
	 *          If null can be fetched from the dataPath and rootFocData
	 * @param attributes
	 * @return
	 */
	public Component newGuiField(FVLayout layout, String xmlTagKey,// Gets the
																																	// ComponentCreator
																																	// from
																																	// factory (If
																																	// null will
																																	// be fetched
																																	// by the )
			String name, IFocData rootFocData,// the starting point to use with
																				// dataPath to get to the IFocData
			String dataPath,// the dataPath to get to the IFocData starting from the
											// rootFocData
			IFocData focData,// If null can be fetched from the dataPath and
												// rootFocData
			FocXMLAttributes attributes) {

		AbstractComponent component = null;

		if(rootFocData == null){
			rootFocData = getFocData();
		}

		if(focData == null && rootFocData != null && dataPath != null && !dataPath.isEmpty()){
			focData = getDataByPath(rootFocData, dataPath);
		}

		if(xmlTagKey == null || xmlTagKey.isEmpty()){
			xmlTagKey = FVGUIFactory.getInstance().getKeyForProperty(focData);
		}

		FocXMLGuiComponentCreator vField = xmlTagKey != null ? FVGUIFactory.getInstance().get(xmlTagKey) : null;
		if(vField == null){
			if(xmlTagKey != null){
				Globals.showNotification("Tag not recognized", "" + xmlTagKey, FocWebEnvironment.TYPE_ERROR_MESSAGE);
			}else{
				// In this case the tag is not wrong it is null Then maybe the dataPath
				// was wrong
				String value = attributes != null ? attributes.getValue(FXML.ATT_MASK_DATA_FOUND_ERROR) : null;
				if(value == null || (!value.equalsIgnoreCase("true") && !value.equals("1"))){
					if(focData == null && dataPath != null && !dataPath.isEmpty()){
						if(dataPath.contains("[")){
							//When the path contains an array item we do not popup an error because it is possible to have an empty list.
							//This was added when we encountered the Signature[1] case. The Document might not be signed yet
						}else{
							Globals.showNotification("Data not found", "  for dataPath: " + dataPath + "", FocWebEnvironment.TYPE_ERROR_MESSAGE);
							if(ConfigInfo.isDebugMode()){
								debug_PrintFieldNames();
							}
						}
					}else if(focData == null){
						Globals.showNotification("Data not found", " because datapath is null or empty", FocWebEnvironment.TYPE_ERROR_MESSAGE);
					}else{
						Globals.showNotification("Tag not found (Data found)", " dataPath: " + dataPath + "", FocWebEnvironment.TYPE_ERROR_MESSAGE);
					}
				}
			}
		}else{
			IFocData iFocData_SentToObject = focData;
			if(iFocData_SentToObject instanceof FField)
				iFocData_SentToObject = null;
			FocXMLGuiComponent guiComponent = null;

			FProperty property = null;
			IFocData propFocData = rootFocData.iFocData_getDataByPath(dataPath);
			if(propFocData instanceof FProperty){
				property = (FProperty) propFocData;
			}

			guiComponent = vField.newGuiComponent(FocXMLLayout.this, iFocData_SentToObject, attributes, rootFocData, dataPath);
			
			// If the dataPath contains . then this means that the component needs to
			// recall the setDataPath when some properties change.
			// ---------------------------------------------------------------------------------------------------
			if(dataPath != null && dataPath.contains(".")){
				int index = dataPath.indexOf(".");
				String subDataPath = dataPath.substring(0, index);
				mapDataPath2ListenerAction_PutGuiComponentWithDataPath(subDataPath, guiComponent);
			}
			
			// If any attribute contains a $F{ this means that they are dependent on
			// some field that might be on screen.
			// ---------------------------------------------------------------------------------------------------
			if(attributes != null){
				for(int i = 0; i < attributes.getLength(); i++){
					ArrayList<String> arrayOfFields = attributes.getArrayOfFieldsUsed(i);
					if(arrayOfFields != null){
						for(int idx = 0; idx < arrayOfFields.size(); idx++){
							String valueDataPath = arrayOfFields.get(idx);
							if(valueDataPath != null && valueDataPath.contains(".")){
								int index = valueDataPath.indexOf(".");
								String subDataPath = valueDataPath.substring(0, index);
								mapDataPath2ListenerAction_PutGuiComponentWithDataPath(subDataPath, guiComponent);
							}
						}
					}
				}
			}
			
			component = (AbstractComponent) guiComponent;

			if(guiComponent != null){
				guiComponent.copyMemoryToGui();

				addComponentToGuiAndMap(component, name, attributes, layout);
				
  			//hadi18032016
//				if(guiComponent != null && ConfigInfo.isContextHelpActive() && guiComponent.getAttributes() != null && guiComponent.getAttributes().getValue(FXML.ATT_HELP) != null){
//					getContextHelpComponentsList(true).add(guiComponent);
//				}
				//hadi18032016
			}

			if(isEnableRightsApplicationToGuiFields() && !guiComponent.getDelegate().isEditable()){
				if(guiComponent.getFormField() != null && !(guiComponent instanceof FVTableWrapperLayout)){
					guiComponent.getFormField().setEnabled(false);
				}
			}

		}
		return component;
	}

	public void setImmediateComponentAllowed(boolean immediateComponentAllowed){
		this.immediateComponentAllowed = immediateComponentAllowed;
	}
	
	public boolean isImmediateComponentAllowed(){
		return immediateComponentAllowed;
	}
	
	protected boolean field_IsImmediate(FocXMLGuiComponent guiComponent, IFocData focData) {
		boolean shouldBeImmediate = false;

		if(guiComponent != null){
			shouldBeImmediate = isComponentSetImmediate(guiComponent);
		
			if(!shouldBeImmediate){
				if(focData != null && focData instanceof FProperty){
					FProperty property = (FProperty) focData;
					if(			property.hasListeners() 
							|| 	mapDataPath2ListenerAction_GetListenerAction(guiComponent) != null
							|| 	(property.getFocField() != null && property.getFocField().getPropertyValidator() != null
							||  property instanceof FTime)//We always listen immediately to time in case they type with bad format 
							){
						shouldBeImmediate = true;
					}
				}
			}
		}
		
		return shouldBeImmediate;
	}
	
	public void addListenerToField_AndSetRequired(FocXMLGuiComponent guiComponent) {
		if(guiComponent != null){
			IFocData focData = guiComponent.getFocData();
			
			//Setting the field as immediate
			//------------------------------
			boolean shouldBeImmediate = false;
			
			if(isImmediateComponentAllowed()){
				shouldBeImmediate = field_IsImmediate(guiComponent, focData);
			}
			  
			if(shouldBeImmediate){
				addFieldToValueChangeListener(guiComponent);
			}
			
			//Setting the field as mandatory = Required
			//-----------------------------------------
			if(focData != null && focData instanceof FProperty){
				FProperty property = (FProperty) focData;
				FField field = property.getFocField();
				if(field != null && guiComponent.getFormField() != null){
					guiComponent.getFormField().setRequired(field.isMandatory());
				}
			}
		}
	}

	public void addFieldToValueChangeListener(FocXMLGuiComponent guiComponent){
		Field fieldToListenTo = guiComponent.getFormField();
		if(fieldToListenTo != null && fieldToListenTo instanceof AbstractComponent){
			getValueChangeListener().addField(fieldToListenTo);	
		}				
	}
	
	private boolean isComponentSetImmediate(FocXMLGuiComponent guiComp) {
		boolean hasTheSetImmediateAttribute = false;
		Attributes attr = guiComp.getAttributes();
		if(attr != null){
			String value = attr.getValue(FXML.ATT_IMMEDIATE);
			if(value != null && !value.isEmpty()){
				if(value.equals("true")){
					hasTheSetImmediateAttribute = true;
				}
			}
		}
		return hasTheSetImmediateAttribute;
	}

	public Component newGuiPaletteComponent(FVLayout layout, String compTypeName, String mapName, String dataPath, IFocData focData, Attributes attributes) {
		Component component = null;

		AttributesImpl impl = attributes != null ? new FocXMLAttributes(this, attributes) : null;

		FocXMLGuiComponentCreator guiCreator = FVGUIFactory.getInstance().get(compTypeName);
		if(guiCreator != null){
			FocXMLGuiComponent guiComp = guiCreator.newGuiComponent(FocXMLLayout.this, focData, impl, getFocData(), dataPath);
			component = (Component) guiComp;
		}else{
			Globals.showNotification("Gui XML Tag unknown", "" + compTypeName, FocWebEnvironment.TYPE_ERROR_MESSAGE);
		}
		addComponentToGuiAndMap(component, mapName, impl, layout);

		return component;
	}

	public Iterator<FocXMLGuiComponent> getXMLComponentIterator(){
		return getComponentMap() != null ? getComponentMap().values().iterator() : null;
	}
	
	@Override
	public void refresh() {
		if(getComponentMap() != null){
			ArrayList<FVTableWrapperLayout> tableWrappers = null;
			
			Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
			while (iter != null && iter.hasNext()){
				FocXMLGuiComponent component = iter.next();
				if(component != null && component instanceof FVTableWrapperLayout){
					if(tableWrappers == null) tableWrappers = new ArrayList<FVTableWrapperLayout>();
					tableWrappers.add((FVTableWrapperLayout) component);
				}
				//Might cause some performance issues
				else if(component instanceof FocXMLLayout){
					FocXMLLayout layout = (FocXMLLayout) component;
					layout.refresh();
				}
			}
			
			if(tableWrappers != null){
				for(int i=0; i<tableWrappers.size(); i++){
					tableWrappers.get(i).refresh();
				}
			}
		}

		if(getValidationLayout() != null){
			getValidationLayout().refreshFullScreenIcon();
		}

		markAsDirtyRecursive();
	}

	@Override
	public void beforeViewChangeListenerFired(){
		IFocData iFocData = getFocData();
		if(iFocData != null && iFocData instanceof FocDataWrapper){
			FocDataWrapper dataWrapper = (FocDataWrapper) iFocData;
			dataWrapper.removeFilter_FocXMLLayout();
		}
	}
	
	public void re_parseXMLAndBuildGui() {
		removeAllComponents();
		dispose_ChildLayout();
		dispose_ComponentsMap();
		parseXMLAndBuildGui();
		if(validationLayout != null){
			if(Globals.isValo()){
				FocWebApplication focWebApplication = findAncestor(FocWebApplication.class);
				if(focWebApplication != null){
					focWebApplication.replaceFooterLayout(validationLayout);
				}else{
					addComponent(validationLayout);
					setComponentAlignment(validationLayout, Alignment.MIDDLE_CENTER);
				}
			}else{
				addComponentAsFirst(validationLayout);
				setComponentAlignment(validationLayout, Alignment.MIDDLE_CENTER);
			}
		}		
		/*
		if(getCentralPanel() != null){		
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), getXMLView().getXmlViewKey(), getFocData());
			getCentralPanel().changeCentralPanelContent(centralPanel, false);
		}else{
			Globals.logString("Could not FocXMLLayout.re_parseXMLAndBuildGui() because no entral Panel!!!!!!!");
		}
		*/
	}

	@Override
	public void validationDiscard(FVValidationLayout validationLayout) {
		if(focData != null && (getValidationSettings(false) == null || getValidationSettings(false).isCommitData())){
			focData.iFocData_cancel();
		}
		//Propagating the Validation Actions to child layouts if linked...
		for(int i=0; i<childXMLLayoutArray_Size(); i++){
			FocXMLLayout layout = childXMLLayoutArray_Get(i);
			if(layout.isCommitWithParent()){
				layout.validationDiscard(validationLayout); 
			}
		}
	}

	public void popupMessageForComponent(FocXMLGuiComponent comp) {
		if(comp != null){
			IFocData focData = comp.getFocData();
			if(focData != null && focData instanceof FProperty){
				FProperty prop = (FProperty) focData;
				if(prop != null){
					FField fld = prop.getFocField();

					String caption = comp.getDelegate().getCaptionFromAttributes();
					if(Utils.isStringEmpty(caption)){
						caption = fld.getTitle();
						if(Utils.isStringEmpty(caption)){
							caption = fld.getName();
						}
					}
					String title = "Alert";
					String message = "Please enter data for : " + caption;
					if (isArabic()) {
						title = "تنبيه";
						message = "الرجاء إدخال المعلومات التالية" + " : " + caption;
					}
					
					OptionDialog dialog = new OptionDialog(title, message) {
						@Override
						public boolean executeOption(String optionName) {
							return false;
						}
					};
					dialog.addOption("OK", isArabic() ? "نعم" : "Ok");
					if(Globals.getApp().isUnitTest()){
						FocUnitDictionary.getInstance().getLogger().addInfo("Dialog popup: "+title+" - "+message);
					}
					dialog.popup();
				}
			}
		}
	}
	
	public boolean checkMandatoryGuiFieldsAreFilled(){
		boolean error = false;
		
		//Even though we have a data valildation check in the data layer or FocObject, we are doing a check on the GUI component
		//To be able to give a meaningful message dependent also on the FocXMLLayout language
		Iterator<FocXMLGuiComponent> iter = getComponentMapIterator();
		while(iter != null && iter.hasNext()){
			FocXMLGuiComponent comp = iter.next();
			if(comp != null){
				IFocData focData = comp.getFocData();
				if(focData != null && focData instanceof FProperty){
					FProperty prop = (FProperty) focData;
					if(prop != null){
						FField fld = prop.getFocField();
						FocObject focObj = prop.getFocObject();
						if(fld != null && focObj != null && fld.isMandatory() && !focObj.isDeleted()){
							boolean valid = focObj.isPropertyDataValid(fld, prop);
							if(!valid){
								popupMessageForComponent(comp);
								error = true;
								break;
							}
						}
					}
				}
			}
		}

		return error;
	}
	
	public boolean validateDataBeforeCommit(FVValidationLayout validationLayout){
		boolean error = checkMandatoryGuiFieldsAreFilled();
		return error;
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = copyGuiToMemory();
		if(!error && (getValidationSettings(false) == null || getValidationSettings(false).isCommitData())){
			//We should check all before starting to save
			if(!error) error = innerLayout_CommitOrCheckData(false);
			if(!error) error = validateDataBeforeCommit(validationLayout);
		}
		
		//Propagating the Validation Actions to child layouts if linked...
		for(int i=0; i<childXMLLayoutArray_Size() && !error; i++){
			FocXMLLayout layout = childXMLLayoutArray_Get(i);
			if(layout.isCommitWithParent()){
				error = layout.validationCheckData(validationLayout); 
			}
		}
		
		return error;
	}

	private ILoggable pushLogInfoForLoggable() {
		ILoggable loggable = null;
		AccessSubject accessSubject = (focData != null && focData instanceof AccessSubject) ? (AccessSubject)focData : null ; 
		while(accessSubject != null && loggable == null) {
			if(accessSubject instanceof FocObject) {
				loggable = (ILoggable) ((FocObject) accessSubject).workflow_GetFirstFatherLoggable();
			}
			accessSubject = accessSubject.getFatherSubject();
		}
		
		if(loggable != null) {
  		LoggableChangeCumulator logger = LoggableChangeCumulator.getInstanceForThread();
  		JSONLog log = logger.get(loggable);
  		if(log != null) {
  			loggable = null;
  		} else {
    		logger.push(loggable);
  		}
		}
		return loggable; 
	}
	
	private void consumeLogInfoForLoggable(ILoggable loggable) {
		if(loggable != null) {
			LoggableChangeCumulator logCumulator = LoggableChangeCumulator.getInstanceForThread();
			if(logCumulator != null) logCumulator.insertLogLine_IfNotInsertedYet(loggable);
		}
	}
	
	@Override
	public boolean validationCommit(FVValidationLayout validationLayout) {
		boolean error = false;
		if(!error && (getValidationSettings(false) == null || getValidationSettings(false).isCommitData())){
			ILoggable loggable = pushLogInfoForLoggable();
			
			if(!error) error = innerLayout_CommitOrCheckData(true);
			if(!error && focData != null){
				error = focData.iFocData_validate();
				if(!error) {
					consumeLogInfoForLoggable(loggable);
				}
			}
		}
		
		if(!error) {
			//Propagating the Validation Actions to child layouts if linked...
			for(int i=0; i<childXMLLayoutArray_Size(); i++){
				FocXMLLayout layout = childXMLLayoutArray_Get(i);
				if(layout.isCommitWithParent()){
					error = layout.validationCommit(validationLayout); 
				}
			}
		}
		
		return error;
	}
	
	@Override
	public void validationAfter(FVValidationLayout validationLayout, boolean commited) {

		//Propagating the Validation Actions to child layouts if linked...
		for(int i=0; i<childXMLLayoutArray_Size(); i++){
			FocXMLLayout layout = childXMLLayoutArray_Get(i);
			if(layout.isCommitWithParent()){
				layout.validationAfter(validationLayout, commited); 
			}
		}
	}

	@Override
	public boolean copyGuiToMemory() {
		return copyGuiToMemory(null, null);
	}

	private boolean copyGuiToMemoryForASingleComponent(FocXMLGuiComponent obj) {
		boolean error = false;
		if(obj != null && obj.getFocData() instanceof FProperty){
			if(ConfigInfo.comboBoxShowDropDownEvenWhenDisabled() && !obj.getDelegate().isEditable()){
				obj.copyMemoryToGui();
				if(obj.getFormField() != null) obj.getFormField().markAsDirty();
			}else{
				FProperty property = (FProperty) obj.getFocData();
	
				if(property != null){
					boolean backup = property.isDesactivateListeners();
					property.setDesactivateListeners(true);
					error = obj.copyGuiToMemory();
					property.setDesactivateListeners(backup);
				}else{
					error = obj.copyGuiToMemory();
				}
			}
		}
		return error;
	}

	/*
	public FVLayout searchLayoutsByNameAttribute(String name){
		FVLayout layout = null;
		if(!Utils.isStringEmpty(name)){
			String myName = getAttributes() != null ? getAttributes().getValue(FXML.ATT_NAME) : null;
			if(myName != null && myName.equals(name)){
				layout = this;
			}else{
				for(int i = 0; i < childXMLLayoutArray_Size(); i++){
					FocXMLLayout child = childXMLLayoutArray_Get(i);
					if(child != null){
						layout = child.searchLayoutsByNameAttribute(myName);
						layout.getAttributes()
					}
				}
			}
		}
		return layout;
	}
	*/
	
	public void scanLayoutsAndShowViewValidationLayouts() {
		for(int i = 0; i < childXMLLayoutArray_Size(); i++){
			FocXMLLayout child = childXMLLayoutArray_Get(i);
			if(child != null){
				child.showValidationLayout(true);
				child.scanLayoutsAndShowViewValidationLayouts();
			}
		}
	}

	private void scanAllComponentsAndAddToHelpContext_Internal(final FVHelpLayout helpLayout) {
		try{
			Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
			if(iter != null){
				while(iter.hasNext()){
	  			FocXMLGuiComponent focXMLGuiComponent = iter.next();
  				if(focXMLGuiComponent != null && focXMLGuiComponent.getAttributes() != null){
  					String help = focXMLGuiComponent.getAttributes().getValue(FXML.ATT_HELP);
  					if(help != null){
  						helpLayout.addComponentToContextHelp(focXMLGuiComponent);
//  						contextHelpComponentsList.add(focXMLGuiComponent);
  					}
  				}
				}
			}
			
			childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
				@Override
				public boolean before(FocXMLLayout layout) {
					layout.scanAllComponentsAndAddToHelpContext_Internal(helpLayout);
					return false;
				}
	
				@Override
				public boolean after(FocXMLLayout layout) {
					return false;
				}
			});
		}catch(Exception e){
			Globals.logString("EVERPRO EXCEPTION CAUGHT AT : FocXMLLayout.scanAllComponentsAndExpandAllTrees_LocalLayout()");
			Globals.logException(e);
		}
	}
						
	public void scanComponentsAndAddToHelpContext(final FVHelpLayout helpLayout) {
		if(getParentLayout() != null){
			getParentLayout().scanComponentsAndAddToHelpContext(helpLayout);
		}else{
			scanAllComponentsAndAddToHelpContext_Internal(helpLayout);
		}
	}
	
	private void scanAllComponentsAndExpandAllTrees_LocalLayout() {
		try{
			Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
			if(iter != null){
				while(iter.hasNext()){
					Component comp = (Component) iter.next();
					if(comp != null && comp instanceof FVTableWrapperLayout){
						ITableTree tableOrTree = ((FVTableWrapperLayout)comp).getTableOrTree();
						if(tableOrTree != null && tableOrTree instanceof FVTreeTable){
							((FVTreeTable)tableOrTree).expandCollapseNodes(true);
						}
						tableOrTree.getTableTreeDelegate().fitTreeToMax();
					}
				}
			}
		}catch(Exception e){
			Globals.logString("EVERPRO EXCEPTION CAUGHT AT : FocXMLLayout.scanAllComponentsAndExpandAllTrees_LocalLayout()");
			Globals.logException(e);
		}
	}
						
	public void scanComponentsAndExpandAllTrees() {
		scanAllComponentsAndExpandAllTrees_LocalLayout();
		childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
			@Override
			public boolean before(FocXMLLayout layout) {
				layout.scanComponentsAndExpandAllTrees();
				return false;
			}

			@Override
			public boolean after(FocXMLLayout layout) {
				return false;
			}
		});
	}
	
	private boolean scanComponentsAndcopyGuiToMemory() {
		boolean error = false;
		if(getParentLayout() != null){
			error = getParentLayout().scanComponentsAndcopyGuiToMemory();
		}else{
			error = scanComponentsAndcopyGuiToMemory_Local();
			childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
				@Override
				public boolean before(FocXMLLayout layout) {
					layout.scanComponentsAndcopyGuiToMemory_Local();
					return false;
				}

				@Override
				public boolean after(FocXMLLayout layout) {
					return false;
				}
			});
		}
		return error;
	}

	private boolean scanComponentsAndcopyGuiToMemory_Local() {
		boolean error = false;
		try{
			Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
			while (iter != null && iter.hasNext()){
				Component comp = (Component) iter.next();
				if(comp.isVisible() && comp instanceof FocXMLGuiComponent){
					FocXMLGuiComponent obj = (FocXMLGuiComponent) comp;
					if(obj.getFocData() instanceof FProperty){
						boolean localError = copyGuiToMemoryForASingleComponent(obj);
						error = error || localError;
					}
				}
			}
		}catch(Exception e){
			Globals.logException(e);
		}
		return error;
	}

  public boolean propertyChangeIntention(FocObject focObject, FProperty property, Object before, String after, FocXMLGuiComponent componentModified) {
  	return false;
  }
  
  public boolean isPropertyChangeSuspended() {
  	boolean isSus = propertyChangeSuspended;
  	
		for(int i = 0; !isSus && i < childXMLLayoutArray_Size(); i++){
			FocXMLLayout layout = childXMLLayoutArray_Get(i);
			isSus = layout.isPropertyChangeSuspended();
		}
  	
  	return isSus;
  }
  
	public boolean propertyChangeIntention_Accepted(FocXMLGuiComponent componentModified, FProperty propertyOfEvent) {
		boolean error = false;
		
		try{
			propertyChangeSuspended = false;
			error = scanComponentsAndcopyGuiToMemory();
			// Because if we have 2 components CODE and NAME for the same datapath
			// PARTY or JOB...
			// the copyGuiToMemory should take into account the modified one at last
			// and not the other,
			// Otherwise we will have the last one that comes in the iteration foring
			// its value even though we modified the first one.
			boolean locError = copyGuiToMemoryForASingleComponent(componentModified);
			error = error || locError;

			if(propertyOfEvent != null && !propertyOfEvent.isDesactivateListeners()){
				propertyOfEvent.notifyListeners(true);
			}

			if(componentModified != null){
				DataPathListenerAction frmulasArray = mapDataPath2ListenerAction_GetListenerAction(componentModified);
				if(frmulasArray != null){
					frmulasArray.applyFormulasOfAllContexts();
					frmulasArray.resetPropertyObjects(getFocData());
				}
			}else{
				mapDataPath2ListenerAction_ApplyVisibilityFormulas();
			}
			
			copyMemoryToGui();
		}catch (Exception e){
			Globals.logException(e);
			error = true;
		}
		return error;
	}
	
	public void propertyChangeIntention_Rejected(FocXMLGuiComponent componentModified, FProperty propertyOfEvent) {
		propertyChangeSuspended = false;
		if(componentModified != null) {
			boolean backup = setReactToGuiChangeDisable(true);
			componentModified.copyMemoryToGui();
			setReactToGuiChangeDisable(backup);
		}
	}
	
	public boolean copyGuiToMemory(FocXMLGuiComponent componentModified, FProperty propertyOfEvent) {
		boolean error = false;
	
		try{
			//PropertyChangeIntention
			//In This case we want to make sure that FocObject Allows the Change before triggering any listeners
			if(!propertyChangeSuspended) {
				if(propertyOfEvent != null && componentModified != null) {
					Object valueBefore = propertyOfEvent != null ? propertyOfEvent.getObject() : null;
					String valueAfter  = componentModified.getValueString(); 
					if(!error && propertyOfEvent != null) {
						FocObject fatherObj = propertyOfEvent.getFocObject();
						propertyChangeSuspended = propertyChangeIntention(fatherObj, propertyOfEvent, valueBefore, valueAfter, componentModified);
					}
				}
			}
			
			if(propertyChangeSuspended) {
				error = true;
			} else {
				error = propertyChangeIntention_Accepted(componentModified, propertyOfEvent);
			}
		}catch (Exception e){
			Globals.logException(e);
			error = true;
		}
		return error;
	}

	public void copyMemoryToGui() {
		if(getParentLayout() != null){
			getParentLayout().copyMemoryToGui();
		}else{
			copyMemoryToGui_Local();
			childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
				@Override
				public boolean before(FocXMLLayout layout) {
					layout.copyMemoryToGui_Local();
					return false;
				}

				@Override
				public boolean after(FocXMLLayout layout) {
					return false;
				}
			});
		}
	}

	protected void copyMemoryToGui_Local() {
		setReactToGuiChangeDisable(true);
		Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
		while (iter != null && iter.hasNext()){
			FocXMLGuiComponent obj = iter.next();
			if(obj.getDelegate() != null){
				IFocData prevFocData = obj.getFocData();
				IFocData newFocData = obj.getDelegate().refreshFocData();// ATTENTION This should not be
																						// called systematically
				//If it is an Encapsulator and Data (FProperty) has changed, this might mean that the component needs to be changed.
				if(obj instanceof FVEncapsulatorLayout && prevFocData != newFocData){
					FVEncapsulatorLayout encapsulator = (FVEncapsulatorLayout) obj;
					String xmlTagKey = FVGUIFactory.getInstance().getKeyForProperty(newFocData);
					if(xmlTagKey != null && !xmlTagKey.equals(obj.getXMLType())){
						FocXMLAttributes atribs = new FocXMLAttributes(FocXMLLayout.this, obj.getAttributes());
						atribs.addAttribute(FXML.ATT_ENCAPSULATE, "false");
						atribs.removeAttribute(FXML.ATT_CAPTION);
						atribs.addAttribute(FXML.ATT_IMMEDIATE, "true");
						
						FocXMLGuiComponentCreator vField = xmlTagKey != null ? FVGUIFactory.getInstance().get(xmlTagKey) : null;
						FocXMLGuiComponent newField = vField.newGuiComponent(FocXMLLayout.this, newFocData, atribs, obj.getDelegate().getRootFocData(), obj.getDelegate().getDataPath());
						if(newField != null){
							addFieldToValueChangeListener(newField);
							encapsulator.replaceField(newField);
						}
					}
				}
			}
			if(!(obj instanceof FocXMLLayout)){
		    boolean backupReadOnly = false;
		    Field fld = obj.getFormField();
		    if(fld != null){
		    	backupReadOnly = fld.isReadOnly();
		    	fld.setReadOnly(false);
		    }
				obj.copyMemoryToGui();
		    if(fld != null && backupReadOnly){
		    	fld.setReadOnly(backupReadOnly);
		    }
			}
		}
		mapDataPath2ListenerAction_ApplyVisibilityFormulas();
		setReactToGuiChangeDisable(false);
	}

	private IFocData getDataByPath(String path) {
		return getDataByPath(focData, path);
	}

	private IFocData getDataByPath(IFocData rootFocData, String path) {
		IFocData data = null;
		if(path != null){
			if(path.equals(FXML.DATA_ROOT)){
				data = rootFocData;
			}else if(path.startsWith(FXML.DATA_STORE)){
				String subPath=path.substring(FXML.DATA_STORE.length(), path.length()-1);
				String[] args = subPath.split(",");
				if(args.length == 1){
					data = (IFocData) DataStore.getInstance().getList(args[0]);
				}
			}else{
				data = rootFocData != null ? rootFocData.iFocData_getDataByPath(path) : null;
			}
		}
		return data;
	}

	@Override
	public void goBack(FocCentralPanel focCentralPanel_BeforevalidationCheckDataIsCalled) {
		FocCentralPanel focCentralPanel = findAncestor(FocCentralPanel.class);
		if(focCentralPanel == null){//Normally we never get inside these brackets, and even if we do the find should then give null again. Because since 2014-04-25 we have moved the find to before the validationCheckData listeners. 
			focCentralPanel = focCentralPanel_BeforevalidationCheckDataIsCalled;
		}
//		if(focCentralPanel instanceof FocWebVaadinWindow){
			FocXMLLayout root = this;
			while(root.getParentLayout() != null){
				root = root.getParentLayout();
			}
			if(focCentralPanel != null) focCentralPanel.goBack(root);
//		}else{
//			FocWebApplication.getInstanceForThread().removeWindow(findAncestor(Window.class));
//			if(getMainWindow() != null){
//				getMainWindow().refreshCentralPanelAndRightPanel();
//			}
//		}
	}

	public void print() {
		FocXMLLayout lay = this;
		while (lay.getParentLayout() != null){
			lay = lay.getParentLayout();
		}
		lay.printThisLayout2();
	}

	/*
	public void printThisLayout2(Window mWindow) {
		// getWindow().setScrollable(false);
		// ((FocCentralPanel)getWindow()).getCentralPanelWrapper().setScrollable(false);
		getWindow().setBorder(Window.BORDER_NONE);
		getWindow().executeJavaScript("print();");
	}
	*/

	public void printThisLayout2() {
		JavaScript.getCurrent().execute("print();");
		

//		XMLView xmlView = getXMLView();
//
//		Window window = new Window();
////		window.setScrollable(false);
//		FocXMLLayout printingForm = new FocXMLLayout();
//		printingForm.init((FocCentralPanel) getMainWindow(), xmlView, getFocData());
//		printingForm.parseXMLAndBuildGui();
//		window.setContent(printingForm);
//		// mWindow.open(new ExternalResource(window.getURL()), "_blank", 595, 842,
//		// Window.BORDER_NONE);
//		int prefWidth = 700;
//		try{
//			String prefWidthStr = getPreferredPageWidth();
//			//
//			if(prefWidthStr == null){
//				prefWidthStr = "700";
//			}
//			//
//			prefWidthStr = prefWidthStr.replace("px", "");
//			prefWidth = Integer.parseInt(prefWidthStr);
//			prefWidth += FocCentralPanel.MARGIN_FOR_CENTRAL_PANEL;
//		}catch (Exception e){
//			Globals.logException(e);
//		}
//		
//		window.setWidth(prefWidth+"px");
//		FocWebApplication.getInstanceForThread().addWindow(window);
////		Globals.showNotification("Foc Vaadin 7 compatibility", "FE54GQ", IFocEnvironment.TYPE_ERROR_MESSAGE);
////		getUI().getPage().open(window.getURL(), "_blank");
////		mWindow.open(new ExternalResource(window.getURL()), "_blank", prefWidth, 842, Window.BORDER_NONE);
//		JavaScript.getCurrent().execute("print();");
//		JavaScript.getCurrent().execute("self.close();");
		/*
		 * Change the container of the layout we want to print without re-creating
		 * it
		 * //----------------------------------------------------------------------
		 * --
		 * 
		 * AbstractComponentContainer container = (AbstractComponentContainer)
		 * getParent(); container.removeComponent(this);
		 * 
		 * FocCentralPanel centralPanel = new FocCentralPanel();
		 * centralPanel.fill(); centralPanel.changeCentralPanelContent(this, true);
		 * mWindow.getApplication().addWindow(centralPanel); mWindow.open(new
		 * ExternalResource(centralPanel.getURL()), "_blank", 595, 842,
		 * Window.BORDER_NONE); centralPanel.executeJavaScript("print();"); //
		 * window.executeJavaScript("self.close();");
		 */
	}

	public void printThisLayout(){
	  XMLView xmlView = getXMLView();
	  Window window = new Window();
//	 window.setScrollable(false);//HEREHERE
	  FocXMLLayout printingForm = new FocXMLLayout();
	  printingForm.init((FocCentralPanel) getMainWindow() , xmlView, getFocData());
	  printingForm.parseXMLAndBuildGui();
	  window.setContent(printingForm);
	
	  FocWebApplication.getInstanceForThread().addWindow(window);
//	 mWindow.open(new ExternalResource(window.getURL()), "_blank", 595, 842,
//	 Window.BORDER_NONE);
	  JavaScript.getCurrent().execute("print();");
	  JavaScript.getCurrent().execute("self.close();");
	}
	
	public static final int POSITION_BOTTOM = 0;
	public static final int POSITION_UP     = 1;
	public static final int POSITION_RIGHT  = 2;
	public static final int POSITION_LEFT   = 3;
	
	@Override
	public void showValidationLayout(boolean showBackButton) {
		showValidationLayout(showBackButton, POSITION_BOTTOM);
	}
	
	public void showValidationLayout(boolean showBackButton, int position) {
		if(validationSettings != null && getValidationLayoutVisible()){
			XMLView xmlView = getXMLView();
			if(xmlView != null && xmlView.isHelpFileExist()){
				validationSettings.setWithTips(true);
			}
			
			validationLayout = new FVValidationLayout((INavigationWindow) getNavigationWindow(), this, validationSettings, showBackButton);
//			if(!FocWebApplication.getInstanceForThread().isMobile()){
				if(Globals.isValo()){
					INavigationWindow navigationindow = getParentNavigationWindow();
					
					if(navigationindow instanceof FocWebApplication){
						((FocWebApplication)navigationindow).replaceFooterLayout(validationLayout);
//					FocWebApplication focWebApplication = findAncestor(FocWebApplication.class);
//					if(focWebApplication != null){
//						focWebApplication.replaceFooterLayout(validationLayout);
					}else{
						if(position == POSITION_UP){
							addComponentAsFirst(validationLayout);
						}else{
							addComponent(validationLayout);
						}
						setComponentAlignment(validationLayout, Alignment.BOTTOM_CENTER);
					}
				}else{
					addComponentAsFirst(validationLayout);
					setComponentAlignment(validationLayout, Alignment.MIDDLE_CENTER);
				}
//			}else{
//				validationLayout.setStyleName("transparent");
//				FVValidationLayout bottomValidationLayout = new FVValidationLayout((INavigationWindow) getNavigationWindow(), this, validationSettings, showBackButton);
//				addComponent(bottomValidationLayout);
//				setComponentAlignment(bottomValidationLayout, Alignment.BOTTOM_RIGHT);
//				if(bottomValidationLayout.getDeleteButton(false) != null){
//					bottomValidationLayout.getDeleteButton(false).setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_BLACK));
//				}
//			}
			// setExpandRatio(validationLayout, 1);

			validationLayout.addValidationListener(this);
			
			//Adding a default tip for tables
			/*
			if(getXMLView() != null && getXMLView().getXmlViewKey() != null && (getXMLView().getXmlViewKey().getType() == IXMLViewConst.TYPE_TABLE || getXMLView().getXmlViewKey().getType() == IXMLViewConst.TYPE_TREE)){
				if(getValidationLayout().getTipsButton(false) != null){
					getValidationLayout().setTipMessage("Double click any row for more details.");
				}
			}
			*/
		}
	}
	
	public void setValidationLayoutVisible(boolean showValidationLayout){
		this.showValidationLayout = showValidationLayout;
	}
	
	public boolean getValidationLayoutVisible(){
		return showValidationLayout;
	}

	public class FocXMLHandler extends DefaultHandler {

//		private FocElement currentFocElement = null;
		
		private class FocElement{
			
			private String qName = null;
			private FocXMLAttributes attributes = null;
			private StringBuffer cData = null;
			
			public FocElement(String qName, FocXMLAttributes attributes) {
				this.qName = qName;
				this.attributes = attributes;
			}
			
			public void dispose(){
				qName = null;
				attributes = null;
			}

			public String getqName() {
				return qName;
			}

			public FocXMLAttributes getAttributes() {
				return attributes;
			}
			
			public void appendCData(String data){
				if(cData == null) cData = new StringBuffer();
				cData.append(data);
			}
			
			public StringBuffer getCData(){
				return cData;
			}
		}

		@Override
		public void characters(char[] cBuf, int start, int length) throws SAXException {
//			FocElement focElement = getCurrentFocElement();
			FocElement focElement = getFocElementStack().peek();
			if(focElement != null){
				String cdata = new String(cBuf, start, length);
				focElement.appendCData(cdata);
			}

			/*
			if(focElement != null && focElement.getqName() != null){
				String qName = focElement.getqName();
				
				String cdata = new String(cBuf, start, length);
				boolean isHeader = qName.equals(FXML.TAG_GROUP_HEADER);
				boolean isFooter = qName.equals(FXML.TAG_GROUP_FOOTER);
				if(cdata != null && !cdata.isEmpty() && (isHeader || isFooter)){
					Object object = !getStack().isEmpty() ? getStack().peek() : null;
					if(object instanceof FVReport){
						FVReport fvReport = (FVReport) object;
						String breakdownName = focElement.getAttributes().getValue(FXML.ATT_BREAKDOWN_NAME);
						FVReportGroup iReport_Group = fvReport.getReportGroup(breakdownName);
						if(iReport_Group != null){
							if(isHeader) iReport_Group.setHeaderXML(iReport_Group.getHeaderXML()+cdata);
							else iReport_Group.setFooterXML(iReport_Group.getFooterXML()+cdata);
						}
					}
				}
			}
		  */			
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			FocXMLAttributes focXmlAttributes = new FocXMLAttributes(FocXMLLayout.this, attributes);
			String name = focXmlAttributes.getValue(FXML.ATT_NAME);
			String dataPath = focXmlAttributes.getValue(FXML.ATT_DATA_PATH);
			FocElement focElement = new FocElement(qName, focXmlAttributes);
//			setCurrentFocElement(focElement);
			getFocElementStack().push(focElement);
			// If the path is not specified it is the same as the name
			if(dataPath == null) dataPath = name;
			if(dataPath != null && dataPath.contains(">") && focXmlAttributes.getValue(FXML.ATT_CAPTION_PROPERTY) == null){
				int idOf = dataPath.indexOf(">");
				if(idOf+1 < dataPath.length()){
					String captionProperty = dataPath.substring(idOf+1);
					focXmlAttributes.addAttribute(FXML.ATT_CAPTION_PROPERTY, captionProperty);
					dataPath = dataPath.substring(0, idOf);
					focXmlAttributes.addAttribute(FXML.ATT_DATA_PATH, dataPath);
				}
			}

			Component comp = null;

			if(qName.equals(FXML.TAG_VALIDATION_SETTINGS)){
				validationSettings = new FValidationSettings();

				String title             = focXmlAttributes.getValue(FXML.ATT_FORM_TITLE);
				String withApply         = focXmlAttributes.getValue(FXML.ATT_WITH_APPLY);
				String withSave          = focXmlAttributes.getValue(FXML.ATT_WITH_SAVE);
				String withDiscard       = focXmlAttributes.getValue(FXML.ATT_WITH_DISCARD);
				String withStatus        = focXmlAttributes.getValue(FXML.ATT_WITH_STATUS);
				String withStage         = focXmlAttributes.getValue(FXML.ATT_WITH_STAGE);
				String discardLink       = focXmlAttributes.getValue(FXML.ATT_DISCARD_LINK);
				String applyLink         = focXmlAttributes.getValue(FXML.ATT_APPLY_LINNK);
				String withPrint         = focXmlAttributes.getValue(FXML.ATT_WITH_PRINT);
				String withLog           = focXmlAttributes.getValue(FXML.ATT_WITH_LOG);
				String withAttach        = focXmlAttributes.getValue(FXML.ATT_WITH_ATTACH);
				String withEmail         = focXmlAttributes.getValue(FXML.ATT_WITH_EMAIL);
				String withInternalEmail = focXmlAttributes.getValue(FXML.ATT_WITH_INTERNAL_EMAIL);
				String withTips          = focXmlAttributes.getValue(FXML.ATT_WITH_TIPS);
				String commitData        = focXmlAttributes.getValue(FXML.ATT_COMMIT_DATA);
				String withPDF           = focXmlAttributes.getValue(FXML.ATT_WITH_PDF);
				String withMSWord        = focXmlAttributes.getValue(FXML.ATT_WITH_MSWORD);
				String withViewSelector  = focXmlAttributes.getValue(FXML.ATT_SHOW_VIEW_SELECTOR);
				String withPrintAndExit  = focXmlAttributes.getValue(FXML.ATT_PRINT_AND_EXIT);
				String avoidRowBreak     = focXmlAttributes.getValue(FXML.ATT_AVOID_ROW_BREAK);

				validationSettings.setReportPrintAsWord(focXmlAttributes.getBoolean(FXML.ATT_ALLOW_REPORT_PRINT_AS_WORD, true));
				validationSettings.setReportPrintAsRTF(focXmlAttributes.getBoolean(FXML.ATT_ALLOW_REPORT_PRINT_AS_RTF, true));
				validationSettings.setReportSendEMail(focXmlAttributes.getBoolean(FXML.ATT_ALLOW_REPORT_SEND_EMAIL, true));
				
				if(title != null){
					validationSettings.setTitle(title);
				}

				if(commitData != null){
					if(commitData.equals("true")){
						validationSettings.setCommitData(true);
					}else{
						validationSettings.setCommitData(false);
					}
				}
				
				if(avoidRowBreak != null){
					if(avoidRowBreak.equals("true")){
						validationSettings.setAvoidRowBreak(true);
					}else{
						validationSettings.setAvoidRowBreak(false);
					}
				}
				
				if(withPrintAndExit != null){
					if(withPrintAndExit.equals("true")){
						validationSettings.setWithPrintAndExit(true);
					}else{
						validationSettings.setWithPrintAndExit(false);
					}
				}

				if(withApply != null){
					if(withApply.equals("true")){
						validationSettings.setWithApply(true);
					}else{
						validationSettings.setWithApply(false);
					}
				}
				
				if(withSave == null || withSave.equals("true")){
					validationSettings.setWithSave(true);
				}else{
					validationSettings.setWithSave(false);
				}

				if(withDiscard != null){
					if(withDiscard.equals("false")){
						validationSettings.setWithDiscard(false);
					}else{
						validationSettings.setWithDiscard(true);
					}
				}
				
				if(discardLink != null){
					validationSettings.setDiscardLink(discardLink);
				}
				
				if(applyLink != null){
					validationSettings.setApplyLink(applyLink);
				}

				if(withPrint != null){
					if(withPrint.equals("true")){
						validationSettings.setWithPrint(true);
					}else{
						validationSettings.setWithPrint(false);
					}
				}
				
				if(withLog != null){
					withLog = withLog.trim().toLowerCase();
					if(withLog.equals("true") || withLog.equals("1")){
						validationSettings.setWithLog(true);
					}else{
						validationSettings.setWithLog(false);
					}
				}
				
				if(withPDF != null){
					if(withPDF.equals("true")){
						validationSettings.setWithPDFGenerator(true);
					}else{
						validationSettings.setWithPDFGenerator(false);
					}
				}
				
				if(withMSWord != null){
					if(withMSWord.equals("true")){
						validationSettings.setWithMSWordGenerator(true);
					}else{
						validationSettings.setWithMSWordGenerator(false);
					}
				}

				if(withAttach != null){
					if(withAttach.equals("true")){
						validationSettings.setWithAttach(true);
					}else{
						validationSettings.setWithAttach(false);
					}
				}
				
				if(withEmail != null){
					if(withEmail.equalsIgnoreCase("false")){
						validationSettings.setWithEmail(false);
					}else{
						validationSettings.setWithEmail(true);
					}
				}
				
				if(withInternalEmail != null){
					if(withInternalEmail.equalsIgnoreCase("false")){
						validationSettings.setWithInternalEmail(false);
					}else{
						validationSettings.setWithInternalEmail(true);
					}
				}
				
				if(withViewSelector != null){
					if(withViewSelector.equalsIgnoreCase("false")){
						validationSettings.setWithViewSelector(false);
					}else{
						validationSettings.setWithViewSelector(true);
					}
				}
				
				if(withTips != null){
					if(withTips.equalsIgnoreCase("true")){
						validationSettings.setWithTips(true);
					}else{
						validationSettings.setWithTips(false);
					}
				}
				
				if(withStatus != null && withStatus.equalsIgnoreCase("false")){
					validationSettings.setWithStatus(false);
				}else{
					validationSettings.setWithStatus(true);
				}
				
				if(withStage != null && withStage.equalsIgnoreCase("false")){
					validationSettings.setWithStage(false);
				}else{
					validationSettings.setWithStage(true);
				}

				addComponentToStack(comp, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_SCREEN_HELP)){
				if(focXmlAttributes != null && focXmlAttributes.getValue(FXML.ATT_HELP) != null){
					setScreenHelp(focXmlAttributes.getValue(FXML.ATT_HELP));
				}
				addComponentToStack(null, null, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_CONDITION_FIELD)){
				FocObject focObject = getFocObject();
				if(focObject instanceof FocListFilter){
					newFilterConditionGuiField(FocXMLHandler.this, (FocListFilter) focObject, dataPath, focXmlAttributes);
				}
				addComponentToStack(comp, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_FIELD)){
				FVLayout layout = getCurrentLayout();
				comp = newGuiField(layout, name, dataPath, focXmlAttributes);
				addComponentToStack(comp, name, focXmlAttributes);
				/*
			}else if(qName.equals(FXML.TAG_QR_CODE)){
				String value = focXmlAttributes.getValue(FXML.ATT_VALUE);
				ByteArrayOutputStream out = QRCode.from(value).to(ImageType.PNG).stream();
				ByteArrayOutputStreamToStreamResource streamResource = new ByteArrayOutputStreamToStreamResource(out, formatName + ".JPG");
			 
				FVImageField imageField = getQRCodeImageField();
				if(imageField != null){
					imageField.setEditable(false);
					imageField.getEmbedded().setSource(streamResource.getStreamResource());
				}
				*/
			}else if(	 qName.equals(FXML.TAG_PARAMETER) 
							|| qName.equals(FXML.TAG_DATA_MAP) ){
				try{
					//These tags can either be called:
					//1- Inside an IncludeXML tag
					//2- Any where in the Layout, basically on top -> To set the default values will be overriden by Includes...
					Component lastComponent = getStack().peek();
					FocXMLLayout includedLayout = FocXMLLayout.this;
					if(lastComponent instanceof FocXMLLayout){
						includedLayout = (FocXMLLayout) lastComponent;
					}
					
					String paramName = focXmlAttributes.getValue(FXML.ATT_PARAM_NAME);
					if(paramName == null) paramName = focXmlAttributes.getValue(FXML.ATT_DATA_REPLACE);
					
					String paramValue = focXmlAttributes.getValue(FXML.ATT_PARAM_VALUE);
					if(paramValue == null) paramValue = focXmlAttributes.getValue(FXML.ATT_DATA_WITH);
					
					if(paramName != null && paramValue != null && includedLayout != null){
						FocDataDictionary dataDictionary = includedLayout.getFocDataDictionary(true);
						if(dataDictionary != null && dataDictionary.getParameter(paramName) == null){
							//If the parameter exists already this means that the IncludeXML tag hs set it before, 
							//in which case we do not change it 
							dataDictionary.putParameter(paramName, new FocDataResolver_StringConstant(paramValue));
						}
					}
				}catch (Exception e){
					Globals.logException(e);
				}
				addComponentToGuiAndMap_AndStack(null, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_TABLE_COLUMN)){
				try{
					Object wrapperLayout = getStack().peek();
					ITableTree tableOrTree = null;
					if(wrapperLayout instanceof FVTableWrapperLayout){
						FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) wrapperLayout;
						tableOrTree = tableWrapper.getTableOrTree();
					}else if(wrapperLayout instanceof FVChartWrapperLayout){
						FVChartWrapperLayout chartWrapperLayout = (FVChartWrapperLayout) wrapperLayout;
						tableOrTree = chartWrapperLayout.getTableOrTree();
					}else if(wrapperLayout instanceof FVPivotLayout){
						FVPivotLayout pivotLayout = (FVPivotLayout) wrapperLayout;
						if(pivotLayout != null){
							pivotLayout.addColumn(focXmlAttributes);
						}
					}else if(wrapperLayout instanceof FVReport){
						FVReport report = (FVReport) wrapperLayout;
						tableOrTree = report.getPivotChartDelegate().getTableTree();
						/*FVReport fvReport = (FVReport) wrapperLayout;
						if(fvReport != null){
							fvReport.addColumn(focXmlAttributes);
						}*/
					}

					if(tableOrTree != null){
						tableOrTree.addColumn(focXmlAttributes);
//						if(tableColumn != null){
//							tableColumn.setAttributes(focXmlAttributes);
//						}
					}
				}catch (Exception e){
					Globals.logException(e);
				}
				addComponentToGuiAndMap_AndStack(null, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_PIVOT_BREAKDOWN)){
				try{
					String breakdownName = focXmlAttributes.getValue(FXML.ATT_NAME);
					String groupBy = focXmlAttributes.getValue(FXML.ATT_GROUP_BY);
					String sortBy = focXmlAttributes.getValue(FXML.ATT_SORT_BY);
					String bkdnCapProp = focXmlAttributes.getValue(FXML.ATT_TITLE_CAPTION);
					String bkdnDescriptionProp = focXmlAttributes.getValue(FXML.ATT_DESCRIPTION_CAPTION);
					String bkdnShowTree = focXmlAttributes.getValue(FXML.ATT_BKDN_SHOW_TREE);
					String bkdnDateStart = focXmlAttributes.getValue(FXML.ATT_BKDN_DATE_START);
					String bkdnDateEnd = focXmlAttributes.getValue(FXML.ATT_BKDN_DATE_END);
					String bkdnCutOffDate = focXmlAttributes.getValue(FXML.ATT_BKDN_CUT_OFF_DATE);
					String hideWhenAlone = focXmlAttributes.getValue(FXML.ATT_BKDN_HIDE_WHEN_ALONE);
					String dateGrouping = focXmlAttributes.getValue(FXML.ATT_DATE_GROUPING);
					String titleWhenEmpty       = focXmlAttributes.getValue(FXML.ATT_TITLE_WHEN_EMPTY);
					String descriptionWhenEmpty = focXmlAttributes.getValue(FXML.ATT_DESCRIP_WHEN_EMPTY);
					String wrapNativeObject     = focXmlAttributes.getValue(FXML.ATT_BKDN_WRAPE_NATIVE);

					Object wrapperLayout = getStack().peek();
					
					if(wrapperLayout instanceof FVTableWrapperLayout){
						if(groupBy != null){
//							FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getStack().peek();
//							FVPivotTable fvPivotTable = (FVPivotTable) tableWrapper.getTableOrTree();
//							FPivotTable pivotTable = fvPivotTable.getPivotTable();
//
							FPivotTable pivotTable = null;
							FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getStack().peek();
							if(tableWrapper.getTableOrTree() instanceof IPivotGrid){
								IPivotGrid fvPivotTable = (IPivotGrid) tableWrapper.getTableOrTree();
								pivotTable = fvPivotTable.getPivotTable();
							}else{
								FVPivotTable fvPivotTable = (FVPivotTable) tableWrapper.getTableOrTree();
								pivotTable = fvPivotTable.getPivotTable();
							}
							
							if(pivotTable != null){
								FPivotBreakdown breakdown = pivotTable.addBreakdown(breakdownName, groupBy, bkdnShowTree, bkdnCapProp, bkdnDescriptionProp, bkdnDateStart, bkdnDateEnd, hideWhenAlone, titleWhenEmpty, descriptionWhenEmpty);
								breakdown.setCutOffDate(bkdnCutOffDate);
								breakdown.setWrapeNativeObject(wrapNativeObject);
								breakdown.setSortBy(sortBy);
								breakdown.setDateGrouping(dateGrouping);
							}
						}
					}else if(wrapperLayout instanceof FVChartWrapperLayout){
						FVChartWrapperLayout chartWrapperLayout = (FVChartWrapperLayout) wrapperLayout;
						if(chartWrapperLayout != null && chartWrapperLayout.getFVChart() != null){
							FPivotTable pivotTable = chartWrapperLayout.getFVChart().getPivotTable();
							if(pivotTable != null){
								FPivotBreakdown breakdown = pivotTable.addBreakdown(breakdownName, groupBy, bkdnShowTree, bkdnCapProp, bkdnDescriptionProp, bkdnDateStart, bkdnDateEnd, hideWhenAlone, titleWhenEmpty, descriptionWhenEmpty);
								breakdown.setCutOffDate(bkdnCutOffDate);								
								breakdown.setSortBy(sortBy);
								breakdown.setDateGrouping(dateGrouping);
								breakdown.setWrapeNativeObject(wrapNativeObject);
							}
						}
					}else if(wrapperLayout instanceof FVPivotLayout){
						FVPivotLayout pivotLayout = (FVPivotLayout) wrapperLayout;
						if(pivotLayout != null){
							FPivotTable pivotTable = pivotLayout.getPivotTable();
							if(pivotTable != null){
								FPivotBreakdown breakdown = pivotTable.addBreakdown(breakdownName, groupBy, bkdnShowTree, bkdnCapProp, bkdnDescriptionProp, bkdnDateStart, bkdnDateEnd, hideWhenAlone, titleWhenEmpty, descriptionWhenEmpty);
								breakdown.setCutOffDate(bkdnCutOffDate);			
								breakdown.setDateGrouping(dateGrouping);
								breakdown.setWrapeNativeObject(wrapNativeObject);
							}
						}
					}else if(wrapperLayout instanceof FVReport){
						FVReport report = (FVReport) wrapperLayout;
						if(report != null){
							FPivotTable pivotTable = report.getPivotTable();
							if(pivotTable != null){
								FPivotBreakdown breakdown = pivotTable.addBreakdown(breakdownName, groupBy, bkdnShowTree, bkdnCapProp, bkdnDescriptionProp, bkdnDateStart, bkdnDateEnd, hideWhenAlone, titleWhenEmpty, descriptionWhenEmpty);
								breakdown.setCutOffDate(bkdnCutOffDate);								
								breakdown.setWrapeNativeObject(wrapNativeObject);
								breakdown.setDateGrouping(dateGrouping);
							}
						}
					}
				}catch (Exception e){
					Globals.logException(e);
				}
				addComponentToGuiAndMap_AndStack(null, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_VIEW_KEY_OPEN) || qName.equals(FXML.TAG_VIEW_KEY_NEW)){
				try{
					FVTableWrapperLayout tableWrapperLayout = (FVTableWrapperLayout) getStack().peek();
					FocList list = tableWrapperLayout.getTableOrTree().getFocList();
					TableTreeDelegate delegate = tableWrapperLayout.getTableOrTree().getTableTreeDelegate();
					if(list != null && delegate != null){
						String storageName = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_STORAGE_NAME);
						String typeStr = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_TYPE);
						String context = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_CONTEXT);
						String view = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_VIEW);
						String container = focXmlAttributes.getValue(FXML.ATT_VIEW_CONTAINER);

						XMLViewKey xmlViewKey = new XMLViewKey();
						xmlViewKey.setStorageName(storageName != null ? storageName : list.getFocDesc().getStorageName());
						if(typeStr == null){
							xmlViewKey.setType(XMLViewKey.TYPE_FORM);
						}else{
							xmlViewKey.setType(typeStr);
						}
						xmlViewKey.setContext(context != null ? context : xmlView.getXmlViewKey().getContext());
						xmlViewKey.setUserView(view != null ? view : xmlView.getXmlViewKey().getUserView());

						if(qName.equals(FXML.TAG_VIEW_KEY_OPEN)){
							delegate.setXmlViewKey_Open(xmlViewKey);
							if(container != null)
								delegate.setViewContainer_ForOpen(container);
						}else if(qName.equals(FXML.TAG_VIEW_KEY_NEW)){
							delegate.setXmlViewKey_New(xmlViewKey);
							if(container != null)
								delegate.setViewContainer_ForNew(container);
						}
					}
				}catch (Exception e){
					Globals.logException(e);
				}

				addComponentToStack(comp, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_SHOW_FORM)){
				try{
					FVTableWrapperLayout tableWrapperLayout = (FVTableWrapperLayout) getStack().peek();
					FocList list = tableWrapperLayout.getTableOrTree().getFocList();
					TableTreeDelegate delegate = tableWrapperLayout.getTableOrTree().getTableTreeDelegate();

					if(list != null && delegate != null){
						String storageName = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_STORAGE_NAME);
						String showFormType = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_TYPE);
						String showFormContext = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_CONTEXT);
						String showFormUserView = focXmlAttributes.getValue(FXML.ATT_VIEW_KEY_VIEW);

						XMLViewKey xmlViewKey = new XMLViewKey();
						xmlViewKey.setStorageName(storageName != null ? storageName : list.getFocDesc().getStorageName());
						if(showFormType == null){
							xmlViewKey.setType(XMLViewKey.TYPE_FORM);
						}else{
							xmlViewKey.setType(showFormType);
						}
						xmlViewKey.setContext(showFormContext != null ? showFormContext : xmlView.getXmlViewKey().getContext());
						xmlViewKey.setUserView(showFormUserView != null ? showFormUserView : xmlView.getXmlViewKey().getUserView());

						XMLView xmlView = XMLViewDictionary.getInstance().get(xmlViewKey);
						tableWrapperLayout.setXmlView_ShowForm(xmlView);
					}
				}catch (Exception e){
					Globals.logException(e);
				}

				addComponentToStack(comp, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_GROUP_HEADER) || qName.equals(FXML.TAG_GROUP_FOOTER)){
				Object report_Obj = getStack().isEmpty() ? null : getStack().peek();
				FVReport report = null;
				if(report_Obj != null && report_Obj instanceof FVReport){
					report = (FVReport) report_Obj;
					report.newReportGroup(focXmlAttributes);
				}
				addComponentToStack(report, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_INCLUDE_XML)){
				FVLayout layout = getCurrentLayout();
				comp = newGuiPaletteComponent(layout, qName, name, dataPath, getDataByPath(dataPath), attributes);
				addComponentToStack(comp, name, focXmlAttributes);
			}else if(qName.equals(FXML.TAG_CHART_YAXIS) || qName.equals(FXML.TAG_CHART_XAXIS)){
				try{
					FVChartWrapperLayout chartWrapperLayout = (FVChartWrapperLayout) getStack().peek();
					chartWrapperLayout.setAttributes(focXmlAttributes);
//					FVChart chart = chartWrapperLayout.getFVChart();
//					if(chart != null && qName.equals(FXML.TAG_CHART_YAXIS)){
//						chart.fillYAxis();
//					}else if(chart != null && qName.equals(FXML.TAG_CHART_XAXIS)){
//						chart.fillXAxis();
//					}
				}catch (Exception e){
					Globals.logException(e);
				}
				addComponentToGuiAndMap_AndStack(null, name, focXmlAttributes);
			}else{
				FVLayout layout = getCurrentLayout();
				comp = newGuiPaletteComponent(layout, qName, name, dataPath, getDataByPath(dataPath), attributes);
				addComponentToStack(comp, name, focXmlAttributes);
			}
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			Component comp = getStack().pop();
			FocElement focElement = getFocElementStack().pop();
			
			if(qName.equals(FXML.TAG_TABLE) || qName.equals(FXML.TAG_TREE) || qName.equals(FXML.TAG_PIVOT) || qName.equals(FXML.TAG_HTML_TABLE) || qName.equals(FXML.TAG_TREE_GRID) || qName.equals(FXML.TAG_TABLE_GRID)){
				FVTableWrapperLayout tableLayout = (FVTableWrapperLayout) comp;
				if(tableLayout != null && tableLayout.getTableOrTree() != null && tableLayout.getTableOrTree().getTableTreeDelegate() != null){
					table_BeforeEndElement(tableLayout.getTableOrTree().getTableTreeDelegate().getTableName(), tableLayout.getTableOrTree());
				}
				tableLayout.getTableOrTree().applyFocListAsContainer();
				tableLayout.createShowFormPanelIfRequired();
			}else if(qName.equals(FXML.TAG_INCLUDE_XML)){
				FocXMLLayout layout = (FocXMLLayout) comp;
				if(layout != null){
					layout.parseXMLAndBuildGui();
					showValidationLayoutForInclude_IfNeeded(layout, layout.getAttributes());
//					layout.setParentLayout(FocXMLLayout.this);//2017-05-04
					String commitStr = layout.getAttributes() != null ? layout.getAttributes().getValue(FXML.ATT_IMPORT_COMMIT_WITH_PARENT) : null;
					if(commitStr != null && commitStr.toLowerCase().equals("false")){
						layout.setCommitWithParent(false);
					}else{
						layout.setCommitWithParent(true);
					}
				}
			}else if(qName.equals(FXML.TAG_CHART)){
				FVChartWrapperLayout chartLayout = (FVChartWrapperLayout) comp;
				FVChart chart = chartLayout.getFVChart();
				if(chart != null){
					chart.applyFocListAsContainer();
					chart.drawFVChart();
				}
			}else if(qName.equals(FXML.TAG_PIVOT_LAYOUT)){
				FVPivotLayout pivotLayout = (FVPivotLayout) comp;
				pivotLayout.applyFocListAsContainer();
			}else if(qName.equals(FXML.TAG_REPORT)){
				FVReport report = (FVReport) comp;
				if(report != null){
					report.applyFocListAsContainer();
					report.build();
				}
			}else if(qName.equals(FXML.TAG_HTML_LAYOUT)){
				FVHTMLLayout htmlLayout = (FVHTMLLayout) comp;
				if(htmlLayout != null && focElement != null && focElement.getCData() != null){
					htmlLayout.parseAndBuildHtml(focElement.getCData().toString());
				}
			}else if(qName.equals(FXML.TAG_GROUP_HEADER) || qName.equals(FXML.TAG_GROUP_FOOTER)){
				if(focElement != null && focElement.getAttributes() != null && comp != null && comp instanceof FVReport){
					FVReport report = (FVReport) comp;
					String breakdownName = focElement.getAttributes().getValue(FXML.ATT_BREAKDOWN_NAME);
					if(breakdownName != null){
						FVReportGroup repGroup = report.pushReportGroup(breakdownName);
						
						if(qName.equals(FXML.TAG_GROUP_HEADER)){
							repGroup.setHeaderXML(focElement.getCData().toString());
							repGroup.setHeaderXMLAttributes(focElement.getAttributes());
						}else{
							repGroup.setFooterXML(focElement.getCData().toString());
							repGroup.setFooterXMLAttributes(focElement.getAttributes());
						}
					}
				}
			}
			if(focElement != null){
				focElement.dispose();
				focElement = null;
			}
		}
		
		/*public FocElement getCurrentFocElement() {
			return currentFocElement;
		}

		public void setCurrentFocElement(FocElement focElement) {
			this.currentFocElement = focElement;
		}*/
	}

	private void showValidationLayoutForInclude_IfNeeded(FocXMLLayout layout, Attributes attributes) {
		boolean addValidationLayout = false;
		String showValidationLayout = attributes != null ? attributes.getValue(FXML.ATT_SHOW_VALIDATION_lAYOUT) : null;
		if(showValidationLayout != null && !showValidationLayout.isEmpty() && showValidationLayout.equals("true")){
			addValidationLayout = true;
		}
		if(addValidationLayout){
			layout.showValidationLayout(false);
		}
	}

	public boolean isProcessingCopyMemoryToGui() {
		return isReactToGuiChangeDisable();
	}
	
	private boolean isReactToGuiChangeDisable() {
		return reactToGuiChangeDisable;
	}

	private boolean setReactToGuiChangeDisable(boolean copyingMemoryToGui) {
		boolean ret = this.reactToGuiChangeDisable;
		this.reactToGuiChangeDisable = copyingMemoryToGui;
		return ret;
	}

	public boolean isVisibleWhenApplicable() {
		return getFocData() instanceof FocObject;
	}

	private void createVisibleWhenFormulaContextIfNeeded(String compName, FocXMLGuiComponent guiComponent) {
		if(guiComponent != null && guiComponent.getAttributes() != null){
			// String formulaExpression =
			// guiComponent.getAttributes().getValue(FXML.ATT_VISIBLE_WHEN);
			String formulaExpression = guiComponent.getAttributes() != null ? guiComponent.getAttributes().getValue(FXML.ATT_VISIBLE_WHEN) : null;
			if(formulaExpression != null && !formulaExpression.isEmpty()){
				XMLLayoutFormulaContext xmlLayoutFormulaContext = new XMLLayoutFormulaContext(FocXMLLayout.this, guiComponent, formulaExpression);
				// xmlLayoutFormulaContext.plugListeners();
				xmlLayoutFormulaContext.computeFormulaAndApplyVisibilityOnComponent();
			}
		}
	}

	public FVValidationLayout getValidationLayout() {
		return validationLayout;
	}

	public FocDataDictionary getFocDataDictionary(boolean createIfNeeded) {
		if(focDataDictionary == null && createIfNeeded){
			focDataDictionary = new FocDataDictionary();
			focDataDictionary.setLocalDataDictionary(true);
		}
		return focDataDictionary;
	}

	// ---------------------------------------------------------------------
	// FocXMLGuiComponentListener
	// ---------------------------------------------------------------------
	public class FocXMLGuiComponentListener implements ValueChangeListener {

		private HashSet<Field> fieldsListenedTo = new HashSet<Field>();

		public void dispose(){
			if(fieldsListenedTo != null){
				Iterator<Field> iter = fieldsListenedTo.iterator();
				while(iter != null && iter.hasNext()){
					Field fld = iter.next();
					if(fld != null){
						fld.removeValueChangeListener(this);
					}
				}
				fieldsListenedTo.clear();
				fieldsListenedTo = null;
			}
		}
		
		public void addField(String dataPath) {
			if(dataPath != null && getComponentMap() != null){
				Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
				while (iter != null && iter.hasNext()){
					FocXMLGuiComponent guiComp = iter.next();
					String compDataPath = guiComp.getDelegate().getDataPath();
					if(compDataPath != null){
						if(guiComp != null && compDataPath.equals(dataPath)){
							addField(guiComp.getFormField());
							break;
						}
					}
				}
			}
		}

		public void addField(Field fld) {
			if(fld != null && !fieldsListenedTo.contains(fld)){
				((AbstractComponent) fld).setImmediate(true);
				fld.addValueChangeListener(this);
				fieldsListenedTo.add(fld);
			}
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			try{
				if(!isReactToGuiChangeDisable()){
					FProperty propertyModified = null;
					FocXMLGuiComponent componentModified = null;
					if(event.getProperty() instanceof FocXMLGuiComponent){
						componentModified = (FocXMLGuiComponent) event.getProperty();
						if(componentModified.getFocData() instanceof FProperty){//Sometimes we get a FField
							propertyModified = (FProperty) componentModified.getFocData();
						}
					}else if(event.getProperty() instanceof FProperty){
						propertyModified = (FProperty) event.getProperty();
					}
					boolean copyGuiToMemory = true;
					
					if(Globals.isValo() && 
							event.getProperty() != null && 
							event.getProperty().getValue() != null && 
							event.getProperty().getValue() instanceof Integer &&
							componentModified != null && 
							componentModified instanceof FVObjectComboBox &&
							(((Long)event.getProperty().getValue()) == ((FVObjectComboBox)componentModified).getFocObjectRef_ForTheADDIcon() || 
							((Long)event.getProperty().getValue()) == ((FVObjectComboBox)componentModified).getFocObjectRef_ForTheREFRESHIcon())){
						copyGuiToMemory = false;
					}
					if(copyGuiToMemory){
						copyGuiToMemory(componentModified, propertyModified);
					} else {
						copyMemoryToGui();
					}
//					copyGuiToMemory(componentModified, propertyModified);
					
					// ((FocObject)getFocData()).computePropertiesWithFormula();
				}
			}catch (Exception e){
				Globals.logException(e);
			}
		}
	};

	// -----------------------------------------
	// mapDataPath2ListenerAction
	// -----------------------------------------

	private Map<String, DataPathListenerAction> mapDataPath2ListenerAction_Get(boolean createIfNeeded) {
		if(propertyVisibleWhenMap == null && createIfNeeded){
			propertyVisibleWhenMap = new HashMap<String, DataPathListenerAction>();
		}
		return propertyVisibleWhenMap;
	}

	public DataPathListenerAction mapDataPath2ListenerAction_PushDataPath(String dataPath) {
		DataPathListenerAction listenerAction = mapDataPath2ListenerAction_Get(true).get(dataPath);
		if(listenerAction == null){
			listenerAction = new DataPathListenerAction();
			mapDataPath2ListenerAction_Get(true).put(dataPath, listenerAction);

			getValueChangeListener().addField(dataPath);
		}
		return listenerAction;
	}

	public void mapDataPath2ListenerAction_PutFormulaContext(String dataPath, XMLLayoutFormulaContext context) {
		DataPathListenerAction listenerAction = mapDataPath2ListenerAction_PushDataPath(dataPath);
		listenerAction.addArrayFormulaContext(context);
	}

	public void mapDataPath2ListenerAction_PutGuiComponentWithDataPath(String dataPath, FocXMLGuiComponent guiComp) {
		DataPathListenerAction listenerAction = mapDataPath2ListenerAction_PushDataPath(dataPath);
		listenerAction.addGuiComponentWithDataPath(guiComp);
	}

	public DataPathListenerAction mapDataPath2ListenerAction_GetListenerAction(FocXMLGuiComponent guiComp) {
		DataPathListenerAction propFormula = null;
		if(guiComp != null && guiComp.getDelegate() != null){
			propFormula = mapDataPath2ListenerAction_GetListenerAction(guiComp.getDelegate().getDataPath());
		}
		return propFormula;
	}

	public DataPathListenerAction mapDataPath2ListenerAction_GetListenerAction(String dataPath) {
		DataPathListenerAction value = null;
		Map<String, DataPathListenerAction> map = mapDataPath2ListenerAction_Get(false);
		if(map != null){
			value = map.get(dataPath);
		}
		return value;
	}

	public void mapDataPath2ListenerAction_ApplyVisibilityFormulas() {
		if(getParentLayout() != null){
			getParentLayout().mapDataPath2ListenerAction_ApplyVisibilityFormulas();
		}else{
			mapDataPath2ListenerAction_ApplyVisibilityFormulas_Local();
			childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
				@Override
				public boolean before(FocXMLLayout layout) {
					layout.mapDataPath2ListenerAction_ApplyVisibilityFormulas_Local();
					return false;
				}

				@Override
				public boolean after(FocXMLLayout layout) {
					return false;
				}
			});
		}
	}

	public void mapDataPath2ListenerAction_ApplyVisibilityFormulas_Local() {
		Map<String, DataPathListenerAction> map = mapDataPath2ListenerAction_Get(false);
		if(map != null){
			Iterator<DataPathListenerAction> iter = map.values().iterator();
			while (iter != null && iter.hasNext()){
				DataPathListenerAction formulasCtxtArray = iter.next();
				formulasCtxtArray.applyFormulasOfAllContexts();
			}
		}
	}

	// -----------------------------------------
	// -----------------------------------------

	// ----------------------------------------
	// FocXMLGuiComponent
	// ----------------------------------------

	private FocXMLGuiComponentDelegate delegate = null;

	private Attributes attributes = null;

	@Override
	public void setFocData(IFocData focData) {
		if(focData instanceof FObject){
			focData = ((FObject) focData).getObject_CreateIfNeeded();
		}
		this.focData = focData;
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_INCLUDE_XML;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	
	public void setAttributesOfIncludeNode(Attributes attributes){
		attributesOfIncludeNode = attributes;
	}
	
	public Attributes getAttributesOfIncludeNode(){
		return attributesOfIncludeNode;
	}

	@Override
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		if(delegate == null){
			delegate = new FocXMLGuiComponentDelegate(this);
		}
		return delegate;
	}

	// ----------------------------------------

	@Override
	public String getPreferredPageWidth() {
		String widthAttribute = null;
		if(layouts != null && layouts.size() > 0){
			Attributes attributed = (Attributes) layouts.get(0).getAttributes();
			widthAttribute = attributed != null ? attributed.getValue(FXML.ATT_WIDTH) : null;
		}
		return widthAttribute;
	}

	@Override
	public void addMoreMenuItems(FVValidationLayout validationLayout) {
	}

	@Override
	public String getValueString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValueString(String value) {
		// TODO Auto-generated method stub
	}

	public boolean isEnableRightsApplicationToGuiFields() {
		return enableRightsApplicationToGuiFields;
	}

	public void setEnableRightsApplicationToGuiFields(boolean enableRightsApplicationToGuiFields) {
		this.enableRightsApplicationToGuiFields = enableRightsApplicationToGuiFields;
	}

	public FocXMLLayout getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(FocXMLLayout parentLayout) {
		this.parentLayout = parentLayout;
		if(parentLayout != null){
			parentLayout.childXMLLayoutArray_Add(this);
		}
	}

	public void childXMLLayoutArray_Add(FocXMLLayout xmlLayout) {
		if(childLayoutArray == null){
			childLayoutArray = new ArrayList<FocXMLLayout>();
		}
		childLayoutArray.add(xmlLayout);
	}

	public void childXMLLayoutArray_Remove(FocXMLLayout xmlLayout) {
		if(childLayoutArray != null){
			childLayoutArray.remove(xmlLayout);
		}
	}

	public int childXMLLayoutArray_Size() {
		return (childLayoutArray != null) ? childLayoutArray.size() : 0;
	}

	public FocXMLLayout childXMLLayoutArray_Get(int at) {
		return (childLayoutArray != null) ? childLayoutArray.get(at) : null;
	}

	public boolean childXMLLayoutArray_Scan(IChildFocXMLLayoutScanner scanner) {
		boolean stop = false;
		if(scanner != null && childLayoutArray != null){
			for(int i = 0; i < childXMLLayoutArray_Size(); i++){
				FocXMLLayout layout = childXMLLayoutArray_Get(i);
				stop = scanner.before(layout);
				if(!stop){
					stop = layout.childXMLLayoutArray_Scan(scanner);
					if(!stop){
						stop = scanner.after(layout);
					}
				}
			}
		}
		return stop;
	}
	
	public interface IChildFocXMLLayoutScanner {
		public boolean before(FocXMLLayout layout);

		public boolean after(FocXMLLayout layout);
	}

	/**
	 * The GUI class extending FocXMLLayout can override this method take full
	 * control of the Add event By default it just calls the addItem in the
	 * TableTreeDelegate
	 * 
	 * @param tableName
	 * @param table
	 * @param fatherObject
	 * @return
	 */
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		return table != null && table.getTableTreeDelegate() != null ? table.getTableTreeDelegate().addItem_Internal(fatherObject) : null;
	}
	
	public void table_DeleteItem(ITableTree table, FocObject focObject){
		if(table != null && table.getTableTreeDelegate() != null){
			table.getTableTreeDelegate().delete_NoPopupConfirmation(focObject);
		}
	}
	
	public boolean table_LinkClicked(String tableName, ITableTree table, FVColumnGenerator colGen, FocObject focObject) {
		boolean consumed = false;
		return consumed;
	}	

	/**
	 * This method returns the same focObject in standard FocXMLLayout, but allows
	 * for tables on join object to transform the join FocObject coming from the table
	 * to the flat FocObject we need to open.
	 *  
	 * @param tableName
	 * @param table
	 * @param focObject
	 * @return
	 */
	public FocObject table_OpenItem_GetObjectToOpen(String tableName, ITableTree table, FocObject focObject) {
		return focObject; 
	}

	public XMLViewKey table_OpenItem_GetXMLViewKey(String tableName, ITableTree table, FocObject focObject) {
		return table != null && table.getTableTreeDelegate() != null ? table.getTableTreeDelegate().getXmlViewKey_Open() : null;
	}

	public boolean table_OpenItem_IsFocDataOwner(String tableName, ITableTree table, FocObject focObject) {
		return false;
	}
	
	public ICentralPanel table_OpenItem_ShowForm(String tableName, ITableTree table, FocObject focObject, XMLViewKey xmlViewKey_Open, int viewContainer_Open) {
		boolean focDataOwner = table_OpenItem_IsFocDataOwner(tableName, table, focObject);
		ICentralPanel panel = null;
		// panel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) getMainWindow(), xmlViewKey_Open, focObject);
		panel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey_Open, focObject);
		if (focDataOwner) {
			panel.setFocDataOwner(focDataOwner);
		}
		table.getTableTreeDelegate().openFormPanel(panel, viewContainer_Open);
		return panel;
	}

	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		ICentralPanel panel = null;
		if(table != null){
			table.setSelectedObject(focObject);
			if(table.getTableTreeDelegate() != null){
				focObject = table_OpenItem_GetObjectToOpen(tableName, table, focObject);
				XMLViewKey xmlViewKey_Open = table_OpenItem_GetXMLViewKey(tableName, table, focObject);
				
				if(viewContainer_Open == ITableTree.VIEW_CONTAINER_NEW_BROWSER_TAB) {
					FocWebApplication.getFocWebSession_Static().setPrintingData(null, xmlViewKey_Open, focObject, false);
					UI.getCurrent().getPage().open(UI.getCurrent().getPage().getLocation().getPath(), "_blank");
				}else {
					panel = table_OpenItem_ShowForm(tableName, table, focObject, xmlViewKey_Open, viewContainer_Open);
				}
			}
		}
		return panel;
	}
	
	public void table_ItemDoubleClick(String tableName, ITableTree table, ItemClickEvent event) {
		if(table != null){
			if(table.getTableTreeDelegate() != null){
				table.getTableTreeDelegate().defaultItemDoubleClickAction(event);
			}
		}
	}
	
	public ColumnGenerator table_getGeneratedColumn(String tableName, FVTableColumn tableColumn){
		return null;
	}

	/**
	 * The GUI class extending FocXMLLayout can override this method in case it
	 * wants to popup a dialog for the add or edit item
	 * 
	 * @param tableName
	 * @param table
	 * @param focObject
	 * @return
	 */
	public ICentralPanel table_NewCentralPanel_ForForm(String tableName, ITableTree table, FocObject focObject) {
		return null;
	}

	protected void table_BeforeEndElement(String tableName, ITableTree table) {
		
	}
	
	public boolean isEditable() {
		boolean retEditable = editable;
		if(getParentLayout() != null){
			retEditable = getParentLayout().isEditable();
		}
		return retEditable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public int getViewRights() {
		return viewRights;
	}

	@Override
	public void setViewRights(int viewRights) {
		this.viewRights = viewRights;
	}
	
	public INavigationWindow getNavigationWindow() {
		return findAncestor(FocCentralPanel.class);
	}
	
	public FocCentralPanel getCentralPanel(){
		return findAncestor(FocCentralPanel.class);
	}
	
	private void debug_PrintAllComponents_Local(){
		Globals.logString("COMPONENT MAP LISTING FOR DEBBUG:");
		Globals.logString("---------------------------------");
		Iterator iter = getComponentMap().keySet().iterator();
		while(iter != null && iter.hasNext()){
			String key = (String) iter.next();
			Globals.logString("COMPONENT MAP KEY = "+key);
		}
	}
	
	public void debug_PrintAllComponents(){
		debug_PrintAllComponents_Local();
	  childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
			
			public boolean before(FocXMLLayout layout) {
				layout.debug_PrintAllComponents_Local();
				return true;
			}
			
			public boolean after(FocXMLLayout layout) {
				return false;
			}
		});
	}

	public void debug_PrintFieldNames(){
		FocDesc focDesc = null;
		FocObject focObj = getFocObject();
		if(focObj != null){
			focDesc = focObj.getThisFocDesc();
		}else{
			FocList focList = getFocList();
			if(focList != null){
				focDesc = focList.getFocDesc();
			}
		}
		
		if(focDesc != null){
			Globals.logString("Dictionary First Level Fields:");
			FocFieldEnum fieldEnum = focDesc.newFocFieldEnum(FocFieldEnum.CAT_ALL, FocFieldEnum.LEVEL_PLAIN);
			while(fieldEnum != null && fieldEnum.hasNext()){
				FField fld = (FField) fieldEnum.nextField();
				Globals.logString("  - "+fld.getName());
			}
			Globals.logString("------------------------------");
		}
	}
	
	@Override
	public void refreshEditable() {
	}

	@Override
	public String getLinkSerialisation() {
		return linkSerialisation;
	}

	@Override
	public void setLinkSerialisation(String serialisation) {
		linkSerialisation = serialisation;	
	}

	public Properties getLinkSerialization_Properties(){
		if(linkSerialisationProperties == null && linkSerialisation != null && !linkSerialisation.isEmpty()){
			try{
				linkSerialisationProperties = new Properties();
				StringReader reader = new StringReader(linkSerialisation);
				linkSerialisationProperties.load(reader);
				reader.close();
		  }catch(Exception e){
		  	Globals.logException(e);
		  }
		}
		return linkSerialisationProperties;
	}
	
	public String getLinkSerialization_StringValue(String property){
		String value = null;
		Properties props = getLinkSerialization_Properties();
		if(props != null){
			value = (String) props.get(property);
		}
		return value;
	}
	
	@Override
	public void addedToNavigator() {
		if(FocWebApplication.getInstanceForThread().isMobile() && validationLayout != null){
			FocWebVaadinWindow focWebVaadinWindow = ((FocWebVaadinWindow)getMainWindow());
			if(focWebVaadinWindow != null && focWebVaadinWindow.getCentralHeader() != null){
				HorizontalLayout centralHeader = focWebVaadinWindow.getCentralHeader();
				centralHeader.addComponent(validationLayout);
				centralHeader.setComponentAlignment(validationLayout, Alignment.MIDDLE_RIGHT);
				centralHeader.setExpandRatio(validationLayout, 1);
				centralHeader.setWidth("100%");
			}
		}
	}

	@Override
	public void removedFromNavigator() {
		if(FocWebApplication.getInstanceForThread().isMobile() && validationLayout != null){
			FocWebVaadinWindow focWebVaadinWindow = ((FocWebVaadinWindow)getMainWindow());
			if(focWebVaadinWindow != null && focWebVaadinWindow.getCentralHeader() != null){
				HorizontalLayout centralHeader = focWebVaadinWindow.getCentralHeader();
				centralHeader.removeComponent(validationLayout);
			}
		}
	}

	@Override
	public void optionButtonClicked() {
		if(FocWebApplication.getInstanceForThread().isMobile() && getMainWindow() != null){
			FVOptionMobileLayout forMobile = new FVOptionMobileLayout((FocWebVaadinWindow) getMainWindow(), validationLayout);
			forMobile.init();
			FocWebVaadinWindow focWebVaadinWindow = ((FocWebVaadinWindow)getMainWindow());
			focWebVaadinWindow.getCentralHeader().setWidth("-1px");
			getMainWindow().changeCentralPanelContent(forMobile, true);
		}
	}

	public ITableTree getTableTreeThatOpenedThisForm() {
		return tableTreeThatOpenedThisForm;
	}

	public void setTableTreeThatOpenedThisForm(ITableTree tableTreeThatOpenedThisForm) {
		this.tableTreeThatOpenedThisForm = tableTreeThatOpenedThisForm;
	}

	public boolean isFocDataOwner() {
		return focDataOwner;
	}

	public void setFocDataOwner(boolean focDataOwner) {
		this.focDataOwner = focDataOwner;
	}
	
	public IFocData getFocDataToPrint(){
		return null;
	}

	@Override
	public boolean isGoBackRequested() {
		return goBackRequested;
	}

	@Override
	public void setGoBackRequested(boolean goBackRequested) {
		this.goBackRequested = goBackRequested;
	}
	
	public PrintingAction getPrintingAction(){
		PrintingAction printingAction = null;
//		if(getFocData() != null && getFocData() instanceof FocObject){
		if(getFocObject() != null){
//			FocObject focObject = (FocObject) getFocData();
			FocObject focObject = getFocObject();
			FocDesc focDesc = focObject.getThisFocDesc();
			printingAction = focDesc != null ? focDesc.newPrintingAction() : null;
		}
		return printingAction;
	}
	
	public Component getRootComponent(){
		Component rootComponent = null;
		if(getComponentCount() > 0){
			rootComponent = getComponent(0);
		}
		return rootComponent;
	}
	
	public static void popupInDialog(ICentralPanel contentPanel){
		if(contentPanel instanceof FocXMLLayout){
			((FocXMLLayout)contentPanel).popupInDialog(); 
		}else{
			popupInDialog(contentPanel, " ");
		}
	}
	
	public static void popupInDialog(ICentralPanel contentPanel, String title){
		popupInDialog(contentPanel, title, DEFAULT_DIALOG_WIDTH, DEFAULT_DIALOG_HEIGHT);
	}
	
	public static void popupInDialog(ICentralPanel contentPanel, String title, String width, String height){
		FocCentralPanel centralPanel = new FocCentralPanel();
		centralPanel.fill();
		centralPanel.changeCentralPanelContent(contentPanel, false);
		
		Window optionWindow = centralPanel.newWrapperWindow();
		optionWindow.setResizable(true);
		optionWindow.setCaption(title);
		
		optionWindow.setWidth(width);
		optionWindow.setHeight(height);
		//ATTENTION-MAN
//		optionWindow.getContent().setWidth(width);
//		optionWindow.getContent().setHeight(width);
		//ATTENTION-MAN
		
		FocWebApplication.getInstanceForThread().addWindow(optionWindow);
	}
	
	public void popupInDialog(){
		String width = DEFAULT_DIALOG_WIDTH;
	  String height = DEFAULT_DIALOG_HEIGHT;
	  popupInDialog(null, width, height);
	}
	
	public void popupInDialog(String title, String width, String height){
		Component rootComp = getRootComponent();
		if(rootComp instanceof FocXMLGuiComponent){
			FocXMLGuiComponent guiComp = (FocXMLGuiComponent) rootComp;
			String w = null;
			String h = null;
			if(guiComp.getAttributes() != null) {
				w = guiComp.getAttributes().getValue(FXML.ATT_WIDTH);
				h = guiComp.getAttributes().getValue(FXML.ATT_HEIGHT);
			}
			
			if(w != null && !w.endsWith("%")) width = w;
			if(h != null && !h.endsWith("%")){
				if(h.endsWith("px")){
					h = h.replace("px", "");
					int hInt = Utils.parseInteger(h, 0);
					if(hInt != -1){
						hInt += 35;
					}
//					hInt += 35;
					height = String.valueOf(hInt)+"px";
				}else{
					height = h+20;	
				}
			}
		}
		if(title == null) title = " ";
		if(getValidationSettings(false) != null){
			title = getValidationSettings(false).getTitle();
		}
		popupInDialog(FocXMLLayout.this, title, width, height);
	}
	
	private ArrayList<FVTableWrapperLayout> newArrayListOfTableWrapperLayouts(){
		//Fill array of FVTableWrapperLayout
		ArrayList<FVTableWrapperLayout> arrayList = new ArrayList<FVTableWrapperLayout>();
		Iterator<FocXMLGuiComponent> iter = getComponentMapIterator();
		while(iter != null && iter.hasNext()){
			FocXMLGuiComponent comp = iter.next();
			if(comp instanceof FVTableWrapperLayout){
				arrayList.add((FVTableWrapperLayout) comp);
			}
		}

		return arrayList;
	}
	
	private void innerLayout_AfterConstruction(){
		Iterator<FocXMLGuiComponent> iter = getComponentMapIterator();
		while(iter != null && iter.hasNext()){
			FocXMLGuiComponent comp = iter.next();
			if(comp instanceof FVTableWrapperLayout){
				((FVTableWrapperLayout)comp).innerLayout_AfterConstruction();
			}
		}
	}
	
	private boolean innerLayout_CommitOrCheckData(boolean commit){
		boolean error = false;
		
		//Fill array of FVTableWrapperLayout
		ArrayList<FVTableWrapperLayout> arrayList = newArrayListOfTableWrapperLayouts();

		//Commit Not empty items
		for(int i=0; i<arrayList.size() && !error; i++){
			FVTableWrapperLayout tableWrapperLayout = arrayList.get(i);
			ICentralPanel centralPanel = tableWrapperLayout.innerLayout_GetICentralPanel();
			if(centralPanel != null && centralPanel.getValidationLayout() != null){
				boolean callCommit = true;
				if(centralPanel.getFocData() instanceof FocObject){
					FocObject focObject = (FocObject) centralPanel.getFocData();
					if(focObject.isEmpty() && focObject.isCreated()){
						callCommit = false;
					}
				}
				if(callCommit){
					if(commit){
					  tableWrapperLayout.innerLayout_SetEnableAddEmptyItemAfterCommit(false);
						error = centralPanel.getValidationLayout().commit(false, true);
						tableWrapperLayout.innerLayout_SetEnableAddEmptyItemAfterCommit(true);
					}else{
						error = centralPanel.getValidationLayout().commit(true, false);
					}
				}
			}
		}
		
		arrayList.clear();
		
		return error;
	}
	
	@Override
	public ICentralPanel getRootCentralPanel() {
		FocXMLLayout layout = this;
		while(layout != null && layout.getParentLayout() != null){
			layout = layout.getParentLayout();
		}
  	return layout;
	}

	@Override
	public boolean isRootLayout() {
		return getRootCentralPanel() == this;
	}

	public boolean isCommitWithParent() {
		return commitWithParent;
	}

	public void setCommitWithParent(boolean commitWithParent) {
		this.commitWithParent = commitWithParent;
	}
	
/**
 * You can override this method  	
 * @return
 */
	public String beforeSigning() {
		String error = null;
		
		return error;
	}
	
	public void afterSigning() {
		
	}
	
	public boolean isInnerLayout() {
		FVTableWrapperLayout tableWrapperLayout = findAncestor(FVTableWrapperLayout.class);
		boolean inner = tableWrapperLayout != null;
		return inner; 
	}

	public FocXMLLayout getMainLayoutForInnerLayout() {
		FocXMLLayout xmlLayout = null;
		FVTableWrapperLayout tableWrapperLayout = findAncestor(FVTableWrapperLayout.class);
		xmlLayout = tableWrapperLayout != null ? tableWrapperLayout.findAncestor(FocXMLLayout.class) : null;
		return xmlLayout;
	}

	private void scanComponentsAndSetWYSIWYGDropHandlers() {
		Iterator<FocXMLGuiComponent> iter = getComponentMap().values().iterator();
		if(iter != null){
			while(iter.hasNext()){
  			FocXMLGuiComponent focXMLGuiComponent = iter.next();
				if(focXMLGuiComponent != null && focXMLGuiComponent instanceof FVLayout){
					((FVLayout)focXMLGuiComponent).setWYSIWYGDropHandler();
				}
			}
		}
		
		childXMLLayoutArray_Scan(new IChildFocXMLLayoutScanner() {
			@Override
			public boolean before(FocXMLLayout layout) {
				layout.scanComponentsAndSetWYSIWYGDropHandlers();
				return false;
			}

			@Override
			public boolean after(FocXMLLayout layout) {
				return false;
			}
		});		
	}
	
	public void setWYSIWYGActive(boolean active) {
		if(active) {
			scanComponentsAndSetWYSIWYGDropHandlers();
		}
	}
	
}
