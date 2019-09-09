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
package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;

public abstract class TableListenerAbstract<GenTableItem extends FocObject> implements ITableListener<GenTableItem> {

  @Override
  public void openItem(GenTableItem focObject) {
    
  }

  @Override
  public void addItem(GenTableItem fatherObject, GenTableItem focObject) {
    
  }

  @Override
  public void deleteItem(GenTableItem focObject) {
    
  }
  
  @Override
  public void dispose(){
  	
  }
}
