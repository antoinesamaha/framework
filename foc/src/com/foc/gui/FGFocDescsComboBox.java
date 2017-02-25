package com.foc.gui;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.desc.FocDesc;

@SuppressWarnings("serial")
public class FGFocDescsComboBox extends FGAbstractComboBox {
	
	private HashMap<String , FocDesc> focDescMap = null;
	
	public FGFocDescsComboBox(){
		Iterator<IFocDescDeclaration> iter = Globals.getApp().getFocDescDeclarationIterator();
		while(iter != null && iter.hasNext()){
			IFocDescDeclaration iFocDescDeclaration = iter.next();
			if(iFocDescDeclaration != null){
				FocDesc focDesc = iFocDescDeclaration.getFocDescription();
				if(focDesc != null){
					String name = focDesc.getStorageName();
					addItem(name); 
					putFocDesc(name, focDesc);
				}
			}
		}
	}
	
	private HashMap<String, FocDesc> getFocDescMap(){
		if(this.focDescMap == null){
			this.focDescMap = new HashMap<String, FocDesc>();
		}
		return this.focDescMap;
	}
	
	private void putFocDesc(String focDescName, FocDesc focDesc){
		if(focDescName != null && focDesc != null){
			getFocDescMap().put(focDescName, focDesc);
		}
	}
	
	public FocDesc getSelectedFocDesc(){
		FocDesc selectedFocDesc = null;
		String focDescName = (String)getSelectedItem();
		if(focDescName != null){
			selectedFocDesc = getFocDescMap().get(focDescName);
		}
		return selectedFocDesc;
	}
	
	public void dispose(){
		if(this.focDescMap != null){
			this.focDescMap.clear();
			this.focDescMap = null;
		}
	}

	@Override
	protected String getPropertyStringValue() {
		return (String) getSelectedItem();
	}

	@Override
	protected void setPropertyStringValue(String strValue) {
	}
	
}
