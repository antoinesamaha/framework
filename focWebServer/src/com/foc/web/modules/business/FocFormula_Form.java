package com.foc.web.modules.business;


import com.foc.desc.FocObject;
import com.foc.desc.dataModelTree.DataModelNodeDesc;
import com.foc.desc.dataModelTree.DataModelNodeList;
import com.foc.desc.dataModelTree.DataModelNodeTree;
import com.foc.formula.AbstractFormulaEnvironment;
import com.foc.formula.PropertyFormula;
import com.foc.formula.PropertyFormulaContext;
import com.foc.formula.PropertyFormulaDesc;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.treeGrid.FVTreeGrid;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class FocFormula_Form extends FocXMLLayout {

  private ITableTree tableTree = null;
  private FProperty selectedProperty = null;
  private FVTextField textField = null;
  private FVLabel expressionLabel = null;
  private FVButton appendButton = null;
  private AbstractFormulaEnvironment formulaEnvironment = null;

  private Object focObjectRowId = null;

  @Override
  public void dispose() {
    tableTree = null;
    selectedProperty = null;

    if (textField != null) {
      textField.dispose();
    }
    textField = null;

    if (expressionLabel != null) {
      expressionLabel.dispose();
    }
    expressionLabel = null;

    if (appendButton != null) {
      appendButton.dispose();
    }
    appendButton = null;
    super.dispose();
  }

  @Override
  protected void afterLayoutConstruction() {
  	
    // The text field of the formula
    textField = (FVTextField) getComponentByName("TEXT_FIELD");
//    textField.setColumns(30);
//    setExpandRatio(textField, 1);
    
    // The label of the formula
    expressionLabel = (FVLabel) getComponentByName("LABEL");

    // The add button of the formula
    appendButton = (FVButton) getComponentByName("ADD");
    appendButton.setStyleName(BaseTheme.BUTTON_LINK);
    //appendButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_FORMULA));
    appendButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_EDIT));
    appendButton.addClickListener(new ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        String fieldPath = popupFieldSelection();
        if (fieldPath != null){
          textField.setValueString(textField.getValueString() + fieldPath);
        }
        textField.focus();
      }
    });
  }

  public FProperty getSelectedProperty() {
    return selectedProperty;
  }

  public ITableTree getTableOrTree() {
    return tableTree;
  }

  public void setTableOrTree(ITableTree tableTree) {
    this.tableTree = tableTree;
  }

  public Table getTable() {
    Table result = null;
    if (tableTree != null) {
      result = (Table) tableTree;
    }
    return result;
  }

  public AbstractFormulaEnvironment getFormulaEnvironment() {
    return formulaEnvironment;
  }

  public void setFormulaEnvironment(AbstractFormulaEnvironment fe) {
    this.formulaEnvironment = fe;
  }
  
  public FVTextField getTextField(){
    return textField;
  }
  
  public FVLabel getExpressionLabel(){
    return expressionLabel;
  }

  /**
   * This method needs to be called to initialize the table and the formula
   * environment
   * 
   * @param table
   *          The ITableTree to apply formulas to.
   * @param forEnv
   *          The formula environment.
   */
  public void initialize(ITableTree table, AbstractFormulaEnvironment forEnv) {
    setTableOrTree(table);
    setFormulaEnvironment(forEnv);
    initializeTextFieldListener();
  }

  private void initializeTextFieldListener() {
    if (textField != null) {
      textField.addBlurListener(new BlurListener() {

        @Override
        public void blur(BlurEvent event) {
        	applyTextFieldFormulaInTheProperty();
        }
      });
    }
  }

	public void applyTextFieldFormulaInTheProperty(){
	  if (selectedProperty != null) {
	    if (!selectedProperty.isWithFormula() && !textField.getValueString().equals("")) {
	      formulaEnvironment.applyFormulaToProperty(selectedProperty, "");
	    }
	
	    PropertyFormulaContext propertyFormulaContext = selectedProperty.getPropertyFormulaContext();
	    if (propertyFormulaContext != null) {
	      PropertyFormula propertyFormula = propertyFormulaContext.getPropertyFormula();
	      if (propertyFormula != null) {
	        // A listener on the field expression will use the
	        // AbstractFormulaEnvironment to apply the formula
	        propertyFormula.setExpression(textField.getValueString());
	      }
	    }
      selectedProperty.notifyListeners();
	  }
	}
  
  public FocObject getSelectedFocObject() {
    FocObject result = null;
    ITableTree tempTableTree = getTableOrTree();

    if (tempTableTree != null) {
      FocList list = tempTableTree.getFocList();
      if(list != null){
        if(focObjectRowId != null){
          result = list.searchByReference((Long) focObjectRowId);          
        }
      }
    }
    return result;
  }

  public String popupFieldSelection() {
    String str = null;
    FocObject fatherObj = getSelectedFocObject();
    if (fatherObj != null) {
      DataModelNodeList dataModelNodeList = new DataModelNodeList(fatherObj.getThisFocDesc(), 5, fatherObj);
      DataModelNodeTree dataModelNodeTree = new DataModelNodeTree(dataModelNodeList);
      
      XMLViewKey xmlViewKey = new XMLViewKey(DataModelNodeDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
      ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, dataModelNodeTree);
      
      FocDataModel_Tree treePanel = (FocDataModel_Tree) centralPanel;
      treePanel.setFormulaLayout(this);
      
      INavigationWindow navigationWindow = null;
      if(FocWebApplication.getInstanceForThread().getContent() instanceof INavigationWindow){
      	navigationWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getContent();
      }else{
      	navigationWindow = getMainWindow();	
      }
      if(navigationWindow != null){
      	navigationWindow.changeCentralPanelContent(centralPanel, true);
      }
    }
    return str;
  }
  
  public void triggerFormulaChanges(FProperty selectedProperty, Object rowId, Object columnId){

  	focObjectRowId = rowId;
    boolean fStringEditable = true;

    if (selectedProperty != null) {
      FocFormula_Form.this.selectedProperty = selectedProperty;

      PropertyFormulaContext propertyFormulaContext = selectedProperty.getPropertyFormulaContext();
      if (propertyFormulaContext != null) {
        PropertyFormula propertyFormula = propertyFormulaContext.getPropertyFormula();
        if (propertyFormula != null) {
          FProperty pfProperty = propertyFormula.getFocProperty(PropertyFormulaDesc.FLD_EXPRESSION);
          textField.setValue((String) pfProperty.getObject());
        }
      } else {
        String expression = "";
        if (selectedProperty.getFocField() != null) {
          if (selectedProperty.getFocField().getFormulaString() != null && !selectedProperty.getFocField().getFormulaString().isEmpty()) {
            expression = selectedProperty.getFocField().getFormulaString();
            fStringEditable = false;
          } else {
            // if(outputFieldFormulaGetter != null){
            // expression =
            // outputFieldFormulaGetter.getOutputFieldFormulaExpression(selectionPanel,
            // selectedProperty, row, col);
            // if(expression != null && !expression.isEmpty()){
            // fStringEditable = false;
            // }
            // }
          }
        }
        textField.setValueString(expression);
      }

      textField.setEnabled(fStringEditable);
      appendButton.setEnabled(fStringEditable);
    }

//    String rowCode = getTable().getItem(rowId).getItemProperty("CODE").toString();
    String rowCode = null;
    if(getTableOrTree() instanceof FVTreeGrid){
    	FVTreeGrid treeGrid = (FVTreeGrid) getTableOrTree();
  		if(treeGrid.getSelectedObject() != null && treeGrid.getSelectedObject().getItemProperty("CODE") != null){
  			rowCode = treeGrid.getSelectedObject().getItemProperty("CODE").getValue().toString();
  		}
    }else{
    	rowCode = getTable().getItem(rowId).getItemProperty("CODE").toString();
    }

    expressionLabel.setValue(rowCode + " : " + columnId);
  
  }
}
