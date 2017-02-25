package com.foc.web;

import java.io.Serializable;

import com.foc.FocThreadLocal;
import com.foc.vaadin.FocWebApplication;

//import com.vaadin.service.ApplicationContext.TransactionListener;
//import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FocThreadLocal_Listener {/*implements TransactionListener, Serializable{

  private UI application = null;

  public FocThreadLocal_Listener(UI application){
    this.application = application;
  }
  
  @Override
  public void transactionStart(Application application, Object transactionData) {
    if(this.application == application){
      FocThreadLocal.setApplication((FocWebApplication) application);
    }
  }

  @Override
  public void transactionEnd(Application application, Object transactionData) {
    if(this.application == application){
    	FocThreadLocal.setApplication(null);
    }
  }*/
}