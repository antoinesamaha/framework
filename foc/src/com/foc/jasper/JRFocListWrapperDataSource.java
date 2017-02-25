package com.foc.jasper;

import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class JRFocListWrapperDataSource implements JRDataSource {

  private FocListWrapper list       = null;
  private int       	   i          = 0   ;
  
  public JRFocListWrapperDataSource(FocListWrapper list){
    this.list = list;
    i = 0;
  }
  
  public void dispose(){
  	list = null;
  }
  
  public FocObject getCurrentFocObject(){
  	return i-1 < list.size() ? list.getAt(i-1) : null;
  }

  @Override
  public Object getFieldValue(JRField jrField) throws JRException {
  	return JRFocListDataSource.getFieldValue_Static(getCurrentFocObject(), i, jrField);
  }

  @Override
  public boolean next() throws JRException {
  	boolean next = list != null;
  	if(next){
  		i++;
	  	next = i < list.size();
  	}
    return next;
  }
}
