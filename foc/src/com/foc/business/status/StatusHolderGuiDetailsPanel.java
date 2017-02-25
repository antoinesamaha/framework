package com.foc.business.status;

import java.awt.Component;
import java.awt.GridBagConstraints;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.StaticComponent;

@SuppressWarnings("serial")
public class StatusHolderGuiDetailsPanel extends FPanel{
  
  private FocObject focObject = null;
    
	public StatusHolderGuiDetailsPanel(FocObject focObj, int view, boolean editable){
		super("", FPanel.FILL_NONE);

    this.focObject = focObj;
    Component comp = null;

    int y = 0;
    
    //add(statusButtonsPanel, 1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
    FPanel datesPanel = new FPanel();
    add(datesPanel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

    new StatusFieldPanel(focObj, view, editable, false, datesPanel, 0, y++);
    comp = datesPanel.add(focObject, getIStatusHolderDesc().getFLD_CREATION_USER(), 0, y++);
    comp.setEnabled(false);
    comp = datesPanel.add(focObject, getIStatusHolderDesc().getFLD_CREATION_DATE(), 0, y++);
    comp.setEnabled(false);
    comp = datesPanel.add(focObject, getIStatusHolderDesc().getFLD_VALIDATION_DATE(), 0, y++);
    comp.setEnabled(false);
    comp = datesPanel.add(focObject, getIStatusHolderDesc().getFLD_CLOSURE_DATE(), 0, y++);
    comp.setEnabled(false);
    
    if(!editable){
      StaticComponent.setAllComponentsEnable(this, false, true);
    }
  }
  
  public void dispose(){
    super.dispose();
    focObject = null;
  }
    
  public IStatusHolderDesc getIStatusHolderDesc(){
  	return (IStatusHolderDesc)focObject.getThisFocDesc();
  }

  public IStatusHolder getIStatusHolder(){
  	return (IStatusHolder)focObject;
  }

  public StatusHolder getStatusHolder(){
  	return getIStatusHolder().getStatusHolder();
  }
  
  public FocObject getFocObject(){
  	return focObject;
  }
}
