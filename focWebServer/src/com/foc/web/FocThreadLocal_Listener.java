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
