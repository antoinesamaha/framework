// PROPERTIES
// INSTANCE
//    MAIN
//    PANEL
// DESCRIPTION
// LIST

/*
 * Created on 01-Feb-2005
 */
package com.foc.business.currency;

import java.sql.Date;
import java.util.ArrayList;

import com.foc.*;
import com.foc.admin.FocGroup;
import com.foc.business.multilanguage.MultiLanguage;
import com.foc.desc.*;
import com.foc.desc.field.*;
import com.foc.event.*;
import com.foc.gui.*;
import com.foc.gui.table.*;
import com.foc.list.*;
import com.foc.property.*;

/**
 * @author 01Barmaja
 */
public class DateLineDesc extends FocDesc {
  
  // ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // DESCRIPTION
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  private static ArrayList<Currency> currencyOrderArray = null;
  
  public static final int FLD_DATE      = 1;
  public static final int FLD_CURR_RATE = 2;

  public static final String TABLE_NAME = "CURR_LINE";
  
  public DateLineDesc() {
  	super(DateLine.class, FocDesc.NOT_DB_RESIDENT, TABLE_NAME, true);
  	
  	addReferenceField();
  	
  	FDateField dateField = new FDateField("DATE", "Date", FLD_DATE, true);
  	addField(dateField);
  	
    FocList currList = Currency.getList(false);
    
    Currencies currencies = Currencies.getCurrencies();
    if(currencies != null){
    	currencyOrderArray = new ArrayList<Currency>();
    	
	    int index = 0;
	    for(int i=0; i<currList.size(); i++){
	      Currency curr = (Currency) currList.getFocObject(i);
	      
	      if(Currencies.getCurrencies().getBaseCurrency().getReference().getInteger() != curr.getReference().getInteger()){
	      	if(!currencyOrderArray.contains(curr)){
	      		currencyOrderArray.add(curr);
	      	}
	      }else{
	        Globals.logString("curr == basicCurr: "+curr.getDebugInfo());
	      }
	    }
	    
	    //Globals.logString("! End of curr array index= "+index+" !");
	
	    Currency baseCurr = currencies.getBaseCurrency();
    	for(int i=0; i<currencyOrderArray.size(); i++){
        Currency currency = currencyOrderArray.get(i);
        String title = currency.getName()+"/"+baseCurr.getName();
        if(currency.isReverse()){
          title = baseCurr.getName()+"/"+currency.getName();
        }
    	  
      	FNumField numFocFld = new FNumField("RATE"+i, title, FLD_CURR_RATE+i, false, 12, 6);
      	numFocFld.setDisplayZeroValues(false);
        //FFieldArray focFldArray = new FFieldArray(numFocFld, nbOfCurrencies, fieldArrayPlug);
        addField(numFocFld);
    	}
    }
  }

  public int getCurrencyIndex(Currency currency){
  	return currencyOrderArray.indexOf(currency);  	
  }

  public int getCurrencyCount(){
  	return currencyOrderArray.size();  	
  }

  public Currency getCurrencyAt(int at){
  	return currencyOrderArray.get(at);  	
  }

  public int getCurrencyFieldId_At(int at){
  	return FLD_CURR_RATE+at;  	
  }
  
  public static DateLineDesc getInstance(){
  	return (DateLineDesc) getInstance(TABLE_NAME, DateLineDesc.class);
  }
  
}
