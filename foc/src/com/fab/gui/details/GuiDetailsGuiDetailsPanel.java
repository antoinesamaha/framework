package com.fab.gui.details;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class GuiDetailsGuiDetailsPanel extends FPanel {
	
	private GuiDetails detailsViewDefinition = null;
	
	public GuiDetailsGuiDetailsPanel(FocObject detailsViewDefinition, int viewId){
		super("Gui details", FPanel.FILL_BOTH);
		this.detailsViewDefinition = (GuiDetails)detailsViewDefinition;
		FPanel detailsFieldDefinitionPanel = new GuiDetailsComponentGuiBrowsePanel(this.detailsViewDefinition.getGuiDetailsFieldList(), viewId);
		add(detailsFieldDefinitionPanel, 0, 0);
	}

}
