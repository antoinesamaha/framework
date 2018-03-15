package com.fab;

import java.util.ArrayList;

import com.foc.desc.field.FDescFieldStringBased;

public class FabStatic {
	private static ArrayList<FDescFieldStringBased> fieldArray = null;
	public static void addStringBasedField(FDescFieldStringBased fieldToAdd){
		if(fieldArray == null){
			fieldArray = new ArrayList<FDescFieldStringBased>();
		}
		fieldArray.add(fieldToAdd);
	}
	
	public static void refreshAllTableFieldChoices(){
		if(fieldArray != null){
			for(int i=0; i<fieldArray.size(); i++){
				FDescFieldStringBased field = fieldArray.get(i);
				if(field != null){
					field.re_fillWithAllDeclaredFocDesc();
				}
			}
		}
	}
}
