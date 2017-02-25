package com.foc.vaadin.gui.components.chart;

import org.jfree.chart.JFreeChart;
import org.xml.sax.Attributes;

import com.foc.access.FocDataMap;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotTable;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.objectTree.FObjectTree;
import com.foc.vaadin.FocWebApplication;
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
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class FVChart extends FVVerticalLayout/*extends Chart */implements FocXMLGuiComponent, ITableTree{

	private PivotChartDelegate     pivotChartDelegate = null;
	private FPivotTable                pivotTable     = null;
	private FocXMLGuiComponentDelegate delegate       = null;
	private FocListWrapper             focListWrapper = null;
	private Attributes                 attributes     = null;
	
	public final static int CHART_TYPE_BAR    = 0;
	public final static int CHART_TYPE_COLUMN = 1;
	public final static int CHART_TYPE_LINE   = 2;
	public final static int CHART_TYPE_PIE    = 3;
	
	public abstract void drawFVChart();
	public abstract int  getChartType();
//	public abstract ChartType getChartType();
	
	public FVChart(Attributes attributes) {
		init(attributes);
//		getFVConfiguration().setChart(new ChartModel(getFVConfiguration(), getChartType()));
	}
	
	private void init(Attributes attributes){
		delegate = new FocXMLGuiComponentDelegate(this);
		getPivotChartDelegate().init(attributes, FVChart.this);
		setAttributes(attributes);
	}
	
	public PivotChartDelegate getPivotChartDelegate() {
    if(pivotChartDelegate == null){
    	pivotChartDelegate = new PivotChartDelegate();
    }
    return pivotChartDelegate;
  }

	public FocList getFocList(){
    return (focListWrapper != null) ? focListWrapper.getFocList() : null; 
  }
	
	private void setDataFocList(FocList list){
		if(pivotTable == null){
			pivotTable = new FPivotTable(list);
		}else{
			pivotTable.dispose_Contents();
			pivotTable.setNativeDataFocList(list);
		}
	}

	public FPivotTable getPivotTable() {
		return pivotTable;
	}

	public String getChartTitle(){
		return getAttributes() != null ? getAttributes().getValue(FXML.ATT_CHART_TITLE) : null;
	}

	//-----------------------------------------------------
	//----------------FocXMLGuiComponent-------------------
	//-----------------------------------------------------
	@Override
	public void dispose() {
		super.dispose();
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		
		if(focListWrapper != null){
//			focListWrapper.dispose();
      focListWrapper = null;
    }
		
		if(pivotTable != null){
			pivotTable.dispose();
			pivotTable = null;
		}
		
		if(pivotChartDelegate != null){
			pivotChartDelegate.dispose();
			pivotChartDelegate = null;
		}
		
		attributes = null;
	}

	@Override
	public IFocData getFocData() {
		return null;
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

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}
	
	@Override
	public void setFocData(IFocData focData) {
		if(focData != null){
			if(focData instanceof FocDataMap){
				FocDataMap focDataMap = (FocDataMap) focData;
				focData = focDataMap.getMainFocData();
			}
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
	public String getXMLType() {
		return FXML.TAG_CHART;
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
	//-----------------------------------------------------
	
	//-----------------------------------------------------
	//------------------ITableTree-------------------------
	//-----------------------------------------------------
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
	public FocDataWrapper getFocDataWrapper() {
		return null;
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
	//-----------------------------------------------------

	//-----------------------------------------------------
  //--------------------VAADIN CHART---------------------
//	public Configuration getFVConfiguration(){
//		return getConfiguration();
//	}
	
//	private void addLegend(){
//		Legend legend = new Legend();
//		legend.setLayout(LayoutDirection.VERTICAL);
//		legend.setBackgroundColor("#FFFFFF");
//		legend.setHorizontalAlign(HorizontalAlign.LEFT);
//		legend.setVerticalAlign(VerticalAlign.TOP);
//		legend.setX(50);
//		legend.setY(0);
//		legend.setFloating(true);
//		legend.setShadow(true);
//		getFVConfiguration().setLegend(legend);
//	}
	
	public void refreshRowCache_Foc(){

	}
	
	@Override
	public FocObject getSelectedObject() {
		return null;
	}

	@Override
	public void addItemClickListener(ItemClickListener itemClickListener) {
		
	}
	
	public void attachChart(JFreeChart chartToBeWrapped){
		org.vaadin.addon.JFreeChartWrapper jFreeChartWrapper = new org.vaadin.addon.JFreeChartWrapper(chartToBeWrapped){
			
			@Override
			public UI getUI() {
				return FocWebApplication.getInstanceForThread();
			}
			
			@Override
      public void attach() {
          super.attach();
          setResource("src", getSource());
      }
		};
		
		jFreeChartWrapper.attach();
		addComponent(jFreeChartWrapper);
	}
}
