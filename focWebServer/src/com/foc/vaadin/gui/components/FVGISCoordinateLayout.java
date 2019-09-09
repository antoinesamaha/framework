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
package com.foc.vaadin.gui.components;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.property.FString;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class FVGISCoordinateLayout extends FVHorizontalLayout {

	private FString   property          = null;

	private NumTextField degreeField       = null;
	private NumTextField minutesField      = null;
	private NumTextField milliMinutesField = null;
	private ComboBox     orientationField  = null;
	
	private boolean longitude = false;
	
	public static final String DEGREE        = "º";
	public static final String MINUTES_COMMA = ".";
	public static final String MINUTES       = "'";
	
	public FVGISCoordinateLayout(String caption, boolean longitude, FString property){
		super(null);
		setSpacing(true);
		
		this.longitude = longitude;
		this.property = property;
		
		if(!ConfigInfo.isGuiRTL()){
			Label lbl = new Label(caption);
			lbl.addStyleName("foc-bold");
			lbl.addStyleName("foc-f14");
			addComponent(lbl);
		}
		
		degreeField = new NumTextField(0, longitude ? 180 : 90);
		addComponent(degreeField);

		Label lbl = new Label(DEGREE);
		lbl.addStyleName("foc-bold");
		lbl.addStyleName("foc-f14");
		addComponent(lbl);
		
		minutesField = new NumTextField(0, 60);
		addComponent(minutesField);

		lbl = new Label(MINUTES_COMMA);
		lbl.addStyleName("foc-bold");
		lbl.addStyleName("foc-f14");
		addComponent(lbl);
		addComponent(new Label(MINUTES_COMMA));
		
		milliMinutesField = new NumTextField(0, 999);
		addComponent(milliMinutesField);
		
		lbl = new Label(MINUTES);
		lbl.addStyleName("foc-bold");
		lbl.addStyleName("foc-f14");
		addComponent(lbl);
		
		orientationField = new ComboBox();
		if(longitude){
			orientationField.addItem("E");
			orientationField.addItem("W");
		}else{
			orientationField.addItem("N");
			orientationField.addItem("S");
		}
		orientationField.setWidth("70px");
		addComponent(orientationField);
		
		if(ConfigInfo.isGuiRTL()){
			addComponent(new Label(caption));
		}
		
		copyMemoryToGui();
	}
	
	public void dispose(){
		super.dispose();
		if(degreeField != null) degreeField.dispose();
		if(minutesField != null) minutesField.dispose();
		if(milliMinutesField != null) milliMinutesField.dispose();
		orientationField = null;
	}
	
	public int getMaxDegree(){
		return longitude ? 180 : 90;
	}
	
	@Override
	public boolean copyGuiToMemory() {
		if(property != null){
			String value = "";
			if(degreeField != null){
				value += degreeField.getValue();
			}else{
				value += "0";
			}
			value += DEGREE;
			if(minutesField != null){
				value += minutesField.getValue();
			}else{
				value += "0";
			}			
			value += MINUTES_COMMA;
			if(milliMinutesField != null){
				value += milliMinutesField.getValue();
			}else{
				value += "0";
			}	
			value += MINUTES;
			if(orientationField != null && orientationField.getValue() != null){
				value += orientationField.getValue();
			}else{
				value += "";
			}					
			property.setString(value);
		}

		return super.copyGuiToMemory();
	}
	
	@Override
	public void copyMemoryToGui() {
		if(property != null){
			String text = property.getString();
			
			Globals.logString(text);
			
			int idxDegree   = text.indexOf(DEGREE);
			int idxMinComma = text.indexOf(MINUTES_COMMA);
			int idxMin      = text.indexOf(MINUTES);
			
			if(idxDegree > 0 && idxMinComma > idxDegree && idxMin > idxMinComma){
				String degreeText  = text.substring(0, idxDegree); 
				String minText     = text.substring(idxDegree+1, idxMinComma);
				String milliText   = text.substring(idxMinComma+1, idxMin);
				String orientation = text.substring(idxMin+1, text.length()); 
				
				if(degreeField != null) degreeField.setValue(degreeText);
				if(milliMinutesField != null) milliMinutesField.setValue(milliText);
				if(minutesField != null) minutesField.setValue(minText);
				if(orientationField != null) orientationField.setValue(orientation);
			}
		}

		super.copyMemoryToGui();
	}

	public FString getProperty() {
		return property;
	}

	public TextField getDegreeField() {
		return degreeField;
	}

	public TextField getMinutesField() {
		return minutesField;
	}

	public TextField getMilliMinutesField() {
		return milliMinutesField;
	}

	public ComboBox getOrientationField() {
		return orientationField;
	}

	public boolean isLongitude() {
		return longitude;
	}
	
	public static double convertToAngle(String text){
		double angle = 0;
		
		try{
			int idxDegree   = text.indexOf(DEGREE);
			int idxMinComma = text.indexOf(MINUTES_COMMA);
			int idxMin      = text.indexOf(MINUTES);
			
			if(idxDegree > 0 && idxMinComma > idxDegree && idxMin > idxMinComma){
				String degreeText  = text.substring(0, idxDegree); 
				String minText     = text.substring(idxDegree+1, idxMinComma);
				String milliText   = text.substring(idxMinComma+1, idxMin);
				String orientation = text.substring(idxMin+1, text.length()); 
				
				int deg = Integer.valueOf(degreeText).intValue();
				int min = Integer.valueOf(minText).intValue();
				int milliMin = Integer.valueOf(milliText).intValue();
				
				angle = ((double)deg) + (((double)min)/60) + (((double)milliMin) / 1000) / 60;
				if(orientation.equals("S") || orientation.equals("W")){
					angle = -angle;  
				}
			}
		}catch(Exception e){
			angle = 0;
			Globals.logExceptionWithoutPopup(e);
		}
		return angle;
	}
	
	public class NumTextField extends TextField implements ValueChangeListener {
		protected int max = 100;
		protected int min = 0;
		protected boolean insideListener = false;
		private ValueChangeListener listener = null;
		
		public NumTextField(int min, int max){
			this.min = min;
			this.max = max;
			setWidth("50px");
			addStyleName("numerical");
			setImmediate(true);
			listener = new ValueChangeListener(){

				@Override
				public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
					if(!insideListener){
						insideListener = true;
						String  value = getValue();
						Integer intValue = new Integer("0");
						int     intVal   = 0;
						try{
							intValue = Integer.valueOf(value); 
							intVal = intValue.intValue();
						}catch(Exception e){
							intValue = new Integer("0");
							setValue("0");
						}
						if(intVal > NumTextField.this.max){
							intVal = NumTextField.this.max;
							setValue(""+intVal);
						}else if(intVal < NumTextField.this.min){
							intVal = NumTextField.this.min;
							setValue(""+intVal);
						}
						
						value = getValue();
						while(value.length() < (""+NumTextField.this.max).length()){
							value = "0"+value;
							setValue(value);
						}
						
						copyGuiToMemory();
						insideListener = false;
					}					
				}
			};
			
			addValueChangeListener(listener);
		}
		
		public void dispose(){
			if(listener != null){
				removeValueChangeListener(listener);
				listener = null;
			}
		}
	}
}
