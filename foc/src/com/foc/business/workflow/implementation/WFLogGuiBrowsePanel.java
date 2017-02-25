package com.foc.business.workflow.implementation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.foc.business.workflow.map.WFSignature;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.gui.FGButton;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFLogGuiBrowsePanel extends FListPanel {
	
	public WFLogGuiBrowsePanel(FocList focList, int viewID){
		super("Workflow Log", FPanel.FILL_VERTICAL);
		FocDesc focDesc = focList != null ? (WFLogDesc) focList.getFocDesc() : null;
		if(focDesc != null){
			setFocList(focList);
			FTableView tableView = getTableView();

			tableView.addColumn(focDesc, WFLogDesc.FLD_EVENT_UNDONE, "Undone", false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_EVENT_TYPE, false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_PREVIOUS_STAGE, false);			
			tableView.addColumn(focDesc, WFLogDesc.FLD_TARGET_STAGE, false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_TITLE, false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_USER, false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_DATE_TIME, false);
			tableView.addColumn(focDesc, WFLogDesc.FLD_DESCERIPTION, false);
			
			construct();
			tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
		
			if(getTransaction() != null && getTransaction().workflow_IsAllowUndoSignature()){
				FGButton undoLastSignatureButton = new FGButton("Undo Last Signature");
				getButtonsPanel().addButton(undoLastSignatureButton);
				undoLastSignatureButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						getWorkflow().undoLastSignature();
					}
				});
			}

			WFSignature signature = getWorkflow().nextSignature();
			if(signature != null){
				int idx = signature.getTitleIndex_ForUserAndArea(getWorkflow().getArea());
				if(idx >= 0){
					FGButton signButton = new FGButton("Sign");
					getButtonsPanel().addButton(signButton);
					signButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							getWorkflow().sign();
							getWorkflow().getFocObject().load();
						}
					});
				}
			}
			
			showAddButton(false);
			showRemoveButton(false);
			showEditButton(false);
		}
	}
	
	public FocObject getTransaction(){
		return (FocObject) getFocList().getFatherSubject();
	}
	
	public Workflow getWorkflow(){
		IWorkflow iWorkflow = (IWorkflow) getTransaction();
		return iWorkflow != null ? iWorkflow.iWorkflow_getWorkflow() : null;
	}
}
