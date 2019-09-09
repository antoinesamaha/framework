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
package com.foc.vaadin.gui.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.desc.field.FMultipleChoiceItemInterface;
import com.foc.property.FDescPropertyStringBased;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FocXMLGuiComponent;

@SuppressWarnings("serial")
public class FVMultipleChoiceFocDesc extends FVMultipleChoiceComboBox implements FocXMLGuiComponent {

  public FVMultipleChoiceFocDesc(FProperty property, Attributes attributes) {
    super(property, attributes);
  }
  
  @Override
  protected void fillMultipleChoice(FProperty property){
    if(getFocData() != null && getFocData() instanceof FDescPropertyStringBased){
      property  = (FDescPropertyStringBased) getFocData();
      FDescFieldStringBased    descField = (FDescFieldStringBased) property.getFocField();
     
      Iterator<FMultipleChoiceItem> itr = descField.getChoicesIterator();
      fillWithFocDesc(itr, true);
    }    
  }
  
  private void fillWithFocDesc(Iterator<FMultipleChoiceItem> choices, boolean sort) {

    ArrayList<FMultipleChoiceItem> intermediate = new ArrayList<FMultipleChoiceItem>();

    while (choices != null && choices.hasNext()) {
      FMultipleChoiceItem item = (FMultipleChoiceItem) choices.next();
      if (item != null) {
        // addItem(item.getTitle());
        intermediate.add(item);
      }
    }

    Comparator<FMultipleChoiceItem> comparator = null;

    if (sort) {
      comparator = new Comparator<FMultipleChoiceItem>() {
        public int compare(FMultipleChoiceItem item0, FMultipleChoiceItem item1) {
          return item0.getTitle().compareTo(item1.getTitle());
        }
      };
    } else {
      comparator = new Comparator<FMultipleChoiceItem>() {
        public int compare(FMultipleChoiceItem item0, FMultipleChoiceItem item1) {
          return item0.getId() - item1.getId();
        }
      };
    }
    Collections.sort(intermediate, comparator);
    for (int i = 0; i < intermediate.size(); i++) {
      FMultipleChoiceItemInterface item = (FMultipleChoiceItemInterface) intermediate.get(i);
      addItem(item.getTitle());
    }
  }

}
