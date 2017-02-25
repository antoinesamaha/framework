package com.foc.property;

import java.util.ArrayList;
import java.util.Iterator;

import com.foc.desc.FocObject;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.desc.field.FMultipleChoiceItem;

@SuppressWarnings("serial")
public class FMultipleChoiceString extends FString implements IFMultipleChoiceProperty {
	
	private ArrayList<FMultipleChoiceItem> localChoicesArray = null;
	
	public FMultipleChoiceString(FocObject focObj, int fieldID, String str) {
		super(focObj, fieldID, str);
	}
	
	public void dispose(){
		super.dispose();
		if(this.localChoicesArray != null){
			this.localChoicesArray.clear();
			this.localChoicesArray = null;
		}
	}
	
  public void setString(String str) {
  	super.setString(str);
  }
	
	private int getNextChoiceId(){
		return localChoicesArray == null ? 1 : localChoicesArray.size() + 1;
	}
	
	private ArrayList<FMultipleChoiceItem> getLocalChoicedArray(){
		if(localChoicesArray == null){
			resetLocalSourceList();
		}
		return localChoicesArray;
	}
	
	public void resetLocalSourceList(){
		this.localChoicesArray = new ArrayList<FMultipleChoiceItem>();
		addLocalChoice("");
	}
	
	public void addLocalChoice(String choice){
  	FMultipleChoiceItem multipleChoiceItem = new FMultipleChoiceItem(getNextChoiceId(), choice);
  	getLocalChoicedArray().add(multipleChoiceItem);
	}

	public void removeLocalChoice(String choice){
		FMultipleChoiceItem choiceItem = findChoiceInLocal(choice);
		if(choiceItem != null){
			getLocalChoicedArray().remove(choiceItem);
		}
	}

	private Iterator<FMultipleChoiceItem> getLocalChoicesIterator() {
    Iterator<FMultipleChoiceItem> iter = null;
    if(localChoicesArray != null){
      iter = localChoicesArray.iterator();
    }
    return iter;
  }
	
	public Iterator<FMultipleChoiceItem> getChoiceIterator(){
    Iterator<FMultipleChoiceItem> iter = getLocalChoicesIterator();
    if(iter == null){
	    FMultipleChoiceStringField field = (FMultipleChoiceStringField) this.getFocField();
	    if (field != null) {
	      iter = field.getChoicesIterator();
	    }
    }
    return iter;
  }

	public FMultipleChoiceItem findChoiceInLocal(String choiceValue){
		FMultipleChoiceItem foundItem = null;
		Iterator<FMultipleChoiceItem> iter = getLocalChoicesIterator();
		while(iter != null && iter.hasNext() && foundItem == null){
			FMultipleChoiceItem choice = iter.next();
			if(choice.getTitle().equals(choiceValue)){
				foundItem = choice;
			}
		}
		return foundItem;
	}
	
	public boolean contains(String choiceValue){
		boolean found = findChoiceInLocal(choiceValue) != null;
		return found;
	}
}
