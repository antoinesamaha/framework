package com.foc.business.printing;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PrnLayoutGuiBrowsePanel_Print extends FListPanel {

	private IPrnReportCreator reportCreator = null;
	private PrnReportLauncher repLauncher   = null;
	
	public PrnLayoutGuiBrowsePanel_Print(PrnContext context, IPrnReportCreator reportCreator){
		super("Layout list", FPanel.FILL_NONE);

		this.reportCreator = reportCreator;
		
		FocList focList = context.getLayoutList();
		FocDesc focDesc = PrnLayoutDesc.getInstance();
		if(focDesc != null && focList != null){
			setFocList(focList);
			FTableView tableView = getTableView();

			tableView.addColumn(focDesc, PrnLayoutDesc.FLD_NAME, false);
			tableView.addColumn(focDesc, PrnLayoutDesc.FLD_DESCRIPTION, false);
			
			construct();
			tableView.setColumnResizingMode(FTableView.COLUMN_WIDTH_FACTOR_MODE);

			reportCreator.getLauncher().setLayoutBrowsePanel(this);
			getTotalsPanel().add(reportCreator.getLauncher().newPanel());
		
			showModificationButtons(false);

			if(getFocList().size() > 0){
				//requestFocusInWindow();
				setSelectedObject(getFocList().getFocObject(0));
			}
		}
	}
	
	public void dispose(){
		super.dispose();
		if(reportCreator != null){
			reportCreator.disposeLauncherContent();
			reportCreator = null;
		}
		if(repLauncher != null){
			repLauncher.dispose();
			repLauncher = null;
		}
	}
}
