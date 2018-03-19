package com.foc.vaadin.gui.xmlForm;

import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.filter.BooleanCondition;
import com.foc.list.filter.DateCondition;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.MultipleChoiceCondition;
import com.foc.list.filter.ObjectCondition;
import com.foc.list.filter.StringCondition;

public class FocXMLFilterConditionBuilder {

	public static void addConditionComponents(DefaultHandler handler, FocXMLLayout xmlLayout, FocListFilter listFilter, FilterCondition condition, FocXMLAttributes attributes){
		try{
			//HorizontalLayout
			FocXMLAttributes hLayoutAttributes = new FocXMLAttributes();
			hLayoutAttributes.addAttribute(FXML.ATT_SPACING, "true");
			hLayoutAttributes.addAttribute(FXML.ATT_CAPTION_MARGIN, "0");
			handler.startElement(null, null, FXML.TAG_HORIZONTAL_LAYOUT, hLayoutAttributes);
			String prefix = condition.getFieldPrefix();
			{
				//Label
//			<Label value="نوع الحادثة" style="f14,bold,text-bottom" alignment="middle_center" width="130px"/>
				FocXMLAttributes labelAttributes = new FocXMLAttributes();
				String caption = attributes.getValue(FXML.ATT_CAPTION);
				if(caption == null) {
					caption = condition.getFieldLabel();
				}
				labelAttributes.addAttribute(FXML.ATT_VALUE, caption);
				labelAttributes.addAttribute(FXML.ATT_STYLE, "f14,bold,text-bottom");
				labelAttributes.addAttribute(FXML.ATT_ALIGNMENT, "middle_center");
				labelAttributes.addAttribute(FXML.ATT_WIDTH, "130px");
				handler.startElement(null, null, FXML.TAG_LABEL, labelAttributes);
				handler.endElement(null, null, FXML.TAG_LABEL);
				
				if(condition instanceof DateCondition){
//				<GuiField name="EndDate_OP" width="120px" height="-1px" />
//				<GuiField name="EndDate_FDATE" width="130px" height="-1px" visibleWhen="OR(EndDate_OP=0,EndDate_OP=1,EndDate_OP=3)" />
//				<GuiField name="EndDate_LDATE" width="130px" height="-1px" visibleWhen="OR(EndDate_OP=0,EndDate_OP=2)" />
					
					//Operation
					FocXMLAttributes opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_OP");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "120px");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
					
					String firstDateVisibleWhen = "OR("+prefix+"_OP=0,"+prefix+"_OP=1,"+prefix+"_OP=3)";
					
					opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_FDATE");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "130px");
					opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, firstDateVisibleWhen);
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);

					if(xmlLayout instanceof FocXMLLayout_Filter) {
						opAttributes = new FocXMLAttributes();
						opAttributes.addAttribute(FXML.ATT_NAME, getButtonName_ForFirstDateShifterButton(condition));
						opAttributes.addAttribute(FXML.ATT_ICON, "edit");
						opAttributes.addAttribute(FXML.ATT_BUTTON_LINK_STYLE, "true");
						opAttributes.addAttribute(FXML.ATT_WIDTH, "10px");
						opAttributes.addAttribute(FXML.ATT_HEIGHT, "-1px");
						opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, firstDateVisibleWhen);
						handler.startElement(null, null, FXML.TAG_BUTTON, opAttributes);
						handler.endElement(null, null, FXML.TAG_FIELD);
					}
	
					String lastDateVisibleWhen = "OR("+prefix+"_OP=0,"+prefix+"_OP=2)";
					
					opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_LDATE");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "130px");
					opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, lastDateVisibleWhen);
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);			
					
					if(xmlLayout instanceof FocXMLLayout_Filter) {
						opAttributes = new FocXMLAttributes();
					  opAttributes.addAttribute(FXML.ATT_NAME, getButtonName_ForLastDateShifterButton(condition));
						opAttributes.addAttribute(FXML.ATT_ICON, "edit");
						opAttributes.addAttribute(FXML.ATT_BUTTON_LINK_STYLE, "true");
						opAttributes.addAttribute(FXML.ATT_WIDTH, "10px");
						opAttributes.addAttribute(FXML.ATT_HEIGHT, "-1px");
						opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, lastDateVisibleWhen);
						handler.startElement(null, null, FXML.TAG_BUTTON, opAttributes);
						handler.endElement(null, null, FXML.TAG_FIELD);
					}
					
				}else if(condition instanceof StringCondition){
//				<GuiField name="TypeDisplay_OP" width="120px" height="-1px" />
//				<GuiField name="TypeDisplay_TXT" width="270px" height="-1px" visibleWhen="TypeDisplay_OP>0" />
					
					//Operation
					FocXMLAttributes opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_OP");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "120px");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
					
					opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_TXT");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "270px");
					opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, "OR("+prefix+"_OP>0)");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
				}else if(condition instanceof BooleanCondition){
					//Operation
					FocXMLAttributes opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_VAL");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "120px");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
				}else if(condition instanceof MultipleChoiceCondition){
					//Operation
					FocXMLAttributes opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_OP");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "120px");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
					
					opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_VAL");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "270px");
					opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, "OR("+prefix+"_OP>0)");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
					
				}else if(condition instanceof ObjectCondition){
					String captionProperty = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
					//Guessing the adequate captionProperty 
					if(captionProperty == null){
						ObjectCondition objCond  = (ObjectCondition)condition;
						captionProperty = objCond.getCaptionProperty();
						if(captionProperty == null){
							FObjectField objField = (FObjectField) objCond.getFieldPath().getFieldFromDesc(listFilter.getThisFilterDesc().getSubjectFocDesc());
							FocDesc fieldDesc = objField.getFocDesc();
							
							if(fieldDesc.getFieldByID(FField.FLD_CODE) != null) captionProperty = FField.FNAME_CODE;
							if(fieldDesc.getFieldByID(FField.FLD_NAME) != null) captionProperty = FField.FNAME_NAME;
							if(fieldDesc.getFieldByID(FField.FLD_DESCRIPTION) != null) captionProperty = FField.FNAME_DESCRIPTION;
						}
					}
					
					//Operation
					FocXMLAttributes opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_OP");
					opAttributes.addAttribute(FXML.ATT_WIDTH, "120px");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
					
					opAttributes = new FocXMLAttributes();
					opAttributes.addAttribute(FXML.ATT_NAME, prefix+"_OBJREF");
					opAttributes.addAttribute(FXML.ATT_CAPTION_PROPERTY, captionProperty);
					opAttributes.addAttribute(FXML.ATT_WIDTH, "270px");
					opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, "OR("+prefix+"_OP>0)");
					handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
					handler.endElement(null, null, FXML.TAG_FIELD);
				}
			}
			handler.endElement(null, null, FXML.TAG_HORIZONTAL_LAYOUT);
		}catch(Exception e){
			Globals.logException(e);
		}
	}

	public static String getButtonName_ForFirstDateShifterButton(FilterCondition filterCondition) {
		return filterCondition != null ? filterCondition.getFieldPrefix()+"_FButton" : null;
	}

	public static String getButtonName_ForLastDateShifterButton(FilterCondition filterCondition) {
		return filterCondition != null ? filterCondition.getFieldPrefix()+"_LButton" : null;
	}
}
