package com.foc.vaadin.gui.components.pivot;

import org.xml.sax.Attributes;

import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.chart.FVChart;
import com.foc.vaadin.gui.components.report.FVReport;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class PivotChartDelegate {

	public static final int PIVOT_OPERATION_SUM = 1;

	private FPivotRowTree pivotRowTree = null;
//	private FPivotTable   pivotTable   = null;
	private ITableTree    treeOrTable  = null;
	
	private boolean removeEmptyNodes = true;
	private boolean removeOnlyChild  = false;
	private boolean collapsed        = false;
	
	private int operation = -1;
	
	public PivotChartDelegate() {
	}

	public void dispose(){
		pivotRowTree = null;
		treeOrTable = null;
	}
	
	public void init(Attributes attributes, ITableTree treeOrTable) {
		setTableTree(treeOrTable);
	}
	
	public ITableTree getTableTree(){
		return treeOrTable;
	}
	
	private void setFPivotRowTree(FPivotRowTree pivotRowTree){
		this.pivotRowTree = pivotRowTree;
	}
	
	public FPivotRowTree getFPivotRowTree(){
		if(pivotRowTree == null){
			FPivotTable pivotTable = getFPivotTable();
			if(pivotTable != null){
				pivotRowTree = pivotTable.growPivotRowTree();
			}
		}
		return pivotRowTree;
	}
	
	private void setTableTree(ITableTree treeOrTable){
		this.treeOrTable = treeOrTable;
	}
	
	public FPivotTable getFPivotTable(){
		FPivotTable pivotTable = null;
		if(getTableTree() != null){
			if(getTableTree() instanceof FVPivotTable){
				pivotTable = ((FVPivotTable)getTableTree()).getPivotTable();
			}else if(getTableTree() instanceof FVChart){
				pivotTable = ((FVChart)getTableTree()).getPivotTable();
			}else if(getTableTree() instanceof FVReport){
				pivotTable = ((FVReport)getTableTree()).getPivotTable();
			}
		}
		return pivotTable;
	}

	public void applyFocListAsContainer() {
		FPivotTable pivotTable = getFPivotTable();
		if(pivotTable != null){
			FPivotRowTree pivotRowTree = pivotTable.growPivotRowTree();
			if(pivotRowTree != null){
				setFPivotRowTree(pivotRowTree);
				pivotRowTree.compactScan(shouldRemoveEmptyNodes(), shouldCollapse(), shouldRemoveOnlyChild());
			
				/*
	  		switch (getOperation()) {
	  		case PIVOT_OPERATION_SUM: {
	  			pivotRowTree.sumScan();
	  			break;
	  			}
	  		}
	  		*/
	  		
	  		
	  		// getPivotTable().getTree().propagateSameStringsScan();
	  		// getPivotTable().getTree().propagateSamePropertyScan();
	  		pivotRowTree.sort();
			}
		}
	}
	
	public boolean shouldRemoveEmptyNodes() {
		return removeEmptyNodes;
	}
	
	public void setRemoveEmptyNodes(boolean remove) {
		removeEmptyNodes = remove;
	}
	
	public boolean shouldCollapse() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	public boolean shouldRemoveOnlyChild() {
		return removeOnlyChild;
	}
	
	public void setRemoveOnlyChild(boolean remove){
		removeOnlyChild = remove;
	}

	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		FPivotTable pivotTable = getFPivotTable();
		if(pivotTable != null){
			String dataPath = attributes.getValue(FXML.ATT_DATA_PATH);
			if(dataPath == null || dataPath.isEmpty()){
				dataPath = attributes.getValue(FXML.ATT_NAME);
			}
			pivotTable.getPivotView().addValue(attributes.getValue(FXML.ATT_CAPTION), dataPath, attributes.getValue(FXML.ATT_VALUE_COMPUTE_LEVEL), attributes.getValue(FXML.ATT_PIVOT_AGGREGATION_FORMULA), attributes.getValue(FXML.ATT_FORMULA));
		}
		return null;
	}
	
	public int getOperation() {
		return operation;
	}
	
	public void setOperation(int operation) {
		this.operation = operation;
	}
}
