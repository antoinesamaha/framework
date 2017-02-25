package com.foc.vaadin.gui.mswordGenerator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordHorizontalLayout extends FocMSWordLayout {

	private float spacing = 0;
	
	public FocMSWordHorizontalLayout(FocMSWordLayout pdfLayout, FocXMLAttributes attribute){
		super(pdfLayout, attribute);
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}
	
	@Override
	public void write(IMSWordContainer container) {
		XWPFDocument wordDocument = getWordDocument();
		if(wordDocument != null){
			XWPFTable newTable = container.insertTable();
			applyBorderAttribute(newTable);
			
			XWPFTableRow tableRow = newTable.getRow(0);
			
			for(int rowIndex=0; rowIndex<getComponentNumber(); rowIndex++){
				XWPFTableCell newCell = rowIndex == 0 ? tableRow.getCell(0) : tableRow.createCell();
				
				MSWordWrapper wrapper = new MSWordWrapper(wordDocument, newCell);
				FocMSWordComponent component = getComponentAt(rowIndex);
				if(component != null){
					component.write(wrapper);
				}
			}
		}
	}
	
//	public void layout(float left, float top, float maxWidth, float maxHeight){
//		top  = getMarginTop();
//		left = getMarginLeft();
//
//		int numberOfItemsWithWidth_Undefined = 0; 
//		
//		float layoutAttributeWidth = getAttributeWidth();
//		if(layoutAttributeWidth < 0){//Transform the % width to an absolute one
//			layoutAttributeWidth = -layoutAttributeWidth * maxWidth;
//		}
//		
//		if(layoutAttributeWidth > 0){
//			setWidth(layoutAttributeWidth);
//			//We start by distributing the widths on the percentages
//			//To subtract widths with assigned absolute value
//			float totalAvailableWidth = layoutAttributeWidth - getMarginRight() - getMarginLeft() - (getComponentNumber()-1) * getSpacing();
//			for(int i=0; i<getComponentNumber(); i++){
//				FocMSWordComponent component = getComponentAt(i);
//				float attribWidth = component.getAttributeWidth();
//				if(attribWidth > 0){
//					component.setWidth(attribWidth);
//					totalAvailableWidth -= attribWidth; 
//				}else if(attribWidth == 0){
//					numberOfItemsWithWidth_Undefined++;
//				}
//			}
//	
//			//To subtract widths with assigned percentage
//			float totalAvailableWidthInPercentage = 100;
//			for(int i=0; i<getComponentNumber(); i++){
//				FocMSWordComponent component = getComponentAt(i);
//				float attribWidth = component.getAttributeWidth();
//				if(attribWidth < 0){
//					totalAvailableWidthInPercentage -= -attribWidth;
//					component.setWidth(-attribWidth * totalAvailableWidth / 100);
//				}
//			}
//	
//			//To subtract widths = -1;
//			if(numberOfItemsWithWidth_Undefined > 0){
//				for(int i=0; i<getComponentNumber(); i++){
//					FocMSWordComponent component = getComponentAt(i);
//					float attribWidth = component.getAttributeWidth();
//					if(attribWidth == 0){
//						float percentage = totalAvailableWidthInPercentage / numberOfItemsWithWidth_Undefined;
//						component.setWidth(percentage * totalAvailableWidth / 100);
//					}
//				}
//			}
//		}else{
//			float heightMaximal = 0;
//			
//			for(int i=0; i<getComponentNumber(); i++){
//				if(i > 0) left += getSpacing();
//				
//				FocMSWordComponent component = getComponentAt(i);
//	
//				component.layout(left, top, maxWidth, maxHeight);
//				float componentWidth = component.getWidth();
//				
//				left += componentWidth;
//				
//				if(component.getHeight() > heightMaximal){
//					heightMaximal = component.getHeight();
//				}
//			}
//	
//			left += getMarginRight();
//			setWidth(left);
//		}
//	}
	
	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Horizontal - ("+getDebugString()+")");
		super.debug(indentation);
	}
}
