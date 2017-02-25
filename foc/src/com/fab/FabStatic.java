package com.fab;

import java.util.ArrayList;

import com.foc.desc.field.FMultipleChoiceStringField;

public class FabStatic {
	private static ArrayList<FMultipleChoiceStringField> fieldArray = null;
	public static void addStringBasedField(FMultipleChoiceStringField fieldToAdd){
		if(fieldArray == null){
			fieldArray = new ArrayList<FMultipleChoiceStringField>();
		}
		fieldArray.add(fieldToAdd);
	}
	
	public static void refreshAllTableFieldChoices(){
		if(fieldArray != null){
			for(int i=0; i<fieldArray.size(); i++){
				FMultipleChoiceStringField field = fieldArray.get(i);
				if(field != null){
					field.re_fillWithAllDeclaredFocDesc();
				}
			}
		}
	}
}
