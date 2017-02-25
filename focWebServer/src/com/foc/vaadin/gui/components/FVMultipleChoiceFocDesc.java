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
