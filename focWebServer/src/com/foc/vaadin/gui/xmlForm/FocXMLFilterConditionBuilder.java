package com.foc.vaadin.gui.xmlForm;

import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.filter.BooleanCondition;
import com.foc.list.filter.DateCondition;
import com.foc.list.filter.DateTimeCondition;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.IntegerCondition;
import com.foc.list.filter.MultipleChoiceCondition;
import com.foc.list.filter.NumCondition;
import com.foc.list.filter.ObjectCondition;
import com.foc.list.filter.StringCondition;
import com.foc.list.filter.TimeCondition;
import com.foc.util.Utils;

public class FocXMLFilterConditionBuilder {
	
	private static final String WIDTH_VALUE = "170px";

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

				if(condition instanceof TimeCondition){
					//Operation
					newFieldTag(handler, prefix+"_OP", "120px");
					
					String firstDateVisibleWhen = "OR("+prefix+"_OP="+TimeCondition.OPERATOR_EQUALS+","+prefix+"_OP="+TimeCondition.OPERATOR_GREATER_THAN+","+prefix+"_OP="+TimeCondition.OPERATOR_BETWEEN+")";
					newFieldTag(handler, prefix+"_FTIME", WIDTH_VALUE, firstDateVisibleWhen);
	
					String lastDateVisibleWhen = "OR("+prefix+"_OP="+TimeCondition.OPERATOR_BETWEEN+","+prefix+"_OP="+TimeCondition.OPERATOR_LESS_THAN+")";
					newFieldTag(handler, prefix+"_LTIME", WIDTH_VALUE, lastDateVisibleWhen);
					
				}else if(condition instanceof DateCondition){
					//Operation
					newFieldTag(handler, prefix+"_OP", "120px");
					
					String firstDateVisibleWhen = "OR("+prefix+"_OP=0,"+prefix+"_OP=1,"+prefix+"_OP=3)";
					String firstDateButtonVisibleWhen = "AND($P{DB_RESIDENT},"+firstDateVisibleWhen+")";
					
					newFieldTag(handler, prefix+"_FDATE", WIDTH_VALUE, firstDateVisibleWhen);

					if(xmlLayout instanceof FocXMLLayout_Filter) {
						newShifterButtonTag(handler, getButtonName_ForFirstDateShifterButton(condition), firstDateButtonVisibleWhen);
					}
	
					String lastDateVisibleWhen = "OR("+prefix+"_OP=0,"+prefix+"_OP=2)";
					String lastDateButtonVisibleWhen = "AND($P{DB_RESIDENT},"+lastDateVisibleWhen+")";

					newFieldTag(handler, prefix+"_LDATE", WIDTH_VALUE, lastDateVisibleWhen);
					
					if(xmlLayout instanceof FocXMLLayout_Filter) {
						newShifterButtonTag(handler, getButtonName_ForLastDateShifterButton(condition), lastDateButtonVisibleWhen);
					}

				}else if(condition instanceof DateTimeCondition){
					//Operation
					newFieldTag(handler, prefix+"_OP", "120px");
					
					String firstDateVisibleWhen = "OR("+prefix+"_OP="+DateTimeCondition.OPERATOR_EQUALS+","+prefix+"_OP="+DateTimeCondition.OPERATOR_GREATER_THAN+","+prefix+"_OP="+DateTimeCondition.OPERATOR_BETWEEN+")";
					String firstDateButtonVisibleWhen = "AND($P{DB_RESIDENT},"+firstDateVisibleWhen+")";
					newFieldTag(handler, prefix+"_FDATE", WIDTH_VALUE, firstDateVisibleWhen);
					
					if(xmlLayout instanceof FocXMLLayout_Filter) {
						newShifterButtonTag(handler, getButtonName_ForFirstDateShifterButton(condition), firstDateButtonVisibleWhen);
					}

					String lastDateVisibleWhen = "OR("+prefix+"_OP="+DateTimeCondition.OPERATOR_BETWEEN+","+prefix+"_OP="+DateTimeCondition.OPERATOR_LESS_THAN+")";
					String lastDateButtonVisibleWhen = "AND($P{DB_RESIDENT},"+lastDateVisibleWhen+")";
					newFieldTag(handler, prefix+"_LDATE", WIDTH_VALUE, lastDateVisibleWhen);
					
					if(xmlLayout instanceof FocXMLLayout_Filter) {
						newShifterButtonTag(handler, getButtonName_ForLastDateShifterButton(condition), lastDateButtonVisibleWhen);
					}

				}else if(condition instanceof StringCondition){
					//Operation
					newFieldTag(handler, prefix+"_OP", "120px");
					newFieldTag(handler, prefix+"_TXT", "270px", "OR("+prefix+"_OP>0)");
				}else if(condition instanceof BooleanCondition){
					//Operation
					newFieldTag(handler, prefix+"_VAL", "120px");
				}else if(condition instanceof MultipleChoiceCondition){
					//Operation
					newFieldTag(handler, prefix+"_OP", "120px");
					newFieldTag(handler, prefix+"_VAL", "270px", "OR("+prefix+"_OP>0)");
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
					newFieldTag(handler, prefix+"_OP", "120px");
					newFieldTag(handler, prefix+"_OBJREF", "270px", "OR("+prefix+"_OP>0)", captionProperty);

				}else if(condition instanceof IntegerCondition){
					String fldNameOP = prefix+IntegerCondition.FNAME_OP;
					String fldNameFVAL = prefix+IntegerCondition.FNAME_FVAL;
					String fldNameLVAL = prefix+IntegerCondition.FNAME_LVAL;
					
					//Operation
					newFieldTag(handler, fldNameOP, "120px");
					
					String firstDateVisibleWhen = "OR("+fldNameOP+"="+IntegerCondition.OPERATOR_EQUALS+","+fldNameOP+"="+IntegerCondition.OPERATOR_GREATER_THAN+","+fldNameOP+"="+IntegerCondition.OPERATOR_BETWEEN+")";
					newFieldTag(handler, fldNameFVAL, WIDTH_VALUE, firstDateVisibleWhen);
	
					String lastDateVisibleWhen = "OR("+fldNameOP+"="+IntegerCondition.OPERATOR_BETWEEN+","+fldNameOP+"="+IntegerCondition.OPERATOR_LESS_THAN+")";
					newFieldTag(handler, fldNameLVAL, WIDTH_VALUE, lastDateVisibleWhen);

				}else if(condition instanceof NumCondition){
					String fldNameOP = prefix+NumCondition.FNAME_OP;
					String fldNameFVAL = prefix+NumCondition.FNAME_FVAL;
					String fldNameLVAL = prefix+NumCondition.FNAME_LVAL;
					
					//Operation
					newFieldTag(handler, fldNameOP, "120px");
					
					String firstDateVisibleWhen = "OR("+fldNameOP+"="+IntegerCondition.OPERATOR_EQUALS+","+fldNameOP+"="+IntegerCondition.OPERATOR_GREATER_THAN+","+fldNameOP+"="+IntegerCondition.OPERATOR_BETWEEN+")";
					newFieldTag(handler, fldNameFVAL, WIDTH_VALUE, firstDateVisibleWhen);
	
					String lastDateVisibleWhen = "OR("+fldNameOP+"="+IntegerCondition.OPERATOR_BETWEEN+","+fldNameOP+"="+IntegerCondition.OPERATOR_LESS_THAN+")";
					newFieldTag(handler, fldNameLVAL, WIDTH_VALUE, lastDateVisibleWhen);
				}
			}
			handler.endElement(null, null, FXML.TAG_HORIZONTAL_LAYOUT);
		}catch(Exception e){
			Globals.logException(e);
		}
	}

	public static void newShifterButtonTag(DefaultHandler handler, String fieldName, String visibleWhen) throws Exception {
		FocXMLAttributes opAttributes = new FocXMLAttributes();
	  opAttributes.addAttribute(FXML.ATT_NAME, fieldName);
		opAttributes.addAttribute(FXML.ATT_ICON, "edit");
		opAttributes.addAttribute(FXML.ATT_BUTTON_LINK_STYLE, "true");
		opAttributes.addAttribute(FXML.ATT_WIDTH, "10px");
		opAttributes.addAttribute(FXML.ATT_HEIGHT, "-1px");
		opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, visibleWhen);
		handler.startElement(null, null, FXML.TAG_BUTTON, opAttributes);
		handler.endElement(null, null, FXML.TAG_FIELD);
	}

	public static void newFieldTag(DefaultHandler handler, String fieldName, String width) throws Exception {
		newFieldTag(handler, fieldName, width, null, null);
	}

	public static void newFieldTag(DefaultHandler handler, String fieldName, String width, String visibleWhen) throws Exception {
		newFieldTag(handler, fieldName, width, visibleWhen, null);
	}
		
	public static void newFieldTag(DefaultHandler handler, String fieldName, String width, String visibleWhen, String captionProperty) throws Exception {
		FocXMLAttributes opAttributes = new FocXMLAttributes();
		opAttributes.addAttribute(FXML.ATT_NAME, fieldName);
		opAttributes.addAttribute(FXML.ATT_WIDTH, width);
		opAttributes.addAttribute(FXML.ATT_VISIBLE_WHEN, visibleWhen);
		if(!Utils.isStringEmpty(captionProperty)) {
			opAttributes.addAttribute(FXML.ATT_CAPTION_PROPERTY, captionProperty);
		}
		handler.startElement(null, null, FXML.TAG_FIELD, opAttributes);
		handler.endElement(null, null, FXML.TAG_FIELD);
		

	}
	
	public static String getButtonName_ForFirstDateShifterButton(FilterCondition filterCondition) {
		return filterCondition != null ? filterCondition.getFieldPrefix()+"_FButton" : null;
	}

	public static String getButtonName_ForLastDateShifterButton(FilterCondition filterCondition) {
		return filterCondition != null ? filterCondition.getFieldPrefix()+"_LButton" : null;
	}
}
