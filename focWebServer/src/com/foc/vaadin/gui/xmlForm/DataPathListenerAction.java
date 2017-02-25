package com.foc.vaadin.gui.xmlForm;

import java.util.ArrayList;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;

public class DataPathListenerAction {

  private ArrayList<FocXMLGuiComponent>      array_GuiCompWithDataPath = null;
  private ArrayList<XMLLayoutFormulaContext> array_FormulaContext      = null;
  
  public DataPathListenerAction(){
  }
  
  public void dispose(){
  	if(array_GuiCompWithDataPath != null){
  		array_GuiCompWithDataPath.clear();
  		array_GuiCompWithDataPath = null;
  	}
  	if(array_FormulaContext != null){
  		for(int i=0; i<array_FormulaContext.size(); i++){
  			XMLLayoutFormulaContext xml = array_FormulaContext.get(i);
  			xml.dispose();
  		}
  		array_FormulaContext.clear();
  		array_FormulaContext = null;
  	}
  }
  
  public void resetPropertyObjects(IFocData rootData){
    if(getGuiComponentArrayList() != null){
      for(int i=0; i<getGuiComponentArrayList().size(); i++){
        FocXMLGuiComponent comp = getGuiComponentArrayList().get(i);
        if(comp != null && comp.getDelegate() != null){
          IFocData focData = rootData.iFocData_getDataByPath(comp.getDelegate().getDataPath());
          comp.setFocData(focData);
          comp.setAttributes(comp.getAttributes());
        }
      }
    }
  }
  
  public void applyFormulasOfAllContexts(){
    if(getArraFormulaContext() != null){
      for(int i=0; i<getArraFormulaContext().size(); i++){
        XMLLayoutFormulaContext ctxt = getArraFormulaContext().get(i);
        ctxt.computeFormulaAndApplyVisibilityOnComponent();
      }
    }
  }
  
  private ArrayList<XMLLayoutFormulaContext> getArraFormulaContext(){
    if(array_FormulaContext == null){
      array_FormulaContext = new ArrayList<XMLLayoutFormulaContext>();
    }
    return array_FormulaContext;
  }
  
  public void addArrayFormulaContext(XMLLayoutFormulaContext xmlLayoutFormulaContext){
    if(xmlLayoutFormulaContext != null){
      getArraFormulaContext().add(xmlLayoutFormulaContext);
    }
  }
  
  private ArrayList<FocXMLGuiComponent> getGuiComponentArrayList(){
    if(array_GuiCompWithDataPath == null){
      array_GuiCompWithDataPath = new ArrayList<FocXMLGuiComponent>();
    }
    return array_GuiCompWithDataPath;
  }
  
  public void addGuiComponentWithDataPath(FocXMLGuiComponent guiComponent){
    if(guiComponent != null){
      getGuiComponentArrayList().add(guiComponent);
    }
  }
}