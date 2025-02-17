/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.components.chart;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.foc.dataDictionary.IFocDataResolver;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotRow;
import com.foc.pivot.FPivotRowNode;
import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.pivot.FPivotValue;
import com.foc.pivot.IFocObjectDebug;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.objectTree.FObjectTree;
import com.foc.util.Utils;
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
public class FVPivotLayout extends FVVerticalLayout implements FocXMLGuiComponent, ITableTree{

	private PivotChartDelegate         pivotChartDelegate = null;
	private FPivotTable                pivotTable     = null;
	private FocXMLGuiComponentDelegate delegate       = null;
	private FocListWrapper             focListWrapper = null;
	private Attributes                 attributes     = null;
	
	private boolean debugPrinted = false;
	
	public FVPivotLayout(Attributes attributes) {
		init(attributes);
	}
	
	private void init(Attributes attributes){
		delegate = new FocXMLGuiComponentDelegate(this);
		setAttributes(attributes);
		
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
	
	public PivotChartDelegate getPivotChartDelegate() {
    if(pivotChartDelegate == null){
    	pivotChartDelegate = new PivotChartDelegate();
    }
    return pivotChartDelegate;
  }

	public void createDictionaryResolver(FocXMLLayout xmlLayout){
		if(xmlLayout != null){
			
			xmlLayout.getFocDataDictionary(true).putParameter("PIVOT", new IFocDataResolver() {
				public Object getValue(IFocData focData, ArrayList<String> arguments) {
					Object value = ""; 
					if(arguments != null && arguments.size() == 2){
						FPivotTable pivotTable = getPivotTable();
						if(pivotTable != null && getPivotChartDelegate() != null){
							FPivotRowTree pivotRowTree = pivotTable.getTree(true);
	
							if(!debugPrinted){
								debugPrinted = true;
								pivotTable.debug(new IFocObjectDebug(){
									@Override
									public void debug(FocObject focObject, StringBuffer buffer) { // adapt_notQuery
										if(focObject != null){
											FProperty prop = focObject.getFocPropertyForPath("MVT_DEBIT");
											buffer.append("Db="+prop.getString()+" ");
											
											prop = focObject.getFocPropertyForPath("MVT_CREDIT");
											buffer.append("Cr="+prop.getString()+" ");
										}
									}
								});
							}
							
							FPivotRowNode node         = pivotRowTree.getRoot();
							
							String arg1 = arguments.get(0);
							String dataPath = arguments.get(1);
							
							StringTokenizer stringTokenizer = new StringTokenizer(arg1, ".");
							
							while(node != null && stringTokenizer.hasMoreTokens()){
								String nodeTitle = stringTokenizer.nextToken();
								node = node.findChild(nodeTitle);
							}
							
							if(node != null){
								FPivotRow childPivotRow = node.getObject();
								FocObject focObj        = childPivotRow.getPivotRowObject();
								
								FProperty property = focObj.getFocPropertyForPath(dataPath);
								if(property != null){
									value = property.iFocData_getValue();
//									value = property.getString();
								}
							}
						}
					}
					return value;	
				}
			});
		}
	}
	
	//-----------------------------------------------------
	//----------------FocXMLGuiComponent-------------------
	//-----------------------------------------------------
	@Override
	public void dispose() {
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		
		if(focListWrapper != null){
      focListWrapper.dispose();
      focListWrapper = null;
    }
		
		if(pivotTable != null){
			pivotTable.dispose();
			pivotTable = null;
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
			if(focData instanceof FocList){
				setDataFocList((FocList) focData);
			}else if(focData instanceof FObjectTree){
				setDataFocList(((FObjectTree)focData).getFocList());
			}
      if(focData instanceof FocListWrapper){
        this.focListWrapper = (FocListWrapper) focData;
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
		return FXML.TAG_PIVOT_LAYOUT;
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
	public void delete(long ref) {
	}

	@Override
	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		if(pivotTable != null){
			FPivotValue valueColumn = pivotTable.getPivotView().addValue(attributes.getValue(FXML.ATT_CAPTION), attributes.getValue(FXML.ATT_NAME), attributes.getValue(FXML.ATT_VALUE_COMPUTE_LEVEL), attributes.getValue(FXML.ATT_PIVOT_AGGREGATION_FORMULA), attributes.getValue(FXML.ATT_FORMULA));
		}
		return null;
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
	//-----------------------------------------------------

	@Override
	public FocObject getSelectedObject() {
		return null;
	}
	
	@Override
	public void setSelectedObject(FocObject selectedObject) {
	}
	
	@Override
	public void addItemClickListener(ItemClickListener itemClickListener) {
		
	}
}
