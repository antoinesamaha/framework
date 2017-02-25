package com.fab.gui.browse;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class GuiBrowseGuiDetailsPanel extends FPanel {
	
	private GuiBrowse browseViewDefinition = null;
	
	public GuiBrowseGuiDetailsPanel(FocObject browseViewDefinition, int viewId){
		this.browseViewDefinition = (GuiBrowse) browseViewDefinition;
		FPanel browseColumnBrowsePanel = new GuiBrowseColumnGuiBrowsePanel(this.browseViewDefinition.getBrowseColumnList(), viewId);
		add(browseColumnBrowsePanel, 0, 0);
	}

}
