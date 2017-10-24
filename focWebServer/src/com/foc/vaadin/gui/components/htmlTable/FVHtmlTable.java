package com.foc.vaadin.gui.components.htmlTable;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FNumField;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVHtmlTable extends VerticalLayout implements FocXMLGuiComponent, ITableTree{

	private IFocData                   iFocData   = null;
	private Attributes                 attributes = null;
	private FocXMLGuiComponentDelegate delegate   = null;
	private TableTreeDelegate   tableTreeDelegate = null;
	private FocListWrapper      focListWrapper    = null;
	private FVHtmlTableTags     htmlTableTags     = null;
	private boolean        focListWrapper_Owner   = false;
	private ArrayList<FVHtmlTableRow> htmlTableRowsList = null;
	
	public FVHtmlTable(Attributes attributes) {
		this.attributes = attributes;
		this.htmlTableTags = new FVHtmlTableTags(attributes);
		getTableTreeDelegate(attributes);
  	delegate = new FocXMLGuiComponentDelegate(this);
    init(attributes);
	}
	
	private void init(Attributes attributes){
		setSizeFull();
		if(getHtmlTableTags() != null){
			getHtmlTableTags().openHtmlTag();
			getHtmlTableTags().openHeadTag();
			
			getHtmlTableTags().openCssTag();
			getCssStyleContent();
			getHtmlTableTags().closeCssTag();
			
			getHtmlTableTags().openJavaScriptTag();
			getJavaScriptContent();
			getHtmlTableTags().closeJavaScriptTag();
			
			getHtmlTableTags().closeHeadTag();
			getHtmlTableTags().openBodyTag(); 
		}
	}
	
	@Override
	public void dispose() {
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		if(tableTreeDelegate != null){
			tableTreeDelegate.dispose();
			tableTreeDelegate = null;
		}
		if(focListWrapper != null){
			if(focListWrapper_Owner) focListWrapper.dispose();
			focListWrapper = null;
		}
		if(htmlTableTags != null){
			htmlTableTags.dispose();
			htmlTableTags = null;
		}
		if(htmlTableRowsList != null){
			for(int i=0; i<htmlTableRowsList.size(); i++){
				FVHtmlTableRow htmlTableRow = htmlTableRowsList.get(i);
				if(htmlTableRow != null){
					htmlTableRow.dispose();
					htmlTableRow = null;
				}
			}
			htmlTableRowsList.clear();
			htmlTableRowsList = null;
		}
		attributes = null;
		iFocData   = null;
	}

	public FVHtmlTableTags getHtmlTableTags(){
		return htmlTableTags;
	}
	
	private void getCssStyleContent(){
//		getHtmlTableTags().getHtmlStringBuilder().append("th{position: fixed;}");
//		getHtmlTableTags().getHtmlStringBuilder().append("table{table-layout: fixed; page-break-inside:auto;}");
		getHtmlTableTags().getHtmlStringBuilder().append("table{table-layout: fixed; page-break-inside:avoid;}");
		getHtmlTableTags().getHtmlStringBuilder().append("td{overflow: hidden; }");
		getHtmlTableTags().getHtmlStringBuilder().append("tr { page-break-inside:avoid; page-break-after:auto; }");
		getHtmlTableTags().getHtmlStringBuilder().append("thead {display: table-header-group; hidden; }");
		getHtmlTableTags().getHtmlStringBuilder().append("tfoot { display:table-footer-group; }");
		getHtmlTableTags().getHtmlStringBuilder().append("th{font-weight:bold; background-color:silver;}");
	}
	
	private String getJavaScriptContent(){
		return "";
	}
	
	@Override
	public IFocData getFocData() {
		return iFocData;
	}

	@Override
  public void setFocData(IFocData focData) {
    if(focData != null){
      if(focData instanceof FocListWrapper){
        this.focListWrapper = (FocListWrapper) focData;
        focListWrapper_Owner = false;
      }else if(focData instanceof FocList){
        FocList list = (FocList) focData;
        list.loadIfNotLoadedFromDB();
        this.focListWrapper = new FocListWrapper(list);
        focListWrapper_Owner = true;        
      }
      if(focListWrapper != null){
      	getTableTreeDelegate().adjustFocDataWrappers_FilterAndSroting();
//      	focListWrapper.setTableTree(this);
      }
    }else{
      this.focListWrapper = null;
    }
  }

	@Override
	public String getXMLType() {
		return FXML.TAG_HTML_TABLE;
	}

	@Override
	public Field<?> getFormField() {
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
	public void refreshEditable() {
		/*if(getTableTreeDelegate() != null){
			getTableTreeDelegate().refreshEditable();
		}*/
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
	public FVCheckBox getEditableCheckBox() {
		return getTableTreeDelegate().getEditableCheckBox();
	}

	@Override
	public void setEditable(boolean editable) {
		if(getDelegate() != null){
			getDelegate().setEditable(editable);
		}
	}

	@Override
	public FocDesc getFocDesc() {
		return getFocList() != null ? getFocList().getFocDesc() : null;
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
		getTableTreeDelegate().open(focObject);//remove		
	}

	@Override
	public void delete(long ref) {
		getFocList().removeItem(ref);//remove		
	}

	@Override
	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		FVTableColumn tableColumn = getTableTreeDelegate() != null ? getTableTreeDelegate().addColumn(attributes) : null;
		return tableColumn;
	}

	//TODO
	@Override
	public void applyFocListAsContainer() {
//  	getTableTreeDelegate().setContainerDataSource(focListWrapper);
    setAttributes(getAttributes());
    
  	drawTable();
	}
	
	private ArrayList<FVHtmlTableRow> getHtmlTableRowsList(){
		if(htmlTableRowsList == null){
			htmlTableRowsList = new ArrayList<FVHtmlTableRow>();
		}
		return htmlTableRowsList;
	}
	
	public FVHtmlTableRow newRow(FocObject focObject, int index){
		FVHtmlTableRow htmlTableRow = new FVHtmlTableRow(focObject, this, index);
		getHtmlTableRowsList().add(htmlTableRow);
		return htmlTableRow;
	}
	
	public void openTableTag(){
		if(getHtmlTableTags() != null){
			getHtmlTableTags().openTableTag();
		}
	}
	
	public void closeTableTag(){
		if(getHtmlTableTags() != null){
			getHtmlTableTags().closeTableTag();
		}
	}
	
	private void drawTable(){
		FocDataWrapper focDataWrapper = getFocDataWrapper();
		if(focDataWrapper != null){
			FVTableColumn   tableColumn   = null;
			FVHtmlTableRow htmlTableRow   = null;
			FVHtmlTableCell htmlTableCell = null;
			
			//Table Header
			openTableTag();
			htmlTableRow = newRow(null, -1);
			for(int j=0; j<getTableTreeDelegate().getVisiblePropertiesArrayList().size(); j++){
				tableColumn = getTableTreeDelegate().getVisiblePropertiesArrayList().get(j);
				if(tableColumn != null){
					htmlTableRow.newHeaderCell(tableColumn);
				}
			}
			htmlTableRow.closeRowTag();

			//Table Content
			FocObject focObject = null;
			for(int rowIndex=0; rowIndex<focDataWrapper.size(); rowIndex++){
				focObject = focDataWrapper.getAt(rowIndex);
				if(focObject != null){
					htmlTableRow = newRow(focObject, rowIndex);
					for(int columnIndex=0; columnIndex<getTableTreeDelegate().getVisiblePropertiesArrayList().size(); columnIndex++){
						tableColumn = getTableTreeDelegate().getVisiblePropertiesArrayList().get(columnIndex);
						if(tableColumn != null){
							htmlTableCell = htmlTableRow.newCell(tableColumn);
						}
					}
					htmlTableRow.closeRowTag();
				}
			}
			closeTableTag();
			getHtmlTableTags().closeBodyTag();
			getHtmlTableTags().closeHtmlTag();
		}
		
		try {
			CustomLayout customLayout = new CustomLayout(getHtmlTableTags().getTableByteArrayInputStream());
			customLayout.setSizeFull();
//			customLayout.setHeight("-1px");
			addComponent(customLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void computeFooter(FVTableColumn col) {
		String footerFormula = col.getFooterFormula();
    if(footerFormula != null){
      footerFormula = footerFormula.toUpperCase();
      if(footerFormula.equals("SUM")){
        double value = 0;
        for(int i=0; i<getFocListWrapper().size(); i++){
          FocObject obj = getFocListWrapper().getAt(i);
          if(col.isColumnFormula()){
          	Object objValue = col.computeFormula_ForFocObject(obj);
          	try{
          		value += (Double)objValue; 
          	}catch(Exception e){
          		
          	}
          }else{
            IFocData focData = obj.iFocData_getDataByPath(col.getDataPath());
            if(focData instanceof FProperty){
              FProperty property = (FProperty)focData;
              value += property != null ? property.getDouble() : 0;
            }
          }
        }
        Format format = FNumField.newNumberFormat(20, 2);
//        FVTable.this.setColumnFooter(col.getName() , format.format(value));
      } else if(footerFormula.startsWith("PARENT.")){
        FocObject focObject = (FocObject) getFocList().getFatherSubject();
        String   path = footerFormula.substring(7);
        FProperty property = focObject.getFocPropertyForPath(path);
//        FVTable.this.setColumnFooter(col.getName(), property.getString());
      }
    }		
	}

	@Override
	public void refreshRowCache_Foc() {
	}

	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return getFocDataWrapper() != null ? getFocDataWrapper().setRefreshGuiDisabled(disabled) : false;
	}
	
	public FocListWrapper getFocListWrapper() {
    return (FocListWrapper) getFocDataWrapper();
  }
	
	public void setColumnHeader(Object propertyId, String header) {
	}
	
	public void setColumnWidth(Object propertyId, int width) {
	}
	
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
