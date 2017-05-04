package com.foc.vaadin.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.Attributes;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.property.FMultipleChoiceString;
import com.foc.property.FProperty;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.AbstractSelect;

@SuppressWarnings("serial")
public class FVMultipleChoiceStringField extends FVMultipleChoiceComboBox implements FocXMLGuiComponent, AbstractSelect.NewItemHandler{

  public FVMultipleChoiceStringField(FProperty property, Attributes attributes) {
    super(property, attributes);
    FField fld = property != null ? property.getFocField() : null;
    if(fld != null && !((FMultipleChoiceStringField)fld).isAllowOutofListSelection()){
    	setNewItemsAllowed(false);
    }else{
    	setNewItemsAllowed(true);
    }
    setNewItemHandler(this);
    
    if(attributes != null && attributes.getValue(FXML.ATT_PROMPT) != null){
    	setInputPrompt(attributes.getValue(FXML.ATT_PROMPT));
    }else{
    	setInputPrompt("type new values");
    }
    setImmediate(true);

    /*
    BlurListener focus = new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				String val = (String) getValue();
				if(!Utils.isStringEmpty(latestString) && !Utils.isEqual_String(val, latestString)){
					addNewItem(latestString);
					latestString = null;
					markAsDirty();
				}
			}
		};
    addBlurListener(focus);
    */
  }
	
  /*
  String latestString = null; 
  @Override
  public void changeVariables(Object source, Map<String, Object> variables) {
  	latestString = (String) variables.get("filter");
    super.changeVariables(source, variables);
  }
  */
  
	@Override
  public void addNewItem(String newItemCaption) {
    if(newItemCaption != null){
      addItem(newItemCaption);
      setValue(newItemCaption);
      if(getFocData() != null && getFocData() instanceof FProperty){
      	FProperty property = (FProperty) getFocData();
      	if(property != null && property.getFocField() != null && property.getFocField() instanceof FMultipleChoiceStringField){
      		FMultipleChoiceStringField choiceFieldStringBased = (FMultipleChoiceStringField) property.getFocField();
      		choiceFieldStringBased.dispose_ChoicesArrayList();
          choiceFieldStringBased.setNeedRefresh(true);
      	}
      }
    }
  }
  
  @Override
  protected void fillMultipleChoice(FProperty property){
    if(property != null){
      FMultipleChoiceStringField field = (FMultipleChoiceStringField) property.getFocField();
      if(field != null){
	    	FocObject focObj = property.getFocObject();
	    	ArrayList<String> valuesArray = focObj != null ? focObj.getMultipleChoiceStringBased_ArrayOfValues(field.getID()) : null;
	    	if(valuesArray != null){
	    		for(int i=0; i<valuesArray.size(); i++){
	    			String title = valuesArray.get(i);
	    			addNewItem(title);
	    		}
	    	}else{
//	    		boolean refreshChoices = true;    		
//	    		FVTableWrapperLayout tableWrapper = findAncestor(FVTableWrapperLayout.class);
//	    		
//	    		if(tableWrapper != null){
//	    			refreshChoices = false;
//	    		}
//	    		if(refreshChoices){
//	    			refreshChoices = false;
//	    			field.refreshChoicesArray();
//	    		}
    			field.refreshChoicesArray();
	    		
		      String currentValue = null;//This is used in case we have a value in memory that is not in the Table
		      //If we do not insert the choice we will get an empty GUI field.
		    
		      Iterator<FMultipleChoiceItem> choices = null;
		      if(property != null){
		      	FMultipleChoiceString multipleChoiceStringBasedProp = ((FMultipleChoiceString) property);
		      	currentValue = multipleChoiceStringBasedProp.getString();
		    
		      //Sometimes we want the choices to be in the FocObject directly this is why we have this case
		        choices = multipleChoiceStringBasedProp.getChoiceIterator();
		      }

		      if(choices != null){
		        while(choices.hasNext()){
		          FMultipleChoiceItem choiceItem = choices.next();
		          if(choiceItem != null){
		            addItem(choiceItem.getTitle());
		            if(currentValue != null && choiceItem.getTitle().equals(currentValue)){
		            	currentValue = null;
		            }
		          }
		        }
		        
		        if(currentValue != null){
		        	addItem(currentValue);
		        }
		      }
		      if(property != null && property.getString() != null && !property.getString().isEmpty()){
	        	addItem(property.getString());
	        }
		    }
      }
    }
  }
  
  @Override
  public void setValueString(String value) {
    super.setValueString(value);
  }

  @Override
  public String getValueString() {
  	Object value = getValue();
  	return (String) (value != null && value instanceof String ? value : null);
  }

  @Override
  public void copyMemoryToGui() {
    if(getFocData() instanceof FProperty){
    	Object value = ((FProperty)getFocData()).getValue();
  		Collection<String> coll = (Collection<String>) getItemIds();
  		if(coll != null && !coll.contains(value) && isNewItemsAllowed() && value instanceof String){
  			addNewItem((String)value);
  		}else{
  			setValue(value);
  		}
    }
  }

}