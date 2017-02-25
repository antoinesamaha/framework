package com.foc.vaadin.gui.pdfGenerator;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocPDFHorizontalLayout extends FocPDFLayout {

	private float spacing       = 0;
	
	public FocPDFHorizontalLayout(FocPDFLayout pdfLayout, FocXMLAttributes attribute){
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

		int numberOfItemsWithWidth_Undefined = 0; 
		
		float layoutAttributeWidth = getAttributeWidth();
		if(layoutAttributeWidth < 0){//Transform the % width to an absolute one
			layoutAttributeWidth = -layoutAttributeWidth * maxWidth;
		}
		
		if(layoutAttributeWidth > 0){
			setWidth(layoutAttributeWidth);
			//We start by distributing the widths on the percentages
			//To subtract widths with assigned absolute value
			float totalAvailableWidth = layoutAttributeWidth - getMarginRight() - getMarginLeft() - (getComponentNumber()-1) * getSpacing();
			for(int i=0; i<getComponentNumber(); i++){
				FocPDFComponent component = getComponentAt(i);
				float attribWidth = component.getAttributeWidth();
				if(attribWidth > 0){
					component.setWidth(attribWidth);
					totalAvailableWidth -= attribWidth; 
				}else if(attribWidth == 0){
					numberOfItemsWithWidth_Undefined++;
				}
			}
	
			//To subtract widths with assigned percentage
			float totalAvailableWidthInPercentage = 100;
			for(int i=0; i<getComponentNumber(); i++){
				FocPDFComponent component = getComponentAt(i);
				float attribWidth = component.getAttributeWidth();
				if(attribWidth < 0){
					totalAvailableWidthInPercentage -= -attribWidth;
					component.setWidth(-attribWidth * totalAvailableWidth / 100);
				}
			}
	
			//To subtract widths = -1;
			if(numberOfItemsWithWidth_Undefined > 0){
				for(int i=0; i<getComponentNumber(); i++){
					FocPDFComponent component = getComponentAt(i);
					float attribWidth = component.getAttributeWidth();
					if(attribWidth == 0){
						float percentage = totalAvailableWidthInPercentage / numberOfItemsWithWidth_Undefined;
						component.setWidth(percentage * totalAvailableWidth / 100);
					}
				}
			}
		}else{
			float heightMaximal = 0;
			
			for(int i=0; i<getComponentNumber(); i++){
				if(i > 0) left += getSpacing();
				
				FocPDFComponent component = getComponentAt(i);
	
				component.layout(left, top, maxWidth, maxHeight);
				float componentWidth = component.getWidth();
				
				left += componentWidth;
				
				if(component.getHeight() > heightMaximal){
					heightMaximal = component.getHeight();
				}
			}
	
			left += getMarginRight();
			setWidth(left);
		}
	}
	
	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Horizontal - ("+getDebugString()+")");
		super.debug(indentation);
	}
}




















