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
package com.foc.vaadin;

import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.FVTextArea;
import com.vaadin.ui.AbsoluteLayout;

@SuppressWarnings("serial")
public class LeftPanel extends AbsoluteLayout{
  
  public LeftPanel(){
    setWidth("300px");
    FVTextArea note1 = new FVTextArea(null, null);
    FVTextArea note2 = new FVTextArea(null, null);
    Date date = new Date();
    
    addComponent(date, "left:0px; top:20px");
    addComponent(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_NOTE, false), "left:5px; top:200px");
    addComponent(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_NOTE, false), "left:5px; top:350px");
    
    note1 = noteFieldProperties(note1, "Check with engineering if all drawings are ready for site meeting next tuesday. Consultant want to make sure that generator selection is conform to initial specs.");
    addComponent(note1, "left:42px; top:200px");
    
    note2 = noteFieldProperties(note2, "Prepare quotation for Project Zaabil tours Dubai. Before 5 September.");
    addComponent(note2, "left:42px; top:350px");
  }
  
  private FVTextArea noteFieldProperties(FVTextArea noteLabel, String value){
    noteLabel.setValue(value);
    noteLabel.setWidth("180px");
    noteLabel.setHeight("130px");
    noteLabel.setReadOnly(true);
    return noteLabel;
  }
}
