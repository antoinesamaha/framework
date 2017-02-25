package com.foc.gui;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

import com.foc.Globals;

@SuppressWarnings("serial")
public class FGTextPane extends JTextPane{

  public FGTextPane() {
		super();
		init();
	}

	public FGTextPane(StyledDocument doc) {
		super(doc);
		init();
	}

	private void init(){
		setFont(Globals.getDisplayManager().getDefaultFont());
	  setDisabledTextColor(Globals.getDisplayManager().getDisabledTextColor());
	}
}
