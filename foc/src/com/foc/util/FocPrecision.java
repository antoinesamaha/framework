package com.foc.util;

import com.foc.desc.field.FMultipleChoiceStringField;

public class FocPrecision {
	public static FMultipleChoiceStringField newPrecisionFieldStringBased(String dbName, String title, int fieldID){
	  FMultipleChoiceStringField multFld = new FMultipleChoiceStringField(dbName, title, fieldID, false, 11);
    multFld.addChoice("10000");
    multFld.addChoice("1000");
    multFld.addChoice("100");
    multFld.addChoice("10");
    multFld.addChoice("1");
    multFld.addChoice("");
    multFld.addChoice("0.1");
    multFld.addChoice("0.01");
    multFld.addChoice("0.001");
    multFld.addChoice("0.0001");
    multFld.addChoice("0.00001");
    multFld.addChoice("0.000001");
    multFld.addChoice("0.0000001");
    multFld.addChoice("0.00000001");
    multFld.addChoice("0.000000001");
    multFld.setSortItems(true);
    return multFld;
	}
}
