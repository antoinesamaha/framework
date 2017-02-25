package com.foc.vaadin.gui.pdfGenerator;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocPDFVerticalLayout extends FocPDFLayout {
	
	private float spacing = 0;
	
	public FocPDFVerticalLayout(FocPDFLayout pdfLayout, FocXMLAttributes attribute){
		super(pdfLayout, attribute);
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	@Override
	public void layout(float left, float top, float maxWidth, float maxHeight){
		top  = getMarginTop();
		left = getMarginLeft();
		
		for(int i=0; i<getComponentNumber(); i++){
			if(i > 0) top += getSpacing();
			FocPDFComponent component = getComponentAt(i);
				
			if(component instanceof FocPDFLabel){
				FocPDFLabel label = ((FocPDFLabel)component);
				
			}
			
			top += component.getHeight();
		}

		top += getMarginBottom();
		setHeight(top); 
	}
//	public void layout(float left, float top, float maxWidth, float maxHeight){
//		top  = getMarginTop();
//		left = getMarginLeft();
//		System.out.println("Vertical Layout");
//		for(int i=0; i<getComponentNumber(); i++){
//			if(i > 0) top += getSpacing();
//			
//			getComponentAt(i).layout(left, top, getWidth(), -1);
//			
//			top += getComponentAt(i).getHeight();
//		}
//
//		top += getMarginBottom();
//		setHeight(top); 
//	}
	
	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Vertical - ("+getDebugString()+")");
		super.debug(indentation);
	}
}
