package com.foc.vaadin.gui.components.treeGrid;

import com.vaadin.data.util.converter.Converter;

public abstract class FVAbstractConverter<PRESENTATION, MODEL> implements Converter<PRESENTATION, MODEL> {

	FVColumnEditorRendererConverter editorRendererConverter = null;
	
	public FVAbstractConverter(FVColumnEditorRendererConverter editorRendererConverter){
		this.editorRendererConverter = editorRendererConverter;
	}
	
	public void dispose(){
		editorRendererConverter = null;
	}

}
