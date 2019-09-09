/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
