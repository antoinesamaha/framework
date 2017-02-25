package com.foc.vaadin.gui.components.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotBreakdown;
import com.foc.pivot.FPivotRow;
import com.foc.pivot.FPivotRowNode;
import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.TreeScanner;
import com.foc.tree.objectTree.FObjectTree;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.pivot.PivotChartDelegate;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVReport extends FVVerticalLayout implements FocXMLGuiComponent, ITableTree{

	private FocXMLGuiComponentDelegate delegate   = null;
	private PivotChartDelegate pivotChartDelegate = null;
	private FocListWrapper     focListWrapper     = null;
	private FPivotTable        pivotTable         = null;
	private FocXMLLayout       focXMLLayout       = null;
	private Attributes         attributes         = null;
	private HashMap<String, FVReportGroup> breakdownName_GroupHashMap  = null;
	
	public FVReport(Attributes attributes, FocXMLLayout focXMLLayout) {
		setFocXmlLayout(focXMLLayout);
		init(attributes);
	}
	
	private void init(Attributes attributes){
		delegate = new FocXMLGuiComponentDelegate(this);
		getPivotChartDelegate().init(attributes, this);
		setAttributes(attributes);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		focListWrapper = null;
		focXMLLayout   = null;
		if(pivotTable != null){
			pivotTable.dispose();
			pivotTable = null;
		}
		if(pivotChartDelegate != null){
			pivotChartDelegate.dispose();
			pivotChartDelegate = null;
		}
		attributes = null;
		if(breakdownName_GroupHashMap != null){
			Iterator<String> iterator = breakdownName_GroupHashMap.keySet().iterator();
			while(iterator.hasNext()){
				String breakdownName = iterator.next();
				FVReportGroup iReport_Group = breakdownName_GroupHashMap.get(breakdownName);
				if(iReport_Group != null){
					iReport_Group.dispose();
				}
			}
		}
	}
	
	public void setFocXmlLayout(FocXMLLayout focXMLLayout){
		this.focXMLLayout = focXMLLayout;
	}
	
	public FocXMLLayout getFocXmlLayout(){
		return focXMLLayout;
	}
	
	public void build(){
		getReportValues();
	}

	public FVReportGroup pushReportGroup(String breakdownName){
		FVReportGroup reportGroup = null;
		if(breakdownName != null && !breakdownName.isEmpty()){
			reportGroup = getBreakdownName_GroupHashMap(true).get(breakdownName);
			if(reportGroup == null){
				reportGroup = new FVReportGroup(this);
				getBreakdownName_GroupHashMap(true).put(breakdownName, reportGroup);	
			}
		}
		return reportGroup;
	}
	
	public FVReportGroup newReportGroup(FocXMLAttributes xmlAttributes){
		FVReportGroup reportGroupHeader = null;
		if(xmlAttributes != null){
			String breakdownName = xmlAttributes.getValue(FXML.ATT_BREAKDOWN_NAME);
			if(breakdownName != null){
				reportGroupHeader = getBreakdownName_GroupHashMap(true).get(breakdownName);
				if(reportGroupHeader == null){
					reportGroupHeader = new FVReportGroup(this);
					
					getBreakdownName_GroupHashMap(true).put(breakdownName, reportGroupHeader);	
				}
			}
		}
		return reportGroupHeader;
	}
	
	@Override
	public void setFocData(IFocData focData) {
		if(focData != null){
			if(focData instanceof FocList){
				setDataFocList((FocList) focData);
			}else if(focData instanceof FObjectTree){
				setDataFocList(((FObjectTree)focData).getFocList());
			}
      if(focData instanceof FocListWrapper){
        this.focListWrapper = (FocListWrapper) focData;
        FocList focList = ((FocListWrapper) focData).getFocListFiltered();
        setDataFocList(focList);
      }else if(focData instanceof FocList){
        FocList list = (FocList) focData;
        list.loadIfNotLoadedFromDB();      
        this.focListWrapper = new FocListWrapper(list);
      }
    }else{
      this.focListWrapper = null;
    }
	}
	
	@Override
	public FVCheckBox getEditableCheckBox() {
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
	}

	@Override
	public FocDesc getFocDesc() {
		return null;
	}

	@Override
	public FocList getFocList() {
		return (focListWrapper != null) ? focListWrapper.getFocList() : null;
	}

	@Override
	public FocDataWrapper getFocDataWrapper() {
		return focListWrapper;
	}

	@Override
	public void open(FocObject focObject) {
	}

	@Override
	public void delete(int ref) {
	}

	@Override
	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		return getPivotChartDelegate().addColumn(attributes);
	}

	@Override
	public TableTreeDelegate getTableTreeDelegate() {
  	return null;
  }

	@Override
	public void applyFocListAsContainer() {
		getPivotChartDelegate().applyFocListAsContainer();		
	}

	@Override
	public void computeFooter(FVTableColumn col) {
	}

	@Override
	public void refreshRowCache_Foc() {
	}

	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return false;
	}
	
	@Override
	public String getXMLType() {
		return FXML.TAG_REPORT;
	}
	
	private void setDataFocList(FocList list){
		if(pivotTable == null){
			pivotTable = new FPivotTable(list);
		}else{
			pivotTable.dispose_Contents();
			pivotTable.setNativeDataFocList(list);
			pivotTable.init();
		}
	}
	
	public FPivotTable getPivotTable() {
		return pivotTable;
	}
	
	public PivotChartDelegate getPivotChartDelegate() {
    if(pivotChartDelegate == null){
    	pivotChartDelegate = new PivotChartDelegate();
    }
    return pivotChartDelegate;
  }
	
	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}

	@Override
	public IFocData getFocData() {
		return null;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);		
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void setValueString(String value) {
	}

	@Override
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate;		
	}
	
	public FVReportGroup getReportGroup(String breakdownName){
		return getBreakdownName_GroupHashMap(false).get(breakdownName);
	}
	
	private HashMap<String, FVReportGroup> getBreakdownName_GroupHashMap(boolean create){
		if(breakdownName_GroupHashMap == null && create){
			breakdownName_GroupHashMap = new HashMap<String, FVReportGroup>();
		}
		return breakdownName_GroupHashMap;
	}
	
	private void getReportValues(){
		PivotChartDelegate pivotChartDelegate = getPivotChartDelegate();
		FPivotRowTree pivotRowTree  = pivotChartDelegate != null ? pivotChartDelegate.getFPivotRowTree() : null;
		FPivotTable   pivotTable    = pivotChartDelegate != null ? pivotChartDelegate.getFPivotTable()   : null;
		
		if(pivotTable != null && pivotRowTree != null){
			pivotRowTree.scan(new TreeScanner<FPivotRowNode>() {

				@Override
				public boolean beforChildren(FPivotRowNode node) {
					layoutBreakdownSection(node, true);
					return true;
				}

				@Override
				public void afterChildren(FPivotRowNode node) {
					layoutBreakdownSection(node, false);
				}
				
				public void layoutBreakdownSection(FPivotRowNode node, boolean header){
					FPivotRow pivotRow = node != null ? node.getObject() : null;
					if(pivotRow != null){
						FPivotBreakdown breakdown = pivotRow.getPivotBreakdownStart();
						HashMap<String, FVReportGroup> map = getBreakdownName_GroupHashMap(false);
						if(map != null && breakdown != null){
//							FVReportGroup repGroup = map.get(breakdown.getName());
							String groupByName = breakdown.getGroupBy();
							if(groupByName == null || groupByName.isEmpty()){
								groupByName = breakdown.getName();
							}
							FVReportGroup repGroup = map.get(groupByName);
							if(repGroup != null){
								String xmlString = header ? repGroup.getHeaderXML() : repGroup.getFooterXML();
								if(xmlString != null && !xmlString.isEmpty()){
//									Uncomment For Debug
									Globals.logString("Node Name = "+node.getTitle()+" breakdown ="+groupByName);
									Globals.logString("  -Parsing This Section:"+xmlString+"THE END");
									ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes());
									FocXMLLayoutForReport layout = new FocXMLLayoutForReport(bais);
									layout.init(getWindow(), null, pivotRow);
									layout.parseXMLAndBuildGui();
									addComponent(layout);
								}
							}
						}
					}					
				}
			});
		}
	}
	
	public class FocXMLLayoutForReport extends FocXMLLayout {
		
		private InputStream xmlInputStream = null; 
		
		public FocXMLLayoutForReport(InputStream xmlInputStream){
			this.xmlInputStream = xmlInputStream;
		}
		
		public void dispose(){
			super.dispose();
			xmlInputStream = null;
		}

		@Override
		public void parseXMLAndBuildGui() {
			beforeLayoutConstruction();
			try{
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(xmlInputStream, new FocXMLHandler());
			}catch(Exception e){
				Globals.logException(e);
				goBack(null);
			}
			mapDataPath2ListenerAction_ApplyVisibilityFormulas();
			afterLayoutConstruction();
		}
 
	}

	@Override
	public FocObject getSelectedObject() {
		return null;
	}
	
	@Override
	public void addItemClickListener(ItemClickListener itemClickListener) {
		
	}
}
