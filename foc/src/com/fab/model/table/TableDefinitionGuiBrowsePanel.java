package com.fab.model.table;

import java.awt.event.ActionEvent;

import com.foc.ConfigInfo;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FGPopupMenuItem;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class TableDefinitionGuiBrowsePanel extends FListPanel {
	public static final String VIEW_KEY_TABLE_DEFINITION = "Table Definition";
	
	public TableDefinitionGuiBrowsePanel(FocList list, int viewID){
		super("Table definition", FPanel.FILL_VERTICAL);
		FocDesc desc = TableDefinitionDesc.getInstance();
		setWithScroll(false);

    if (desc != null) {
      if(list == null){
      	list = TableDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
      }else{
      	list.loadIfNotLoadedFromDB();
      }
      if (list != null) {
     		setFocList(list);
        FTableView tableView = getTableView();       
        tableView.setDetailPanelViewID(0);
        
        tableView.addLineNumberColumn();
        tableView.addColumn(desc, TableDefinitionDesc.FLD_NAME, false);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_PROJECT, false);
        tableView.addColumn(desc, FField.FLD_FAB_OWNER, false);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_EXISTING_TABLE, false);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_DB_RESIDENT, "DB|Res", true);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_WITH_REF, "With|Ref", true);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_KEY_UNIQUE, "Key|Unique", true);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_WEB_STRUCTURE, "Web|Structure", true);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_SINGLE_INSTANCE, "Single", true);
        tableView.addColumn(desc, TableDefinitionDesc.FLD_ADD_LOG_FIELDS, "Add logs", true);
        if(ConfigInfo.isForDevelopment()){
        	tableView.addColumn(desc, TableDefinitionDesc.FLD_SERVER_SIDE_PACKAGE, "Source|Code|Package", true);
        }
        tableView.setViewKey(VIEW_KEY_TABLE_DEFINITION);
        construct();
        
      	if(ConfigInfo.isForDevelopment()){
      		FGPopupMenuItem generateServerCodeMenu = new FGPopupMenuItem("Generate server code", this){
						@Override
						public void actionPerformed(ActionEvent e) {
							TableDefinition tableDef = (TableDefinition) getSelectedObject();
							if(tableDef != null){
								tableDef.writeCode_ServerCode();
							}
						}
      		};
      		getTable().getPopupMenu().add(generateServerCodeMenu);
      		
      		FGPopupMenuItem generateClientCodeMenu = new FGPopupMenuItem("Generate client code", this){
						@Override
						public void actionPerformed(ActionEvent e) {
							TableDefinition tableDef = (TableDefinition) getSelectedObject();
							if(tableDef != null){
								if(tableDef.isWebStructure()){
									tableDef.writeCode_ClientCode();
								}
							}
						}
      		};
      		getTable().getPopupMenu().add(generateClientCodeMenu);

      		FGPopupMenuItem generateFocDesc = new FGPopupMenuItem("Generate all code", this){
						@Override
						public void actionPerformed(ActionEvent e) {
							TableDefinition tableDef = (TableDefinition) getSelectedObject();
							if(tableDef != null){
								tableDef.writeCode_ServerCode();

								/*
								codeWriter = new CodeWriter_FocList(tableDef);
								codeWriter.generateCode();
								codeWriter.dispose();
								codeWriter = null;
								*/

								if(tableDef.isWebStructure()){
									tableDef.writeCode_ClientCode();
								}
							}
						}
      		};
      		getTable().getPopupMenu().add(generateFocDesc);
      	}

        tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        requestFocusOnCurrentItem();
        showEditButton(true);
        showDuplicateButton(false);
        showColomnSelectorButton(true);
      }
    }
	}
	
  public FPanel newDetailsPanel(FocObject focObject, int viewID){
    return focObject.isCreated() ? new TableDefinitionGuiDetailsPanel_ForNewItem(focObject, viewID) : null;
  }
}

