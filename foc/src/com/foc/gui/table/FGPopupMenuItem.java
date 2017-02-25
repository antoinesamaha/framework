package com.foc.gui.table;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.foc.desc.FocObject;
import com.foc.gui.FAbstractListPanel;
import com.foc.gui.treeTable.FTreeTableModel;
import com.foc.property.FProperty;
import com.foc.tree.FNode;

@SuppressWarnings("serial")
public abstract class FGPopupMenuItem extends JMenuItem implements ActionListener{
	
	private FAbstractListPanel listPanel = null;
	
	public FGPopupMenuItem(String text, Icon icon, FAbstractListPanel listPanel) {
		super(text, icon);
		setName(text);
		this.listPanel = listPanel;
		addActionListener(this);
	}

	public FGPopupMenuItem(String title, FAbstractListPanel listPanel){
		this(title, null, listPanel);
	}
	
	public void dispose(){
		removeActionListener(this);
		listPanel = null;
	}
	
	public int getSelectedRow(){
		return (listPanel != null && listPanel.getTable() != null) ? listPanel.getTable().getSelectedRow() : -1;
	}

	public int getSelectedColumn(){
		return (listPanel != null && listPanel.getTable() != null) ? listPanel.getTable().getSelectedColumn() : -1;
	}

	public int getSelectedColumnVisibleIndex(){
    FTableView tableView = listPanel != null ? listPanel.getTableView() : null;
    return (tableView != null && getSelectedColumn() >= 0) ? tableView.getVisibleColumnIndex(getSelectedColumn()) : -1;
	}
	
	public FAbstractTableModel getTableModel(){
    FTable table = listPanel != null ? listPanel.getTable() : null;
    return table != null ? (FAbstractTableModel) table.getModel() : null;
	}
	
	public FocObject getRowFocObject(){
		int row = getSelectedRow();
    FAbstractTableModel tableModel = getTableModel();
    return (tableModel != null && row >=0) ? tableModel.getRowFocObject(row) : null;
	}
	
	public FProperty getSelectedProperty(){
		FAbstractTableModel model = getTableModel();
		int row = getSelectedRow();
		int col = getSelectedColumnVisibleIndex();
		return (model != null && row >=0 && col >= 0)? model.getFProperty(row, col) : null;
	}
	
	public FNode getRowNode(){
		FNode node = null;
		
		int row = getSelectedRow();
    FAbstractTableModel tableModel = getTableModel();
    
    if(tableModel instanceof FTreeTableModel){
    	node = ((FTreeTableModel) tableModel).getNodeForRow(row);	
    }
		return node;
	}

	public FAbstractListPanel getListPanel() {
		return listPanel;
	}
	
	public void setListPanel(FAbstractListPanel listPanel) {
		this.listPanel = listPanel;
	}

}
