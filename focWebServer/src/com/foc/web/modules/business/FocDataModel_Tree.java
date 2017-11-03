package com.foc.web.modules.business;

import com.foc.desc.dataModelTree.DataModelNode;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class FocDataModel_Tree extends FocXMLLayout {
  private String selectedString = null;
  private FocFormula_Form formulaLayoutReference = null;
  private ITableTree tableTree = null;
  
  private FVTextField formulaTextFieldCopy  = null;
  private FVLabel     formulaExpressionCopy = null;

  public FocDataModel_Tree() {

  }

  public FocFormula_Form getFormulaLayout() {
    return formulaLayoutReference;
  }

  public void setFormulaLayout(FocFormula_Form formulaLayout) {
    formulaLayoutReference = formulaLayout;
    
    formulaExpressionCopy  = (FVLabel) getComponentByName("LABEL_COPY");
    formulaExpressionCopy.setValueString(formulaLayoutReference.getExpressionLabel().getValueString());
    
    formulaTextFieldCopy   = (FVTextField) getComponentByName("TEXT_FIELD_COPY");
    formulaTextFieldCopy.setValueString(formulaLayoutReference.getTextField().getValueString());
    formulaTextFieldCopy.setColumns(30);
  }

  public String getSelectedString() {
    return selectedString;
  }

  public void setSelectedString(String selection) {
    selectedString = selection;
  }

  public ITableTree getTableTree() {
    if (tableTree == null) {
      FVTableWrapperLayout wrapperLayout = (FVTableWrapperLayout) getComponentByName("DATAMODEL");
      tableTree = wrapperLayout.getTableOrTree();
    }
    return tableTree;
  }
  
  public FVTextField getTextFieldCopy(){
    return formulaTextFieldCopy;
  }
  
  public FVLabel getExrpressionCopy(){
    return formulaExpressionCopy;
  }

  @Override
  protected void afterLayoutConstruction() {
    super.afterLayoutConstruction();

    FVButton addButton = (FVButton) getComponentByName("ADD_BUTTON");
    addButton.addClickListener(new ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        FVTreeTable tempTreeTable = (FVTreeTable)getTableTree();
        Long selectionReference = (Long) tempTreeTable.getValue();
        if(selectionReference != null){          
          DataModelNode selectedDataModelNode = (DataModelNode) tempTreeTable.getFocList().searchByReference(selectionReference);
          if(selectedDataModelNode != null){
            setSelectedString(selectedDataModelNode.getPathSection());
            getTextFieldCopy().setValueString(getTextFieldCopy().getValueString() + getSelectedString());
          }
        }
      }
    });
    
    FVButton replaceButton = (FVButton) getComponentByName("REPLACE_BUTTON");
    replaceButton.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        FVTreeTable tempTreeTable = (FVTreeTable)getTableTree();
        Long selectionReference = (Long) tempTreeTable.getValue();
        
        if(selectionReference != null){
          DataModelNode selectedDataModelNode = (DataModelNode) tempTreeTable.getFocList().searchByReference(selectionReference);
          if(selectedDataModelNode != null){
            setSelectedString(selectedDataModelNode.getPathSection());
            getTextFieldCopy().setValueString(getSelectedString());
          }          
        }
      }
    });
    
    FVButton clearButton = (FVButton) getComponentByName("CLEAR_BUTTON");
    clearButton.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        getTextFieldCopy().setValueString("");
        getFormulaLayout().getTextField().setValueString("");
      }
    });
    
    FVButton closeButton = (FVButton) getComponentByName("CLOSE_BUTTON");
    closeButton.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        getFormulaLayout().getTextField().setValueString(getTextFieldCopy().getValueString());
        goBack(null);
      }
    });
  }

  public String getSelectedFullPath() {
    String result = null;

    return result;
  }

}
