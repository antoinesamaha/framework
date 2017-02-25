package com.foc.gui;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class FGScrollPane extends JScrollPane {

  private Component view = null;
  
  public FGScrollPane(Component view) {
    super(view);
    this.view = view;
  }

  public Component getView() {
    return view;
  }
  
  public void dispose(){
    view = null;
  }
  
  public void setBorder(String title){
    TitledBorder border = new TitledBorder(new BevelBorder(BevelBorder.LOWERED), title);
    //border.setTitleColor(getBackground());
    setBorder(border);
  }
}
