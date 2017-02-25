package com.foc.business.config;

import java.awt.GridBagConstraints;

import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class BusinessConfigGuiDetailsPanel extends FPanel {
  
  public BusinessConfigGuiDetailsPanel(BusinessConfig basicsConfig, int view){
    super("Business Configuration", FPanel.FILL_NONE);    
    
    int y = 0;
            
    FPanel partyCoding = new FPanel();
    partyCoding.setBorder("Party coding");
    add(partyCoding, 0, y++, 5, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
    
    y = 0;

    partyCoding.add(basicsConfig, BusinessConfigDesc.FLD_ADR_BOOK_PARTY_PREFIX, 0, y++);
    partyCoding.add(basicsConfig, BusinessConfigDesc.FLD_ADR_BOOK_PARTY_SEPERATOR, 0, y++);
    partyCoding.add(basicsConfig, BusinessConfigDesc.FLD_ADR_BOOK_PARTY_NBR_DIGITS, 0, y++);
    partyCoding.add(basicsConfig, BusinessConfigDesc.FLD_ADR_BOOK_PARTY_RESET_NUMBERING_WHEN_PREFIX_CHANGE, 1, y++);
  }
}