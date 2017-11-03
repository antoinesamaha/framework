package com.foc.vaadin.gui.tableExports;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FBoolean;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroupPopupView;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.web.dataModel.FocTreeWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;

public abstract class FVTableTreeEXCELExport extends EXCELExport {
	private ArrayList<FVTableColumn> columnsArrayList = null;

	private TableTreeDelegate tableTreeDelegate = null;

	public abstract void scan();

	public FVTableTreeEXCELExport(TableTreeDelegate paramTableTreeDelegate) {
		this.tableTreeDelegate = paramTableTreeDelegate;
		init();
	}

	public void dispose() {
		if(this.tableTreeDelegate != null){
			this.tableTreeDelegate = null;
		}
		this.columnsArrayList = null;
		super.dispose();
	}

	protected String getFileName() {
		return getTableName();
	}

	protected void fillFile() {
		if(this.tableTreeDelegate != null && this.tableTreeDelegate.isRTL()){
			setRightToLeft(true);
		}
		addNewLine();
		setTableTreeColumnsTitle();
		scan();
	}

	protected int getColumnCount(){
		return getVisibleColumns() != null ? getVisibleColumns().size() : 0;
	}
	
	protected FVTableColumn getColumnAt(int index){
		FVTableColumn col = null;
		if(getVisibleColumns() != null){
			if(tableTreeDelegate != null && tableTreeDelegate.isRTL()){
				col = getVisibleColumns().get(getVisibleColumns().size() - index - 1);
			}else{
				col = getVisibleColumns().get(index);
			}
		}
		return col;
	}
	
	protected void setTableTreeColumnsTitle() {
		try{
			for(int i = 0; i < getColumnCount(); i++){
				FVTableColumn localFVTableColumn = getColumnAt(i);
				if(localFVTableColumn != null){
					String str = localFVTableColumn.getCaption();
					addCellValue(str);
				}
			}
			addNewLine();
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}

	public String getTableName() {
		String str = null;
		TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
		if(localTableTreeDelegate != null){
			str = localTableTreeDelegate.getTableName();
			if((str == null) || (str.isEmpty())){
				str = "table_to_excel";
			}
		}
		return str;
	}

	public TableTreeDelegate getTableTreeDelegate() {
		return this.tableTreeDelegate;
	}

	public ArrayList<FVTableColumn> getVisibleColumns() {
		if(this.columnsArrayList == null){
			TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
			if(localTableTreeDelegate != null){
				this.columnsArrayList = localTableTreeDelegate.getVisiblePropertiesArrayList();
			}
		}
		return this.columnsArrayList;
	}

	public FocList getFocList() {
		FocList localFocList = null;
		TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
		if(localTableTreeDelegate != null){
			localFocList = localTableTreeDelegate.getTreeOrTable().getFocList();
		}
		return localFocList;
	}

	public ITableTree getTableOrTree() {
		ITableTree localITableTree = null;
		TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
		if(localTableTreeDelegate != null){
			localITableTree = localTableTreeDelegate.getTreeOrTable();
		}
		return localITableTree;
	}

	public FVTreeTable getFVTreeTable() {
		FVTreeTable localFVTreeTable = null;
		ITableTree localITableTree = getTableOrTree();
		if(localITableTree != null){
			localFVTreeTable = (FVTreeTable) localITableTree;
		}
		return localFVTreeTable;
	}

	public FocTreeWrapper getFocTreeWrapper() {
		FocTreeWrapper localFocTreeWrapper = null;
		FVTreeTable localFVTreeTable = getFVTreeTable();
		if(localFVTreeTable != null){
			localFocTreeWrapper = localFVTreeTable.getFocTreeWrapper();
		}
		return localFocTreeWrapper;
	}

	public String getPropertyStringValue(FocObject paramFocObject, FVTableColumn paramFVTableColumn) {
		String str = "";
		if((paramFVTableColumn != null) && (paramFocObject != null)){
			Object localObject1;
			if(paramFVTableColumn.isColumnFormula()){
				localObject1 = paramFVTableColumn.computeFormula_ForFocObject(paramFocObject);
				if(localObject1 != null){
					str = String.valueOf(localObject1);
				}
			}else{
				Object localObject2;
				if(paramFVTableColumn.getDataPath().equals("_COUNT")){
					localObject1 = (FVTreeTable) getTableOrTree();
					if((localObject1 != null) && (paramFocObject.getReference() != null)){
						int i = paramFocObject.getReference().getInteger();
						if(((FVTreeTable) localObject1).hasChildren(Integer.valueOf(i))){
							localObject2 = Integer.valueOf(((FVTreeTable) localObject1).getChildren(Integer.valueOf(i)).size());
							str = String.valueOf(localObject2);
						}
					}
				}else{
					localObject1 = (getTableTreeDelegate() != null) && (getTableTreeDelegate().getTable() != null) ? getTableTreeDelegate().getTable().getColumnGenerator(paramFVTableColumn.getName()) : null;
					if((localObject1 instanceof Table.ColumnGenerator)){
						Table.ColumnGenerator localColumnGenerator = (Table.ColumnGenerator) localObject1;
						if(localColumnGenerator != null){
							Long itemID = Long.valueOf(paramFocObject.getReference().getLong());
							localObject2 = localColumnGenerator.generateCell(getTableTreeDelegate().getTable(), itemID, paramFVTableColumn.getName());
							if(localObject2 != null){
								if((localObject2 instanceof FVMultipleChoiceOptionGroupPopupView)){
									str = ((FocXMLGuiComponent) localObject2).getValueString();
								}else if((localObject2 instanceof FocXMLGuiComponent)){
									str = ((FocXMLGuiComponent) localObject2).getValueString();
								}else if(localObject2 instanceof Embedded){
									ITableTree tableTree = getTableOrTree();
									FocList list = tableTree != null ? tableTree.getFocList() : null;
									FocObject focObject = list != null ? list.searchByReference(itemID) : null;
									FProperty property = focObject != null ? focObject.getFocPropertyForPath(paramFVTableColumn.getDataPath()) : null;
									if(property instanceof FBoolean){
										str = property.getString();
									}
								}else{
									try{
										str = String.valueOf(localObject2);
									}catch (Exception localException){
										Globals.logExceptionWithoutPopup(localException);
									}
								}
							}
						}
					}
				}
			}
		}
		if((str != null) && (!str.isEmpty()) && (str.contains("\""))){
			str = str.replaceAll("\"", "''");
		}
		return str;
	}
}
