package com.fab;

import javax.swing.JSplitPane;

import com.fab.model.table.TableDefinitionGuiBrowsePanel;
import com.foc.desc.FocObject;
import com.foc.gui.FGCurrentItemPanel;
import com.foc.gui.FGSplitPane;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class FocApplicationBuilderGuiDetailsPanel extends FPanel {
	
	public FocApplicationBuilderGuiDetailsPanel(FocObject applicationBuilder, int view){
		setWithScroll(false);
		FGSplitPane splitPan = new FGSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		FListPanel tablesBrowsePanel = new TableDefinitionGuiBrowsePanel(null, FocObject.DEFAULT_VIEW_ID);
		splitPan.setLeftComponent(tablesBrowsePanel);
		tablesBrowsePanel.setWithScroll(false);
		
  	FGCurrentItemPanel currPanel = new FGCurrentItemPanel(tablesBrowsePanel, FocApplicationBuilder.VIEW_NO_EDIT);
  	splitPan.setRightComponent(currPanel);
		setFrameTitle("Tables");
  	setMainPanelSising(FPanel.FILL_BOTH);
		add(splitPan,0,0);
		
		FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(tablesBrowsePanel.getFocList());	
    }
	}
}
